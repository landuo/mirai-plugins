package io.github.landuo.util;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import io.github.landuo.SpConfig;
import io.github.landuo.SpPluginConfig;
import io.github.landuo.SpQueryPlugin;
import io.github.landuo.constant.SpApiConstant;
import net.coobird.thumbnailator.Thumbnails;
import net.mamoe.mirai.console.data.Value;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author accidia
 */
public class ResourceUtils {
    /**
     * 缩放图片,按比例缩放
     *
     * @param filePath 资源文件的路径
     */
    public static InputStream scale(String filePath) {
        return scale(filePath, 0.5f);
    }

    /**
     * 缩放图片,按比例缩放
     */
    public static InputStream scale(BufferedImage image) {
        try {
            BufferedImage bufferedImage = Thumbnails.of(image).scale(0.5d).asBufferedImage();
            return IoUtil.toStream(ImgUtil.toBytes(bufferedImage, ImgUtil.IMAGE_TYPE_PNG));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缩放图片,按比例缩放
     *
     * @param filePath 资源文件的路径
     */
    public static InputStream scale(String filePath, float scale) {
        InputStream resourceAsStream = SpQueryPlugin.INSTANCE.getResourceAsStream(filePath.substring(1));
        Image newImage = null;
        try {
            newImage = Thumbnails.of(resourceAsStream).scale(scale).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert newImage != null;
        return IoUtil.toStream(ImgUtil.toBytes(newImage, ImgUtil.IMAGE_TYPE_PNG));
    }

    /**
     * 获取外部文件夹的图片
     *
     * @param picSuffix 图片地址后缀
     */
    public static BufferedImage getExternalPicture(String picSuffix) {
        Value<SpConfig> config = SpPluginConfig.INSTANCE.config;
        SpConfig spConfig = config.get();
        String picDir = spConfig.getPicDir();
        File file = FileUtil.file(picDir + picSuffix);
        if (file.exists()) {
            return ImgUtil.read(file);
        }
        // 图片不存在的话就去远程下载
        HttpUtil.downloadFile(SpApiConstant.PICTURE + picSuffix, picDir + picSuffix);
        return ImgUtil.read(picDir + picSuffix);
    }

    public static BufferedImage getWeaponPictureV3(String url) {
        return getExternalPictureV3(url, "weapon3");
    }

    public static BufferedImage getMapPictureV3(String url) {
        return getExternalPictureV3(url, "map3");
    }

    /**
     * 获取外部文件夹的图片
     *
     * @param url        图片地址
     * @param pathPrefix 路径存储前缀
     */
    private static BufferedImage getExternalPictureV3(String url, String pathPrefix) {
        Value<SpConfig> config = SpPluginConfig.INSTANCE.config;
        SpConfig spConfig = config.get();
        String picDir = spConfig.getPicDir();
        String filePath = picDir + File.separator + pathPrefix + File.separator
                + url.substring(url.lastIndexOf("/") + 1);
        File file = FileUtil.file(filePath);
        if (file.exists()) {
            return ImgUtil.read(file);
        }
        // 图片不存在的话就去远程下载
        HttpUtil.downloadFile(url, filePath);
        return ImgUtil.read(filePath);
    }

    /**
     * 获取内部文件夹的图片
     *
     * @param picSuffix 图片地址后缀
     */
    public static BufferedImage getInternalPicture(String picSuffix) {
        InputStream resourceAsStream = SpQueryPlugin.INSTANCE.getResourceAsStream(picSuffix.substring(1));
        return ImgUtil.read(resourceAsStream);
    }

    /**
     * 图片圆角处理
     *
     * @param image  原始图片
     * @param radius 圆角参数
     * @return 圆角处理后的图片
     */
    public static BufferedImage radius(BufferedImage image, int radius) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, image.getWidth(), image.getHeight(), radius, radius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    /**
     * 图片圆角处理
     *
     * @param image 原始图片
     * @return 圆角处理后的图片
     */
    public static BufferedImage radius(BufferedImage image) {
        return radius(image, 40);
    }
}
