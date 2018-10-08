package com.ysgj.order

import android.util.Log
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class Test {
    @Test
    fun test(){
        val a="Mon;Tue9:00,10:00,11:00,;Wed9:00,10:00,11:00,14:00,17:00,20:00,;Thur;Fri;Sat;Sun9:00,10:00,11:00,"
        var nextInt=a.indexOf(";")
        val list= arrayListOf<String>()
        list.add(a.substring(0,nextInt))
        while (nextInt!=-1){
            val start=nextInt
            nextInt=a.indexOf(";",start+1)
            if(nextInt!=-1){
                list.add(a.substring(start+1,nextInt))
            }
        }
        Log.e("activity","fbcfvb $list")

    }
}
