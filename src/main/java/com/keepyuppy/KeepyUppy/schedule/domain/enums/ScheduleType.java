package com.keepyuppy.KeepyUppy.schedule.domain.enums;

import java.util.Arrays;

public enum ScheduleType {
    TEAM,
    USER;

    public static ScheduleType getInstance(String type) {
        return Arrays.stream(ScheduleType.values())
                .filter(scheduleType -> scheduleType
                        .name().equals(type))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
