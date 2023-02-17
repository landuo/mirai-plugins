package top.accidia.bilibili.live;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.data.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author accidia
 */
public class BilibiliLiveCommand extends JCompositeCommand {
    public static final BilibiliLiveCommand INSTANCE = new BilibiliLiveCommand();

    private BilibiliLiveCommand() {
        super(BilibiliLivePlugin.INSTANCE, "live");
    }

    @SubCommand
    @Description("用户登录")
    public void login(CommandContext context) {
        String response = HttpUtil.get("https://passport.bilibili.com/x/passport-login/web/qrcode/generate");
        JSONObject jsonObject = JSONUtil.parseObj(response);
        Value<Live> config = BiliBiliLiveData.INSTANCE.config;
        Live live = config.get();
        String qrcodeKey = jsonObject.getJSONObject("data").getStr("qrcode_key");
        live.setQrcodeKey(qrcodeKey);
        config.set(live);
        if (context.getSender().getSubject() == null) {
            context.getSender().sendMessage(jsonObject.getJSONObject("data").getStr("url"));
        } else {
            context.getSender().getSubject().sendMessage(jsonObject.getJSONObject("data").getStr("url"));
        }
    }

    @SubCommand
    @Description("开启直播")
    public void start(CommandContext context) throws InterruptedException {
        Value<Live> config = BiliBiliLiveData.INSTANCE.config;
        Live live = config.get();
        HttpRequest request = HttpUtil.createGet("https://passport.bilibili.com/x/passport-login/web/qrcode/poll",
                true);
        request.form("qrcode_key", live.getQrcodeKey());
        HttpResponse execute = request.execute();
        List<HttpCookie> cookieList = execute.getCookies();
        List<String> cookies = new ArrayList<>();
        cookieList.forEach(cookie -> cookies.add(JSONUtil.toJsonStr(cookie)));
        cookieList.stream().filter(e -> e.getName().equals("bili_jct")).forEach(csrf -> live.setCsrf(csrf.getValue()));
        live.setCookies(cookies);

        // 延时一秒后再开启直播
        Thread.sleep(1000);
        // 直播
        request = HttpUtil.createPost("https://api.live.bilibili.com/room/v1/Room/startLive");
        request.cookie(cookieList);
        request.contentType("application/x-www-form-urlencoded");
        request.form("room_id", live.getRoomId());
        request.form("platform", live.getPlatform());
        request.form("area_v2", live.getAreaV2());
        request.form("csrf_token", live.getCsrf());
        request.form("csrf", live.getCsrf());
        execute = request.execute();
        JSONObject response = JSONUtil.parseObj(execute.body());
        if (response.getInt("code") != 0) {
            context.getSender().sendMessage(response.getStr("message"));
            return;
        }
        try {
            String[] command = {"/bin/sh", "-c", String.format(live.getCmd(), response.getJSONObject("data").getJSONObject("rtmp").getStr("code")),"&"};
            System.out.println(Arrays.toString(command));
            Process exec = Runtime.getRuntime().exec(command);
            InputStream errorStream = exec.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            context.getSender().sendMessage(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @SubCommand
    @Description("停止直播")
    public void stop(CommandContext context) {
        Value<Live> config = BiliBiliLiveData.INSTANCE.config;
        Live live = config.get();
        List<String> cookieList = live.getCookies();
        List<HttpCookie> cookies = new ArrayList<>();
        cookieList.forEach(str -> cookies.add(JSONUtil.toBean(str, HttpCookie.class)));

        // 直播
        HttpRequest request = HttpUtil.createPost("https://api.live.bilibili.com/room/v1/Room/startLive");
        request.cookie(cookies);
        request.contentType("application/x-www-form-urlencoded");
        request.form("room_id", live.getRoomId());
        request.form("csrf", live.getCsrf());
        HttpResponse execute = request.execute();
        JSONObject response = JSONUtil.parseObj(execute.body());
        if (response.getInt("code") == 0) {
            context.getSender().sendMessage("直播已关闭.");
        } else {
            context.getSender().sendMessage(response.getStr("message"));
        }
    }

}
