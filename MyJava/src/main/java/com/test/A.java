package com.test;

import com.zhaoyuntao.myjava.S;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;

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

    public static <T>T get(Class<T>clazz){
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

    public static void main(String[] args) {
//        B c = get(B.class);
//        c.add(2, 3);
//        System.out.println("Attached:"+new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(1684384654624L));
//        System.out.println("Detached:"+new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(1684384757243L));
//        System.out.println("Dump:"+new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(1684384757247L));
        S.s("1<<0:"+(1<<0));
        S.s("1<<1:"+(1<<1));
        S.s("1<<2:"+(1<<2));
        S.s("1<<3:"+(1<<3));
        S.s("1<<4:"+(1<<4));
    }
}
