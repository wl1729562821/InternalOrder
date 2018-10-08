package cn.yc.library.bean.response

class ListingConfigResponse {

    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-28 19:47:27
     * dataSet : {"groupName":"房源配置","groupNameEn":"","id":10005,"items":[{"groupId":10005,"itemValue":"鞋架","sort":15,"backImg":{},"itemName":"房源配置","standby1":{},"id":20021,"itemValueEn":"shoe rack"},{"groupId":10005,"itemValue":"落地灯","sort":16,"backImg":{},"itemName":"房源配置","standby1":{},"id":20022,"itemValueEn":{}},{"groupId":10005,"itemValue":"沙发椅","sort":17,"backImg":{},"itemName":"房源配置","standby1":{},"id":20023,"itemValueEn":{}},{"groupId":10005,"itemValue":"靠背椅","sort":25,"backImg":{},"itemName":"房源配置","standby1":{},"id":20024,"itemValueEn":{}},{"groupId":10005,"itemValue":"书架","sort":26,"backImg":{},"itemName":"房源配置","standby1":{},"id":20025,"itemValueEn":{}},{"groupId":10005,"itemValue":"电视","sort":27,"backImg":{},"itemName":"房源配置","standby1":{},"id":20026,"itemValueEn":{}},{"groupId":10005,"itemValue":"电视架","sort":28,"backImg":{},"itemName":"房源配置","standby1":{},"id":20027,"itemValueEn":{}},{"groupId":10005,"itemValue":"艺术品","sort":29,"backImg":{},"itemName":"房源配置","standby1":{},"id":20028,"itemValueEn":{}},{"groupId":10005,"itemValue":"餐具","sort":30,"backImg":{},"itemName":"房源配置","standby1":{},"id":20029,"itemValueEn":{}},{"groupId":10005,"itemValue":"锅","sort":31,"backImg":{},"itemName":"房源配置","standby1":{},"id":20030,"itemValueEn":{}},{"groupId":10005,"itemValue":"烤箱","sort":32,"backImg":{},"itemName":"房源配置","standby1":{},"id":20031,"itemValueEn":{}},{"groupId":10005,"itemValue":"洗衣机","sort":33,"backImg":{},"itemName":"房源配置","standby1":{},"id":20032,"itemValueEn":{}},{"groupId":10005,"itemValue":"冰箱","sort":34,"backImg":{},"itemName":"房源配置","standby1":{},"id":20033,"itemValueEn":{}},{"groupId":10005,"itemValue":"电灶台","sort":35,"backImg":{},"itemName":"房源配置","standby1":{},"id":20034,"itemValueEn":{}},{"groupId":10005,"itemValue":"燃气灶台","sort":36,"backImg":{},"itemName":"房源配置","standby1":{},"id":20035,"itemValueEn":{}},{"groupId":10005,"itemValue":"微波炉","sort":37,"backImg":{},"itemName":"房源配置","standby1":{},"id":20036,"itemValueEn":{}},{"groupId":10005,"itemValue":"咖啡机","sort":38,"backImg":{},"itemName":"房源配置","standby1":{},"id":20037,"itemValueEn":{}},{"groupId":10005,"itemValue":"电热壶","sort":39,"backImg":{},"itemName":"房源配置","standby1":{},"id":20038,"itemValueEn":{}},{"groupId":10005,"itemValue":"洗碗机","sort":40,"backImg":{},"itemName":"房源配置","standby1":{},"id":20039,"itemValueEn":{}},{"groupId":10005,"itemValue":"垃圾桶","sort":41,"backImg":{},"itemName":"房源配置","standby1":{},"id":20040,"itemValueEn":{}},{"groupId":10005,"itemValue":"餐桌椅","sort":42,"backImg":{},"itemName":"房源配置","standby1":{},"id":20041,"itemValueEn":{}},{"groupId":10005,"itemValue":"淋浴","sort":43,"backImg":{},"itemName":"房源配置","standby1":{},"id":20042,"itemValueEn":{}},{"groupId":10005,"itemValue":"浴池","sort":44,"backImg":{},"itemName":"房源配置","standby1":{},"id":20043,"itemValueEn":{}},{"groupId":10005,"itemValue":"床","sort":45,"backImg":{},"itemName":"房源配置","standby1":{},"id":20044,"itemValueEn":{}},{"groupId":10005,"itemValue":"床垫","sort":46,"backImg":{},"itemName":"房源配置","standby1":{},"id":20045,"itemValueEn":{}},{"groupId":10005,"itemValue":"床头柜","sort":47,"backImg":{},"itemName":"房源配置","standby1":{},"id":20046,"itemValueEn":{}},{"groupId":10005,"itemValue":"床灯","sort":48,"backImg":{},"itemName":"房源配置","standby1":{},"id":20047,"itemValueEn":{}},{"groupId":10005,"itemValue":"梳妆台","sort":49,"backImg":{},"itemName":"房源配置","standby1":{},"id":20048,"itemValueEn":{}},{"groupId":10005,"itemValue":"窗帘","sort":50,"backImg":{},"itemName":"房源配置","standby1":{},"id":20049,"itemValueEn":{}},{"groupId":10005,"itemValue":"书桌","sort":51,"backImg":{},"itemName":"房源配置","standby1":{},"id":20050,"itemValueEn":{}},{"groupId":10005,"itemValue":"书桌椅","sort":52,"backImg":{},"itemName":"房源配置","standby1":{},"id":20051,"itemValueEn":{}},{"groupId":10005,"itemValue":"沙发桌","sort":53,"backImg":{},"itemName":"房源配置","standby1":{},"id":20052,"itemValueEn":{}}],"groupCode":"SYS_HOUSE_CONFIG_DICTCODE"}
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null
    var systemTime: String? = null
    var dataSet: DataSetBean? = null
    var pageInfo: Any? = null
    override fun toString(): String {
        return "ListingConfigResponse{" +
                "result=" + result +
                ", message='" + message + '\''.toString() +
                ", token=" + token +
                ", systemTime='" + systemTime + '\''.toString() +
                ", dataSet=" + dataSet +
                ", pageInfo=" + pageInfo +
                '}'.toString()
    }

