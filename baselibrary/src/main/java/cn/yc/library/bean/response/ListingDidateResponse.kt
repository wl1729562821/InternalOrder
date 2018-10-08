package cn.yc.library.bean.response

class ListingDidateResponse {

    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-25 14:21:16
     * dataSet : {"credentials":{"id":17,"applyId":53,"houseId":86,"languageVersion":0,"applicantType":0,"mandataryCopiesImg1":null,"mandataryCopiesImg2":null,"mandataryCopiesImg3":null,"mandataryCopiesImg4":null,"mandataryCopiesImg5":null,"mandataryCopiesImg6":null,"mandataryCopiesImg7":null,"mandataryCopiesImg8":null,"mandataryCopiesImg9":null,"mandataryCopiesImg10":null,"mandataryPassportImg1":null,"mandataryPassportImg2":null,"mandataryPassportImg3":null,"mandataryVisaImg1":null,"mandataryVisaImg2":null,"mandataryVisaImg3":null,"mandataryIdcardImg1":null,"mandataryIdcardImg2":null,"mandataryIdcardImg3":null,"mandataryIdcardImg4":null,"letterAuthorization":null,"letterAuthorization2":"group1/M01/00/01/rBLBRFtz34aAVqGqAADjnj--4cQ000.png","rentAuthorizationSignImg1":null,"rentAuthorizationSignImg2":null,"rentAuthorizationSignImg3":null,"formaConfirmImg1":null,"formaConfirmImg2":null,"formaConfirmImg3":null,"pocImg1":"group1/M01/00/01/rBLBRFtz34aAMqXEAABgsosxdBw512.png","pocImg2":"group1/M01/00/01/rBLBRFtz34aAK6R4AABALaUdrUU654.png","pocImg3":null,"reoPassportImg1":"group1/M00/00/01/rBLBRFtz34aAYyp_AABVlsuXtDM726.png","reoPassportImg2":null,"reoPassportImg3":null,"houseRentContractImg1":null,"houseRentContractImg2":null,"houseRentContractImg3":null,"houseRentContractImg4":null,"houseHoldImg1":null,"houseHoldImg2":null,"houseHoldImg3":null,"houseHoldImg4":null,"houseHoldImg5":null,"houseHoldImg6":null,"houseHoldImg7":null,"houseHoldImg8":null,"houseHoldImg9":null,"houseHoldImg10":null},"houses":{"id":53,"languageVersion":0,"memberId":4445,"applicantType":0,"leaseType":0,"applyType":0,"housingTypeDictcode":"20055","city":"深圳市","community":"龙岗区","subCommunity":"坂田街道","property":null,"address":"永香路4巷8号","longitude":"0.0","latitude":"0.0","phoneNumber":null,"villageName":"龙岗区坂田","buildingName":"生态园创新楼","houseUnitNo":6,"houseFloor":8,"roomName":"555","houseTypeDictcode":"0","houseAcreage":600,"parkingSpace":0,"bathroomNum":1,"bedroomNum":3,"houseOrientationDictcode":"0","houseDecorationDictcode":"1","houseLabelDictcode":"0","houseFloorDictcode":"0","houseConfigDictcode":",20021,20022,20023,20024,20025,20026,20027,20028,20029,20030,20031,20032,20033,20034,20035,20036,20037,20038,20039,20040,20041,20042,20043,20044,20045,20046,20047,20048,20049,20050,20051,20052","housingStatus":0,"payNode":1,"isPromissoryBuild":0,"beginRentDate":"2017-08-20 00:00:00","isHouseLoan":0,"houseRent":9000,"minHouseRent":8000,"contacts":null,"email":"","facebook":"","twitter":"","instagram":"","rentCustomerName":"","rentCustomerPhone":"","haveKey":0,"appointmentLookTime":"Mon ;Tue ;Wed ;Thur ;Fri ;Sat ;Sun ;","appointmentDoorTime":"2018-08-17 10:56:47","appointmentMeetPlace":"深圳市龙岗区坂田街道永香路4巷8号","expectBargainHouseDate":null,"isCheck":2,"remarks":null,"isDel":0,"createBy":4445,"createTime":"2018-08-15 10:46:30","updateBy":null,"updateTime":"2018-08-15 10:46:30","standby1":null,"standby2":null,"standby3":null,"standby4":null,"standby5":null,"isCustomerServiceRelation":0,"advanceReservationDay":0,"startRentDate":null,"bargainHouseDate":null}}
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var dataSet: DataSetBean? = null
    var pageInfo: Any? = null
    var phoneNumber=""

