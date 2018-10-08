package cn.yc.library.bean.response

class CityResponse {
    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-08-28 18:34:29
     * dataSet : [{"sub":[{"sub":[{"level":3,"cityNameCn":"桃源街道","latitude":114.47502,"cityNameEn":"","pid":2,"id":16,"longitude":22.59693},{"level":3,"cityNameCn":"粤海街道","latitude":114.47502,"cityNameEn":"","pid":2,"id":17,"longitude":22.59693},{"level":3,"cityNameCn":"西丽街道","latitude":114.47502,"cityNameEn":"","pid":2,"id":18,"longitude":22.59693}],"level":2,"cityNameCn":"南山区","latitude":113.93047,"cityNameEn":"nanshan","pid":1,"id":2,"longitude":22.533012},{"sub":[{"level":3,"cityNameCn":"沙头角街道","latitude":114.47502,"cityNameEn":"","pid":9,"id":51,"longitude":22.59693},{"level":3,"cityNameCn":"海山街道","latitude":114.47502,"cityNameEn":"","pid":9,"id":52,"longitude":22.59693},{"level":3,"cityNameCn":"盐田街道","latitude":114.47502,"cityNameEn":"","pid":9,"id":53,"longitude":22.59693},{"level":3,"cityNameCn":"梅沙街道","latitude":114.47502,"cityNameEn":"","pid":9,"id":54,"longitude":22.59693}],"level":2,"cityNameCn":"盐田区","latitude":114.23688,"cityNameEn":"yantian","pid":1,"id":9,"longitude":22.5565},{"sub":[{"level":3,"cityNameCn":"坪山街道","latitude":114.47502,"cityNameEn":"","pid":10,"id":55,"longitude":22.59693},{"level":3,"cityNameCn":"坑梓街道","latitude":114.47502,"cityNameEn":"","pid":10,"id":56,"longitude":22.59693}],"level":2,"cityNameCn":"坪山区","latitude":114.34625,"cityNameEn":"pinshan","pid":1,"id":10,"longitude":22.691254},{"sub":[{"level":3,"cityNameCn":"南澳街道","latitude":114.47502,"cityNameEn":"","pid":11,"id":57,"longitude":22.59693},{"level":3,"cityNameCn":"大鹏街道","latitude":114.47502,"cityNameEn":"","pid":11,"id":58,"longitude":22.59693},{"level":3,"cityNameCn":"葵涌街道","latitude":114.47502,"cityNameEn":"","pid":11,"id":59,"longitude":22.59693}],"level":2,"cityNameCn":"大鹏新区","latitude":114.47502,"cityNameEn":"dapeng","pid":1,"id":11,"longitude":22.59693}],"level":1,"cityNameCn":"深圳市","latitude":114.05786,"cityNameEn":"shenzhen","pid":-1,"id":1,"longitude":22.543097}]
     * pageInfo : null
     */

    var result: Int = 0
    var message: String? = null
    var token: Any? = null

    var systemTime: String? = null
    var pageInfo: Any? = null
    var dataSet: List<DataSetBean>? = null

    override fun toString(): String {
        return "CityResponse{" +
                "result=" + result +
                ", message='" + message + '\''.toString() +
                ", token=" + token +
                ", systemTime='" + systemTime + '\''.toString() +
                ", pageInfo=" + pageInfo +
                ", dataSet=" + dataSet +
                '}'.toString()
    }

    class DataSetBean {

