
package io.github.landuo.pojo;

import com.google.gson.annotations.SerializedName;
import io.github.landuo.pojo.battle.Special;
import io.github.landuo.pojo.battle.Sub;
import lombok.ToString;


import java.io.Serializable;


@SuppressWarnings("unused")
@ToString
public class Weapon implements Serializable {

    @SerializedName("en_US")
    private String mEnUS;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("main_power_up")
    private String mMainPowerUp;
    @SerializedName("name")
    private String mName;
    @SerializedName("special")
    private Special mSpecial;
    @SerializedName("sub")
    private Sub mSub;
    @SerializedName("type")
    private String mType;
    @SerializedName("zh_CN")
    private String mZhCN;
    @SerializedName("shortName")
    private String mShortName;

    public String getEnUS() {
        return mEnUS;
    }

    public void setEnUS(String enUS) {
        mEnUS = enUS;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getMainPowerUp() {
        return mMainPowerUp;
    }

    public void setMainPowerUp(String mainPowerUp) {
        mMainPowerUp = mainPowerUp;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Special getSpecial() {
        return mSpecial;
    }

    public void setSpecial(Special special) {
        mSpecial = special;
    }

    public Sub getSub() {
        return mSub;
    }

    public void setSub(Sub sub) {
        mSub = sub;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getZhCN() {
        return mZhCN;
    }

    public void setZhCN(String zhCN) {
        mZhCN = zhCN;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String mShortName) {
        this.mShortName = mShortName;
    }
}