    class DataSetBean {
        /**
         * credentials : {"id":17,"applyId":53,"houseId":86,"languageVersion":0,"applicantType":0,"mandataryCopiesImg1":null,"mandataryCopiesImg2":null,"mandataryCopiesImg3":null,"mandataryCopiesImg4":null,"mandataryCopiesImg5":null,"mandataryCopiesImg6":null,"mandataryCopiesImg7":null,"mandataryCopiesImg8":null,"mandataryCopiesImg9":null,"mandataryCopiesImg10":null,"mandataryPassportImg1":null,"mandataryPassportImg2":null,"mandataryPassportImg3":null,"mandataryVisaImg1":null,"mandataryVisaImg2":null,"mandataryVisaImg3":null,"mandataryIdcardImg1":null,"mandataryIdcardImg2":null,"mandataryIdcardImg3":null,"mandataryIdcardImg4":null,"letterAuthorization":null,"letterAuthorization2":"group1/M01/00/01/rBLBRFtz34aAVqGqAADjnj--4cQ000.png","rentAuthorizationSignImg1":null,"rentAuthorizationSignImg2":null,"rentAuthorizationSignImg3":null,"formaConfirmImg1":null,"formaConfirmImg2":null,"formaConfirmImg3":null,"pocImg1":"group1/M01/00/01/rBLBRFtz34aAMqXEAABgsosxdBw512.png","pocImg2":"group1/M01/00/01/rBLBRFtz34aAK6R4AABALaUdrUU654.png","pocImg3":null,"reoPassportImg1":"group1/M00/00/01/rBLBRFtz34aAYyp_AABVlsuXtDM726.png","reoPassportImg2":null,"reoPassportImg3":null,"houseRentContractImg1":null,"houseRentContractImg2":null,"houseRentContractImg3":null,"houseRentContractImg4":null,"houseHoldImg1":null,"houseHoldImg2":null,"houseHoldImg3":null,"houseHoldImg4":null,"houseHoldImg5":null,"houseHoldImg6":null,"houseHoldImg7":null,"houseHoldImg8":null,"houseHoldImg9":null,"houseHoldImg10":null}
         * houses : {"id":53,"languageVersion":0,"memberId":4445,"applicantType":0,"leaseType":0,"applyType":0,"housingTypeDictcode":"20055","city":"深圳市","community":"龙岗区","subCommunity":"坂田街道","property":null,"address":"永香路4巷8号","longitude":"0.0","latitude":"0.0","phoneNumber":null,"villageName":"龙岗区坂田","buildingName":"生态园创新楼","houseUnitNo":6,"houseFloor":8,"roomName":"555","houseTypeDictcode":"0","houseAcreage":600,"parkingSpace":0,"bathroomNum":1,"bedroomNum":3,"houseOrientationDictcode":"0","houseDecorationDictcode":"1","houseLabelDictcode":"0","houseFloorDictcode":"0","houseConfigDictcode":",20021,20022,20023,20024,20025,20026,20027,20028,20029,20030,20031,20032,20033,20034,20035,20036,20037,20038,20039,20040,20041,20042,20043,20044,20045,20046,20047,20048,20049,20050,20051,20052","housingStatus":0,"payNode":1,"isPromissoryBuild":0,"beginRentDate":"2017-08-20 00:00:00","isHouseLoan":0,"houseRent":9000,"minHouseRent":8000,"contacts":null,"email":"","facebook":"","twitter":"","instagram":"","rentCustomerName":"","rentCustomerPhone":"","haveKey":0,"appointmentLookTime":"Mon ;Tue ;Wed ;Thur ;Fri ;Sat ;Sun ;","appointmentDoorTime":"2018-08-17 10:56:47","appointmentMeetPlace":"深圳市龙岗区坂田街道永香路4巷8号","expectBargainHouseDate":null,"isCheck":2,"remarks":null,"isDel":0,"createBy":4445,"createTime":"2018-08-15 10:46:30","updateBy":null,"updateTime":"2018-08-15 10:46:30","standby1":null,"standby2":null,"standby3":null,"standby4":null,"standby5":null,"isCustomerServiceRelation":0,"advanceReservationDay":0,"startRentDate":null,"bargainHouseDate":null}
         */

