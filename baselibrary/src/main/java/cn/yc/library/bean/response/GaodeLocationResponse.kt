package cn.yc.library.bean.response

class GaodeLocationResponse {
    /**
     * status : 1
     * info : OK
     * infocode : 10000
     * regeocode : {"formatted_address":"广东省深圳市南山区粤海街道金证科技大厦金证科技大楼","addressComponent":{"country":"中国","province":"广东省","city":"深圳市","citycode":"0755","district":"南山区","adcode":"440305","township":"粤海街道","towncode":"440305007000","neighborhood":{"name":[],"type":[]},"building":{"name":"金证科技大厦","type":"商务住宅;楼宇;商务写字楼"},"streetNumber":{"street":"高新南五道","number":"9号","location":"113.947453,22.5349481","direction":"西","distance":"29.7262"},"businessAreas":[{"location":"113.94703575799998,22.542234107999988","name":"科技园","id":"440305"},{"location":"113.95482722485211,22.54298533136095","name":"大冲","id":"440305"},{"location":"113.95071633333332,22.514798891666672","name":"深圳湾","id":"440305"}]}}
     */

    var status: String? = null
    var info: String? = null
    var infocode: String? = null
    var regeocode: RegeocodeBean? = null

    class RegeocodeBean {
        /**
         * formatted_address : 广东省深圳市南山区粤海街道金证科技大厦金证科技大楼
         * addressComponent : {"country":"中国","province":"广东省","city":"深圳市","citycode":"0755","district":"南山区","adcode":"440305","township":"粤海街道","towncode":"440305007000","neighborhood":{"name":[],"type":[]},"building":{"name":"金证科技大厦","type":"商务住宅;楼宇;商务写字楼"},"streetNumber":{"street":"高新南五道","number":"9号","location":"113.947453,22.5349481","direction":"西","distance":"29.7262"},"businessAreas":[{"location":"113.94703575799998,22.542234107999988","name":"科技园","id":"440305"},{"location":"113.95482722485211,22.54298533136095","name":"大冲","id":"440305"},{"location":"113.95071633333332,22.514798891666672","name":"深圳湾","id":"440305"}]}
         */

        var formatted_address: String? = null
        var addressComponent: AddressComponentBean? = null

        class AddressComponentBean {
            /**
             * country : 中国
             * province : 广东省
             * city : 深圳市
             * citycode : 0755
             * district : 南山区
             * adcode : 440305
             * township : 粤海街道
             * towncode : 440305007000
             * neighborhood : {"name":[],"type":[]}
             * building : {"name":"金证科技大厦","type":"商务住宅;楼宇;商务写字楼"}
             * streetNumber : {"street":"高新南五道","number":"9号","location":"113.947453,22.5349481","direction":"西","distance":"29.7262"}
             * businessAreas : [{"location":"113.94703575799998,22.542234107999988","name":"科技园","id":"440305"},{"location":"113.95482722485211,22.54298533136095","name":"大冲","id":"440305"},{"location":"113.95071633333332,22.514798891666672","name":"深圳湾","id":"440305"}]
             */

            var country: String? = null
            var province: String? = null
            var city: String? = null
            var citycode: String? = null
            var district: String? = null
            var adcode: String? = null
            var township: String? = null
            var towncode: String? = null
            var neighborhood: NeighborhoodBean? = null
            var building: BuildingBean? = null
            var streetNumber: StreetNumberBean? = null
            var businessAreas: List<BusinessAreasBean>? = null

            class NeighborhoodBean {
                var name: List<*>? = null
                var type: List<*>? = null
            }

            class BuildingBean {
                /**
                 * name : 金证科技大厦
                 * type : 商务住宅;楼宇;商务写字楼
                 */

                var name: String? = null
                var type: String? = null
                override fun toString(): String {
                    return "BuildingBean(name=$name, type=$type)"
                }
            }

            class StreetNumberBean {
                /**
                 * street : 高新南五道
                 * number : 9号
                 * location : 113.947453,22.5349481
                 * direction : 西
                 * distance : 29.7262
                 */

                var street: String? = null
                var number: String? = null
                var location: String? = null
                var direction: String? = null
                var distance: String? = null
                override fun toString(): String {
                    return "StreetNumberBean(street=$street, number=$number, location=$location, direction=$direction, distance=$distance)"
                }
            }

            class BusinessAreasBean {
                /**
                 * location : 113.94703575799998,22.542234107999988
                 * name : 科技园
                 * id : 440305
                 */

                var location: String? = null
                var name: String? = null
                var id: String? = null
                override fun toString(): String {
                    return "BusinessAreasBean(location=$location, name=$name, id=$id)"
                }
            }

            override fun toString(): String {
                return "AddressComponentBean(country=$country, province=$province, city=$city, citycode=$citycode, district=$district, adcode=$adcode, township=$township, towncode=$towncode, neighborhood=$neighborhood, building=$building, streetNumber=$streetNumber, businessAreas=$businessAreas)"
            }
        }

        override fun toString(): String {
            return "RegeocodeBean(formatted_address=$formatted_address, addressComponent=$addressComponent)"
        }
    }

    override fun toString(): String {
        return "GaodeLocationResponse(status=$status, info=$info, infocode=$infocode, regeocode=$regeocode)"
    }
}
