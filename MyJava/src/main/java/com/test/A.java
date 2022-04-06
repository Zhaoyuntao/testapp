package com.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
        B c = get(B.class);
        c.add(2, 3);
    }
}
