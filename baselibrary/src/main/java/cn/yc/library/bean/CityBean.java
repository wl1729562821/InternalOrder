package cn.yc.library.bean;

import java.util.List;

public class CityBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "data=" + data +
                '}';
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "City='" + City + '\'' +
                    ", Community='" + Community + '\'' +
                    ", Subcommunity='" + Subcommunity + '\'' +
                    ", Property='" + Property + '\'' +
                    '}';
        }

        /**
         * City  : Dubai
         * Community : Academic City
         * Subcommunity : Block 3
         * Property : Desert Palm
         */

        private String City;
        private String Community;
        private String Subcommunity;
        private String Property;

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getCommunity() {
            return Community;
        }

        public void setCommunity(String Community) {
            this.Community = Community;
        }

        public String getSubcommunity() {
            return Subcommunity;
        }

        public void setSubcommunity(String Subcommunity) {
            this.Subcommunity = Subcommunity;
        }

        public String getProperty() {
            return Property;
        }

        public void setProperty(String Property) {
            this.Property = Property;
        }
    }
}
