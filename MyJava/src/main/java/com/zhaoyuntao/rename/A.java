package com.zhaoyuntao.rename;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeList;

/**
 * created by zhaoyuntao
 * on 5/19/21
 * description:
 */
class A {
    static int a = 0;

    {
        System.out.println(" 1");
    }

    static {
        System.out.println("static 1");
    }

    static int b = 0;
    int bb = 13;

    {
        System.out.println(" 2");
        bb = 1;
    }

    static {
        System.out.println("static 2");
    }

    public A() {
        System.out.println("A()");
    }

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
//        int count = 100000000;
//        long time = System.currentTimeMillis();
//        C c;
//        for (int i = 0; i < count; i++) {
//            Constructor<C> constructor;
//            try {
//                constructor = C.class.getConstructor();
//                c = constructor.newInstance();
//            } catch (Throwable e) {
//                throw new RuntimeException(e);
//            }
//        }
//        System.out.println("new use:" + (System.currentTimeMillis() - time) + "ms");
//        time = System.currentTimeMillis();
//        Constructor<C> constructor = null;
//        try {
//            constructor = C.class.getConstructor();
//        } catch (Throwable e) {
//        }
//        for (int i = 0; i < count; i++) {
//            try {
//                c = constructor.newInstance();
//            } catch (Throwable e) {
//                throw new RuntimeException(e);
//            }
//        }
////        c = null;
//        System.out.println("new use:" + (System.currentTimeMillis() - time) + "ms");
//        int a =1;
//        Object aa=a;
//        System.out.println(""+(aa instanceof Integer));
//        A aa = new A();
//        System.out.println("a:" + a + " b:" + b + " bb:" + aa.bb);
//        System.out.println("\\6");
//        System.out.println("\\6".matches("\\\\6"));


        Calendar c = Calendar.getInstance();
        System.out.println(c.get(Calendar.MONTH));


        System.out.println(c.get(Calendar.YEAR));

        new ArrayList<>();

//        set1(new G<List>());
        set1(new G<ArrayList>());
        set1(new G<AttributeList>());

        set2(new G<List>());
        set2(new G<ArrayList>());
//        set2(new G<AttributeList>());

//        G <? super ArrayList> g = new G<AttributeList>();
        G<? super ArrayList> g2 = new G<List>();

        G<? extends ArrayList> g3 = new G<AttributeList>();
//        G <? extends ArrayList> g4 = new G<List>();

//        set1(g);
//        set2(g);

    }

    public void func1(String string) {

    }

    public <T> void func2(T t) {

    }

    public static class G<T> {
    }

    public static void set1(G<? extends ArrayList> g) {

    }

    public static void set2(G<? super ArrayList> g) {

    }
}
