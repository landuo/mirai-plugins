package io.github.landuo.rcon.plugin;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import oshi.hardware.GlobalMemory;

/**
 * @author accidia
 */
public class StatusCommand extends JSimpleCommand {
    public static final StatusCommand INSTANCE = new StatusCommand();

    private StatusCommand() {
        super(L4d2QueryPlugin.INSTANCE, "ss");
    }

    @Handler
    public static void status(CommandContext context) {
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        StringBuilder builder = new StringBuilder();
        builder.append("CPU型号: ").append(OshiUtil.getProcessor().getProcessorIdentifier().getName()).append("\n");
        builder.append("CPU温度: ").append(OshiUtil.getSensors().getCpuTemperature()).append("°C").append("\n");
        builder.append("CPU使用率: ").append(cpuInfo.getUser()).append("%").append("\n");
        GlobalMemory memory = OshiUtil.getMemory();
        builder.append("内存使用: ").append((memory.getTotal() - memory.getAvailable()) / 1073741824L).append("G").append("\n");
        builder.append("内存剩余可用: ").append(memory.toString().replace("Available: ", "").replace(" GiB", "G")).append("\n");
        builder.append("内存使用率: ").append((int) (NumberUtil.div((memory.getTotal() - memory.getAvailable()),
                memory.getTotal()) * 100)).append("%").append("\n");
        builder.append("系统: ").append(OshiUtil.getOs()).append("\n");
        long upTime = OshiUtil.getOs().getSystemUptime();
        long day = upTime / (60 * 60 * 24);
        long hour = (upTime - day * 60 * 60 * 24) / (60 * 60);
        long minute = (upTime - day * 60 * 60 * 24 - hour * 60 * 60) / 60;
        builder.append("运行时间: ").append(day).append("天").append(hour).append("时").append(minute).append("分").append("\n");
        context.getSender().sendMessage(builder.toString());
    }

}
