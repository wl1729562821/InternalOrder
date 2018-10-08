package cn.yc.library.bean.response;

import java.util.List;

public class AttendanceResponse {
    @Override
    public String toString() {
        return "AttendanceResponse{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", token=" + token +
                ", systemTime='" + systemTime + '\'' +
                ", dataSet=" + dataSet +
                ", pageInfo=" + pageInfo +
                '}';
    }

    /**
     * result : 0
     * message : 请求成功
     * token : null
     * systemTime : 2018-07-31 14:23:25
     * dataSet : {"attendance":[{"postTime":"2018-07-30 20:10:45"}],"vacates":[{"vacateType":1,"days":2,"beginTime":"2018-07-26 14:15:18","endTime":"2018-07-28 14:15:26"}]}
     * pageInfo : null
     */

    private int result;
    private String message;
    private Object token;
    private String systemTime;
    private DataSetBean dataSet;
    private Object pageInfo;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

    public DataSetBean getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSetBean dataSet) {
        this.dataSet = dataSet;
    }

    public Object getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Object pageInfo) {
        this.pageInfo = pageInfo;
    }

    public static class DataSetBean {
        private List<AttendanceBean> attendance;
        private List<VacatesBean> vacates;

        @Override
        public String toString() {
            return "DataSetBean{" +
                    "attendance=" + attendance +
                    ", vacates=" + vacates +
                    '}';
        }

        public List<AttendanceBean> getAttendance() {
            return attendance;
        }

        public void setAttendance(List<AttendanceBean> attendance) {
            this.attendance = attendance;
        }

        public List<VacatesBean> getVacates() {
            return vacates;
        }

        public void setVacates(List<VacatesBean> vacates) {
            this.vacates = vacates;
        }

        public static class AttendanceBean {
            /**
             * postTime : 2018-07-30 20:10:45
             */

            private String postTime;

            @Override
            public String toString() {
                return "AttendanceBean{" +
                        "postTime='" + postTime + '\'' +
                        '}';
            }

            public String getPostTime() {
                return postTime;
            }

            public void setPostTime(String postTime) {
                this.postTime = postTime;
            }
        }

        public static class VacatesBean {
            /**
             * vacateType : 1
             * days : 2
             * beginTime : 2018-07-26 14:15:18
             * endTime : 2018-07-28 14:15:26
             */

            private int vacateType;
            private int days;
            private String beginTime;
            private String endTime;

            public int getVacateType() {
                return vacateType;
            }

            public void setVacateType(int vacateType) {
                this.vacateType = vacateType;
            }

            public int getDays() {
                return days;
            }

            public void setDays(int days) {
                this.days = days;
            }

            public String getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(String beginTime) {
                this.beginTime = beginTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            @Override
            public String toString() {
                return "VacatesBean{" +
                        "vacateType=" + vacateType +
                        ", days=" + days +
                        ", beginTime='" + beginTime + '\'' +
                        ", endTime='" + endTime + '\'' +
                        '}';
            }
        }
    }
}
