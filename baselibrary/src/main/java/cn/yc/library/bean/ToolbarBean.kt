package cn.yc.library.bean

class ToolbarBean(var title:String,var backTitle:String,var setting:Boolean){


    constructor():this("","",false)

    var back:Boolean=true

    var settingTitle=""

    var settingsDrawable=-1
}