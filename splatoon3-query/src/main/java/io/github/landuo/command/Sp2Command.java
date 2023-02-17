package io.github.landuo.command;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import io.github.landuo.SpQueryPlugin;
import io.github.landuo.util.CacheUtils;
import io.github.landuo.util.ResourceUtils;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * splatoon2 查询命令
 *
 * @author accidia
 */
public class Sp2Command extends JCompositeCommand {
    public static final Sp2Command INSTANCE = new Sp2Command();

    private Sp2Command() {
        super(SpQueryPlugin.INSTANCE, "sp2");
    }

    @SubCommand("图")
    @Description("查看Splatoon2对战日程")
    public void schedule(CommandContext context) {
        schedule(context, DateUtil.hour(new Date(), true));
    }

    @SubCommand("图")
    @Description("查看Splatoon2对战日程")
    public void schedule(CommandContext context, @Name("小时 (24小时制)") Integer hour) {
        if (context.getSender().getUser() == null) {
            context.getSender().sendMessage("控制台无法返回此内容.");
            return;
        }
        // 获取对战开始时间(偶数点)
        int startHour = hour >> 1 << 1;
        // 组装map的key
        String startTime = (startHour < (DateUtil.thisHour(true) >> 1 << 1)
                ? DateUtil.format(DateUtil.tomorrow(), "MM-dd") : DateUtil.format(DateUtil.date(), "MM-dd"))
                + CharSequenceUtil.SPACE + ((startHour < 10 ? "0" : "") + startHour);
        BufferedImage bufferedImage = CacheUtils.getScheduleByTime(startTime);
        MessageChainBuilder messages = new MessageChainBuilder();
        messages.append(Contact.uploadImage(context.getSender().getSubject(), ResourceUtils.scale(bufferedImage)));
        messages.append(new At(context.getSender().getUser().getId()));
        context.getSender().getSubject().sendMessage(messages.build());
    }

    @SubCommand("工")
    @Description("查看Splatoon2打工日程")
    public void salmon(CommandContext context) {
        if (context.getSender().getUser() == null) {
            context.getSender().sendMessage("控制台无法返回此内容.");
            return;
        }
        BufferedImage bufferedImage = CacheUtils.getSalmonByTime(DateUtil.date());
        MessageChainBuilder messages = new MessageChainBuilder();
        messages.append(Contact.uploadImage(context.getSender().getSubject(), ResourceUtils.scale(bufferedImage)));
        messages.append(new At(context.getSender().getUser().getId()));
        context.getSender().getSubject().sendMessage(messages.build());
    }
}
