
package io.github.landuo.pojo.salmon;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


@SuppressWarnings("unused")
public class Salmon implements Serializable {

    @SerializedName("details")
    private List<SalmonDetail> mDetails;
    @SerializedName("schedules")
    private List<SalmonSchedule> mSchedules;

    public List<SalmonDetail> getDetails() {
        return mDetails;
    }

    public void setDetails(List<SalmonDetail> details) {
        mDetails = details;
    }

    public List<SalmonSchedule> getSchedules() {
        return mSchedules;
    }

    public void setSchedules(List<SalmonSchedule> schedules) {
        mSchedules = schedules;
    }

}
