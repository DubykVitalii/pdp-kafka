package com.avenga.kafkamessageservice.service;

import com.avenga.kafkamessageservice.exception.JsonSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ConcurrentMap<String, CopyOnWriteArrayList<SseEmitter>> userEmitters = new ConcurrentHashMap<>();
    private final ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    public void sendNotificationToTopic(String topicName, Object object) {
        CopyOnWriteArrayList<SseEmitter> emitters = userEmitters.get(topicName);
        if (emitters != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonString = objectMapper.writeValueAsString(object);
                emitters.forEach(emitter -> {
                    try {
                        emitter.send(jsonString, MediaType.APPLICATION_JSON);
                    } catch (IOException e) {
                        emitters.remove(emitter);
                    }
                });
            } catch (JsonProcessingException e) {
                throw new JsonSerializationException("Error serializing", e);
            }
        }
    }

    public SseEmitter subscribe(String topicName) {
        SseEmitter emitter = new SseEmitter(1800000L);
        nonBlockingService.execute(() -> {
            try {
                userEmitters.computeIfAbsent(topicName, k -> new CopyOnWriteArrayList<>()).add(emitter);
                emitter.onCompletion(() -> removeEmitter(topicName, emitter));
                emitter.onTimeout(() -> removeEmitter(topicName, emitter));
            } catch (Exception ex) {
                removeEmitter(topicName, emitter);
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    private void removeEmitter(String userId, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            emitters.remove(emitter);
        }
    }
}

