package com.test.test3app;

import android.content.Context;

import java.io.InputStream;

public class Hanzi2Pinyin {
    private static final String[][] PINYIN_TABLE_HEADER = new String[][] {
            null, { "ling" }, { "yi" }, { "ding", "zheng" }, { "kao" },
            { "qi" }, { "shang" }, { "xia" }, { "wan", "mo" }, { "zhang" },
            { "san" }, { "ji" }, { "bu", "fou" }, { "yu" }, { "mian" },
            { "gai" }, { "chou" }, { "zhuan" }, { "qie", "ju" }, { "pi" },
            { "shi" }, { "qiu" }, { "bing" }, { "ye" }, { "cong" }, { "dong" },
            { "si" }, { "cheng" }, { "diu" }, { "liang" }, { "you" },
            { "yan" }, { "sang" }, { "shu" }, { "jiu" }, { "ge" }, { "ya" },
            { "pan" }, { "zhong" }, { "jie" }, { "feng" }, { "guan" },
            { "chuan" }, { "chan" }, { "lin" }, { "zhuo" }, { "zhu" },
            { "wan" }, { "dan" }, { "wei" }, { "jing" }, { "li" }, { "ju" },
            { "pie" }, { "fu" }, { "nai" }, { "tuo" }, { "me", "ma", "yao" },
            { "zhi" }, { "wu" }, { "zha" }, { "hu" }, { "fa" },
            { "le", "yue" }, { "ping" }, { "pang" }, { "qiao" }, { "guai" },
            { "cheng", "sheng" }, { "yin" }, { "mie", "nie" }, { "xi" },
            { "xiang" }, { "nang" }, { "jia" }, { "mai" }, { "luan" },
            { "ru" }, { "sha" }, { "na" }, { "gan", "qian" },
            { "qian", "gan" }, { "gui", "jun", "qiu" }, { "gan" }, { "jue" },
            { "le", "liao" }, { "zheng" }, { "er" }, { "chu" }, { "kui" },
            { "yun" }, { "sui" }, { "gen" }, { "xie" }, { "ji", "qi" },
            { "tou" }, { "wang", "wu" }, { "kang" }, { "ta" }, { "jiao" },
            { "hai" }, { "heng" }, { "mu" }, { "ting" }, { "qin", "qing" },
            { "bo" }, { "lian" }, { "duo" }, { "ren" }, { "wang" },
            { "shen", "shi" }, { "le" }, { "ding" }, { "ze" }, { "jin" },
            { "pu" }, { "chou", "qiu" }, { "ba" }, { "reng" }, { "fo", "fu" },
            { "lun" }, { "cang" }, { "zi", "zai" }, { "xian" }, { "cha" },
            { "hong" }, { "tong" }, { "qian" }, { "ge", "yi" }, { "di" },
            { "dai" }, { "chao" }, { "chang" }, { "sa" }, { "men" },
            { "yang" }, { "jian" }, { "jia", "jie" }, { "yao" }, { "fen" },
            { "fang" }, { "pei" }, { "diao" }, { "dun" }, { "wen" }, { "xin" },
            { "ai" }, { "xiu" }, { "bei" }, { "chen" }, { "tang" }, { "huo" },
            { "hui", "kuai" }, { "cui", "zu" }, { "chuan", "zhuan" },
            { "che" }, { "cang", "chen" }, { "xun" }, { "chi" }, { "xuan" },
            { "nu" }, { "bo", "bai", "ba" }, { "gu" }, { "ni" }, { "ban" },
            { "xu" }, { "zhou" }, { "shen" }, { "qu" }, { "si", "ci" },
            { "beng" }, { "si", "shi" }, { "jia", "ga", "qie", "qia" },
            { "dian", "tian" }, { "han" }, { "bu" }, { "bi" }, { "shao" },
            { "ci" }, { "zuo" }, { "ti", "ben" }, { "zhan" }, { "he" },
            { "she" }, { "yi", "die" }, { "gou" }, { "ning" }, { "yong" },
            { "wa" }, { "ka" }, { "huai" }, { "lao" }, { "bai" }, { "ming" },
            { "nai", "er" }, { "gui" }, { "quan" }, { "tiao" }, { "xing" },
            { "kan" }, { "lai" }, { "kua" }, { "guang" }, { "mi" }, { "an" },
            { "lu" }, { "mou" }, { "dong", "tong" }, { "gong" }, { "zai" },
            { "lv" }, { "jiao", "yao" }, { "zhen" }, { "ce", "ze", "zhai" },
            { "kuai" }, { "chai" }, { "nong" }, { "hou" }, { "jiong" },
            { "cuo" }, { "qin" }, { "nan" }, { "hao" }, { "bian", "pian" },
            { "tui" }, { "cu" }, { "e" }, { "kuang" }, { "ku" }, { "jun" },
            { "lang" }, { "zu" }, { "hun" }, { "pai" }, { "su" }, { "bao" },
            { "yu", "shu" }, { "si", "qi" }, { "xin", "shen" }, { "ti" },
            { "liang", "lia" }, { "xiao" }, { "biao" }, { "ti", "chu" },
            { "fei" }, { "zan" }, { "cui" }, { "lia", "liang" }, { "zong" },
            { "tian" }, { "dao" }, { "tan" }, { "chui" }, { "peng" },
            { "tang", "chang" }, { "jing", "liang" }, { "kong" }, { "juan" },
            { "wo" }, { "luo" }, { "song" }, { "leng" }, { "zi" }, { "ben" },
            { "cai" }, { "zhai" }, { "qing" }, { "ying" }, { "nuan" },
            { "chun" }, { "jie", "ji" }, { "ruo" }, { "pian" }, { "sheng" },
            { "huang" }, { "si", "cai" }, { "duan" }, { "ce", "ze" }, { "ou" },
            { "za", "zan" }, { "lou", "lv" }, { "kui", "gui" }, { "sou" },
            { "yuan" }, { "rong" }, { "ma" }, { "bang" }, { "dian" },
            { "shan" }, { "suo" }, { "bin" }, { "nuo" }, { "lei" }, { "zao" },
            { "ao" }, { "qiang" }, { "piao" }, { "man" }, { "zun" },
            { "liao" }, { "deng" }, { "fan" }, { "hui" }, { "tie" },
            { "seng" }, { "tong", "zhuang" }, { "jiang" }, { "min" },
            { "sai" }, { "dang" }, { "tai" }, { "lan" }, { "meng" },
            { "neng" }, { "qiong" }, { "lie" }, { "du" }, { "teng" },
            { "long" }, { "rang" }, { "zan", "cuan" }, { "xiong" },
            { "chong" }, { "zhao" }, { "dui" }, { "ke" }, { "mian", "wen" },
            { "tu" }, { "chang", "zhang" }, { "shi", "ke" }, { "dou" },
            { "mao" }, { "bai", "ke" }, { "nei" }, { "liu", "lu" },
            { "qi", "ji" }, { "zi", "ci" }, { "shou" }, { "ran" }, { "gang" },
            { "ce" }, { "gua" }, { "mao", "mo" }, { "zui" }, { "kou" },
            { "feng", "ping" }, { "mei" }, { "zhun" }, { "cou" }, { "kai" },
            { "kan", "qian" }, { "ao", "wa" }, { "chuang" }, { "qie" },
            { "cun" }, { "hua", "huai" }, { "yue" }, { "liu" }, { "bie" },
            { "bao", "pao" }, { "shua" }, { "quan", "xuan" }, { "cha", "sha" },
            { "lou" }, { "xue", "xiao" }, { "la" }, { "pou" },
            { "yan", "shan" }, { "bo", "bao" }, { "tuan", "zhuan" }, { "fou" },
            { "jiao", "chao" }, { "hua" }, { "cuan" }, { "mo" }, { "keng" },
            { "jing", "jin" }, { "le", "lei" }, { "weng" }, { "tao" },
            { "pao" }, { "da" }, { "nao" }, { "chi", "shi" }, { "za" },
            { "cang", "zang" }, { "suan" }, { "qu", "ou" }, { "bian" },
            { "nian" }, { "zu", "cu" }, { "dan", "chan", "shan" },
            { "nan", "na" }, { "shuai", "lv" }, { "bu", "bo" },
            { "ka", "qia" }, { "ang", "yang" }, { "que" }, { "juan", "quan" },
            { "chang", "han", "an" }, { "zhe" }, { "ce", "si" },
            { "sha", "xia" }, { "qin", "jin" }, { "chang", "an" },
            { "can", "cen", "shen", "san" }, { "can", "cen", "shen" },
            { "shuang" }, { "zhui" }, { "die" }, { "rui" }, { "ju", "gou" },
            { "tao", "dao" }, { "zhao", "shao" }, { "po" },
            { "ye", "xie", "she" }, { "mie" }, { "yu", "xu" }, { "mang" },
            { "he", "ge" }, { "he", "xia" }, { "a" }, { "xue" }, { "tun" },
            { "bi", "pi" }, { "fou", "pi" }, { "hang", "keng" }, { "shun" },
            { "zhi", "zi" }, { "chuo" }, { "ou", "hong", "hou" }, { "gao" },
            { "na", "ne" }, { "m" }, { "dai", "tai" }, { "bei", "bai" },
            { "yuan", "yun" }, { "guo" }, { "pen" }, { "ni", "ne" },
            { "gua", "gu" }, { "ci", "zi" }, { "he", "ke", "a", "ha" },
            { "ju", "zui" }, { "he", "huo" }, { "za", "ze", "zha" },
            { "he", "huo", "hu" }, { "ka", "ga" }, { "xi", "die" },
            { "ge", "ka", "lo", "luo" }, { "shuai" }, { "zan", "za" },
            { "ke", "hai" }, { "wai", "he", "wo", "wa", "gua", "guo" },
            { "yan", "ye" }, { "pin" }, { "ha" }, { "hui", "yue" }, { "yo" },
            { "e", "o" }, { "na", "nei", "ne", "nai" }, { "geng" }, { "shuo" },
            { "bai", "bei" }, { "miu" }, { "huan" }, { "hu", "xia" },
            { "zhou", "zhao" }, { "nou" }, { "ken" }, { "chuo", "chuai" },
            { "pa" }, { "se" }, { "nie" }, { "tan", "chan", "tuo" },
            { "die", "zha" }, { "re", "nuo" }, { "wo", "o" },
            { "can", "sun", "qi" }, { "zha", "cha" }, { "miao" }, { "cao" },
            { "a", "sha" }, { "ai", "yi" }, { "da", "ta" }, { "jia", "lun" },
            { "hai", "hei" }, { "ng", "n" }, { "dia" }, { "ga" },
            { "gu", "jia" }, { "xu", "shi" }, { "de", "dei" },
            { "zuo", "chuai" }, { "chao", "zhao" }, { "fu", "m" },
            { "hei", "mo" }, { "can" }, { "cheng", "ceng" }, { "o" },
            { "xue", "jue" }, { "huo", "o" }, { "xia", "he" }, { "ca", "cha" },
            { "xiao", "ao" }, { "jiao", "jue" }, { "ca" }, { "nin" },
            { "jian", "nan" }, { "tuan" }, { "dun", "tun" }, { "kun" },
            { "ri" }, { "quan", "juan" }, { "lve" }, { "yuan", "huan" },
            { "wei", "xu" }, { "di", "de" }, { "qia" }, { "qi", "yin" },
            { "huai", "pi", "pei" }, { "fo" }, { "chi", "di" },
            { "tong", "dong" }, { "huan", "yuan" }, { "cen" },
            { "mai", "man" }, { "pu", "bu" }, { "sao" }, { "peng", "beng" },
            { "duo", "hui" }, { "bao", "bu", "pu" }, { "ruan" },
            { "e", "ai", "ye" }, { "ta", "da" }, { "sai", "se" }, { "zang" },
            { "zeng" }, { "huai", "pi" }, { "zhuang" }, { "qiao", "ke" },
            { "qun" }, { "wai" }, { "da", "dai" }, { "hang", "ben" },
            { "ben", "tao" }, { "jia", "ga" }, { "en" }, { "zou" },
            { "qi", "xie", "qie" }, { "zhuang", "zang" }, { "nv", "ru" },
            { "niu" }, { "nai", "ni" }, { "kuo" }, { "lao", "mu" }, { "rao" },
            { "niang" }, { "na", "nuo" }, { "chou", "zhou" }, { "rou" },
            { "niao" }, { "han", "nan" }, { "nen" },
            { "huan", "qiong", "xuan" }, { "sun" }, { "bei", "bo" },
            { "chan", "can" }, { "wan", "yuan" }, { "jia", "gu", "jie" },
            { "kuan" }, { "su", "xiu" }, { "xun", "将" }, { "lv", "luo" },
            { "she", "ye", "yi" }, { "jiang", "qiang" }, { "wei", "yu" },
            { "xun", "xin" }, { "wang", "you" },
            { "long", "mang", "meng", "pang" }, { "chi", "che" },
            { "wei", "yi" }, { "niao", "sui" }, { "ceng" }, { "ping", "bing" },
            { "shu", "zhu" }, { "tun", "zhun" }, { "ang" }, { "zhi", "shi" },
            { "jiao", "qiao" }, { "ya", "ai" }, { "wei", "wai" },
            { "qian", "kan" }, { "ke", "jie" }, { "xi", "gui", "juan" },
            { "cha", "chai", "ci" }, { "xiang", "hang" }, { "shui" },
            { "chou", "dao" }, { "dao", "tao" }, { "sen" },
            { "chuang", "zhuang" }, { "guang", "an" }, { "du", "duo" },
            { "pang", "mang", "meng" }, { "bi", "bei" }, { "nong", "long" },
            { "zang", "zhuang" }, { "qiang", "jiang" }, { "dan", "tan" },
            { "pang", "fang" }, { "hen" }, { "cong", "zong" }, { "shi", "ti" },
            { "de" }, { "zhi", "zheng" }, { "te" }, { "te", "tui" },
            { "zhong", "song" }, { "zen" }, { "si", "sai" }, { "fu", "fei" },
            { "nen", "nin" }, { "nv" }, { "e", "wu" }, { "kui", "li" },
            { "re" }, { "kai", "qi" }, { "qian", "qie" }, { "te", "ni" },
            { "gang", "zhuang" }, { "xu", "qu" }, { "xi", "hu" },
            { "zha", "za" }, { "ba", "pa" }, { "fu", "fan" },
            { "kang", "gang" }, { "ban", "pan" }, { "zhua" }, { "zhe", "she" },
            { "ze", "zhai" }, { "mo", "ma" }, { "fu", "bi" }, { "chai", "ca" },
            { "tuo", "ta" }, { "ao", "niu" }, { "pan", "pin", "fan" },
            { "kuo", "gua" }, { "shuan" }, { "zhuai", "ye" }, { "shi", "she" },
            { "ru", "na" }, { "wo", "zhua" }, { "xie", "jia" },
            { "suo", "sa", "sha" }, { "jiao", "ku" }, { "qi", "xi" },
            { "gun" }, { "guai", "guo" }, { "dan", "shan" },
            { "chan", "shan", "can" }, { "ti", "di" }, { "xu", "ju" },
            { "chuai" }, { "she", "die", "ye" }, { "bang", "peng" },
            { "qiang", "cheng" }, { "guo", "guai" }, { "gai", "xi" },
            { "chan", "shan", "xian", "can" }, { "cuo", "zuo" },
            { "zhua", "wo" }, { "bo", "bai" }, { "zuan" }, { "dun", "dui" },
            { "shu", "shuo" }, { "yi", "du" }, { "tiao", "tou" }, { "hang" },
            { "wu", "yu" }, { "pang", "bang" }, { "wu", "mo" },
            { "xu", "kua" }, { "shai" }, { "sheng", "cheng" },
            { "jing", "ying" }, { "bao", "pu" }, { "pu", "bao" }, { "zhuai" },
            { "ceng", "zeng" }, { "pi", "bi" }, { "juan", "zui" },
            { "pu", "po", "piao" }, { "shan", "sha" }, { "shao", "biao" },
            { "pa", "ba" }, { "chou", "niu" }, { "zhi", "qi" },
            { "zong", "cong" }, { "gou", "ju" }, { "bao", "fu" },
            { "tuo", "duo" }, { "gui", "ju" }, { "zuo", "zha" },
            { "cha", "zha" }, { "zha", "shan" }, { "li", "yue" },
            { "gua", "kuo" }, { "bing", "ben" }, { "xiao", "jiao" },
            { "he", "hu" }, { "heng", "hang" }, { "jie", "ju" },
            { "gui", "hui" }, { "shao", "sao" }, { "bing", "bin" },
            { "pou", "bang", "bei" }, { "leng", "ling" }, { "zhao", "zhuo" },
            { "zhui", "chui" }, { "bei", "pi" }, { "shen", "zhen" },
            { "hu", "ku" }, { "shun", "dun" }, { "kai", "jie" },
            { "dian", "zhen" }, { "jian", "kan" }, { "bin", "bing" },
            { "qi", "se" }, { "mo", "mu" }, { "rao", "nao" }, { "run" },
            { "tong", "chuang" }, { "mi", "ni" }, { "ai", "ei" },
            { "yi", "qi", "ji" }, { "she", "xi" }, { "chou", "xiu" },
            { "yin", "yan" }, { "ke", "qiao" }, { "shi", "zhi" },
            { "mang", "meng" }, { "tang", "shang" }, { "shen", "chen" },
            { "dun", "zhuan" }, { "mei", "mo" }, { "bo", "po" },
            { "mi", "bi" }, { "long", "shuang" }, { "luo", "po" },
            { "xi", "xian" }, { "qie", "jie" }, { "pai", "pa" },
            { "qian", "jian" }, { "hu", "xu" }, { "jun", "xun" },
            { "yong", "chong" }, { "wo", "guo" }, { "ye", "yi" },
            { "biao", "hu" }, { "mian", "sheng" }, { "qiu", "jiao", "jiu" },
            { "kui", "hui" }, { "zhen", "qin" }, { "ni", "niao" },
            { "ying", "xing" }, { "luo", "ta", "lei" }, { "kuo", "huo" },
            { "liao", "lao" }, { "hei" }, { "cheng", "deng" },
            { "kuai", "hui" }, { "zui", "cui" }, { "dui", "wei" },
            { "gui", "jiong" }, { "gui", "que" }, { "pao", "bao" },
            { "luo", "lao" }, { "qu", "jun" }, { "zhuo", "chao" },
            { "nuan", "xuan" }, { "shu", "shou" }, { "yun", "yu" },
            { "zhua", "zhao" }, { "mou", "mu" }, { "jian", "qian" },
            { "an", "han" }, { "he", "hao", "mo" }, { "bai", "pi" },
            { "xie", "he" }, { "lv", "shuai" }, { "yang", "chang" },
            { "min", "wen" }, { "bin", "fen" }, { "hun", "hui" },
            { "zhuo", "zuo" }, { "xiang", "hong" }, { "qian", "wa" },
            { "ding", "ting" }, { "ting", "ding" }, { "chu", "xu" },
            { "fan", "pan" }, { "pi", "shu", "ya" }, { "nve", "yao" },
            { "dan", "da" }, { "chi", "zhi" }, { "jia", "xia" },
            { "chai", "cuo" }, { "lai", "ji" }, { "de", "di" },
            { "jiao", "jia" }, { "gai", "ge" }, { "sheng", "xing" },
            { "zhe", "zhuo", "zhao" }, { "ju", "qu" }, { "bin", "pin" },
            { "jin", "qin", "guan" }, { "shi", "dan" }, { "hua", "xu" },
            { "qi", "qie" }, { "la", "li" }, { "wei", "gui" }, { "luo", "ge" },
            { "shuo", "shi" }, { "bang", "pang" }, { "ji", "zhai" },
            { "shan", "chan" }, { "chan", "shan" }, { "zhong", "chong" },
            { "cheng", "chen" }, { "yin", "xun" }, { "ze", "zuo" },
            { "yun", "jun" }, { "jia", "ce" }, { "shi", "yi" }, { "xu", "yu" },
            { "cui", "sui" }, { "zhan", "nian" }, { "zhou", "yu" },
            { "san", "shen" }, { "gu", "yu" }, { "mi", "mei" }, { "xi", "ji" },
            { "yue", "yao" }, { "hong", "gong" }, { "xie", "jie" },
            { "beng", "bing", "peng" }, { "gei", "ji" }, { "zong", "zeng" },
            { "qi", "qing" }, { "lun", "guan" }, { "xian", "xuan" },
            { "suo", "su" }, { "fan", "po" },
            { "mou", "miao", "miu", "mu", "liao" }, { "zhou", "yao", "you" },
            { "qiao", "sao" }, { "jiao", "zhuo" }, { "xian", "qian" },
            { "ji", "gei" }, { "chuo", "chao" }, { "lv", "lu" },
            { "mou", "miao", "miu" }, { "ba", "pi" }, { "pi", "po", "bi" },
            { "zhai", "di" }, { "zhuan", "duan" }, { "lei", "le" },
            { "yu", "yo" }, { "pang", "pan" }, { "mo", "mai" },
            { "mai", "mo" }, { "fu", "pu" }, { "la", "xi" }, { "a", "yan" },
            { "chuo", "zhui" }, { "bei", "bi" }, { "la", "ge" },
            { "nao", "er" }, { "guang", "jiong" }, { "ban", "bo", "pan" },
            { "se", "shai" }, { "jie", "gai" }, { "yan", "yuan" },
            { "fei", "fu" }, { "zhu", "ning" }, { "di", "ti" },
            { "tiao", "shao" }, { "ruo", "re" }, { "ping", "peng" },
            { "qie", "jia" }, { "qian", "xi" }, { "yi", "ti" },
            { "hun", "xun" }, { "qian", "xun" }, { "sha", "suo" },
            { "guan", "wan" }, { "lang", "liang" }, { "fu", "piao" },
            { "wan", "yu", "yun" }, { "lu", "lv" }, { "luo", "la", "lao" },
            { "ye", "she", "xie" }, { "shen", "ren" }, { "gai", "ge", "he" },
            { "qiu", "xu", "fu" }, { "liao", "lu" }, { "man", "wan" },
            { "bo", "bu" }, { "yu", "wei" }, { "fan", "bo" },
            { "shen", "can", "cen", "san" }, { "lian", "xian" },
            { "zang", "cang" }, { "yao", "yue" }, { "nve" },
            { "chong", "hui" }, { "she", "yi" }, { "xia", "ha" },
            { "bang", "beng" }, { "qi", "zhi" }, { "qu", "ju" },
            { "ha", "ge" }, { "shao", "xiao" }, { "e", "yi" }, { "la", "zha" },
            { "you", "qiu" }, { "li", "xi" }, { "shi", "zhe" },
            { "xie", "xue" }, { "xing", "hang", "heng" }, { "shuai", "cui" },
            { "jia", "qia", "jie" }, { "shang", "chang" }, { "ti", "xi" },
            { "chu", "zhu", "zhe" }, { "tun", "tui" }, { "zhe", "xi" },
            { "tan", "qin" }, { "jian", "xian" }, { "jue", "jiao" },
            { "zui", "zi" }, { "jie", "xie" }, { "ne" }, { "tiao", "diao" },
            { "ei", "xi" }, { "shuo", "shui", "yue" }, { "du", "dou" },
            { "shui", "shei" }, { "diao", "tiao" },
            { "shuo", "shui", "yue", "tuo" }, { "ai", "ei", "xi" },
            { "bi", "ben" }, { "jia", "gu" }, { "zei" }, { "zhuan", "zuan" },
            { "ju", "qie" }, { "qu", "cu" }, { "bao", "bo" }, { "li", "luo" },
            { "xi", "qi" }, { "di", "zhi" }, { "dun", "cun" }, { "che", "ju" },
            { "ya", "zha", "ga" }, { "zhuan", "zhuai" }, { "hai", "huan" },
            { "zhe", "zhei" }, { "po", "pai" }, { "yi", "wei" },
            { "huan", "hai" }, { "na", "nei" },
            { "xie", "ye", "ya", "yu", "xu" }, { "xun", "huan" },
            { "dou", "du" }, { "dan", "zhen" }, { "zuo", "cu" },
            { "fa", "po" }, { "shi", "shai" }, { "xian", "xi" },
            { "yao", "diao", "tiao" }, { "hao", "gao" }, { "zu", "chuo" },
            { "qian", "yan" }, { "dang", "cheng" }, { "chan", "xin", "tan" },
            { "zhang", "chang" }, { "e", "yan" }, { "du", "she" },
            { "kan", "han" }, { "yan", "e" }, { "dian", "yan" }, { "a", "e" },
            { "po", "bei", "pi" }, { "lu", "liu" }, { "jiang", "xiang" },
            { "tao", "yao" }, { "wei", "kui" }, { "zhui", "cui", "wei" },
            { "juan", "jun" }, { "que", "qiao" }, { "jun", "juan" },
            { "lu", "lou" }, { "ge", "ji" }, { "man", "men" },
            { "qiao", "shao" }, { "eng" }, { "jing", "geng" },
            { "jie", "xie", "jia" }, { "zhan", "chan" }, { "dun", "du" },
            { "shi", "si", "yi" }, { "tai", "dai" }, { "piao", "biao" },
            { "tou", "shai" }, { "fu", "fo" }, { "li", "ge" },
            { "po", "tuo", "bo" }, { "gui", "xie", "wa", "kui" },
            { "qing", "zheng" }, { "ba", "bo" }, { "gui", "xie", "hua" },
            { "yiao" }, { "pian", "bin" }, { "gu", "hu" }, { "niao", "diao" },
            { "hu", "gu" }, { "gu", "hu", "he" }, { "jun", "qun" },
            { "ma", "me", "mo" }, { "mo", "me" }, { "min", "mian", "meng" },
            { "zi", "ji" }, { "yin", "ken" } };

