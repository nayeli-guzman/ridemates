package com.ridemates.app.messaging.application;

import com.ridemates.app.messaging.domain.chat.ChatService;
import com.ridemates.app.messaging.domain.chat.MessageHistory;
import com.ridemates.app.messaging.domain.chat.MessageItem;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class LiveChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void send(final MessageItem message, Principal principal) {
        String to = chatService.getChannelOf(principal.getName());
        System.out.println("to = " + to);
        MessageHistory processed = new MessageHistory(
                principal.getName(),
                to,
                message.getContent(),
                String.valueOf(System.currentTimeMillis())
        );
        System.out.println("processed = " + processed);

        chatService.cacheMessage(processed);
        messagingTemplate.convertAndSend("/channel/private/" + to, processed);
    }
}
