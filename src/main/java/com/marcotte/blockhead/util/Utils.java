package com.marcotte.blockhead.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils
{
    public static boolean almostEqual( double a, double b)
    {
        return almostEqual( a, b, 1E-7);
    }

    public static boolean almostEqual( double a, double b, double eps)
    {
        return Math.abs(a-b) < eps;
    }

    public static Timestamp dateConvert(String dateString, String pattern ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(dateString));
        return Timestamp.valueOf(localDateTime);
    }

    public static String timestampToDateStr_mmddyyyy( Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return  localDateTime.format(formatter);
    }
}
