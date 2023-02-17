
package io.github.landuo.pojo.salmon;

import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.util.List;


@SuppressWarnings("unused")
public class SalmonDetail implements Serializable {

    @SerializedName("end_time")
    private Long mEndTime;
    @SerializedName("stage")
    private SalmonStage mStage;
    @SerializedName("start_time")
    private Long mStartTime;
    @SerializedName("weapons")
    private List<SalmonWeapon> mWeapons;

    public Long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Long endTime) {
        mEndTime = endTime;
    }

    public SalmonStage getStage() {
        return mStage;
    }

    public void setStage(SalmonStage stage) {
        mStage = stage;
    }

    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Long startTime) {
        mStartTime = startTime;
    }

    public List<SalmonWeapon> getWeapons() {
        return mWeapons;
    }

    public void setWeapons(List<SalmonWeapon> weapons) {
        mWeapons = weapons;
    }

}
