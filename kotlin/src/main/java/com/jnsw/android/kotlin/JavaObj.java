package com.jnsw.android.kotlin;

/**
 * Created by foxundermoon on 2015/5/4.
 */
public class JavaObj {
    int id;
    String name;

    public String sayHello(String msg) {
        return "hello" + id + name +msg;
    }
    public String  callKotlinFun(){
        KotObj kotObj = new KotObj(1,"kotlin");
        return kotObj.sayHello("hello world");
    }
}
