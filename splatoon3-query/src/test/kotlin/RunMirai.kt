import io.github.landuo.SpQueryPlugin
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.utils.BotConfiguration
import java.io.File

/**
 * @author accidia
 */
suspend fun main() {

    MiraiConsoleTerminalLoader.startAsDaemon()

    SpQueryPlugin.INSTANCE.load()
    SpQueryPlugin.INSTANCE.enable()

    val bot = MiraiConsole.addBot(QQ号, "密码") {
        fileBasedDeviceInfo("/Users/accidia/IdeaProjects/mirai-plugins/bots/QQ号/device.json")
        workingDir = File("/Users/accidia/IdeaProjects/mirai-plugins")
        protocol = BotConfiguration.MiraiProtocol.IPAD
    }.alsoLogin()

    MiraiConsole.job.join()
}