package com.ridemates.app.messaging.domain.chat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageHistory extends MessageItem {
    protected final String time;
    protected final String from, to;

    public MessageHistory(String from, String to, String content, String time) {
        super(content);
        this.from = from;
        this.to = to;
        this.time = time;
    }

    @Override
    public String toString() {
        return "MessageHistory{" +
                "time=" + time +
                ", from='" + from + '\'' +
                ", content='" + content + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
