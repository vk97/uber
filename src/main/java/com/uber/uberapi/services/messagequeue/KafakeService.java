package com.uber.uberapi.services.messagequeue;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Service
public class KafakeService implements MessageQueue {
    private final Map<String, Queue<MQMessage>> topics= new HashMap<>();

    @Override
    public void sendMessage(String topic, MQMessage message) {
        topics.putIfAbsent(topic, new LinkedList<>());
        topics.get(topic).add(message);
    }

    @Override
    public MQMessage consumeMessage(String topic) {
        MQMessage message = topics.getOrDefault(topic,new LinkedList<>()).poll();
        return message;
    }
}
