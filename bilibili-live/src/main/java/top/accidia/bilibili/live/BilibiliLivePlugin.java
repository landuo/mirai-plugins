package top.accidia.bilibili.live;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

public final class BilibiliLivePlugin extends JavaPlugin {
    public static final BilibiliLivePlugin INSTANCE = new BilibiliLivePlugin();

    private BilibiliLivePlugin() {
        super(new JvmPluginDescriptionBuilder("top.accidia.bilibili-live", "0.1.0")
                .name("Bilibili Live")
                .info("bilibili直播插件")
                .author("accidia")
                .build());
    }

    @Override
    public void onEnable() {
        CommandManager.INSTANCE.registerCommand(BilibiliLiveCommand.INSTANCE, false);
        this.reloadPluginData(BiliBiliLiveData.INSTANCE);
        getLogger().info("Plugin loaded!");
    }
}