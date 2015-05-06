package com.jnsw.android.kotlin

/**
 * Created by foxundermoon on 2015/5/4.
 */

class KotObj(val id: Int, val name: String) {
    fun sayHello(msg: String): String {
        return "hello$id $name $msg";
    }
}