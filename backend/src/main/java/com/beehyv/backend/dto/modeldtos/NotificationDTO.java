package com.beehyv.backend.dto.modeldtos;

public record NotificationDTO(
        Integer notificationId,
        String notificationTitle,
        String description
) {
}
