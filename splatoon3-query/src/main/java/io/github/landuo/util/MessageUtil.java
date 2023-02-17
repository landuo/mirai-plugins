package io.github.landuo.util;

import cn.hutool.core.util.StrUtil;
import io.github.landuo.pojo.Weapon;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;

/**
 * @author accidia
 */
public class MessageUtil {

    public static String[] splitCommands(String message) {
        // 命令以.开头，去除.后才是真正的命令
        String realContent = message.substring(1);
        // 命令拆分,兼容命令后接参数的数据: 武器 红管
        return realContent.trim().split("\\s+");
    }

    /**
     * 发送武器信息
     */
    public static void sendWeaponMessage(MessageEvent event, MessageChainBuilder messages, Weapon weapon) {
        messages.append(Contact.uploadImage(event.getSubject(), ResourceUtils.scale(weapon.getImage()))).append("\n")
                .append("简体中文: ").append(weapon.getZhCN()).append("\n")
                .append(StrUtil.isNotBlank(weapon.getShortName()) ? "简称: " + weapon.getShortName() + "\n"
                        : StrUtil.EMPTY)
                .append("英语: ").append(weapon.getEnUS()).append("\n").append("武器类型: ").append(weapon.getType())
                .append("\n").append("副武器: ")
                .append(Contact.uploadImage(event.getSubject(), ResourceUtils.scale(weapon.getSub().getImageA())))
                .append(weapon.getSub().getName()).append("\n").append("大招: ")
                .append(Contact.uploadImage(event.getSubject(), ResourceUtils.scale(weapon.getSpecial().getImageA())))
                .append(weapon.getSpecial().getName()).append("\n")
                .append(Contact.uploadImage(event.getSubject(), ResourceUtils.scale("/images/main_power_up.png")))
                .append("主武器增强: ").append(weapon.getMainPowerUp());
        if (event instanceof GroupMessageEvent) {
            messages.append("\n").append(new At(event.getSender().getId()));
        }
        event.getSubject().sendMessage(messages.build());
    }
}
