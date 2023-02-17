
package io.github.landuo.pojo.salmon;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class SalmonSchedule implements Serializable {

    @SerializedName("end_time")
    private Long mEndTime;
    @SerializedName("start_time")
    private Long mStartTime;

    public Long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Long endTime) {
        mEndTime = endTime;
    }

    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Long startTime) {
        mStartTime = startTime;
    }

}
