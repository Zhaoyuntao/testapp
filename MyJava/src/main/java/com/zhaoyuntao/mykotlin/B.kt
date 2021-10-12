package com.zhaoyuntao.mykotlin

/**
 * created by zhaoyuntao
 * on 20/09/2021
 * description:
 */
data class B constructor(var aa: String, val dd: String) : MySingleTon("aaaaaaaaa") {
    var a: String? = null
    var cc: String? = null
        get() {
            println("getCC")
            return field
        }
        set(value) {
            println("setCC")
            field = value
        }
    var bb = "bbb"

    val find: (Int) -> String = {
        "$it"
    }
    val age = 12
    fun getAge(aaaa: (Int) -> String) {
        println(aaaa(age))
    }

    fun check(checkFun: (Int) -> Boolean): Boolean {
        return checkFun(age)
    }

    init {
        cc = aa
        bb = dd
//        var a1 = false
//        a1 = aa.isEmpty()
//        var a = a1 ?: "${bb}a"
    }

    operator fun plus(b: B): B {
        return B(aa, b.dd);
    }
}