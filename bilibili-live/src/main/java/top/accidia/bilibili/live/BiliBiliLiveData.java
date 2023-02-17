package top.accidia.bilibili.live;

import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginData;

/**
 * @author accidia
 */
public class BiliBiliLiveData extends JavaAutoSavePluginData {
    public static final BiliBiliLiveData INSTANCE = new BiliBiliLiveData();

    private BiliBiliLiveData() {
        super("bilibili-live");
    }

    public final Value<Live> config = typedValue("config", createKType(Live.class), new Live()
    );
    
}
