package com.ridemates.app.messaging.domain.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageItem {
    protected final String content;

    public MessageItem(String content) {
        this.content = content;
    }
}
