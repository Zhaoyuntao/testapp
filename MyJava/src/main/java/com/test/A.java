package com.test;

import com.zhaoyuntao.myjava.S;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * created by zhaoyuntao
 * on 05/04/2022
 * description:
 */
class A {
    public interface B {
        @MyAnnotation("hello")
        public void add(int... a);
    }

    public static <T> T get(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//                Integer a = (Integer) args[0];
//                Integer b = (Integer) args[1];
                System.out.println("方法名：" + method.getName());
                for (Object arg : args) {
                    System.out.println("参数：" + arg.getClass().getSimpleName() + " " + arg);
                }

                MyAnnotation c = method.getAnnotation(MyAnnotation.class);
                System.out.println("注解：" + c.value());
                return o;
            }
        });
    }

    public static class C {
        private String a = "123";

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            C c = (C) o;
            return Objects.equals(a, c.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }
    }

    public static void main(String[] args) {
//        B c = get(B.class);
//        c.add(2, 3);
//        System.out.println("Attached:"+new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(1684384654624L));
//        System.out.println("Detached:"+new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(1684384757243L));
//        System.out.println("Dump:"+new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(1684384757247L));
//        S.s("1<<0:"+(1<<0));
//        S.s("1<<1:"+(1<<1));
//        S.s("1<<2:"+(1<<2));
//        S.s("1<<3:"+(1<<3));
//        S.s("1<<4:"+(1<<4));
//        LinkedHashMap<String,String>linkedHashMap=new LinkedHashMap<>();
//        linkedHashMap.put("2","2");
//        linkedHashMap.put("1","1");
//        linkedHashMap.put("3","3");
//        linkedHashMap.put("4","4");
//        for(Map.Entry<String, String> a:linkedHashMap.entrySet()){
//            S.s(a.getKey());
//        }
//        C c= new C();
//        C c2= new C();
//        S.s(c.hashCode());
//        S.s(c2.hashCode());
//        S.s(Objects.hashCode(c));
//        S.s(Objects.hashCode(c2));
//        S.s(c.equals(c2));

        Set<String> list = new HashSet<>();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
//        S.s(list.subList(0, 2));
//        S.s(list.subList(2, 3));
//        S.s(list.subList(0, 5));
//        S.s(subList(list, 0, 5));
        S.s(">>>" + limitString("abcdefg", -1, 5));
        S.s(">>>" + limitString("abcdefg", 3, 5));
        S.s(">>>" + limitString("abcdefg", 7, 1));
        S.s(">>>" + limitString("abcdefg", 10, 5));
        S.s(">>>" + limitString("abcdefg", 12, 5));
    }

    public static String limitString(String content, int from, int limit) {
        if (content == null || content.length() <= limit) {
            return content;
        } else {
            int start = Math.max(0, Math.min(from, content.length()));
            int end = Math.max(start, Math.min(start + limit, content.length()));
            return content.substring(start, end);
        }
    }

    public static <T1> List<T1> subList(Collection<T1> originList, int from, int to) {
        if (originList == null || originList.size() == 0) {
            return new ArrayList<>(0);
        }
        from = Math.max(0, Math.min(from, originList.size()));
        to = Math.max(from, Math.min(to, originList.size()));
        S.s("from:" + from + " to:" + to);
        if (from == 0 && to == originList.size()) {
            return new ArrayList<>(originList);
        } else {
            return new ArrayList<>(originList).subList(from, to);
        }
    }
}
