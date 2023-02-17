
package io.github.landuo.pojo.battle;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

import java.io.Serializable;


@SuppressWarnings("unused")
@ToString
public class League implements Serializable {

    @SerializedName("end_time")
    private Long mEndTime;
    @SerializedName("game_mode")
    private GameMode mGameMode;
    @SerializedName("id")
    private Long mId;
    @SerializedName("rule")
    private Rule mRule;
    @SerializedName("stage_a")
    private StageA mStageA;
    @SerializedName("stage_b")
    private StageB mStageB;
    @SerializedName("start_time")
    private Long mStartTime;

    public Long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Long endTime) {
        mEndTime = endTime;
    }

    public GameMode getGameMode() {
        return mGameMode;
    }

    public void setGameMode(GameMode gameMode) {
        mGameMode = gameMode;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Rule getRule() {
        return mRule;
    }

    public void setRule(Rule rule) {
        mRule = rule;
    }

    public StageA getStageA() {
        return mStageA;
    }

    public void setStageA(StageA stageA) {
        mStageA = stageA;
    }

    public StageB getStageB() {
        return mStageB;
    }

    public void setStageB(StageB stageB) {
        mStageB = stageB;
    }

    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Long startTime) {
        mStartTime = startTime;
    }

}
