
package io.github.landuo.pojo.battle;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

import java.io.Serializable;


@SuppressWarnings("unused")
@ToString
public class Rule implements Serializable {

    @SerializedName("key")
    private String mKey;
    @SerializedName("multiline_name")
    private String mMultilineName;
    @SerializedName("name")
    private String mName;

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getMultilineName() {
        return mMultilineName;
    }

    public void setMultilineName(String multilineName) {
        mMultilineName = multilineName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
