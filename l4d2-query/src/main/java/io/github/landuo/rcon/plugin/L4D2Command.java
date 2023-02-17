package io.github.landuo.rcon.plugin;

import io.github.landuo.l4d2.entity.A2sPlayers;
import io.github.landuo.l4d2.entity.Player;
import io.github.landuo.l4d2.entity.RconRequest;
import io.github.landuo.l4d2.entity.SourceServerInfo;
import io.github.landuo.l4d2.exception.ServiceException;
import io.github.landuo.l4d2.utils.A2sUtil;
import io.github.landuo.l4d2.utils.RconUtil;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.data.Value;

import java.util.List;
import java.util.Optional;

/**
 * @author accidia
 */
public class L4D2Command extends JCompositeCommand {
    public static final L4D2Command INSTANCE = new L4D2Command();

    private L4D2Command() {
        super(L4d2QueryPlugin.INSTANCE, "l4d2");
    }

    @SubCommand
    @Description("添加L4D2服务器, rcon密码为空时只能查看不可执行命令")
    public void add(CommandContext context, @Name("自定义服务器名") String name, @Name("ip") String ip, @Name("port") Integer port, @Name("rcon密码") String password) {
        Value<List<Server>> servers = L4d2PluginData.INSTANCE.servers;
        List<Server> serverList = servers.get();
        // 查看服务器名是否重复
        if (L4d2PluginData.INSTANCE.serverExist(name)) {
            context.getSender().sendMessage("此服务器名已存在，请更换一个.");
            return;
        }
        // 没有重复则添加进插件的数据中
        Server server = new Server(name, ip, port, password, context.getSender().getUser() != null ? context.getSender().getUser().getId() : 0);
        serverList.add(server);
        servers.set(serverList);
        context.getSender().sendMessage("服务器添加成功.");
    }

    @SubCommand
    @Description("移除L4D2服务器")
    public void remove(CommandContext context, @Name("自定义服务器名") String name) {
        Value<List<Server>> servers = L4d2PluginData.INSTANCE.servers;
        List<Server> serverList = servers.get();
        Optional<Server> optional = serverList.stream().filter(s -> name.equals(s.getName())).findFirst();
        if (!optional.isPresent()) {
            context.getSender().sendMessage("该服务器不存在，请确认后重试.");
            return;
        }
        boolean remove = serverList.removeIf(s -> context.getSender().getUser() == null ||
                (name.equals(s.getName()) && s.getOnwer() == context.getSender().getUser().getId()));
        context.getSender().sendMessage(remove ? "服务器移除成功." : "您无权对此服务器进行操作.");
    }

    @SubCommand
    @Description("列出已记录的服务器")
    public void ls(CommandContext context) {
        Value<List<Server>> servers = L4d2PluginData.INSTANCE.servers;
        List<Server> serverList = servers.get();
        StringBuilder builder = new StringBuilder();
        if (!serverList.isEmpty()) {
            serverList.forEach(s -> builder.append(s.getName()).append("\n"));
        } else {
            builder.append("无记录.");
        }
        context.getSender().sendMessage(builder.toString());
    }

    @SubCommand
    @Description("重命名L4D2服务器")
    public void rename(CommandContext context, @Name("旧自定义服务器名") String oldName, @Name("新自定义服务器名") String newName) {
        Server server = getServerWithServerName(context, oldName);
        if (L4d2PluginData.INSTANCE.hasPerm(oldName, context.getSender().getUser())) {
            context.getSender().sendMessage("您无权对此服务器进行操作.");
            return;
        }
        if (L4d2PluginData.INSTANCE.serverExist(newName)) {
            context.getSender().sendMessage("此服务器名已存在，请更换一个.");
            return;
        }
        remove(context, oldName);
        add(context, newName, server.getIp(), server.getPort(), server.getPassword());
    }

    @SubCommand
    @Description("服务器执行命令")
    public void exec(CommandContext context, @Name("自定义的服务器名") String serverName, @Name("执行的命令") String command) {
        Server server = getServerWithServerName(context, serverName);
        if (L4d2PluginData.INSTANCE.hasPerm(serverName, context.getSender().getUser())) {
            context.getSender().sendMessage("您无权对此服务器进行操作.");
            return;
        }
        RconRequest request = new RconRequest(server.getIp(), server.getPort(), server.getPassword(), command);
        try {
            String response = RconUtil.send(request);
            if (response.length() > 5000) {
                context.getSender().sendMessage("数据太大, 不能发送.");
                return;
            }
            context.getSender().sendMessage(response);
        } catch (ServiceException e) {
            context.getSender().sendMessage(e.getMessage());
        }
    }

    @SubCommand
    @Description("查看服务器信息")
    public void server(CommandContext context, @Name("自定义的服务器名") String serverName) {
        Server server = getServerWithServerName(context, serverName);
        String ip = server.getIp();
        Integer port = server.getPort();
        SourceServerInfo serverInfo = A2sUtil.getA2sInfo(ip + ":" + port);
        if (serverInfo == null || serverInfo.getName() == null) {
            context.getSender().sendMessage("暂时查询不到服务器, 请稍后再试.");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("服务器名: ").append(serverInfo.getName()).append("\n");
        builder.append("游戏: ").append(serverInfo.getGame()).append("\n");
        builder.append("地图: ").append(serverInfo.getMap()).append("\n");
        builder.append("玩家: ").append(serverInfo.getPlayers()).append("/").append(serverInfo.getMaxPlayers()).append("\n");
        builder.append("Value反作弊: ").append("secured".equals(serverInfo.getVac()) ? "安全" : "不安全").append("\n");
        builder.append("延迟: ").append(serverInfo.getTimes()).append("\n");

        A2sPlayers players = A2sUtil.getPlayers(ip + ":" + port);
        if (players.getPlayers() == null || players.getPlayers().isEmpty()) {
            context.getSender().sendMessage(builder.toString());
            return;
        }
        builder.append("-------------------------").append("\n");
        for (Player player : players.getPlayers()) {
            builder.append(String.format("玩家: %s, 比分: %d, 时间: %s", player.getName(), player.getScore(), player.getDuration())).append("\n");
        }
        context.getSender().sendMessage(builder.toString());
    }

    private Server getServerWithServerName(CommandContext context, String serverName) {
        Value<List<Server>> servers = L4d2PluginData.INSTANCE.servers;
        List<Server> serverList = servers.get();
        Optional<Server> optional = serverList.stream().filter(s -> serverName.equals(s.getName())).findFirst();
        if (!optional.isPresent()) {
            context.getSender().sendMessage("该服务器不存在，请确认后重试.");
            throw new RuntimeException("服务器 {" + serverName + "} 不存在");
        }
        return optional.get();
    }

}
