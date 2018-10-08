package cn.yc.library.bean

class AttendanceBean(val title:String,val content:String){

    var containsRest=false

    var type=0 //0：代表正常就是不显示 1：请假 2：休息 3：出勤 4:没有出勤

    var header=false
    set(value){
        if(header){
            empty=false
        }
        field=value
    }

    var empty=false
    set(value) {
        if(value){
            header=false
        }
        field=value
    }

    constructor():this("","")

    constructor(title:String):this(title,"")

    constructor(title:String,empty:Boolean):this(title,""){
        this.empty=empty
    }

    constructor(title:String,type:Int):this(title,""){
        this.type=type
    }
}