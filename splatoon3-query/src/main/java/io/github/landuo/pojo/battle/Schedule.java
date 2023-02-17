
package io.github.landuo.pojo.battle;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;


import java.io.Serializable;
import java.util.List;


@SuppressWarnings("unused")
@ToString
public class Schedule implements Serializable {

    @SerializedName("gachi")
    private List<Gachi> mGachi;
    @SerializedName("league")
    private List<League> mLeague;
    @SerializedName("regular")
    private List<Regular> mRegular;

    public List<Gachi> getGachi() {
        return mGachi;
    }

    public void setGachi(List<Gachi> gachi) {
        mGachi = gachi;
    }

    public List<League> getLeague() {
        return mLeague;
    }

    public void setLeague(List<League> league) {
        mLeague = league;
    }

    public List<Regular> getRegular() {
        return mRegular;
    }

    public void setRegular(List<Regular> regular) {
        mRegular = regular;
    }

}
