package io.github.landuo.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.img.ImgUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import io.github.landuo.SpQueryPlugin;
import io.github.landuo.api.SplatoonService;
import io.github.landuo.enums.GameModeEnum;
import io.github.landuo.pojo.battle.Gachi;
import io.github.landuo.pojo.battle.League;
import io.github.landuo.pojo.battle.Regular;
import io.github.landuo.pojo.battle.Schedule;
import io.github.landuo.pojo.salmon.Salmon;
import io.github.landuo.pojo.salmon.SalmonDetail;
import io.github.landuo.pojo.salmon.SalmonWeapon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author accidia
 */
public class CacheUtils extends SplatoonService {
    private static final Map<String, BufferedImage> SCHEDULE_PIC_V2 = new HashMap<>(16, 0.8f);
    private static final Map<String, BufferedImage> SCHEDULE_PIC_V3 = new HashMap<>(16, 0.8f);
    private static BufferedImage SALMON_PIC_V2 = null;
    private static BufferedImage SALMON_PIC_V3 = null;
    private static LocalDateTime SALMON_END_TIME_V3 = null;
    private static Schedule schedule;
    private static Salmon salmon;
    private static JsonObject schedulesV3;

    static {
        buildBattleSchedulesV2();
        buildSalmonScheduleV2();
        buildSchedulesV3();
    }

    private static void buildSchedulesV3() {
        schedulesV3 = getSchedulesV3();
        buildSalmonPictureV3(schedulesV3.getAsJsonObject("coopGroupingSchedule"));
        buildSchedulePictureV3(schedulesV3);
    }

