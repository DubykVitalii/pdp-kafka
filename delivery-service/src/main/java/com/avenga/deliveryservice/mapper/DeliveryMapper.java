package com.avenga.deliveryservice.mapper;

import com.avenga.deliveryservice.model.persistance.Delivery;
import com.avenga.deliveryservice.model.record.DeliveryRecord;
import org.mapstruct.Mapper;

@Mapper
public interface DeliveryMapper {

    Delivery toDelivery(DeliveryRecord deliveryRecord);

    DeliveryRecord toDeliveryRecord(Delivery delivery);
}
