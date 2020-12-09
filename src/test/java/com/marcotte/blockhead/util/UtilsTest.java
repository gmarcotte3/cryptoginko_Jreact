package com.marcotte.blockhead.util;

import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void dateConvert() {
        String timestampAsString = "Nov 12, 2018 13:02:56.12345678";
        String pattern = "MMM dd, yyyy HH:mm:ss.SSSSSSSS";

        Timestamp timestamp = Utils.dateConvert(timestampAsString, pattern);
        assertEquals("2018-11-12 13:02:56.12345678", timestamp.toString());
    }

    @Test
    public void dateConvert2() {
        String timestampAsString = "Mon Nov 12, 2018 13:02:56";
        String pattern = "EEE MMM dd, yyyy HH:mm:ss";

        Timestamp timestamp = Utils.dateConvert(timestampAsString, pattern);
        assertEquals("2018-11-12 13:02:56.0", timestamp.toString());
    }

    @Test
    public void dateConvert3() {
        String timestampAsString = "Mon Nov 12, 2018 13:02:56 GMT+13:00";
        String pattern = "EEE MMM dd, yyyy HH:mm:ss z";

        Timestamp timestamp = Utils.dateConvert(timestampAsString, pattern);
        assertEquals("2018-11-12 13:02:56.0", timestamp.toString());
    }

    //01234567890
    //Tue Feb 06 2018 15:13:11 GMT+1300 (New Zealand Daylight Time)
    @Test
    public void dateConvert4() {
        String timestampAsString = "Fri Dec 04 2020 13:44:13 GMT+13:00";
        String pattern = "EEE MMM dd yyyy HH:mm:ss z";

        Timestamp timestamp = Utils.dateConvert(timestampAsString, pattern);
        assertEquals("2020-12-04 13:44:13.0", timestamp.toString());
    }

    @Test
    public void timestampToDateStr_mmddyyyy() {
        //Generate a date for Jan. 9, 2013, 10:11:12 AM
        Timestamp timestamp = Timestamp.valueOf("2013-01-09 10:11:12");
        String dateStr = Utils.timestampToDateStr_mmddyyyy(timestamp);
        assertEquals("09-01-2013", dateStr);
    }

    @Test
    public void timestampToDateStr_mmddyyyy2() {
        //Generate a date for dec. 31, 2013, 23:59:59
        Timestamp timestamp = Timestamp.valueOf("2013-12-31 23:59:59");
        String dateStr = Utils.timestampToDateStr_mmddyyyy(timestamp);
        assertEquals("31-12-2013", dateStr);
    }
}