package io.github.landuo.enums;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Optional;

/**
 * 游戏模式枚举类
 * 
 * @author accidia
 */
public enum GameModeEnum {
    /**
     *
     */
    SPLAT_ZONES("splat_zones", "Splat Zones", "区域"), RAINMAKER("rainmaker", "Rainmaker", "鱼"),
    CLAM_BLITZ("clam_blitz", "Clam Blitz", "蛤蜊"), TOWER_CONTROL("tower_control", "Tower Control", "塔"),
    TURF_WAR("turf_war", "Turf War", "涂地");

    private final String key;
    private final String secondKey;
    private final String zhName;

    public static String getZhNameByKey(String key) {
        Optional<GameModeEnum> gameModeEnum = Arrays.stream(values())
                .filter(e -> e.getKey().equals(key) || e.getSecondKey().equals(key)).findFirst();
        return gameModeEnum.isPresent() ? gameModeEnum.get().getZhName() : StrUtil.EMPTY;
    }

    GameModeEnum(String key, String secondKey, String zhName) {
        this.key = key;
        this.secondKey = secondKey;
        this.zhName = zhName;
    }

    public String getKey() {
        return key;
    }

    public String getSecondKey() {
        return secondKey;
    }

    public String getZhName() {
        return zhName;
    }
}
