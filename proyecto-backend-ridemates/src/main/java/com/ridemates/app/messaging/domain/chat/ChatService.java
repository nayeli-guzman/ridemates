package com.ridemates.app.messaging.domain.chat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final Cache<String, List<MessageHistory>> cacheHistory = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES) // For this demo, it is temporal
            .build();
    private final Map<String, String> cacheChannel = new HashMap<>();

    public void cacheMessage(MessageHistory message) {
        cacheHistory.asMap().compute(message.getTo(), (key, messageItems) -> {
            if (messageItems == null) {
                messageItems = new ArrayList<>();
            }
            messageItems.add(message);
            System.out.println("messageItems = " + messageItems);
            return messageItems;
        });
    }


    public void sendHistory(String sender, String owner) {
        List<MessageHistory> messageHistories = cacheHistory.getIfPresent(owner);
        if (messageHistories != null) {
            messageHistories.forEach(messageHistory -> {
                if (!messageHistory.getTo().equals(sender)) {
                    return;
                }
                messagingTemplate.convertAndSend("/channel/private/" + sender, messageHistory);
            });
        }
    }

    public void cacheChannelOf(String mail, String channel) {
        cacheChannel.put(mail, channel);
    }

    public String getChannelOf(String mail) {
       return cacheChannel.get(mail);
    }
}
