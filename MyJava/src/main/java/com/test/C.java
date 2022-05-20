package com.test;

import com.zhaoyuntao.myjava.S;

/**
 * created by zhaoyuntao
 * on 18/05/2022
 * description:
 */
class C {
    public static void main(String[] args) {
        float angle = 45;
        double sin30 = Math.sin(Math.toRadians(30));
        double sin150 = Math.sin(Math.toRadians(150));
        double sin210 = Math.sin(Math.toRadians(210));
        double sin330 = Math.sin(Math.toRadians(330));

        double cos30 = Math.cos(Math.toRadians(30));
        double cos150 = Math.cos(Math.toRadians(150));
        double cos210 = Math.cos(Math.toRadians(210));
        double cos330 = Math.cos(Math.toRadians(330));

        S.s("Math.sin30:" + sin30);
        S.s("Math.sin150:" + sin150);
        S.s("Math.sin210:" + sin210);
        S.s("Math.sin330:" + sin330);
        S.s("Math.cos30:" + cos30);
        S.s("Math.cos150:" + cos150);
        S.s("Math.cos210:" + cos210);
        S.s("Math.cos330:" + cos330);
    }
}
