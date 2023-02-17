package top.accidia.bilibili.live

import kotlinx.serialization.Serializable

/**
 * @author accidia
 */
@Serializable
class Live constructor() {

    var cmd: String? =
        "nohup raspivid -o - -t 0 -vf -hf -fps 30 -b 6000000 -rot 180| ffmpeg -re -stream_loop -1 -i \"/home/pi/bgm.mp3\" -f h264 -i - -vcodec copy -acodec aac -b:a 192k -f flv \"rtmp://live-push.bilivideo.com/live-bvc/%s\""
    var roomId: Int? = 10808281
    var platform: String? = "pc"
    var areaV2: Int? = 369
    var qrcodeKey: String? = ""
    var csrf: String? = ""
    var cookies: List<String> = listOf()

}