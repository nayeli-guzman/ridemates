package com.ridemates.app.messaging.domain.chat;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

@Component
public class ChatListener {
    private final ChatService chatService;

    public ChatListener(ChatService chatService) {
        this.chatService = chatService;
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        String destination = (String) event.getMessage().getHeaders().get("simpDestination");
        System.out.println("destination = " + destination);
        if (destination == null) {
            return;
        }

        int index = destination.indexOf("/channel/private/");
        System.out.println("index = " + index);
        if (index == -1) {
            return;
        }

        Principal user = event.getUser();
        if (user == null) {
            return;
        }
        String username = user.getName();
        System.out.println("username = " + username);
        String owner = destination.substring(index + "/channel/private/".length());
        System.out.println("owner = " + owner);
        chatService.sendHistory(username, owner);
        chatService.cacheChannelOf(username, owner);
    }
}
