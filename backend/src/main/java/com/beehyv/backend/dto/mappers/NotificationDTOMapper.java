package com.beehyv.backend.dto.mappers;

import com.beehyv.backend.dto.modeldtos.NotificationDTO;
import com.beehyv.backend.models.Notification;

import java.util.function.Function;

public class NotificationDTOMapper implements Function<Notification, NotificationDTO> {
    @Override
    public NotificationDTO apply(Notification notification) {
        return new NotificationDTO(
                notification.getNotificationId(),
                notification.getNotificationTitle(),
                notification.getDescription()
        );
    }
}