    class DataSetBean {
        /**
         * groupName : 房源配置
         * groupNameEn :
         * id : 10005
         * items : [{"groupId":10005,"itemValue":"鞋架","sort":15,"backImg":{},"itemName":"房源配置","standby1":{},"id":20021,"itemValueEn":"shoe rack"},{"groupId":10005,"itemValue":"落地灯","sort":16,"backImg":{},"itemName":"房源配置","standby1":{},"id":20022,"itemValueEn":{}},{"groupId":10005,"itemValue":"沙发椅","sort":17,"backImg":{},"itemName":"房源配置","standby1":{},"id":20023,"itemValueEn":{}},{"groupId":10005,"itemValue":"靠背椅","sort":25,"backImg":{},"itemName":"房源配置","standby1":{},"id":20024,"itemValueEn":{}},{"groupId":10005,"itemValue":"书架","sort":26,"backImg":{},"itemName":"房源配置","standby1":{},"id":20025,"itemValueEn":{}},{"groupId":10005,"itemValue":"电视","sort":27,"backImg":{},"itemName":"房源配置","standby1":{},"id":20026,"itemValueEn":{}},{"groupId":10005,"itemValue":"电视架","sort":28,"backImg":{},"itemName":"房源配置","standby1":{},"id":20027,"itemValueEn":{}},{"groupId":10005,"itemValue":"艺术品","sort":29,"backImg":{},"itemName":"房源配置","standby1":{},"id":20028,"itemValueEn":{}},{"groupId":10005,"itemValue":"餐具","sort":30,"backImg":{},"itemName":"房源配置","standby1":{},"id":20029,"itemValueEn":{}},{"groupId":10005,"itemValue":"锅","sort":31,"backImg":{},"itemName":"房源配置","standby1":{},"id":20030,"itemValueEn":{}},{"groupId":10005,"itemValue":"烤箱","sort":32,"backImg":{},"itemName":"房源配置","standby1":{},"id":20031,"itemValueEn":{}},{"groupId":10005,"itemValue":"洗衣机","sort":33,"backImg":{},"itemName":"房源配置","standby1":{},"id":20032,"itemValueEn":{}},{"groupId":10005,"itemValue":"冰箱","sort":34,"backImg":{},"itemName":"房源配置","standby1":{},"id":20033,"itemValueEn":{}},{"groupId":10005,"itemValue":"电灶台","sort":35,"backImg":{},"itemName":"房源配置","standby1":{},"id":20034,"itemValueEn":{}},{"groupId":10005,"itemValue":"燃气灶台","sort":36,"backImg":{},"itemName":"房源配置","standby1":{},"id":20035,"itemValueEn":{}},{"groupId":10005,"itemValue":"微波炉","sort":37,"backImg":{},"itemName":"房源配置","standby1":{},"id":20036,"itemValueEn":{}},{"groupId":10005,"itemValue":"咖啡机","sort":38,"backImg":{},"itemName":"房源配置","standby1":{},"id":20037,"itemValueEn":{}},{"groupId":10005,"itemValue":"电热壶","sort":39,"backImg":{},"itemName":"房源配置","standby1":{},"id":20038,"itemValueEn":{}},{"groupId":10005,"itemValue":"洗碗机","sort":40,"backImg":{},"itemName":"房源配置","standby1":{},"id":20039,"itemValueEn":{}},{"groupId":10005,"itemValue":"垃圾桶","sort":41,"backImg":{},"itemName":"房源配置","standby1":{},"id":20040,"itemValueEn":{}},{"groupId":10005,"itemValue":"餐桌椅","sort":42,"backImg":{},"itemName":"房源配置","standby1":{},"id":20041,"itemValueEn":{}},{"groupId":10005,"itemValue":"淋浴","sort":43,"backImg":{},"itemName":"房源配置","standby1":{},"id":20042,"itemValueEn":{}},{"groupId":10005,"itemValue":"浴池","sort":44,"backImg":{},"itemName":"房源配置","standby1":{},"id":20043,"itemValueEn":{}},{"groupId":10005,"itemValue":"床","sort":45,"backImg":{},"itemName":"房源配置","standby1":{},"id":20044,"itemValueEn":{}},{"groupId":10005,"itemValue":"床垫","sort":46,"backImg":{},"itemName":"房源配置","standby1":{},"id":20045,"itemValueEn":{}},{"groupId":10005,"itemValue":"床头柜","sort":47,"backImg":{},"itemName":"房源配置","standby1":{},"id":20046,"itemValueEn":{}},{"groupId":10005,"itemValue":"床灯","sort":48,"backImg":{},"itemName":"房源配置","standby1":{},"id":20047,"itemValueEn":{}},{"groupId":10005,"itemValue":"梳妆台","sort":49,"backImg":{},"itemName":"房源配置","standby1":{},"id":20048,"itemValueEn":{}},{"groupId":10005,"itemValue":"窗帘","sort":50,"backImg":{},"itemName":"房源配置","standby1":{},"id":20049,"itemValueEn":{}},{"groupId":10005,"itemValue":"书桌","sort":51,"backImg":{},"itemName":"房源配置","standby1":{},"id":20050,"itemValueEn":{}},{"groupId":10005,"itemValue":"书桌椅","sort":52,"backImg":{},"itemName":"房源配置","standby1":{},"id":20051,"itemValueEn":{}},{"groupId":10005,"itemValue":"沙发桌","sort":53,"backImg":{},"itemName":"房源配置","standby1":{},"id":20052,"itemValueEn":{}}]
         * groupCode : SYS_HOUSE_CONFIG_DICTCODE
         */

        var groupName: String? = null
        var groupNameEn: Any? = null
        var id: Int = 0
        var groupCode: String? = null
        var items: List<ItemsBean>? = null

        class ItemsBean {
            /**
             * groupId : 10005
             * itemValue : 鞋架
             * sort : 15
             * backImg : {}
             * itemName : 房源配置
             * standby1 : {}
             * id : 20021
             * itemValueEn : shoe rack
             */

            var groupId: Int = 0
            var itemValue: String? = null
            var sort: Int = 0
            var backImg:Any?=null
            var itemName: String? = null
            var standby1: Any? = null
            var id: Int = 0
            var itemValueEn: String? = null
            override fun toString(): String {
                return "ItemsBean(groupId=$groupId, itemValue=$itemValue, sort=$sort, backImg=$backImg, itemName=$itemName, standby1=$standby1, id=$id, itemValueEn=$itemValueEn)"
            }

        }

        override fun toString(): String {
            return "DataSetBean(groupName=$groupName, groupNameEn=$groupNameEn, id=$id, groupCode=$groupCode, items=$items)"
        }
    }
}
