package io.github.landuo;

import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginConfig;

/**
 * splatoon配置
 *
 * @author accidia
 */
public class SpPluginConfig extends JavaAutoSavePluginConfig {
    public static final SpPluginConfig INSTANCE = new SpPluginConfig();

    private SpPluginConfig() {
        super("sp-config");
    }

    public final Value<SpConfig> config = typedValue("config",
            createKType(SpConfig.class), new SpConfig()
    );

}
