package io.github.landuo.rcon.plugin;

import io.github.landuo.l4d2.entity.A2sPlayers;
import io.github.landuo.l4d2.entity.Player;
import io.github.landuo.l4d2.entity.SourceServerInfo;
import io.github.landuo.l4d2.exception.ServiceException;
import io.github.landuo.l4d2.utils.A2sUtil;
import io.github.landuo.l4d2.utils.L4d2Util;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.console.data.Value;

import java.util.List;
import java.util.Optional;

/**
 * @author accidia
 */
public class ServerCommand extends JSimpleCommand {
    public static final ServerCommand INSTANCE = new ServerCommand();

    private ServerCommand() {
        super(L4d2QueryPlugin.INSTANCE, "查询", "server");
    }


    @Handler
    public void server(CommandContext context) {
        A2sUtil.soTimeOut = 2000;
        Value<List<Server>> servers = L4d2PluginData.INSTANCE.servers;
        List<Server> serverList = servers.get();
        StringBuilder builder = new StringBuilder();
        if (!serverList.isEmpty()) {
            serverList.parallelStream().forEach(s -> {
                SourceServerInfo a2sInfo;
                try {
                    a2sInfo = L4d2Util.getServerInfo(s.getIp(), s.getPort(), s.getPassword());
                } catch (ServiceException e) {
                    builder.append("服务器").append(s.getName()).append("连接不上，原因为：").append(e.getMessage()).append("\n");
                    return;
                }
                builder.append("服务器：").append(a2sInfo.getName()).append("\n");
                builder.append("地图：").append(a2sInfo.getMap()).append("\n");
                builder.append("人数：").append(a2sInfo.getPlayers()).append("/").append(a2sInfo.getMaxPlayers()).append("\n");
                builder.append("延迟：").append(a2sInfo.getTimes()).append("ms").append("\n").append("-----------------").append("\n");
            });
        } else {
            builder.append("无记录.");
        }
        context.getSender().sendMessage(builder.toString());
    }

    @Handler
    public void server(CommandContext context, @Name("自定义的服务器名") String serverName) {
        Server server = getServerWithServerName(context, serverName);
        String ip = server.getIp();
        Integer port = server.getPort();
        SourceServerInfo serverInfo;
        try {
            serverInfo = L4d2Util.getServerInfo(server.getIp(), server.getPort(), server.getPassword());
        } catch (ServiceException e) {
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

        A2sPlayers players = L4d2Util.getPlayers(ip, port, server.getPassword());
        if (players == null || players.getPlayers() == null || players.getPlayers().isEmpty()) {
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
