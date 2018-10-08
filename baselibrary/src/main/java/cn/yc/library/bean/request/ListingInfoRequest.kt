package cn.yc.library.bean.request

class ListingInfoRequest {

    var type=-1 //0：出租 1：出售
    var lastType=-1
    var id=""

    var listingType=-1//0:poa

    var listing=0  //房源归属选择

    var poiCopyImg1="" //poa复印件照片1
    var poiCopyImg2="" //poa复印件照片1
    val imgList_1= arrayListOf<String>().apply {
        for(i in 0..9){
            add("")
        }
    } //poa复印件照片1

    val imgList_10= arrayListOf<String>().apply {
        for(i in 0..9){
            add("")
        }
    }

    var wtrImg1=""
    var wtrImg2=""
    val imgList_2= arrayListOf<String>().apply {
        for(i in 0..2){
            add("")
        }
    } //poa复印件照片1
    val imgList_3= arrayListOf<String>().apply {
        for(i in 0..2){
            add("")
        }
    } //poa复印件照片1
    val imgList_4= arrayListOf<String>().apply {
        for(i in 0..3){
            add("")
        }
    } //poa复印件照片1
    val imgList_5= arrayListOf<String>().apply {
        for(i in 0..2){
            add("")
        }
    } //poa复印件照片1
    val imgList_6= arrayListOf<String>().apply {
        for(i in 0..2){
            add("")
        }
    } //poa复印件照片1
    val imgList_7= arrayListOf<String>().apply {
        for(i in 0..2){
            add("")
        }
    } //poa复印件照片1
    val imgList_8= arrayListOf<String>().apply {
        for(i in 0..3){
            add("")
        }
    } //poa复印件照片1

    var bwtrImg1=""
    var bwtrImg2=""

    var bwtr1Img1=""
    var bwtr2Img2=""

    var fwcqImg1="" //房屋产权
    var fwcqImg2=""

    var fwcqzmrImg1="" //房屋产权护照
    var fwcqzmrImg2=""

    var fwhxImg1=""
    var fwhxImg2=""

    var formImg1=""
    var formImg2=""

    var fwzpImg1=""
    var fwzpImg2=""

    fun refresh(){
        imgList_1.clear()
        imgList_2.clear()
        imgList_3.clear()
        imgList_4.clear()
        imgList_5.clear()
        imgList_6.clear()
        imgList_7.clear()
        imgList_10.clear()
        poiCopyImg1="" //poa复印件照片1
        poiCopyImg2="" //poa复印件照片1

        wtrImg1=""
        wtrImg2=""

        bwtrImg1=""
        bwtrImg2=""

        bwtr1Img1=""
        bwtr2Img2=""

        fwcqImg1=""
        fwcqImg2=""

        fwcqzmrImg1=""
        fwcqzmrImg2=""

        fwhxImg1=""
        fwhxImg2=""

        formImg1=""
        formImg2=""
    }

    override fun toString(): String {
        return "ListingInfoRequest(type=$type, lastType=$lastType, id='$id', listingType=$listingType, listing=$listing, poiCopyImg1='$poiCopyImg1', poiCopyImg2='$poiCopyImg2', imgList_1=$imgList_1, imgList_10=$imgList_10, wtrImg1='$wtrImg1', wtrImg2='$wtrImg2', imgList_2=$imgList_2, imgList_3=$imgList_3, imgList_4=$imgList_4, imgList_5=$imgList_5, imgList_6=$imgList_6, imgList_7=$imgList_7, imgList_8=$imgList_8, bwtrImg1='$bwtrImg1', bwtrImg2='$bwtrImg2', bwtr1Img1='$bwtr1Img1', bwtr2Img2='$bwtr2Img2', fwcqImg1='$fwcqImg1', fwcqImg2='$fwcqImg2', fwcqzmrImg1='$fwcqzmrImg1', fwcqzmrImg2='$fwcqzmrImg2', fwhxImg1='$fwhxImg1', fwhxImg2='$fwhxImg2', formImg1='$formImg1', formImg2='$formImg2', fwzpImg1='$fwzpImg1', fwzpImg2='$fwzpImg2')"
    }

}