        var credentials: CredentialsBean? = null
        var houses: HousesBean? = null
        var phoneNumber=""

        class CredentialsBean {
            /**
             * id : 17
             * applyId : 53
             * houseId : 86
             * languageVersion : 0
             * applicantType : 0
             * mandataryCopiesImg1 : null
             * mandataryCopiesImg2 : null
             * mandataryCopiesImg3 : null
             * mandataryCopiesImg4 : null
             * mandataryCopiesImg5 : null
             * mandataryCopiesImg6 : null
             * mandataryCopiesImg7 : null
             * mandataryCopiesImg8 : null
             * mandataryCopiesImg9 : null
             * mandataryCopiesImg10 : null
             * mandataryPassportImg1 : null
             * mandataryPassportImg2 : null
             * mandataryPassportImg3 : null
             * mandataryVisaImg1 : null
             * mandataryVisaImg2 : null
             * mandataryVisaImg3 : null
             * mandataryIdcardImg1 : null
             * mandataryIdcardImg2 : null
             * mandataryIdcardImg3 : null
             * mandataryIdcardImg4 : null
             * letterAuthorization : null
             * letterAuthorization2 : group1/M01/00/01/rBLBRFtz34aAVqGqAADjnj--4cQ000.png
             * rentAuthorizationSignImg1 : null
             * rentAuthorizationSignImg2 : null
             * rentAuthorizationSignImg3 : null
             * formaConfirmImg1 : null
             * formaConfirmImg2 : null
             * formaConfirmImg3 : null
             * pocImg1 : group1/M01/00/01/rBLBRFtz34aAMqXEAABgsosxdBw512.png
             * pocImg2 : group1/M01/00/01/rBLBRFtz34aAK6R4AABALaUdrUU654.png
             * pocImg3 : null
             * reoPassportImg1 : group1/M00/00/01/rBLBRFtz34aAYyp_AABVlsuXtDM726.png
             * reoPassportImg2 : null
             * reoPassportImg3 : null
             * houseRentContractImg1 : null
             * houseRentContractImg2 : null
             * houseRentContractImg3 : null
             * houseRentContractImg4 : null
             * houseHoldImg1 : null
             * houseHoldImg2 : null
             * houseHoldImg3 : null
             * houseHoldImg4 : null
             * houseHoldImg5 : null
             * houseHoldImg6 : null
             * houseHoldImg7 : null
             * houseHoldImg8 : null
             * houseHoldImg9 : null
             * houseHoldImg10 : null
             */

