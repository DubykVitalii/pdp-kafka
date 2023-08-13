package com.avenga.kafkamessageservice.controller;

import com.avenga.kafkamessageservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    @GetMapping("/subscribe")
    public SseEmitter subscribeToNotifications(@RequestParam("topic") String topic) {
        return notificationService.subscribe(topic);
    }
}
