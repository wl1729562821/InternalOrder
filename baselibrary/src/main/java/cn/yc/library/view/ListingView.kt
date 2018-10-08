package cn.yc.library.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import cn.yc.library.R
import cn.yc.library.utils.get
import cn.yc.library.utils.setStringRes
import kotlinx.android.synthetic.main.view_listing.view.*
import kotlin.collections.set

class ListingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private val TAG="ListingView"

    private val mData= hashMapOf<Int,Boolean>().apply {
        put(0,false)
        put(1,false)
        put(2,true)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        mData[2] = enabled
    }

    fun setListingData(houseConfigDictcode:String){
        houseConfigDictcode.split(",").forEach {value->
            for (it in 0 until mSelectorDataTwoMap.size) {
                if(mSelectorDataTwoMap[it]==value){
                    mViewListTwo[it].setBackgroundResource(mSelectedImgTwo[it])
                    mSelectorTwoMap[it]=true
                }
            }
        }
    }

    fun setHousesData(houseConfigDictcode:String){
        houseConfigDictcode.split(",").forEach {value->
            for (it in 0 until mSelectorDataMap.size) {
                if(mSelectorDataMap[it]==value){
                    mViewList[it].setBackgroundResource(mSelectedImg[it])
                    mSelectorMap[it]=true
                }
            }
        }
    }

    private val mSelectedImg = arrayListOf<Int>().apply {
        add(R.mipmap.package_img1)
        add(R.mipmap.package_img2)
        add(R.mipmap.package_img3)
        add(R.mipmap.package_img4)
        add(R.mipmap.packages_img5)
        add(R.mipmap.package_img11)
        add(R.mipmap.package_img12)
        add(R.mipmap.package_img13)
        add(R.mipmap.package_img14)
        add(R.mipmap.package_img15)
        add(R.mipmap.package_img21)
        add(R.mipmap.package_img22)
        add(R.mipmap.package_img23)
        add(R.mipmap.package_img24)
    }
    private val mUnSelectedImg = arrayListOf<Int>().apply {
        add(R.mipmap.package_img1_un)
        add(R.mipmap.package_img2_un)
        add(R.mipmap.package_img3_un)
        add(R.mipmap.package_img4_un)
        add(R.mipmap.package_img5_un)
        add(R.mipmap.package_img11_un)
        add(R.mipmap.package_img12_un)
        add(R.mipmap.package_img13_un)
        add(R.mipmap.package_img14_un)
        add(R.mipmap.package_img15_un)
        add(R.mipmap.package_img21_un)
        add(R.mipmap.package_img22_un)
        add(R.mipmap.package_img23_un)
        add(R.mipmap.package_img24_un)
    }

    private val mSelectedImgTwo = arrayListOf<Int>().apply {
        add(R.mipmap.houses_img1)
        add(R.mipmap.houses_img2)
        add(R.mipmap.houses_img3)
        add(R.mipmap.houses_img4)
        add(R.mipmap.houses_img5)
        add(R.mipmap.houses_img11)
        add(R.mipmap.houses_img12)
        add(R.mipmap.houses_img13)
        add(R.mipmap.houses_img14)
        add(R.mipmap.houses_img15)
        add(R.mipmap.houses_img21)
        add(R.mipmap.houses_img22)
        add(R.mipmap.houses_img23)
        add(R.mipmap.houses_img24)
        add(R.mipmap.houses_img25)
        add(R.mipmap.houses_img31)
        add(R.mipmap.houses_img32)
        add(R.mipmap.houses_img33)
        add(R.mipmap.houses_img34)
        add(R.mipmap.houses_img35)
        add(R.mipmap.houses_img41)
        add(R.mipmap.houses_img42)
        add(R.mipmap.houses_img43)
        add(R.mipmap.houses_img44)
        add(R.mipmap.houses_img45)
        add(R.mipmap.houses_img51)
        add(R.mipmap.houses_img52)
        add(R.mipmap.houses_img53)
        add(R.mipmap.houses_img54)
        add(R.mipmap.houses_img55)
        add(R.mipmap.houses_img61)
        add(R.mipmap.houses_img62)
    }

    private val mUnSelectedImgTwo = arrayListOf<Int>().apply {
        add(R.mipmap.houses_img1_un)
        add(R.mipmap.houses_img2_un)
        add(R.mipmap.houses_img3_un)
        add(R.mipmap.houses_img4_un)
        add(R.mipmap.houses_img5_un)
        add(R.mipmap.houses_img11_un)
        add(R.mipmap.houses_img12_un)
        add(R.mipmap.houses_img13_un)
        add(R.mipmap.houses_img14_un)
        add(R.mipmap.houses_img15_un)
        add(R.mipmap.houses_img21_un)
        add(R.mipmap.houses_img22_un)
        add(R.mipmap.houses_img23_un)
        add(R.mipmap.houses_img24_un)
        add(R.mipmap.houses_img25_un)
        add(R.mipmap.houses_img31_un)
        add(R.mipmap.houses_img32_un)
        add(R.mipmap.houses_img33_un)
        add(R.mipmap.houses_img34_un)
        add(R.mipmap.houses_img35_un)
        add(R.mipmap.houses_img41_un)
        add(R.mipmap.houses_img42_un)
        add(R.mipmap.houses_img43_un)
        add(R.mipmap.houses_img44_un)
        add(R.mipmap.houses_img45_un)
        add(R.mipmap.houses_img51_un)
        add(R.mipmap.houses_img52_un)
        add(R.mipmap.houses_img53_un)
        add(R.mipmap.houses_img54_un)
        add(R.mipmap.houses_img55_un)
        add(R.mipmap.houses_img61_un)
        add(R.mipmap.houses_img62_un)
    }

    private val mViewList= arrayListOf<ImageView>()
    private val mViewListTwo= arrayListOf<ImageView>()

    private val mSelectorMap = hashMapOf<Int, Boolean>().apply {
        for (i in 0..13) {
            put(i, true)
        }
    }
    private val mSelectorTwoMap = hashMapOf<Int, Boolean>().apply {
        for (i in 0..31) {
            put(i, true)
        }
    }
    private val mSelectorDataMap = hashMapOf<Int,String>().apply {
        for (i in 0..11) {
            val value=20076+i+1
            put(i,value.toString())
        }
        put(12,"20090")
        put(13,"20089")
    }

    private val mSelectorDataTwoMap = hashMapOf<Int,String>().apply {
        for (i in 0..31) {
            val value=20020+i+1
            put(i,value.toString())
        }
    }

    private val mStringMap= hashMapOf<Int,ArrayList<Int>>().apply {
        put(0, arrayListOf<Int>().apply {
            add(R.string.houses_type1)
            add(R.string.houses_type2)
            add(R.string.houses_type3)
            add(R.string.houses_type4)
            add(R.string.houses_type5)
        })
        put(1, arrayListOf<Int>().apply {
            add(R.string.houses_type6)
            add(R.string.houses_type7)
            add(R.string.houses_type8)
            add(R.string.houses_type9)
            add(R.string.houses_type10)
        })
        put(2, arrayListOf<Int>().apply {
            add(R.string.houses_type11)
            add(R.string.houses_type12)
            add(R.string.houses_type13)
            add(R.string.houses_type14)
            add(R.string.houses_type15)
        })
        put(3, arrayListOf<Int>().apply {
            add(R.string.houses_type16)
            add(R.string.houses_type17)
            add(R.string.houses_type18)
            add(R.string.houses_type19)
            add(R.string.houses_type20)
        })
        put(4, arrayListOf<Int>().apply {
            add(R.string.houses_type21)
            add(R.string.houses_type22)
            add(R.string.houses_type23)
            add(R.string.houses_type24)
            add(R.string.houses_type25)
        })
        put(5, arrayListOf<Int>().apply {
            add(R.string.houses_type26)
            add(R.string.houses_type27)
            add(R.string.houses_type28)
            add(R.string.houses_type29)
            add(R.string.houses_type30)
        })
        put(6, arrayListOf<Int>().apply {
            add(R.string.houses_type31)
            add(R.string.houses_type32)
            add(R.string.houses_type32)
            add(R.string.houses_type32)
            add(R.string.houses_type32)
        })
    }

    init {
        init()
    }

    private fun init() {
        val setIndex:(Int,RelativeLayout)->Unit={index,view->
            if(mViewList.size<14){
                mViewList.add(view.getChildAt(0) as ImageView)
            }
            Log.e(TAG,"setIndex ${mSelectorMap[index]} $index")
            view.getChildAt(0).setBackgroundResource(if (mSelectorMap[index] == true) {
                mUnSelectedImg[index]
            } else {
                mSelectedImg[index]
            })
            mSelectorMap[index] = !(mSelectorMap[index] ?: false)
        }
        val setIndexTwo:(Int,RelativeLayout)->Unit={index,view->
            if(mViewListTwo.size<32){
                mViewListTwo.add(view.getChildAt(0) as ImageView)
            }
            Log.e(TAG,"setIndex ${mSelectorTwoMap[index]} $index")
            view.getChildAt(0).setBackgroundResource(if (mSelectorTwoMap[index] == true) {
                mUnSelectedImgTwo[index]
            } else {
                mSelectedImgTwo[index]
            })
            mSelectorTwoMap[index] = !(mSelectorTwoMap[index] ?: false)
        }
        addView(LayoutInflater.from(context).inflate(R.layout.view_listing,null,false).apply {
            package_sq_tv.setOnClickListener {
                if(mData[0]==false){
                    package_img.visibility=View.GONE
                }else{
                    package_img.visibility=View.VISIBLE
                }
                mData[0] = mData[0]==false
            }
            listing_header_tv.text="${context.getString(R.string.houses_tv37)}A"
            listing_header_tv2.text="${context.getString(R.string.houses_tv37)}B"
            sq_tv.setOnClickListener {
                if(mData[1]==false){
                    content_img.visibility=View.GONE
                }else{
                    content_img.visibility=View.VISIBLE
                }
                mData[1] = mData[1]==false
            }

            setIndex(0, package_img1)
            setIndex(1, package_img2)
            setIndex(2, package_img3)
            setIndex(3, package_img4)
            setIndex(4, package_img5)
            setIndex(5, package_img11)
            setIndex(6, package_img12)
            setIndex(7, package_img13)
            setIndex(8, package_img14)
            setIndex(9, package_img15)
            setIndex(10, package_img21)
            setIndex(11, package_img22)
            setIndex(12, package_img23)
            setIndex(13, package_img24)
            package_img1.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(0, package_img1)
            }
            package_img2.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(1, package_img2)
            }
            package_img3.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(2, package_img3)
            }
            package_img4.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(3, package_img4)
            }
            package_img5.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(4, package_img5)
            }

            package_img11.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(5, package_img11)
            }
            package_img12.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(6, package_img12)
            }
            package_img13.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(7, package_img13)
            }
            package_img14.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(8, package_img14)
            }
            package_img15.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(9, package_img15)
            }
            package_img21.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(10, package_img21)
            }
            package_img22.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(11, package_img22)
            }
            package_img23.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(12, package_img23)
            }
            package_img24.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndex(13, package_img24)
            }

            val setClick:(LinearLayout,Int)->Unit={view,pageIndex->
                (0 until view.childCount).forEach { index->
                    val x=if(pageIndex==0 && index==0){
                        0
                    }else if(pageIndex==0 && index!=0){
                        index
                    }
                    else{
                        pageIndex+index
                    }
                    setIndexTwo(x,view[index] as RelativeLayout)
                    view[index]?.setOnClickListener {
                        if(mData[2]==false){
                            return@setOnClickListener
                        }
                        setIndexTwo(x,view[index] as RelativeLayout)
                    }
                }
            }
            setClick(parent1,0)
            setClick(parent2,5)
            setClick(parent3,10)
            setClick(parent4,15)
            setClick(parent5,20)
            setClick(parent6,25)
            setIndexTwo(30,parent7[0] as RelativeLayout)
            setIndexTwo(31,parent7[1] as RelativeLayout)
            parent7[0]?.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndexTwo(30,parent7[0] as RelativeLayout)
            }
            parent7[1]?.setOnClickListener {
                if(mData[2]==false){
                    return@setOnClickListener
                }
                setIndexTwo(31,parent7[1] as RelativeLayout)
            }

            val setValue:(LinearLayout,ArrayList<Int>)->Unit={view,list->
                (0 until view.childCount).forEach {
                    (view[it] as? TextView)?.setStringRes(list[it])
                }
            }
            setValue(listing_tv1,mStringMap[0]!!)
            setValue(listing_tv2,mStringMap[1]!!)
            setValue(listing_tv3,mStringMap[2]!!)
            setValue(listing_tv4,mStringMap[3]!!)
            setValue(listing_tv5,mStringMap[4]!!)
            setValue(listing_tv6,mStringMap[5]!!)
            setValue(listing_tv7,mStringMap[6]!!)
        })
    }

    private fun getString(data:HashMap<Int, Boolean>,stringData:HashMap<Int,String>):String{
        var add=""
        for (i in 0 until data.size) {
            if (data[i] == true) {
                add += "${stringData[i]},"
            }
        }
        return add
    }

    fun getListing():String=getString(mSelectorTwoMap,mSelectorDataTwoMap)

    fun getHouses():String=getString(mSelectorMap,mSelectorDataMap)
}
