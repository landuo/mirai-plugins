package io.github.landuo.rcon.plugin;

import kotlin.Lazy;
import kotlin.LazyKt;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.permission.*;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;

public final class L4d2QueryPlugin extends JavaPlugin {
    public static final L4d2QueryPlugin INSTANCE = new L4d2QueryPlugin();

    private L4d2QueryPlugin() {
        super(new JvmPluginDescriptionBuilder("io.github.landuo.l4d2-query", "0.1.0")
                .name("L4D2 Query")
                .author("accidia")
                .build());
    }

    @Override
    public void onEnable() {
        CommandManager.INSTANCE.registerCommand(L4D2Command.INSTANCE, false);
        CommandManager.INSTANCE.registerCommand(StatusCommand.INSTANCE, false);
        CommandManager.INSTANCE.registerCommand(ServerCommand.INSTANCE, false);
        this.reloadPluginData(L4d2PluginData.INSTANCE);
        getLogger().info("Plugin loaded!");
    }

    public static final Lazy<Permission> myCustomPermission = LazyKt.lazy(() -> {  // Lazy: Lazy 是必须的, console 不允许提前访问权限系统
        // 注册一条权限节点 org.example.mirai-example:my-permission
        // 并以 org.example.mirai-example:* 为父节点

        // @param: parent: 父权限
        //                 在 Console 内置权限系统中, 如果某人拥有父权限
        //                 那么意味着此人也拥有该权限 (org.example.mirai-example:my-permission)
        // @func: PermissionIdNamespace.permissionId: 根据插件 id 确定一条权限 id
        try {
            return PermissionService.getInstance().register(
                    INSTANCE.permissionId("my-permission"),
                    "一条自定义权限",
                    INSTANCE.getParentPermission()
            );
        } catch (PermissionRegistryConflictException e) {
            throw new RuntimeException(e);
        }
    });

    public static boolean hasCustomPermission(User usr) {
        PermitteeId pid;
        if (usr instanceof Member) {
            pid = new AbstractPermitteeId.ExactMember(((Member) usr).getGroup().getId(), usr.getId());
        } else {
            pid = new AbstractPermitteeId.ExactUser(usr.getId());
        }
        return PermissionService.hasPermission(pid, myCustomPermission.getValue());
    }
}