    private static void buildSchedulePictureV3(JsonObject data) {
        int bgWidth = 1500;
        int bgHeight = 1250;
        int picWidth = 640;
        int picHeight = 360;
        int paddingLeft = 170;
        int paddingTop = 100;
        int picPaddingTop = 15;
        int picPaddingLeft = 20;
        // ?????????12?????????????????????map???
        for (int i = 0; i < 12; i++) {
            JsonArray regularSchedules = data.getAsJsonObject("regularSchedules").getAsJsonArray("nodes");
            JsonObject regular = regularSchedules.get(i).getAsJsonObject();
            String startTime = regular.get("startTime").getAsString();
            // regularMatchSetting??????????????????????????????
            if (regular.get("regularMatchSetting") instanceof JsonNull){
                continue;
            }
            JsonObject bankara = null;
            for (JsonElement jsonElement : data.getAsJsonObject("bankaraSchedules").getAsJsonArray("nodes")) {
                bankara = jsonElement.getAsJsonObject();
                if (bankara.get("startTime").getAsString().equals(startTime)) {
                    break;
                }
            }

            // ??????????????????
            BufferedImage bufferedImage = new BufferedImage(bgWidth, bgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.drawImage(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/bg-squids.png")), 0, 0, bgWidth,
                    bgHeight, null);
            graphics.dispose();
            drawScheduleV3(picWidth, picHeight, paddingLeft, paddingTop, picPaddingTop, picPaddingLeft, regular,
                    bankara, bufferedImage);
            // ImgUtil.write(bufferedImage,
            // FileUtil.file("/Users/accidia/pic/" + DateUtils.formatLocalDateTime(startTime, null) + ".png"));
            // ???????????????????????????map???
            SCHEDULE_PIC_V3.put(DateUtils.formatLocalDateTime(startTime, "MM-dd HH"),
                    ResourceUtils.radius(bufferedImage));
        }
    }

    private static void drawScheduleV3(int picWidth, int picHeight, int paddingLeft, int paddingTop, int picPaddingTop,
                                       int picPaddingLeft, JsonObject regular, JsonObject bankara, BufferedImage bufferedImage) {
        Graphics graphics = bufferedImage.getGraphics();
        // ?????????????????????????????????
        Font font = new Font("Baoli SC", Font.BOLD, 150);
        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
        // ????????????
        graphics.drawString(
                DateUtils.formatLocalDateTime(regular.get("startTime").getAsString(), null) + " - "
                        + DateUtils.formatLocalDateTime(regular.get("endTime").getAsString(), null),
                paddingLeft, paddingTop);
        font = new Font("Baoli SC", Font.BOLD, 150);
        graphics.setFont(font);
        // ??????????????????
        String gameMode = GameModeEnum.getZhNameByKey(
                regular.getAsJsonObject("regularMatchSetting").getAsJsonObject("vsRule").get("name").getAsString());
        // ????????????
        for (int j = 0; j < gameMode.length(); j++) {
            graphics.drawString(String.valueOf(gameMode.charAt(j)), 10, 250 + 150 * j);
        }

        // ???????????????????????????
        BufferedImage regularStageA = ResourceUtils.radius(ImgUtil
                .read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/vsStage/" + regular.getAsJsonObject("regularMatchSetting")
                        .getAsJsonArray("vsStages").get(0).getAsJsonObject().get("vsStageId").getAsString() + ".png")));
        BufferedImage regularStageB = ResourceUtils.radius(ImgUtil
                .read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/vsStage/" + regular.getAsJsonObject("regularMatchSetting")
                        .getAsJsonArray("vsStages").get(1).getAsJsonObject().get("vsStageId").getAsString() + ".png")));
        graphics.drawImage(regularStageA, paddingLeft, paddingTop + picPaddingTop, picWidth, picHeight, null);
        graphics.drawImage(regularStageB, paddingLeft + picPaddingLeft + picWidth, paddingTop + picPaddingTop, picWidth,
                picHeight, null);

        JsonObject challenge = null;
        JsonObject open = null;
        if ("CHALLENGE".equals(
                bankara.getAsJsonArray("bankaraMatchSettings").get(0).getAsJsonObject().get("mode").getAsString())) {
            challenge = bankara.getAsJsonArray("bankaraMatchSettings").get(0).getAsJsonObject();
            open = bankara.getAsJsonArray("bankaraMatchSettings").get(1).getAsJsonObject();
        } else {
            challenge = bankara.getAsJsonArray("bankaraMatchSettings").get(1).getAsJsonObject();
            open = bankara.getAsJsonArray("bankaraMatchSettings").get(0).getAsJsonObject();
        }

        // ??????????????????
        gameMode = GameModeEnum.getZhNameByKey(challenge.getAsJsonObject("vsRule").get("name").getAsString());
        // ????????????
        for (int j = 0; j < gameMode.length(); j++) {
            graphics.drawString(String.valueOf(gameMode.charAt(j)), 10, (gameMode.length() == 1 ? 700 : 600) + 100 * j);
        }
        // ???????????????????????????
        BufferedImage challengeStageA = ResourceUtils.radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/vsStage/"
                + challenge.getAsJsonArray("vsStages").get(0).getAsJsonObject().get("vsStageId").getAsString()
                + ".png")));
        BufferedImage challengeStageB = ResourceUtils.radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/vsStage/"
                + challenge.getAsJsonArray("vsStages").get(1).getAsJsonObject().get("vsStageId").getAsString()
                + ".png")));
        graphics.drawImage(challengeStageA, paddingLeft, paddingTop + picPaddingTop * 2 + picHeight, picWidth,
                picHeight, null);
        graphics.drawImage(challengeStageB, paddingLeft + picPaddingLeft + picWidth,
                paddingTop + picPaddingTop * 2 + picHeight, picWidth, picHeight, null);

        // ??????????????????
        gameMode = GameModeEnum.getZhNameByKey(open.getAsJsonObject("vsRule").get("name").getAsString());
        // ????????????
        for (int j = 0; j < gameMode.length(); j++) {
            graphics.drawString(String.valueOf(gameMode.charAt(j)), 10,
                    (gameMode.length() == 1 ? 1100 : 1000) + j * 100);
        }
        // ???????????????????????????
        BufferedImage openStageA = ResourceUtils.radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/vsStage/"
                + open.getAsJsonArray("vsStages").get(0).getAsJsonObject().get("vsStageId").getAsString() + ".png")));
        BufferedImage openStageB = ResourceUtils.radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/vsStage/"
                + open.getAsJsonArray("vsStages").get(1).getAsJsonObject().get("vsStageId").getAsString() + ".png")));
        graphics.drawImage(openStageA, paddingLeft, paddingTop + picPaddingTop * 3 + picHeight * 2, picWidth, picHeight,
                null);
        graphics.drawImage(openStageB, paddingLeft + picPaddingLeft + picWidth,
                paddingTop + picPaddingTop * 3 + picHeight * 2, picWidth, picHeight, null);

        int iconMarginLeft = 745;
        // ????????????????????????
        graphics.drawImage(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/battle-regular.png")), iconMarginLeft, 180,
                156, 156, null);
        graphics.drawImage(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/battle-ranked.png")), iconMarginLeft, 550,
                156, 156, null);
        graphics.drawImage(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/battle-league.png")), iconMarginLeft, 940,
                156, 156, null);
    }

    /**
     * ??????splatoon3???????????????
     */
    private static void buildSalmonPictureV3(JsonObject coopGroupingSchedule) {
        int bgWidth = 1050;
        int bgHeight = 2250;
        SALMON_PIC_V3 = new BufferedImage(bgWidth, bgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = SALMON_PIC_V3.getGraphics();
        // ???????????????
        graphics.setColor(Color.decode("#ff5600"));
        graphics.fillRect(0, 0, bgWidth, bgHeight);
        if (coopGroupingSchedule.getAsJsonObject("regularSchedules") != null) {
            JsonArray coopSchedules = coopGroupingSchedule.getAsJsonObject("regularSchedules").getAsJsonArray("nodes");
            if (coopSchedules.size() != 0) {
                int mapWidth = 640;
                int mapHeight = 360;
                int weaponWH = 180;
                int fontSize = 70;
                for (int i = 0; i < 5; i++) {
                    int beginHeight = i * 450;
                    JsonObject jsonObject = coopSchedules.get(i).getAsJsonObject();
                    BufferedImage mapImage = ResourceUtils.getMapPictureV3(jsonObject.getAsJsonObject("setting")
                            .getAsJsonObject("coopStage").getAsJsonObject("image").get("url").getAsString());
                    Font f = new Font("Baoli SC", Font.PLAIN, fontSize);
                    Color mycolor = Color.WHITE;
                    graphics.setColor(mycolor);
                    graphics.setFont(f);
                    LocalDateTime startTime = DateUtils.toLocalDateTime(jsonObject.get("startTime").getAsString());
                    LocalDateTime endTime = DateUtils.toLocalDateTime(jsonObject.get("endTime").getAsString());
                    String time = LocalDateTimeUtil.format(startTime, "MM-dd HH:mm") + " - "
                            + LocalDateTimeUtil.format(endTime, "MM-dd HH:mm");
                    if (i == 0) {
                        SALMON_END_TIME_V3 = endTime;
                    }
                    // ????????????
                    graphics.drawString(time, 20, beginHeight += fontSize);
                    // ????????????
                    graphics.drawImage(ResourceUtils.radius(mapImage), 10, beginHeight += 10, mapWidth, mapHeight,
                            null);
                    // ???????????????????????????
                    int weaponBeginWidth = 10 + mapWidth;
                    int weaponBeginHeight = beginHeight - 10;
                    for (int j = 0; j < 4; j++) {
                        JsonObject weapon = jsonObject.getAsJsonObject("setting").getAsJsonArray("weapons").get(j)
                                .getAsJsonObject();
                        BufferedImage weaponImage = ResourceUtils
                                .getWeaponPictureV3(weapon.getAsJsonObject("image").get("url").getAsString());
                        graphics.drawImage(weaponImage, (weaponBeginWidth + (j % 2 * weaponWH)),
                                (weaponBeginHeight + (j > 1 ? weaponWH : 0)), weaponWH, weaponWH, null);
                    }
                }
                SALMON_PIC_V3 = ResourceUtils.radius(SALMON_PIC_V3);
            }
        }
        if (coopGroupingSchedule.getAsJsonObject("regularSchedules") != null
                && coopGroupingSchedule.getAsJsonObject("bigRunSchedules").getAsJsonArray("nodes").size() != 0) {
            System.err.println(coopGroupingSchedule.getAsJsonObject("bigRunSchedules"));
        }
    }

    /**
     * ?????????????????????????????????
     */
    private static void buildSalmonScheduleV2() {
        salmon = getSalmon();
        int bgWidth = 1050;
        int bgHeight = 900;
        SALMON_PIC_V2 = new BufferedImage(bgWidth, bgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = SALMON_PIC_V2.getGraphics();
        // ???????????????
        graphics.setColor(Color.decode("#ff5600"));
        graphics.fillRect(0, 0, bgWidth, bgHeight);
        buildSalmonPicture(salmon.getDetails());
    }

    /**
     * ??????????????????????????????
     *
     * @param startTime ???????????????????????????: MM-dd HH
     */
    public static BufferedImage getScheduleByTime(String startTime) {
        boolean containsKey = SCHEDULE_PIC_V2.containsKey(startTime);
        if (!containsKey) {
            SCHEDULE_PIC_V2.clear();
            buildBattleSchedulesV2();
        }
        return SCHEDULE_PIC_V2.get(startTime);
    }

    /**
     * ??????????????????????????????
     *
     * @param startTime ???????????????????????????: MM-dd HH
     */
    public static BufferedImage getScheduleByTimeV3(String startTime) {
        boolean containsKey = SCHEDULE_PIC_V3.containsKey(startTime);
        if (!containsKey) {
            SCHEDULE_PIC_V3.clear();
            buildSchedulesV3();
        }
        return SCHEDULE_PIC_V3.get(startTime);
    }

    /**
     * ??????????????????????????????
     *
     * @param nowTime ????????????
     */
    public static BufferedImage getSalmon3(LocalDateTime nowTime) {
        if (nowTime.isAfter(SALMON_END_TIME_V3)) {
            buildSchedulesV3();
        }
        return SALMON_PIC_V3;
    }

    /**
     * ??????????????????????????????
     *
     * @param nowTime ????????????
     */
    public static BufferedImage getSalmonByTime(DateTime nowTime) {
        boolean dataExpired = salmon.getDetails().stream()
                .anyMatch(e -> e.getEndTime().compareTo(nowTime.getTime() / 1000) <= 0);
        if (dataExpired) {
            buildSalmonScheduleV2();
        }
        return SALMON_PIC_V2;
    }

    /**
     * ?????????????????????????????????
     */
    private static void buildBattleSchedulesV2() {
        schedule = SplatoonService.getSchedules();
        int bgWidth = 1500;
        int bgHeight = 1250;
        int picWidth = 640;
        int picHeight = 360;
        int paddingLeft = 170;
        int paddingTop = 100;
        int picPaddingTop = 15;
        int picPaddingLeft = 20;
        // ?????????12?????????????????????map???
        for (int i = 0; i < schedule.getGachi().size(); i++) {
            Regular regular = schedule.getRegular().get(i);
            Long startTime = regular.getStartTime();
            Gachi gachi = schedule.getGachi().stream().filter(e -> e.getStartTime().equals(startTime)).findFirst()
                    .get();
            League league = schedule.getLeague().stream().filter(e -> e.getStartTime().equals(startTime)).findFirst()
                    .get();

            // ??????????????????
            BufferedImage bufferedImage = new BufferedImage(bgWidth, bgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.drawImage(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/bg-squids.png")), 0, 0, bgWidth,
                    bgHeight, null);
            graphics.dispose();
            drawSchedule(picWidth, picHeight, paddingLeft, paddingTop, picPaddingTop, picPaddingLeft, regular, gachi,
                    league, bufferedImage);
            // ???????????????????????????map???
            SCHEDULE_PIC_V2.put(DateUtils.formatDate(regular.getStartTime(), "MM-dd HH"),
                    ResourceUtils.radius(bufferedImage));
        }
    }

    private static void drawSchedule(int picWidth, int picHeight, int paddingLeft, int paddingTop, int picPaddingTop,
                                     int picPaddingLeft, Regular regular, Gachi gachi, League league, BufferedImage bufferedImage) {
        Graphics graphics = bufferedImage.getGraphics();
        // ?????????????????????????????????
        Font font = new Font("Baoli SC", Font.BOLD, 150);
        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
        // ????????????
        graphics.drawString(DateUtils.formatDate(regular.getStartTime(), "HH:mm") + " - "
                + DateUtils.formatDate(regular.getEndTime(), "HH:mm"), paddingLeft, paddingTop);
        font = new Font("Baoli SC", Font.BOLD, 150);
        graphics.setFont(font);
        // ??????????????????
        String gameMode = GameModeEnum.getZhNameByKey(regular.getRule().getKey());
        // ????????????
        for (int j = 0; j < gameMode.length(); j++) {
            graphics.drawString(String.valueOf(gameMode.charAt(j)), 10, 250 + 150 * j);
        }

        // ???????????????????????????
        BufferedImage regularStageA = ResourceUtils
                .radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream(regular.getStageA().getImage().substring(1))));
        BufferedImage regularStageB = ResourceUtils
                .radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream(regular.getStageB().getImage().substring(1))));
        graphics.drawImage(regularStageA, paddingLeft, paddingTop + picPaddingTop, picWidth, picHeight, null);
        graphics.drawImage(regularStageB, paddingLeft + picPaddingLeft + picWidth, paddingTop + picPaddingTop, picWidth,
                picHeight, null);

        // ??????????????????
        gameMode = GameModeEnum.getZhNameByKey(gachi.getRule().getKey());
        // ????????????
        for (int j = 0; j < gameMode.length(); j++) {
            graphics.drawString(String.valueOf(gameMode.charAt(j)), 10, (gameMode.length() == 1 ? 700 : 600) + 100 * j);
        }
        // ???????????????????????????
        BufferedImage gachiStageA = ResourceUtils
                .radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream(gachi.getStageA().getImage().substring(1))));
        BufferedImage gachiStageB = ResourceUtils
                .radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream(gachi.getStageB().getImage().substring(1))));
        graphics.drawImage(gachiStageA, paddingLeft, paddingTop + picPaddingTop * 2 + picHeight, picWidth, picHeight,
                null);
        graphics.drawImage(gachiStageB, paddingLeft + picPaddingLeft + picWidth,
                paddingTop + picPaddingTop * 2 + picHeight, picWidth, picHeight, null);

        // ??????????????????
        gameMode = GameModeEnum.getZhNameByKey(league.getRule().getKey());
        // ????????????
        for (int j = 0; j < gameMode.length(); j++) {
            graphics.drawString(String.valueOf(gameMode.charAt(j)), 10,
                    (gameMode.length() == 1 ? 1100 : 1000) + j * 50);
        }
        // ???????????????????????????
        BufferedImage leagueStageA = ResourceUtils
                .radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream(league.getStageA().getImage().substring(1))));
        BufferedImage leagueStageB = ResourceUtils
                .radius(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream(league.getStageB().getImage().substring(1))));
        graphics.drawImage(leagueStageA, paddingLeft, paddingTop + picPaddingTop * 3 + picHeight * 2, picWidth,
                picHeight, null);
        graphics.drawImage(leagueStageB, paddingLeft + picPaddingLeft + picWidth,
                paddingTop + picPaddingTop * 3 + picHeight * 2, picWidth, picHeight, null);

        int iconMarginLeft = 745;
        // ????????????????????????
        graphics.drawImage(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/battle-regular.png")), iconMarginLeft, 180,
                156, 156, null);
        graphics.drawImage(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/battle-ranked.png")), iconMarginLeft, 550,
                156, 156, null);
        graphics.drawImage(ImgUtil.read(SpQueryPlugin.INSTANCE.getResourceAsStream("images/battle-league.png")), iconMarginLeft, 940,
                156, 156, null);
    }

    /**
     * @param salmonDetailList ??????????????????
     */
    private static void buildSalmonPicture(List<SalmonDetail> salmonDetailList) {
        salmonDetailList.sort(Comparator.comparing(SalmonDetail::getStartTime));
        int mapWidth = 640;
        int mapHeight = 360;
        int weaponWH = 180;
        int fontSize = 70;
        for (int i = 0; i < salmonDetailList.size(); i++) {
            int beginHeight = i * 450;
            SalmonDetail salmonDetail = salmonDetailList.get(i);
            BufferedImage mapImage = ResourceUtils.getExternalPicture(salmonDetail.getStage().getImage());
            Font f = new Font("Baoli SC", Font.PLAIN, fontSize);
            Color mycolor = Color.WHITE;
            Graphics graphics = SALMON_PIC_V2.getGraphics();
            graphics.setColor(mycolor);
            graphics.setFont(f);
            String time = DateUtils.formatDate(salmonDetail.getStartTime(), "MM-dd HH:mm") + " - "
                    + DateUtils.formatDate(salmonDetail.getEndTime(), "MM-dd HH:mm");
            // ????????????
            graphics.drawString(time, 20, beginHeight += fontSize);
            // ????????????
            graphics.drawImage(ResourceUtils.radius(mapImage), 10, beginHeight += 10, mapWidth, mapHeight, null);

            // ???????????????????????????
            int weaponBeginWidth = 10 + mapWidth;
            int weaponBeginHeight = beginHeight - 10;
            for (int j = 0; j < salmonDetail.getWeapons().size(); j++) {
                SalmonWeapon weapon = salmonDetail.getWeapons().get(j);
                BufferedImage weaponImage;
                if (weapon.getWeapon() != null) {
                    weaponImage = ResourceUtils.getInternalPicture(weapon.getWeapon().getImage());
                } else {
                    if (weapon.getId().equals("-2")) {
                        weaponImage = ResourceUtils.getInternalPicture(
                                "/images/coop_weapons/7076c8181ab5c49d2ac91e43a2d945a46a99c17d.png");
                    } else {
                        weaponImage = ResourceUtils.getInternalPicture(
                                "/images/coop_weapons/746f7e90bc151334f0bf0d2a1f0987e311b03736.png");
                    }
                }

                graphics.drawImage(weaponImage, (weaponBeginWidth + (j % 2 * weaponWH)),
                        (weaponBeginHeight + (j > 1 ? weaponWH : 0)), weaponWH, weaponWH, null);
            }
        }
        SALMON_PIC_V2 = ResourceUtils.radius(SALMON_PIC_V2);
    }

}
