package com.ridemates.app.health;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class Health {

        @RequestMapping("/check")
        public String check() {
            return "I am alive!";
        }

        @RequestMapping()
        public String health() {
            return "Estamos ready mi gente!";
        }

}
