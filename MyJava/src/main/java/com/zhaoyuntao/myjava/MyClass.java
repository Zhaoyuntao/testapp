package com.zhaoyuntao.myjava;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class MyClass {
    public static final String PATTERN_PHONE_NUMBER = "(?<!\\d)([0-9]{1,3}[ \\-])?[0-9]{7,15}(?!\\d)";

    public static final String PATTERN_URL_MEETING = "https://(meetings|svc)\\.(matrx|matrixmeeting)\\.(xyz|us)/#/[a-z]/([0-9]{8,12})/([0-9a-zA-Z]*)(/([0-9a-zA-Z\\-]+))?";
    public static void main(String[] args) {
        Pattern p = Pattern.compile(PATTERN_URL_MEETING);
        S.s(p.matcher("https:\\/\\/meetings.matrx.io\\/#\\/j\\/287613236\\/269b5e53f8cdab7b0e4fbdc00af4aa83047b9bd84e0ada93\\/UAE-971-1000000").matches());
    }
}