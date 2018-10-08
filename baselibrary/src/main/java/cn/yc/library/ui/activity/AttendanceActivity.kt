package cn.yc.library.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.bean.AttendanceBean
import cn.yc.library.bean.ToolbarBean
import cn.yc.library.bean.response.AttendanceResponse
import cn.yc.library.bean.response.BaseResponse
import cn.yc.library.data.toolbarInit
import cn.yc.library.ui.adapter.AttendanceAdapter
import cn.yc.library.utils.getTime
import cn.yc.library.utils.getWeek
import cn.yc.library.vm.OrderViewModel
import kotlinx.android.synthetic.main.activity_attendance.*


class AttendanceActivity:BaseActivity(){

    private var mViewModel: OrderViewModel?=null
    private var mAdapter: AttendanceAdapter?=null

    override val layoutId: Int
        get() = R.layout.activity_attendance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel=mViewModel?:getViewModel()
        toolbarInit {
            mToolbar=toolbar
            data= ToolbarBean(getString(R.string.toolbar_title_1),"",false).apply {
            }
            onBack {
                finish()
            }
        }
        attendance_rv?.apply {
            layoutManager= LinearLayoutManager(this@AttendanceActivity)
            mAdapter=AttendanceAdapter(this@AttendanceActivity)
            adapter=mAdapter
        }
        refresh_layout?.setOnRefreshListener {
            request()
        }
        refresh_layout.isRefreshing=true
        request()
    }

    override fun onError(code: Int, throwable: Throwable) {
        refresh_layout.isRefreshing=false
    }

    override fun <T> onNext(dat: T) {
        refresh_layout.isRefreshing=false
        ((dat as? BaseResponse<*>)?.data as? AttendanceResponse)?.also{ data->
            Log.e(TAG,"onNext $this")
            var time=""
            data.dataSet?.attendance?.forEach {
                time=it.postTime
            }
            data.dataSet?.vacates?.forEach {
                time=it.beginTime
            }
            if(time.length<5){
                time=data.systemTime
            }
            val timeList=time.split("-")
            val size= getTime(timeList[0].toInt(),timeList[1].toInt())
            val lastSize= getTime(timeList[0].toInt(),timeList[1].toInt()-1)
            //获取当月第一天的星期
            val week= getWeek(timeList[0].toInt(),timeList[1].toInt())

            Log.e(TAG,"hehe ${timeList[0].toInt()} ${timeList[1].toInt()} $size $lastSize  $week")

            val arraList= arrayListOf<ArrayList<AttendanceBean>>().apply {
                add(arrayListOf<AttendanceBean>().apply {
                    add(AttendanceBean(getString(R.string.date_tv1),"").apply {
                        header=true
                    })
                    add(AttendanceBean(getString(R.string.date_tv2)).apply {
                        header=true
                    })
                    add(AttendanceBean(getString(R.string.date_tv3)).apply {
                        header=true
                    })
                    add(AttendanceBean(getString(R.string.date_tv4)).apply {
                        header=true
                    })
                    add(AttendanceBean(getString(R.string.date_tv5)).apply {
                        header=true
                    })
                    add(AttendanceBean(getString(R.string.date_tv6)).apply {
                        header=true
                    })
                    add(AttendanceBean(getString(R.string.date_tv7)).apply {
                        header=true
                    })
                })
                add(arrayListOf<AttendanceBean>().apply {
                    var leaveCount=0
                    (1..7).forEach {
                        val title=if(it<week){
                            lastSize-week+it
                        }else{
                            it-week+1
                        }
                        add(AttendanceBean("$title").apply {
                            if(it<week){
                                empty=true
                            }else{
                                empty=false
                                header=false
                                //获取正常上班记录是不是有当前天数
                                data.dataSet?.attendance?.filter {bean->
                                    Log.e(TAG,"hhe ${bean.postTime.split("-")[2].substring(0,2)}")
                                    bean.postTime.split("-")[2].substring(0,2).toInt()==title}?.forEach {
                                    type=3
                                }
                                //如果没有出勤再去判断请假记录
                                if(type!=3){
                                    //如果有请假记录就算请假再去算天数
                                    data.dataSet?.vacates?.filter {bean->
                                        bean.beginTime.split("-")[2].substring(0,2).toInt()==title}?.forEach {
                                        type=1
                                        //获取请假天数
                                        leaveCount=it.days
                                    }
                                    //如果没有请假再也没有出勤那么去判断是不是周末
                                    if(type!=1 && (it==1 || it==7)){
                                        //如果是周末那么就是休息
                                        type=2
                                        if(leaveCount>0){
                                            leaveCount--
                                        }
                                    }else{
                                        type=4
                                        if(leaveCount>0){
                                            leaveCount--
                                        }
                                    }
                                }
                            }
                        })
                    }
                })
                var end=0
                val count=(size-(7-week+1))/7
                Log.e(TAG,"hvxv $count $week $size $lastSize")
                (1..count).forEach { index->
                    val start=7*index-week+1
                    var leaveCount=0
                    add(arrayListOf<AttendanceBean>().apply {
                        (1..7).forEach {
                            end=it+start
                            add(AttendanceBean("${it+start}").apply {
                                empty=false
                                header=false
                                //获取正常上班记录是不是有当前天数
                                data.dataSet?.attendance?.filter {bean->
                                    bean.postTime.split("-")[2].substring(0,2).toInt()==it+start}?.forEach {
                                    type=3
                                }
                                //如果没有出勤再去判断请假记录
                                if(type!=3){
                                    //如果有请假记录就算请假再去算天数
                                    data.dataSet?.vacates?.filter {bean->
                                        bean.beginTime.split("-")[2].substring(0,2).toInt()==it+start}?.forEach {
                                        type=1
                                        //获取请假天数
                                        leaveCount=it.days
                                    }
                                    //如果没有请假再也没有出勤那么去判断是不是周末
                                    if(type!=1 && (it==1 || it==7)){
                                        //如果是周末那么就是休息
                                        type=2
                                        if(leaveCount>0){
                                            leaveCount--
                                        }
                                    }else{
                                        type=4
                                        if(leaveCount>0){
                                            leaveCount--
                                        }
                                    }
                                }
                            })
                        }
                    })
                }
                val remaining=size-end
                Log.e(TAG,"dxgvxdcv $remaining")
                if(remaining>0){
                    add(arrayListOf<AttendanceBean>().apply {
                        var leaveCount=0
                        var index=1
                        for(it in 1..7){
                            var start=0
                            Log.e(TAG,"dxgvxdcv $remaining $it")
                            if(remaining>=it){
                                start = end+it
                            }else{
                                start=index
                                index++
                            }
                            add(AttendanceBean("$start").apply {
                                if(end+it<=size){
                                    empty=false
                                    header=false
                                    //获取正常上班记录是不是有当前天数
                                    data.dataSet?.attendance?.filter {bean->
                                        bean.postTime.split("-")[2].substring(0,2).toInt()==start && remaining>it}?.forEach {
                                        type=3
                                    }
                                    //如果没有出勤再去判断请假记录
                                    if(type!=3){
                                        //如果有请假记录就算请假再去算天数
                                        data.dataSet?.vacates?.filter {bean->
                                            bean.beginTime.split("-")[2].substring(0,2).toInt()==start && remaining>it}?.forEach {
                                            type=1
                                            //获取请假天数
                                            leaveCount=it.days
                                        }
                                        //如果没有请假再也没有出勤那么去判断是不是周末
                                        if(type!=1 && (it==1 || it==7)){
                                            //如果是周末那么就是休息
                                            type=2
                                            if(leaveCount>0){
                                                leaveCount--
                                            }
                                        }else{
                                            type=4
                                            if(leaveCount>0){
                                                leaveCount--
                                            }
                                        }
                                    }
                                }else{
                                    empty=true
                                }
                            })
                        }
                    })
                }
                add(arrayListOf<AttendanceBean>())
            }
            mAdapter?.refresh(arraList,(timeList as? ArrayList<String>)?: arrayListOf())

            //val list= arrayListOf<>().apply {  }
        }
    }

    private fun request(){
        mViewModel?.getAttendance()
    }
}