    private static Hanzi2Pinyin sInstance = null;
    private Context mContext;

    private Hanzi2Pinyin(Context context) {
        mContext = context;
        if(context == null) {
            return;
        }

        try {
            InputStream pinyinTable = mContext.getResources().getAssets().open("hanzi.bin");

            int sum = 0;
            while (sum < HANZI_PINYIN_TABLE.length) {
                int len = pinyinTable.read(HANZI_PINYIN_TABLE, sum,
                        HANZI_PINYIN_TABLE.length - sum);
                if (len == -1) {
                    break;
                }
                sum += len;
            }
            // pinyinTable.read(HANZI_PINYIN_TABLE);
            if (pinyinTable != null) {
                pinyinTable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Hanzi2Pinyin getInstance(Context context) {
        synchronized (Hanzi2Pinyin.class) {
            if (sInstance != null) {
                return sInstance;
            }

            sInstance = new Hanzi2Pinyin(context);
            return sInstance;
        }
    }

    protected final int MIN_PINYIN_CHAR = 12295;
    protected final int MAX_PINYIN_CHAR = 40869;
    protected final int PINYIN_TABLE_START = (1 << 13);
    protected final int PINYIN_TABLE_LENGTH = PINYIN_TABLE_START
            + ((MAX_PINYIN_CHAR - MIN_PINYIN_CHAR + 1) << 1);
    protected final byte[] HANZI_PINYIN_TABLE = new byte[PINYIN_TABLE_LENGTH];

    public final boolean isHanzi(char src) {
        return (HANZI_PINYIN_TABLE[src >> 3] & (1 << (src & 0x7))) != 0;
    }

    public String getAsString(final String input) {
        if (input == null || input.length() == 0) {
            return "";
        }
        int size = input.length();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            String target[] = hanziToPinyin(input.charAt(i));
            if (target != null && target.length > 0) {
                sb.append(target[0]);
            } else {
                sb.append(input.charAt(i));
            }
        }

        return sb.toString();
    }

    public final String[] hanziToPinyin(char src) {
        if (src < MIN_PINYIN_CHAR || src > MAX_PINYIN_CHAR) {
            return new String[] { String.valueOf(src).toLowerCase() };
        }
        return PINYIN_TABLE_HEADER[readShort(HANZI_PINYIN_TABLE,
                PINYIN_TABLE_START + ((src - MIN_PINYIN_CHAR) << 1))];
    }

    public boolean isHanzi2(char src) {
        if (src < MIN_PINYIN_CHAR || src > MAX_PINYIN_CHAR) {
            return false;
        }
        return PINYIN_TABLE_HEADER[readShort(HANZI_PINYIN_TABLE,
                PINYIN_TABLE_START + ((src - MIN_PINYIN_CHAR) << 1))] != null;
    }

    private final short readShort(byte[] bytes, int offset) {
        short x = 0;
        for (int i = 0; i < 2; i++) {
            x = (short) ((x << 8) | (bytes[offset++] & 0xFF));
        }
        return x;
    }
}
