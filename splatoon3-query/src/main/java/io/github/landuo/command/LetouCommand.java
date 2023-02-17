package io.github.landuo.command;

import cn.hutool.core.util.RandomUtil;
import io.github.landuo.SpQueryPlugin;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JSimpleCommand;

import java.util.HashSet;
import java.util.Set;

/**
 * @author accidia
 */
public class LetouCommand extends JSimpleCommand {
    public static final LetouCommand INSTANCE = new LetouCommand();

    private LetouCommand() {
        super(SpQueryPlugin.INSTANCE, "乐透");
    }

    @Handler
    public void letou(CommandContext context) {
        letou(context, 1);
    }

    @Handler
    public void letou(CommandContext context, @Name("个数") Integer count) {
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < count; i++) {
            msg.append(randomLt()).append("\n");
        }
        context.getSender().sendMessage(msg.toString());
    }

    private String randomLt() {
        Set<Integer> front = new HashSet<>();
        while (front.size() < 5) {
            front.add(RandomUtil.randomInt(1, 36));
        }
        Set<Integer> backend = new HashSet<>();
        while (backend.size() < 2) {
            backend.add(RandomUtil.randomInt(1, 13));
        }
        Integer[] fronts = front.stream().sorted().toArray(Integer[]::new);
        Integer[] backends = backend.stream().sorted().toArray(Integer[]::new);
        return String.format("前区: %02d %02d %02d %02d %02d, 后区: %02d %02d", fronts[0], fronts[1], fronts[2], fronts[3],
                fronts[4], backends[0], backends[1]);
    }
}
