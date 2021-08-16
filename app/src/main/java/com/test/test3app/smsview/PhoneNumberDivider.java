package com.test.test3app.smsview;

import android.text.TextUtils;

/**
 * created by zhaoyuntao
 * on 2019-10-25
 * description:
 */
public class PhoneNumberDivider {

    /**
     * divide a phone number string to several part
     * @param phoneNumber
     * @param dividerString divider string
     * @param divider width of divider part
     *
     *                for example:
     *                param : 123456789 , "-" , [1,2,3]
     *                return 1-23-456-789
     *
     *                param : 123 , "-" , [1,2,3]
     *                return 1-23
     *
     *                param : 1234 , "-" , [1,2,3]
     *                return 1-23-4
     * @return
     */
    public static String dividePhoneNumber(String phoneNumber, String dividerString, int[] divider) {
        if (TextUtils.isEmpty(phoneNumber) || divider == null || divider.length == 0) {
            return phoneNumber;
        }
        phoneNumber = phoneNumber.trim();
        StringBuilder stringBuilder = new StringBuilder();
        int length = 0;
        for (int i = 0; i < divider.length; i++) {
            if(divider[i]<=0){
                continue;
            }
            int start = length;
            int end = length + divider[i];
            if (start > phoneNumber.length() - 1) {
                break;
            } else if (end >= phoneNumber.length()) {
                stringBuilder.append(phoneNumber.substring(start));
                break;
            } else {
                if (i < divider.length - 1) {
                    stringBuilder.append(phoneNumber.substring(start, end)).append(dividerString);
                } else {
                    stringBuilder.append(phoneNumber.substring(start, end)).append(dividerString);
                    stringBuilder.append(phoneNumber.substring(end));
                }
            }

            length += divider[i];
            System.out.println(length);
        }

        return stringBuilder.toString();
    }
}