            var id: Int = 0
            var applyId: Int = 0
            var houseId: Int = 0
            var languageVersion: Int = 0
            var applicantType: Int = 0
            var mandataryCopiesImg1: String? = null
            var mandataryCopiesImg2: String? = null
            var mandataryCopiesImg3: String? = null
            var mandataryCopiesImg4: String? = null
            var mandataryCopiesImg5: String? = null
            var mandataryCopiesImg6: String? = null
            var mandataryCopiesImg7: String? = null
            var mandataryCopiesImg8: String? = null
            var mandataryCopiesImg9: String? = null
            var mandataryCopiesImg10: String? = null
            var mandataryPassportImg1: String? = null
            var mandataryPassportImg2: String? = null
            var mandataryPassportImg3: String? = null
            var mandataryVisaImg1: String? = null
            var mandataryVisaImg2: String? = null
            var mandataryVisaImg3: String? = null
            var mandataryIdcardImg1: String? = null
            var mandataryIdcardImg2: String? = null
            var mandataryIdcardImg3: String? = null
            var mandataryIdcardImg4: String? = null
            var letterAuthorization: String? = null
            var letterAuthorization2: String? = null
            var rentAuthorizationSignImg1: String? = null
            var rentAuthorizationSignImg2: String? = null
            var rentAuthorizationSignImg3: String? = null
            var formaConfirmImg1: String? = null
            var formaConfirmImg2: String? = null
            var formaConfirmImg3: String? = null
            var pocImg1: String? = null
            var pocImg2: String? = null
            var pocImg3: String? = null
            var reoPassportImg1: String? = null
            var reoPassportImg2: String? = null
            var reoPassportImg3: String? = null
            var houseRentContractImg1: String? = null
            var houseRentContractImg2: String? = null
            var houseRentContractImg3: String? = null
            var houseRentContractImg4: String? = null
            var houseHoldImg1: String? = null
            var houseHoldImg2: String? = null
            var houseHoldImg3: String? = null
            var houseHoldImg4: String? = null
            var houseHoldImg5: String? = null
            var houseHoldImg6: String? = null
            var houseHoldImg7: String? = null
            var houseHoldImg8: String? = null
            var houseHoldImg9: String? = null
            var houseHoldImg10: String? = null
            override fun toString(): String {
                return "CredentialsBean(id=$id, applyId=$applyId, houseId=$houseId, languageVersion=$languageVersion, applicantType=$applicantType, mandataryCopiesImg1=$mandataryCopiesImg1, mandataryCopiesImg2=$mandataryCopiesImg2, mandataryCopiesImg3=$mandataryCopiesImg3, mandataryCopiesImg4=$mandataryCopiesImg4, mandataryCopiesImg5=$mandataryCopiesImg5, mandataryCopiesImg6=$mandataryCopiesImg6, mandataryCopiesImg7=$mandataryCopiesImg7, mandataryCopiesImg8=$mandataryCopiesImg8, mandataryCopiesImg9=$mandataryCopiesImg9, mandataryCopiesImg10=$mandataryCopiesImg10, mandataryPassportImg1=$mandataryPassportImg1, mandataryPassportImg2=$mandataryPassportImg2, mandataryPassportImg3=$mandataryPassportImg3, mandataryVisaImg1=$mandataryVisaImg1, mandataryVisaImg2=$mandataryVisaImg2, mandataryVisaImg3=$mandataryVisaImg3, mandataryIdcardImg1=$mandataryIdcardImg1, mandataryIdcardImg2=$mandataryIdcardImg2, mandataryIdcardImg3=$mandataryIdcardImg3, mandataryIdcardImg4=$mandataryIdcardImg4, letterAuthorization=$letterAuthorization, letterAuthorization2=$letterAuthorization2, rentAuthorizationSignImg1=$rentAuthorizationSignImg1, rentAuthorizationSignImg2=$rentAuthorizationSignImg2, rentAuthorizationSignImg3=$rentAuthorizationSignImg3, formaConfirmImg1=$formaConfirmImg1, formaConfirmImg2=$formaConfirmImg2, formaConfirmImg3=$formaConfirmImg3, pocImg1=$pocImg1, pocImg2=$pocImg2, pocImg3=$pocImg3, reoPassportImg1=$reoPassportImg1, reoPassportImg2=$reoPassportImg2, reoPassportImg3=$reoPassportImg3, houseRentContractImg1=$houseRentContractImg1, houseRentContractImg2=$houseRentContractImg2, houseRentContractImg3=$houseRentContractImg3, houseRentContractImg4=$houseRentContractImg4, houseHoldImg1=$houseHoldImg1, houseHoldImg2=$houseHoldImg2, houseHoldImg3=$houseHoldImg3, houseHoldImg4=$houseHoldImg4, houseHoldImg5=$houseHoldImg5, houseHoldImg6=$houseHoldImg6, houseHoldImg7=$houseHoldImg7, houseHoldImg8=$houseHoldImg8, houseHoldImg9=$houseHoldImg9, houseHoldImg10=$houseHoldImg10)"
            }
        }

