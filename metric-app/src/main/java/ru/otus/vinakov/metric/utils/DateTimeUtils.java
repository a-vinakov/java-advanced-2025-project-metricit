package ru.otus.vinakov.metric.utils;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class DateTimeUtils {

    public static ZonedDateTime timestampToZonedDateTime(Timestamp timestamp) {
        return timestamp.toInstant().atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime timestampToZonedDateTime(Timestamp timestamp, String timezone) {
        return timestamp.toInstant().atZone(ZoneId.of(timezone != null ? timezone : TimeZone.getDefault().getID()));
    }

}