        /**
         * sub : [{"sub":[{"level":3,"cityNameCn":"桃源街道","latitude":114.47502,"cityNameEn":"","pid":2,"id":16,"longitude":22.59693},{"level":3,"cityNameCn":"粤海街道","latitude":114.47502,"cityNameEn":"","pid":2,"id":17,"longitude":22.59693},{"level":3,"cityNameCn":"西丽街道","latitude":114.47502,"cityNameEn":"","pid":2,"id":18,"longitude":22.59693}],"level":2,"cityNameCn":"南山区","latitude":113.93047,"cityNameEn":"nanshan","pid":1,"id":2,"longitude":22.533012},{"sub":[{"level":3,"cityNameCn":"沙头角街道","latitude":114.47502,"cityNameEn":"","pid":9,"id":51,"longitude":22.59693},{"level":3,"cityNameCn":"海山街道","latitude":114.47502,"cityNameEn":"","pid":9,"id":52,"longitude":22.59693},{"level":3,"cityNameCn":"盐田街道","latitude":114.47502,"cityNameEn":"","pid":9,"id":53,"longitude":22.59693},{"level":3,"cityNameCn":"梅沙街道","latitude":114.47502,"cityNameEn":"","pid":9,"id":54,"longitude":22.59693}],"level":2,"cityNameCn":"盐田区","latitude":114.23688,"cityNameEn":"yantian","pid":1,"id":9,"longitude":22.5565},{"sub":[{"level":3,"cityNameCn":"坪山街道","latitude":114.47502,"cityNameEn":"","pid":10,"id":55,"longitude":22.59693},{"level":3,"cityNameCn":"坑梓街道","latitude":114.47502,"cityNameEn":"","pid":10,"id":56,"longitude":22.59693}],"level":2,"cityNameCn":"坪山区","latitude":114.34625,"cityNameEn":"pinshan","pid":1,"id":10,"longitude":22.691254},{"sub":[{"level":3,"cityNameCn":"南澳街道","latitude":114.47502,"cityNameEn":"","pid":11,"id":57,"longitude":22.59693},{"level":3,"cityNameCn":"大鹏街道","latitude":114.47502,"cityNameEn":"","pid":11,"id":58,"longitude":22.59693},{"level":3,"cityNameCn":"葵涌街道","latitude":114.47502,"cityNameEn":"","pid":11,"id":59,"longitude":22.59693}],"level":2,"cityNameCn":"大鹏新区","latitude":114.47502,"cityNameEn":"dapeng","pid":1,"id":11,"longitude":22.59693}]
         * level : 1
         * cityNameCn : 深圳市
         * latitude : 114.05786
         * cityNameEn : shenzhen
         * pid : -1
         * id : 1
         * longitude : 22.543097
         */

        var level: Int = 0
        var cityNameCn: String? = null
        var latitude: Double = 0.toDouble()
        var cityNameEn: String? = null
        var pid: Int = 0
        var id: Int = 0
        var longitude: Double = 0.toDouble()
        var sub: List<SubBeanX>? = null
        override fun toString(): String {
            return "DataSetBean{" +
                    "level=" + level +
                    ", cityNameCn='" + cityNameCn + '\''.toString() +
                    ", latitude=" + latitude +
                    ", cityNameEn='" + cityNameEn + '\''.toString() +
                    ", pid=" + pid +
                    ", id=" + id +
                    ", longitude=" + longitude +
                    ", sub=" + sub +
                    '}'.toString()
        }

        class SubBeanX {
            /**
             * sub : [{"level":3,"cityNameCn":"桃源街道","latitude":114.47502,"cityNameEn":"","pid":2,"id":16,"longitude":22.59693},{"level":3,"cityNameCn":"粤海街道","latitude":114.47502,"cityNameEn":"","pid":2,"id":17,"longitude":22.59693},{"level":3,"cityNameCn":"西丽街道","latitude":114.47502,"cityNameEn":"","pid":2,"id":18,"longitude":22.59693}]
             * level : 2
             * cityNameCn : 南山区
             * latitude : 113.93047
             * cityNameEn : nanshan
             * pid : 1
             * id : 2
             * longitude : 22.533012
             */

            var level: Int = 0
            var cityNameCn: String? = null
            var latitude: Double = 0.toDouble()
            var cityNameEn: String? = null
            var pid: Int = 0
            var id: Int = 0
            var longitude: Double = 0.toDouble()
            var sub: List<SubBean>? = null

            class SubBean {
                /**
                 * level : 3
                 * cityNameCn : 桃源街道
                 * latitude : 114.47502
                 * cityNameEn :
                 * pid : 2
                 * id : 16
                 * longitude : 22.59693
                 */

                var level: Int = 0
                var cityNameCn: String? = null
                var latitude: Double = 0.toDouble()
                var cityNameEn: String? = null
                var pid: Int = 0
                var id: Int = 0
                var longitude: Double = 0.toDouble()
                override fun toString(): String {
                    return "SubBean(level=$level, cityNameCn=$cityNameCn, latitude=$latitude, cityNameEn=$cityNameEn, pid=$pid, id=$id, longitude=$longitude)"
                }
            }

            override fun toString(): String {
                return "SubBeanX(level=$level, cityNameCn=$cityNameCn, latitude=$latitude, cityNameEn=$cityNameEn, pid=$pid, id=$id, longitude=$longitude, sub=$sub)"
            }
        }
    }
}
