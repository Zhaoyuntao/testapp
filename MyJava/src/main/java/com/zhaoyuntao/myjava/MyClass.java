package com.zhaoyuntao.myjava;

import java.util.List;
import java.util.regex.Pattern;

public class MyClass {
    public static final String PATTERN_PHONE_NUMBER = "(?<!\\d)([0-9]{1,3}[ \\-])?[0-9]{7,15}(?!\\d)";

    public static final String PATTERN_URL_MEETING = "https://(meetings|svc)\\.(matrx|matrixmeeting)\\.(xyz|us)/#/[a-z]/([0-9]{8,12})/([0-9a-zA-Z]*)(/([0-9a-zA-Z\\-]+))?";

    public static void main(String[] args) {
        Pattern p = Pattern.compile(PATTERN_URL_MEETING);
//        test("(\\(.*?\\))","abcdefg(Hello.java:100)asdjnjaks(HH.java:200)sadlm"));

//       test(PatternUtils.PATTERN_EMAIL,"是是是abcde@asbad.com案例是多么"));
//       test(PatternUtils.PATTERN_EMAIL,"萨克的acde.abcde@asbad.com山卡拉对拉丝款"));
//        test(PatternUtils.PATTERN_EMAIL,"撒库拉代码了abcde_abcde@asbad.com卡里打卡啦"));

//        test(PatternUtils.PATTERN_PHONE_NUMBER, "撒库拉代码了_12345678卡里打卡啦");
//        test(PatternUtils.PATTERN_PHONE_NUMBER, "撒库拉代码了-12345678卡里打卡啦");
//        test(PatternUtils.PATTERN_PHONE_NUMBER, "撒库拉代码了971 12345678卡里打卡啦");
//        test(PatternUtils.PATTERN_PHONE_NUMBER, "撒库拉代码了971-12345678卡里打卡啦");
//        test(PatternUtils.PATTERN_PHONE_NUMBER, "撒库拉代码了http:12345678卡里打卡啦");
//        test(PatternUtils.PATTERN_PHONE_NUMBER, "撒库拉代码了971 12345678 123卡里打卡啦");
//        test(PatternUtils.PATTERN_PHONE_NUMBER, "撒库拉代码了http:123.123.123卡里打卡啦");

        test(PatternUtils.WEB_URL, "撒库拉代码了www.baidu.com卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了baidu.com卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了http://www.baidu.com卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了htTp://www.baidu.cOm卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了http:/www.baidu.com卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了http://a.a卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了http://a.aa卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了http://www.baidu.com/a=1卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了http://www.baidu.com/a=1&b=2卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了http://www.baidu.com/a=1&b=2 abc卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了http://www.baidu.com/a/b/c/d/a=1&b=2 abc卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了http://192.168.3.3:1024卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了123.233.23.2卡里打卡啦");
        test(PatternUtils.WEB_URL, "撒库拉代码了yuntao.zhao@gmail.com卡里打卡啦");
    }

    public static void test(String patternString, String content) {
        S.s("=====================================================> [" + content + "]");
        List<TPatternGroupBean> s = PatternUtils.match(patternString, content);
        if (s == null || s.size() == 0) {
            S.e("no result");
        } else {
            S.s("\nResult:" + s);
        }
    }
}