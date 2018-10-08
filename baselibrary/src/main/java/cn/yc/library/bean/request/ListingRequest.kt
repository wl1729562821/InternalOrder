package cn.yc.library.bean.request

class ListingRequest{
     var applyId=""
     var houseId=""
     var taskId:String="" //
     var languageVersion:String="" //语言版本（0：中文，1：英文）默认为0
     var applicantType:String="1" //申请人类型：0业主 1 poa
     var leaseType:String="0" //预约类型（0：出租，1：出售）
     var applyType:String="0" //申请类型（0：自主完善，1：联系客服上传，2：业务员上传）
     var housingTypeDictcode:String="" //房屋类型 ，查询数据字典
     var city:String="" //城市
     var community:String="" //社区
     var province=""
     var subCommunity:String="" //子社区
     var property:String="" //项目
     var address:String="" //房源所在地址
     var longitude:String="" //经度
     var latitude:String="" //纬度
     var phoneNumber:String="" //手机号
     var villageName:String="" //小区名称
     var buildingName:String="" //楼名/别墅名
     var houseUnitNo:String="" //单元号
     var houseFloor:String="" //楼层
     var roomName:String="" //门牌号
     var houseAcreage:String="" //房屋面积
     var parkingSpace:String="" //停车位
     var bathroomNum:String="" //浴室数量
     var bedroomNum:String="" //卧室数量
     var houseDecorationDictcode:String="" //房屋装修 0：带家具，1：不带家具
     var houseConfigDictcode:String="" //房源配置，ids
     var housingStatus:String="" //房屋状态（0：空房，1：出租，2：自住，3：准现房）
     var payNode:String="" //支付节点 , 1....12/月
     var isPromissoryBuild=0 //房源状态：0>期房，1>现房
     var startRentDate:String="" //@string/houses_tv46
     var houseSelfContainedDictcode:String=""
     var isHouseLoan:String="" // 否有房贷：0>无，1>有
     var houseRent:String="" //期望租金/或出售价
     var minHouseRent:String="" //最低租金/或出售价
     var contacts:String="" //联系人
     var email:String="" //邮箱
     var facebook:String="" //facebook
     var twitter:String="" //twitter
     var instagram:String="" //instagram
     var rentCustomerName:String="" //租客姓名
     var rentCustomerPhone:String="" //租客电话
     var haveKey:String="" // 否有钥匙：0>无,1有（有选择 否交钥匙时才传值，否则该参数不用传）
     var appointmentLookTime:String="0" //预约看房时间（1、全选时，传空值值，不用传时间点。2、选择周几的时间点，多选时传值以分号分隔。3、选择客服联系时，不用传值。4、选择提前几天预约，需传几天的值，以及选择的时间点）		示例值：2、选择Mon 的 9:00，传值 ： Mon 9:00 。选择 Mon 的 9:00和10:00，传值：Mon 9:00,10:00。 选择Mon，Tue 的9:00和10:00，传值：Mon 9:00,10:00;Tue 9:00,10:00。
     var isCustomerServiceRelation:String="" //预约时间设置， 否客服联系（0：否，1： ）
     var advanceReservationDay:String="" //提前几天预约（默认为0）
     var bargainHouseDate:String="" //预计交房日期
     var remarks:String="" //备注
     var mandataryCopiesImg1:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryCopiesImg2:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryCopiesImg3:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryCopiesImg4:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryCopiesImg5:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryCopiesImg6:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryCopiesImg7:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryCopiesImg8:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryCopiesImg9:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryCopiesImg10:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg1:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg2:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg3:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg4:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg5:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg6:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg7:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg8:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg9:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseHoldImg10:String="" //POA复印件
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryVisaImg1:String="" //被委托人签证照片1
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryVisaImg2:String="" //被委托人签证照片1
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryVisaImg3:String="" //被委托人签证照片1
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryIdcardImg1:String="" //被委托人ID卡照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryIdcardImg2:String="" //被委托人ID卡照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryIdcardImg3:String="" //被委托人ID卡照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryIdcardImg4:String="" //被委托人ID卡照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var letterAuthorization:String="" //委托书（出租房东本人：房屋租赁委托文件签字，出售房东本人：formA确认，出租POA：委托书，出售POA：委托书）
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var letterAuthorization2:String="" //委托书（出租房东本人：房屋租赁委托文件签字，出售房东本人：formA确认，出租POA：委托书，出售POA：委托书）
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var pocImg1:String="" //房产证照片1
     set(value) {
          if(value!="add_xx1_xx1" && value!="add_xx_xx"){
               field=value    
          }
     }
     var pocImg2:String="" //房产证照片1
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var pocImg3:String="" //房产证照片1
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var reoPassportImg1:String="" //房屋产权人护照照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var reoPassportImg2:String="" //房屋产权人护照照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var reoPassportImg3:String="" //房屋产权人护照照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseRentContractImg1:String="" //房屋租赁合同图片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseRentContractImg2:String="" //房屋租赁合同图片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseRentContractImg3:String="" //房屋租赁合同图片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseRentContractImg4:String="" //房屋租赁合同图片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryPassportImg1:String="" //被委托人护照照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryPassportImg2:String="" //被委托人护照照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var mandataryPassportImg3:String="" //被委托人护照照片
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }

     var rentAuthorizationSignImg1=""
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var rentAuthorizationSignImg2=""
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var rentAuthorizationSignImg3=""
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }

     var formaConfirmImg1=""
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var formaConfirmImg2=""
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var formaConfirmImg3=""
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }

     var houseImg1:String="" //房源图片1
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseImg2:String="" //房源图片2
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseImg3:String="" //房源图片3
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseImg4:String="" //房源图片4
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseImg5:String="" //房源图片5
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseImg6:String="" //房源图片6
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseImg7:String="" //房源图片7
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseImg8:String="" //房源图片8
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseImg9:String="" //房源图片9
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var houseImg10:String="" //房源图片10
          set(value) {
               if(value!="add_xx1_xx1" && value!="add_xx_xx"){
                    field=value
               }
          }
     var setting:String="" //自动应答参数
     var isAutoAnswer:String="" //自动应答开关设置（0：关，1：开）
     var plotNumber="" //地块信息
     var typeOfArea="" //区域信息(0:Free Hold 1:Lease Hold)
     var titleDeedNumber="" //产权证书编号
     var propertyNumber="" //产权编号
     var masterDevelpoerName="" //地产开发商
     override fun toString(): String {
          return "ListingRequest(applyId='$applyId', houseId='$houseId', taskId='$taskId', languageVersion='$languageVersion', applicantType='$applicantType', leaseType='$leaseType', applyType='$applyType', housingTypeDictcode='$housingTypeDictcode', city='$city', community='$community', subCommunity='$subCommunity', property='$property', address='$address', longitude='$longitude', latitude='$latitude', phoneNumber='$phoneNumber', villageName='$villageName', buildingName='$buildingName', houseUnitNo='$houseUnitNo', houseFloor='$houseFloor', roomName='$roomName', houseAcreage='$houseAcreage', parkingSpace='$parkingSpace', bathroomNum='$bathroomNum', bedroomNum='$bedroomNum', houseDecorationDictcode='$houseDecorationDictcode', houseConfigDictcode='$houseConfigDictcode', housingStatus='$housingStatus', payNode='$payNode', isPromissoryBuild='$isPromissoryBuild', startRentDate='$startRentDate', isHouseLoan='$isHouseLoan', houseRent='$houseRent', minHouseRent='$minHouseRent', contacts='$contacts', email='$email', facebook='$facebook', twitter='$twitter', instagram='$instagram', rentCustomerName='$rentCustomerName', rentCustomerPhone='$rentCustomerPhone', haveKey='$haveKey', appointmentLookTime='$appointmentLookTime', isCustomerServiceRelation='$isCustomerServiceRelation', advanceReservationDay='$advanceReservationDay', bargainHouseDate='$bargainHouseDate', remarks='$remarks', mandataryCopiesImg1='$mandataryCopiesImg1', mandataryCopiesImg2='$mandataryCopiesImg2', mandataryCopiesImg3='$mandataryCopiesImg3', mandataryCopiesImg4='$mandataryCopiesImg4', mandataryCopiesImg5='$mandataryCopiesImg5', mandataryCopiesImg6='$mandataryCopiesImg6', mandataryCopiesImg7='$mandataryCopiesImg7', mandataryCopiesImg8='$mandataryCopiesImg8', mandataryCopiesImg9='$mandataryCopiesImg9', mandataryCopiesImg10='$mandataryCopiesImg10', houseHoldImg1='$houseHoldImg1', houseHoldImg2='$houseHoldImg2', houseHoldImg3='$houseHoldImg3', houseHoldImg4='$houseHoldImg4', houseHoldImg5='$houseHoldImg5', houseHoldImg6='$houseHoldImg6', houseHoldImg7='$houseHoldImg7', houseHoldImg8='$houseHoldImg8', houseHoldImg9='$houseHoldImg9', houseHoldImg10='$houseHoldImg10', mandataryVisaImg1='$mandataryVisaImg1', mandataryVisaImg2='$mandataryVisaImg2', mandataryVisaImg3='$mandataryVisaImg3', mandataryIdcardImg1='$mandataryIdcardImg1', mandataryIdcardImg2='$mandataryIdcardImg2', mandataryIdcardImg3='$mandataryIdcardImg3', mandataryIdcardImg4='$mandataryIdcardImg4', letterAuthorization='$letterAuthorization', letterAuthorization2='$letterAuthorization2', pocImg1='$pocImg1', pocImg2='$pocImg2', pocImg3='$pocImg3', reoPassportImg1='$reoPassportImg1', reoPassportImg2='$reoPassportImg2', reoPassportImg3='$reoPassportImg3', houseRentContractImg1='$houseRentContractImg1', houseRentContractImg2='$houseRentContractImg2', houseRentContractImg3='$houseRentContractImg3', houseRentContractImg4='$houseRentContractImg4', mandataryPassportImg1='$mandataryPassportImg1', mandataryPassportImg2='$mandataryPassportImg2', mandataryPassportImg3='$mandataryPassportImg3', rentAuthorizationSignImg1='$rentAuthorizationSignImg1', rentAuthorizationSignImg2='$rentAuthorizationSignImg2', rentAuthorizationSignImg3='$rentAuthorizationSignImg3', formaConfirmImg1='$formaConfirmImg1', formaConfirmImg2='$formaConfirmImg2', formaConfirmImg3='$formaConfirmImg3', houseImg1='$houseImg1', houseImg2='$houseImg2', houseImg3='$houseImg3', houseImg4='$houseImg4', houseImg5='$houseImg5', houseImg6='$houseImg6', houseImg7='$houseImg7', houseImg8='$houseImg8', houseImg9='$houseImg9', houseImg10='$houseImg10', setting='$setting', isAutoAnswer='$isAutoAnswer', plotNumber='$plotNumber', typeOfArea='$typeOfArea', titleDeedNumber='$titleDeedNumber', propertyNumber='$propertyNumber', masterDevelpoerName='$masterDevelpoerName')"
     }


}
