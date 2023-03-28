package com.gbsfo.ecommerce.configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.gbsfo.ecommerce.utils.time.TimeUtils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TimeUtilsTestConfiguration {

    public static final String TEST_CURRENT_TIME = "2020-05-30 18:00:00";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

    @Bean
    public TimeUtils timeUtils() {
        Clock fixedClock = Clock.fixed(Instant.from(formatter.parse(TEST_CURRENT_TIME)), ZoneId.of("UTC"));
        return new TimeUtils(fixedClock);
    }

}