package io.github.landuo.rcon.plugin;

import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginData;
import net.mamoe.mirai.contact.User;

import java.util.List;
import java.util.Objects;

/**
 * @author accidia
 */
public class L4d2PluginData extends JavaAutoSavePluginData {
    public static final L4d2PluginData INSTANCE = new L4d2PluginData();

    private L4d2PluginData() {
        super("l4d2-query");
    }

    public final Value<List<Server>> servers = typedValue("servers",
            createKType(List.class, createKType(Server.class))
    );

    public boolean serverExist(String serverName) {
        List<Server> serverList = servers.get();
        return !serverList.isEmpty() && serverList.stream().anyMatch(s -> Objects.equals(s.getName(), serverName));
    }

    /**
     * 查看发送人是否有权限操作该服务器
     *
     * @param serverName 服务器名字
     * @param user       发送人
     * @return
     */
    public boolean hasPerm(String serverName, User user) {
        // 控制台ID为0
        if (user == null) {
            return false;
        }
        List<Server> serverList = servers.get();
        return serverList.isEmpty() || serverList.stream().noneMatch(s -> Objects.equals(s.getName(), serverName)
                && user.getId() == s.getOnwer());
    }
}
