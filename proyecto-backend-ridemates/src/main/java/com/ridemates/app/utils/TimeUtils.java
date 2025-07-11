package com.ridemates.app.utils;

import java.text.SimpleDateFormat;

public interface TimeUtils {
    SimpleDateFormat LIVE_CHAT_FORMAT = new SimpleDateFormat("HH:mm");

    static String getCurrentTime() {
        return LIVE_CHAT_FORMAT.format(System.currentTimeMillis());
    }
}
