package com.zhaoyuntao.rename;

import com.zhaoyuntao.myjava.S;

/**
 * created by zhaoyuntao
 * on 5/19/21
 * description:
 */
class A {
    public static class B implements Cloneable{
        String str="0";

        @Override
        protected B clone() {
            try {
                return (B) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException();
            }
        }
    }


    public static B change(B b){
        b=new B();
        b.str="1";
        return b;
    }
    public static void main(String[] args) {
//        B b=new B();
//        S.s("b:"+b+" "+b.str);
//        B b2=change(b);
//        S.s("b:"+b+" "+b.str);
//        S.s("b2:"+b2+" "+b2.str);
        B b=new B();
        b.str="1";
        B b2=  b.clone();
        S.s("b2:"+b2.str);
    }
}
