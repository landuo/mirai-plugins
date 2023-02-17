
package io.github.landuo.pojo.battle;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;


import java.io.Serializable;


@SuppressWarnings("unused")
@ToString
public class GameMode implements Serializable {

    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
