package com.nor.sdpplugin.other;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.text.SimpleDateFormat;

public class PersianDateTimeUtil {

    private static final ULocale PERSIAN_LOCALE = new ULocale("fa_IR@calendar=persian");

    public static String getDate() {
        Calendar persianCalendar = Calendar.getInstance(PERSIAN_LOCALE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", PERSIAN_LOCALE);
        return sdf.format(persianCalendar);
    }

    public static String getTime() {
        Calendar persianCalendar = Calendar.getInstance(PERSIAN_LOCALE);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", PERSIAN_LOCALE);
        return sdf.format(persianCalendar);
    }

    public static String getDateTime() {
        Calendar persianCalendar = Calendar.getInstance(PERSIAN_LOCALE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", PERSIAN_LOCALE);
        return sdf.format(persianCalendar);
    }

    public static void main(String[] args) {
        System.out.println("تاریخ: " + PersianDateTimeUtil.getDate());
        System.out.println("ساعت: " + PersianDateTimeUtil.getTime());
        System.out.println("تاریخ و ساعت: " + PersianDateTimeUtil.getDateTime());
    }
}