        class HousesBean {
            /**
             * id : 53
             * languageVersion : 0
             * memberId : 4445
             * applicantType : 0
             * leaseType : 0
             * applyType : 0
             * housingTypeDictcode : 20055
             * city : 深圳市
             * community : 龙岗区
             * subCommunity : 坂田街道
             * property : null
             * address : 永香路4巷8号
             * longitude : 0.0
             * latitude : 0.0
             * phoneNumber : null
             * villageName : 龙岗区坂田
             * buildingName : 生态园创新楼
             * houseUnitNo : 6
             * houseFloor : 8
             * roomName : 555
             * houseTypeDictcode : 0
             * houseAcreage : 600
             * parkingSpace : 0
             * bathroomNum : 1
             * bedroomNum : 3
             * houseOrientationDictcode : 0
             * houseDecorationDictcode : 1
             * houseLabelDictcode : 0
             * houseFloorDictcode : 0
             * houseConfigDictcode : ,20021,20022,20023,20024,20025,20026,20027,20028,20029,20030,20031,20032,20033,20034,20035,20036,20037,20038,20039,20040,20041,20042,20043,20044,20045,20046,20047,20048,20049,20050,20051,20052
             * housingStatus : 0
             * payNode : 1
             * isPromissoryBuild : 0
             * beginRentDate : 2017-08-20 00:00:00
             * isHouseLoan : 0
             * houseRent : 9000
             * minHouseRent : 8000
             * contacts : null
             * email :
             * facebook :
             * twitter :
             * instagram :
             * rentCustomerName :
             * rentCustomerPhone :
             * haveKey : 0
             * appointmentLookTime : Mon ;Tue ;Wed ;Thur ;Fri ;Sat ;Sun ;
             * appointmentDoorTime : 2018-08-17 10:56:47
             * appointmentMeetPlace : 深圳市龙岗区坂田街道永香路4巷8号
             * expectBargainHouseDate : null
             * isCheck : 2
             * remarks : null
             * isDel : 0
             * createBy : 4445
             * createTime : 2018-08-15 10:46:30
             * updateBy : null
             * updateTime : 2018-08-15 10:46:30
             * standby1 : null
             * standby2 : null
             * standby3 : null
             * standby4 : null
             * standby5 : null
             * isCustomerServiceRelation : 0
             * advanceReservationDay : 0
             * startRentDate : null
             * bargainHouseDate : null
             */

