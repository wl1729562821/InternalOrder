package cn.yc.library.bean

import android.net.Uri

class CameraBean {
    var requestResultCode=-1
    var imgPath=""

    var imgUri:Uri?=null
    override fun toString(): String {
        return "CameraBean(requestResultCode=$requestResultCode, imgPath='$imgPath', imgUri=$imgUri)"
    }
}