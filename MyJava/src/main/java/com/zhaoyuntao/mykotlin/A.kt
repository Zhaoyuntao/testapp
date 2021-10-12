package com.zhaoyuntao.mykotlin

fun main() {
//    val a=A();
//    println(MySingleTon.cc)
//    repeat(2){
//        print("11112");
//    }

//    for (a in list) {
//        println(a)
//////    a.cc="aaaajjjj"
//////        print(a.cc)
////        println()
//    }
//    var a = mutableMapOf<String, Int>("a" to 1, "b" to 2, "2" to 2);
//    show()
//    abc(1,2,3,b="a")
//    val b = B("aa", "aaa")
//    val b2 = B("bb", "bbb")
//
////    println(b.find(12322))
//    var result = b2.check { it > 13 }
//    println(result)
//
//    var b3=B("ab","cd");
//    b3=b3.apply {
//        aa="aaaa"
//    }
//    println(b3.aa)
//
//    val c=b3 as B
//    val c2=b3 as? B
//    println((b3 as B).cc)

        println(aaaaa())

}
val aa=2
fun aaaaa():Boolean{
    var a= mutableMapOf<Int,Int>(1 to 2,2 to 3)
    var b= mutableMapOf<Int,Int>(1 to 2 ,2 to 3, 3 to 4)
    var name=4
    return synchronized(aa){
        a.get(name)
    }?:b.get(name)!=null
}

fun abc(vararg a: Int, b: String) {
    for (s in a) {
        println(s)
    }
}

//fun show():String{
//    val list: MutableList<Int> = (1..19).toMutableList()
//    abc@for(i in list){
//        for(i in list){
//            println("$i aaa")
//            if(i==3){
//                return
//            }
//        }
//        println(i)
//    }
//    return "end"
//}

/**
 * created by zhaoyuntao
 * on 19/09/2021
 * description:
 */
class A constructor(var b: String = "abc", var bb: Int = 2, var bbb: String = "abc") {
    init {
//        b="1"
//        val a=2
//        println(23 add 2)
        testSet()
        var a = 100;
        while (a-- > 0) {
            a = a add {
                a--
            }
            println("a:" + a)
        }
    }

    //    constructor():this(bbb="a",bb=2,b="1"){
//        println("kkkkk")
//    }
//    init {
//        println(b+"d")
//    }
//
//    fun abcd(vararg a:Int ,  b:String="mm",c:String="1"){
//
//    }
//
    inline infix fun <A> Int.add(block: () -> A): A = block()

    fun testSet() {
        val a: MutableSet<String> = mutableSetOf()
        a.add("1")
        println(a)
    }
}