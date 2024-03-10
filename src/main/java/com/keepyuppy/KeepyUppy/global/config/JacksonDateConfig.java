package com.keepyuppy.KeepyUppy.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonDateConfig {

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String timeZone = "Asia/Seoul";

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder().serializers(
                        new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat).withZone(ZoneId.of(timeZone))),
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat).withZone(ZoneId.of(timeZone)))
                )
                .serializationInclusion(JsonInclude.Include.NON_NULL);
    }
}

