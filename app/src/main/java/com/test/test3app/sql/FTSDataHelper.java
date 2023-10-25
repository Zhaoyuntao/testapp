package com.test.test3app.sql;

import java.util.Random;

import im.turbo.basetools.utils.StringUtils;

/**
 * created by zhaoyuntao
 * on 11/10/2023
 */
public class FTSDataHelper {

    public static String getRandomData() {
        return getRandomData(new Random().nextBoolean());
    }

    public static String getRandomData(boolean longData) {
        String[] data = longData ? getLongData() : getShortData();
        return addSpace(data[new Random().nextInt(data.length)]);
    }

    public static String addSpace(String data) {
        StringBuilder stringBuilder = new StringBuilder();
        StringUtils.getCharCount(data, new StringUtils.CharIterator() {
            @Override
            public boolean getCharString(String item, int codePoint) {
                stringBuilder.append(item).append(" ");
                return true;
            }
        });
        return stringBuilder.toString();
    }

    public static String[] getShortData() {
        return new String[]{
                "Hello world",
                "H e l l o w o r l d",
                "What can I help you with?",
                "Thank you so much.",
                "Nice to meet you!",
                "Whats that means?",
                "Right way",
                "좋은 아침이에요",
                "ضصثقفغعهخح",
                "ض ص ث ق ف غ ع ه خ ح",
                "좋 은 아 침 이 에 요",
                "就撒旷达科技啥",
                "就 撒 旷 达 科 技 啥",
                "你好",
                "我有什么可以帮助您的？",
                "非常感谢！",
                "非常高兴认识你！",
                "那是什么意思",
                "意思那是什么",
                "马上到！",
        };
    }

    public static String[] getLongData() {
        return new String[]{
                "HashCode doesn't always return the memory address (which itself may be bogus since objects may be relocated in memory). A class that provides its own hashCode might have an algorithm that causes collisions (two different objects with the same hashCode).",
                "Additionally, you can get equals involved: Two objects, where a!=b but a.equals(b) is true, must have the same hashCode or certain data structures like hashmaps, hashsets, LRU caches, etc, will not work properly. However, if two objects that are not equal have the same hashCode, this poses no problem--hashCode is used in many cases as a hint for performance improvement (e.g. in hashMap). While poor hashCode implementations such as return 1; will not cause failure of a properly-written data structure, they will cause performance to drop (e.g. in the case of a HashMap, amortized O(1) becomes O(N)).",
                "Thirdly, even the best hashCode will necessarily have collisions if there are more than 4,294,967,296 different objects of that class. This is because there are only 4,294,967,296 distict hashCode values possible because hashCode is an int, and by pigeonhole princple.",
                "If two objects are equal according to the equals(Object) method, then calling the hashCode method on each of the two objects must produce the same integer result.It is not required that if two objects are unequal according to the java.lang.Object.equals(java.lang.Object) method, then calling the hashCode method on each of the two objects must produce distinct integer results. However, the programmer should be aware that producing distinct integer results for unequal objects may improve the performance of hashtables.",
                "HelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHello",
                "Hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh",

                "HashCode 并不总是返回内存地址（它本身可能是假的，因为对象可能会在内存中重新定位）。提供自己的类hashCode可能具有导致冲突的算法（两个不同的对象具有相同的 hashCode）。",
                "此外，您还可以equals参与其中：两个对象，其中a!=bbuta.equals(b)为true，必须具有相同的hashCode，否则某些数据结构（如hashmap、hashset、LRU缓存等）将无法正常工作。然而，如果两个不相等的对象具有相同的 hashCode，这不会造成任何问题——hashCode在许多情况下用作性能改进的提示（例如在 hashMap 中）。虽然糟糕的 hashCode 实现return 1;不会导致正确编写的数据结构失败，但它们会导致性能下降（例如，在 HashMap 的情况下，摊销 O(1) 变为 O(N)）。",
                "第三，如果该类有超过 4,294,967,296 个不同的对象，即使是最好的 hashCode 也必然会发生冲突。这是因为只有 4,294,967,296 个不同的 hashCode 值可能，因为 hashCode 是一个 int，并且按照鸽巢原理。",
                "如果根据 equals(Object) 方法两个对象相等，则对这两个对象调用 hashCode 方法必须产生相同的整数结果。如果根据 java.lang.Object.equals(java.lang.Object) 方法两个对象不相等，则不要求对这两个对象中的每一个调用 hashCode 方法必须产生不同的整数结果。但是，程序员应该意识到，为不相等的对象生成不同的整数结果可能会提高哈希表的性能。",
                "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好",
                "你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你你",

                "https://www.google.com/search?q=java+how+to+known+two+object+are+different+when+override+hash&newwindow=1&sca_esv=572520602&rlz=1C5CHFA_enAE1017AE1017&sxsrf=AM9HkKnxv6NjFvj-y5UMzXyB__QHHjg9Kw%3A1697024203592&ei=y4gmZdWWI8aL9u8P3cqB-AQ&ved=0ahUKEwiVqNHm8-2BAxXGhf0HHV1lAE8Q4dUDCBA&uact=5&oq=java+how+to+known+two+object+are+different+when+override+hash&gs_lp=Egxnd3Mtd2l6LXNlcnAiPWphdmEgaG93IHRvIGtub3duIHR3byBvYmplY3QgYXJlIGRpZmZlcmVudCB3aGVuIG92ZXJyaWRlIGhhc2hIhrwBUJQLWN2yAXALeAGQAQGYAdAEoAGGX6oBCzItMzQuMTAuMC4xuAEDyAEA-AEBwgIKEAAYRxjWBBiwA8ICChAhGKABGMMEGArCAggQIRigARjDBMICBRAAGKIEwgIHECEYoAEYCsICBBAhGBXCAgQQIRgK4gMEGAAgQYgGAZAGCA&sclient=gws-wiz-serp",
        };
    }
}
