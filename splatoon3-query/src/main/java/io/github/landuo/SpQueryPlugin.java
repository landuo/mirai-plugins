package io.github.landuo;

import io.github.landuo.command.LetouCommand;
import io.github.landuo.command.Sp2Command;
import io.github.landuo.command.Sp3Command;
import io.github.landuo.util.ApplicationUtil;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

import java.io.InputStream;

public final class SpQueryPlugin extends JavaPlugin {
    public static final SpQueryPlugin INSTANCE = new SpQueryPlugin();

    private SpQueryPlugin() {
        super(new JvmPluginDescriptionBuilder("io.github.landuo", "0.1.0")
                .name("Splatoon Query")
                .info("Splatoon2和Splatoon3查询插件")
                .author("accidia")
                .build());
    }

    @Override
    public void onEnable() {
        CommandManager.INSTANCE.registerCommand(Sp2Command.INSTANCE, false);
        CommandManager.INSTANCE.registerCommand(Sp3Command.INSTANCE, false);
        CommandManager.INSTANCE.registerCommand(LetouCommand.INSTANCE, false);
        this.reloadPluginConfig(SpPluginConfig.INSTANCE);
        ApplicationUtil.initWeaponData();
        ApplicationUtil.initSchedule();
        getLogger().info("Plugin loaded!");
    }
}