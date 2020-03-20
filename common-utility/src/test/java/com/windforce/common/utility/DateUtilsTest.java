package com.windforce.common.utility;

import org.junit.Test;

import java.util.Calendar;

public class DateUtilsTest {

    /**
     * @param args
     */
    @Test
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.add(Calendar.DAY_OF_YEAR, -1);
//		System.out.println(DateUtils.isBetweenHourOfDay(10, cal.getTime()));
    }

}
