
package io.github.landuo.pojo.salmon;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class SalmonStage implements Serializable {

    @SerializedName("image")
    private String mImage;
    @SerializedName("name")
    private String mName;

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
