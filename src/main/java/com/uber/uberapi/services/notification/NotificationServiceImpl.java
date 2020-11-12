package com.uber.uberapi.services.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void notify(String phoneNumber, String message) {
        System.out.printf("Notified %s about %s",phoneNumber,message);
    }
}
