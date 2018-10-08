package cn.yc.library.bean

import android.graphics.Color

class AdapterBean {

    var type=0
    var data= arrayListOf<AdapterData>()

    class AdapterData{
        var title=""
        var color=Color.parseColor("#3c3c3c")
        var selected=false
        var checkHeader=false
        override fun toString(): String {
            return "AdapterData(title='$title', color=$color, selected=$selected, checkHeader=$checkHeader)"
        }
    }

    constructor()

    constructor(t:Int,bg:Boolean,list:ArrayList<AdapterData>){
        type=t
        data=list
    }
    constructor(t:Int,bg:Boolean){
        type=t
    }
    constructor(t:Int){
        type=t
    }


}