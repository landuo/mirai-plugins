
package io.github.landuo.pojo.battle;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;


import java.io.Serializable;


@SuppressWarnings("unused")
@ToString
public class Special implements Serializable {

    @SerializedName("id")
    private String mId;
    @SerializedName("image_a")
    private String mImageA;
    @SerializedName("image_b")
    private String mImageB;
    @SerializedName("name")
    private String mName;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getImageA() {
        return mImageA;
    }

    public void setImageA(String imageA) {
        mImageA = imageA;
    }

    public String getImageB() {
        return mImageB;
    }

    public void setImageB(String imageB) {
        mImageB = imageB;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