            var id: Int = 0
            var languageVersion: Int = 0
            var memberId: Int = 0
            var applicantType: Int = 0
            var leaseType: Int = -1
            var applyType: Int = 0
            var housingTypeDictcode: String? = null
            var city: String? = null
            var community: String? = null
            var subCommunity: String? = null
            var property: Any? = null
            var address: String? = null
            var longitude: String? = null
            var latitude: String? = null
            var phoneNumber: Any? = null
            var villageName: String? = null
            var buildingName: String? = null
            var houseUnitNo: Int = 0
            var houseFloor: Int = 0
            var roomName: String? = null
            var houseTypeDictcode: String? = null
            var houseAcreage: Int = 0
            var parkingSpace: Int = 0
            var bathroomNum: Int = 0
            var bedroomNum: Int = 0
            var houseOrientationDictcode: String? = null
            var houseDecorationDictcode: String? = null
            var houseLabelDictcode: String? = null
            var houseFloorDictcode: String? = null
            var houseConfigDictcode: String? = null
            var houseSelfContainedDictcode:String?=null
            var housingStatus: Int = 0
            var payNode: Int = 0
            var isPromissoryBuild: Int = 0
            var beginRentDate: String? = null
            var isHouseLoan: Int = 0
            var houseRent: Int = 0
            var minHouseRent: Int = 0
            var contacts: Any? = null
            var email: String? = null
            var facebook: String? = null
            var twitter: String? = null
            var instagram: String? = null
            var rentCustomerName: String? = null
            var rentCustomerPhone: String? = null
            var haveKey: Int = 0
            var plotNumber=""
            var typeOfArea=""
            var titleDeedNumber=""
            var propertyNumber=""
            var masterDevelpoerName=""
            var appointmentLookTime: String? = null
            var appointmentDoorTime: String? = null
            var appointmentMeetPlace: String? = null
            var expectBargainHouseDate: Any? = null
            var isCheck: Int = 0
            var remarks: Any? = null
            var isDel: Int = 0
            var createBy: Int = 0
            var createTime: String? = null
            var updateBy: Any? = null
            var updateTime: String? = null
            var standby1: Any? = null
            var standby2: Any? = null
            var standby3: Any? = null
            var standby4: Any? = null
            var standby5: Any? = null
            var isCustomerServiceRelation: Int = 0
            var advanceReservationDay: Int = 0
            var startRentDate: String? = null
            var bargainHouseDate: String? = null
            override fun toString(): String {
                return "HousesBean(id=$id, languageVersion=$languageVersion, memberId=$memberId, applicantType=$applicantType, leaseType=$leaseType, applyType=$applyType, housingTypeDictcode=$housingTypeDictcode, city=$city, community=$community, subCommunity=$subCommunity, property=$property, address=$address, longitude=$longitude, latitude=$latitude, phoneNumber=$phoneNumber, villageName=$villageName, buildingName=$buildingName, houseUnitNo=$houseUnitNo, houseFloor=$houseFloor, roomName=$roomName, houseTypeDictcode=$houseTypeDictcode, houseAcreage=$houseAcreage, parkingSpace=$parkingSpace, bathroomNum=$bathroomNum, bedroomNum=$bedroomNum, houseOrientationDictcode=$houseOrientationDictcode, houseDecorationDictcode=$houseDecorationDictcode, houseLabelDictcode=$houseLabelDictcode, houseFloorDictcode=$houseFloorDictcode, houseConfigDictcode=$houseConfigDictcode, housingStatus=$housingStatus, payNode=$payNode, isPromissoryBuild=$isPromissoryBuild, beginRentDate=$beginRentDate, isHouseLoan=$isHouseLoan, houseRent=$houseRent, minHouseRent=$minHouseRent, contacts=$contacts, email=$email, facebook=$facebook, twitter=$twitter, instagram=$instagram, rentCustomerName=$rentCustomerName, rentCustomerPhone=$rentCustomerPhone, haveKey=$haveKey, appointmentLookTime=$appointmentLookTime, appointmentDoorTime=$appointmentDoorTime, appointmentMeetPlace=$appointmentMeetPlace, expectBargainHouseDate=$expectBargainHouseDate, isCheck=$isCheck, remarks=$remarks, isDel=$isDel, createBy=$createBy, createTime=$createTime, updateBy=$updateBy, updateTime=$updateTime, standby1=$standby1, standby2=$standby2, standby3=$standby3, standby4=$standby4, standby5=$standby5, isCustomerServiceRelation=$isCustomerServiceRelation, advanceReservationDay=$advanceReservationDay, startRentDate=$startRentDate, bargainHouseDate=$bargainHouseDate)"
            }
        }

        override fun toString(): String {
            return "DataSetBean(credentials=$credentials, houses=$houses)"
        }
    }

    override fun toString(): String {
        return "ListingDidateResponse(result=$result, message=$message, token=$token, systemTime=$systemTime, dataSet=$dataSet, pageInfo=$pageInfo)"
    }

}
