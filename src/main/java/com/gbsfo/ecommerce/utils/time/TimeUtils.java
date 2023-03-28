package com.gbsfo.ecommerce.utils.time;

import java.time.Clock;
import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class TimeUtils {

    private final Clock clock;

    public TimeUtils() {
        this.clock = Clock.systemUTC();
    }

    public TimeUtils(Clock clock) {
        this.clock = clock;
    }

    public Instant getCurrentTime() {
        return Instant.now(clock);
    }
}