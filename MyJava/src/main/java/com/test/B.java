package com.test;

import com.zhaoyuntao.myjava.S;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * created by zhaoyuntao
 * on 11/04/2022
 * description:
 */
class B {
    public static class C {
        String uuid;
        long time;

        public C(String uuid, long time) {
            this.uuid = uuid;
            this.time = time;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof C)) return false;
            C c = (C) o;
            return Objects.equals(uuid, c.uuid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid);
        }

        @Override
        public String toString() {
            return uuid;
        }
    }

    public static void main(String[] args) {
//        JSONArray jsonArray=new JSONArray();
//        jsonArray.put(1);
//        jsonArray.put(2);
//        jsonArray.put(3);
//        S.s("jsonArray:"+jsonArray.toString(2));
//        JSONObject jsonObject=new JSONObject(jsonArray.toString());
//        S.s("jsonObject:"+jsonObject.toString(2));

//        S.s("+971 1234 323 21 ".replaceAll("\\s|\\+",""));
//        List<C> set = new ArrayList<>();
//        C c0 = new C("hello0", 0);
//        C c1 = new C("hello1", 100);
//        C c2 = new C("hello2", 200);
//        C c3 = new C("hello3", 300);
//        C c4 = new C("hello4", 400);
//        C c5 = new C("hello5", 500);
//        C c6 = new C("hello6", 600);
//        set.add(c0);
//        set.add(c1);
//        set.add(c2);
//        set.add(c3);
//        set.add(c4);
//        set.add(c5);
//        set.add(c6);
//
//        S.s("--------------------------------------");
//        S.s(set);
//        S.s("--------------------------------------");
//        S.s("sub 0-1:" + set.subList(0, 1));
//        set.removeAll(set.subList(2, 2+2));
//        S.s(set);
//        S.s("--------------------------------------");
    }
}
