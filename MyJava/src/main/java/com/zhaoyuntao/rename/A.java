package com.zhaoyuntao.rename;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by zhaoyuntao
 * on 5/19/21
 * description:
 */
class A {
    public static class B {
        private String name;
        private int number;
        private boolean ok;
        private long time;
        private float width;
        private double height;
        private List<String> list;
        private B b;

        @Override
        public String toString() {
            return "B{" +
                    "name='" + name + '\'' +
                    ", number=" + number +
                    ", ok=" + ok +
                    ", time=" + time +
                    ", width=" + width +
                    ", height=" + height +
                    ", list=" + list +
                    ", b=" + b +
                    '}';
        }
    }

    public static class C extends B {
        private String abc;

        @Override
        public String toString() {
            return "C{" +
                    "abc='" + abc + '\'' +
                    "super='" + super.toString() + '\'' +
                    '}';
        }
    }

    private static <T> void setDefaultValues(Object o) {
        for (Class<?> clazz = o.getClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length > 0) {
                for (Field field : fields) {
                    if (field != null) {
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        try {
                            Object value = field.get(o);
                            if (!field.isSynthetic() && (value == null
                                    || (value instanceof Number && ((Number) value).doubleValue() == 0))
                                    || value instanceof Boolean
                            ) {
                                if (field.getType() == int.class) {
                                    field.set(o, (int) 1);
                                } else if (field.getType() == long.class) {
                                    field.set(o, (long) System.currentTimeMillis());
                                } else if (field.getType() == float.class) {
                                    field.set(o, (float) 1f);
                                } else if (field.getType() == double.class) {
                                    field.set(o, (double) 1d);
                                } else if (field.getType() == short.class) {
                                    field.set(o, (short) 1);
                                } else if (field.getType() == byte.class) {
                                    field.set(o, (byte) 1);
                                } else if (field.getType() == String.class) {
                                    field.set(o, (String) "Test");
                                } else if (field.getType() == boolean.class) {
                                    field.set(o, (boolean) true);
                                } else if (field.getType() == char.class) {
                                    field.set(o, (char) 't');
                                } else if (field.getType() == List.class) {
                                    field.set(o, new ArrayList<>());
                                } else if (field.getType() == Map.class) {
                                    field.set(o, new HashMap<>());
                                } else {
//                                    System.out.println("getConstructors:"+ field.getType().getClass());
                                    Object fieldValue = field.getType().newInstance();
                                    field.set(o, fieldValue);
                                    if (field.getType() != o.getClass()) {
                                        setDefaultValues(fieldValue);
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        field.setAccessible(accessible);
                    }
                }
            }
        }
    }


    public static class AA {
        public void s() {
            System.out.println("AA");
        }
    }

    public static class AAA extends AA {
        @Override
        public void s() {
            super.s();
        }
    }

    public static void main(String[] args) {
//        C c = new C();
//        setDefaultValues(c);
//        try {
//            System.out.println("c:" + C.class.getConstructor());
//            System.out.println("c:" + C.class.getConstructor().newInstance());
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
//        new AA(){
//            @Override
//            public void s() {
//                super.s();
//            }
//        }.s();
        int count = 100000000;
        long time = System.currentTimeMillis();
        C c;
        for (int i = 0; i < count; i++) {
            Constructor<C> constructor;
            try {
                constructor = C.class.getConstructor();
                c = constructor.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("new use:" + (System.currentTimeMillis() - time) + "ms");
        time = System.currentTimeMillis();
        Constructor<C> constructor = null;
        try {
            constructor = C.class.getConstructor();
        } catch (Throwable e) {
        }
        for (int i = 0; i < count; i++) {
            try {
                c = constructor.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
//        c = null;
        System.out.println("new use:" + (System.currentTimeMillis() - time) + "ms");
    }
}
