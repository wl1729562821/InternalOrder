package cn.yc.library.bean

import android.os.Handler
import android.util.Log
import android.widget.TextView

class TimeBean{

    private val mOtherViewList= hashMapOf<String,TextView>()

    fun addOtherView(textView: TextView){
        Log.e("TimeBean","addOtherView ${textView.context}")
        if(startTimeInt<=0){
            textView.text="00:00:00"
        }
        if(mOtherViewList.containsKey(textView.context.javaClass.simpleName)){
            mOtherViewList["${textView.context.javaClass.simpleName}1"] = textView
        }else{
            mOtherViewList[textView.context.javaClass.simpleName] = textView
        }
    }

    fun removeOtherView(tag:String){
        mOtherViewList.filter { it.key.contains(tag) }.forEach {
            Log.e("TimeBean","removeOtherView ${it.key}")
            mOtherViewList.remove(it.key)
        }
    }

    var position:Int=0

    var startTime=""
    var endTime=""

    var startTimeInt=0
    var endTimeInt=0

    var checkPost=false

    var mHnalder: Handler?=null
    var textView: TextView?=null

    var runnable: Runnable = object : Runnable {
        override fun run() {
            startTimeInt--
            if(startTimeInt<=0){
                textView?.text = "00:00:00"
                mOtherViewList.forEach {
                    it.value?.text="00:00:00"
                }
                mOtherViewList.forEach {
                    it.value?.text="00:00:00"
                }
                mHnalder?.removeCallbacks(this)
                return
            }
            var value=getValue()

            textView?.text = value
            mOtherViewList.forEach {
                it.value?.text=value
            }
            if (startTimeInt > 1) {
                mHnalder?.postDelayed(this, 1000)
            }
        }
    }

    private fun getValue():String{
        var value=""

        val formatLongToTimeStr = formatLongToTimeStr(startTimeInt)
        val split = formatLongToTimeStr.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

        value+=split[0] + ":"
        value+=split[1] + ":"
        value+=split[2]
        return value
    }

    fun run(index:Int){
        if((checkPost || index!=position)&&index!=-1){
            return
        }
        checkPost=true
        mHnalder?.postDelayed(runnable,1000)
    }

    fun formatLongToTimeStr(l: Int): String {
        var hour = 0
        var minute = 0
        var second = 0
        second = l!!.toInt()
        if (second > 60) {
            minute = second / 60         //取整
            second = second % 60         //取余
        }

        if (minute > 60) {
            hour = minute / 60
            minute = minute % 60
        }
        return hour.toString() + ":" + minute + ":" + second

    }
}