package com.ridemates.app.messaging.application;

import com.ridemates.app.messaging.domain.tracking.Tracking;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LiveTrackingController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/tracking")
    public void send(final Tracking tracking) {
        System.out.println("tracking = " + tracking);
        messagingTemplate.convertAndSend("/channel/tracking/" + tracking.getDriverUsername(), tracking);
    }


}
