package io.github.landuo.api;

import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.landuo.constant.SpApiConstant;
import io.github.landuo.pojo.battle.Schedule;
import io.github.landuo.pojo.salmon.Salmon;

/**
 * @author accidia
 */
public class SplatoonService {

    protected static Schedule getSchedules() {
        System.out.println("发起http请求查询对战时刻表");
        return getScheduleData(SpApiConstant.DATA_SCHEDULE, Schedule.class);
    }

    protected static Salmon getSalmon() {
        System.out.println("发起http请求查询打工时刻表");
        return getScheduleData(SpApiConstant.SALMON_RUN_SCHEDULES, Salmon.class);
    }

    protected static JsonObject getSchedulesV3() {
        System.out.println("发起http请求查询splatoon3时刻表");
        return getScheduleData(SpApiConstant.SCHEDULES_V3, JsonObject.class).getAsJsonObject("data");
    }

    private static <T> T getScheduleData(String url, Class<T> classOfT) {
        String response = HttpUtil.get(url);
        Gson gson = new Gson();
        return gson.fromJson(response, classOfT);
    }
}
