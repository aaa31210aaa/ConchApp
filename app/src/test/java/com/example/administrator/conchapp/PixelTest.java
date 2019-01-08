package com.example.administrator.conchapp;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 屏幕适配解决方案
 */
public class PixelTest {
    public static final String WRITE_ROOT_PATH = "c:\\pixel";

    public static final int BASE_WIDTH = 320;

    public static final int BASE_HEIGHT = 480;

    public static final DecimalFormat decimalFormat = new DecimalFormat("#.0");

    public static final String[] ALL_SIZE = new String[]{
            "480x320",
            "640x480",
            "800x480",
            "854x480",
            "960x540",
            "1024x600",
            "1024x768",
            "1184x720",
            "1196x720",
            "1280x720",
            "1280x768",
            "1280x800",
            "1812x1080",
            "1920x1080",
            "2560x1440"
    };

    @Test
    public void test() {
        try {
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            File file = new File(WRITE_ROOT_PATH);

            //先判断文件是否存在
            if (!file.exists()) {
                file.mkdirs();
            }

            //然后就开始生成文件
            for (String size : ALL_SIZE) {
                String[] dimens = size.split("x");
                int height = Integer.valueOf(dimens[0]);
                int width = Integer.valueOf(dimens[1]);

                generate(width, height);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 适配的原理在于，比如我的美工给我的图片的基准分辨率是1280 * 720的
     * 但是呢，我有一个800 * 600像素的手机
     * 这时候我们实际上是把 800像素长度分成了 1280份。
     * 800像素的单位1 和 1280的单位1虽然在具体的像素上是不同的，但是在屏幕的百分比上是一样的
     *
     * @param width
     * @param height
     */
    void generate(int width, int height) throws IOException {
        File parentFile = new File(WRITE_ROOT_PATH + "\\values-" + width + "x" + height);
        if (!parentFile.exists())
            parentFile.mkdirs();

        File generateXFile = new File(parentFile, "dimens_x.xml");
        PrintWriter writerX = new PrintWriter(new FileWriter(generateXFile));
        float scaleX = width * 1.0f / BASE_WIDTH;
        System.out.println("scaleX : " + scaleX + " width" + width + " base : " + BASE_WIDTH);
        write(writerX, "x", scaleX, BASE_WIDTH);
        writerX.close();

        File generateYFile = new File(parentFile, "dimens_y.xml");
        PrintWriter writerY = new PrintWriter(new FileWriter(generateYFile));
        float scaleY = height * 1.0f / BASE_HEIGHT;
        write(writerY, "y", scaleY, BASE_HEIGHT);
        writerY.close();
    }

    /**
     * @param writer
     * @param xy       如果计算x就传入"x"
     * @param scale    缩放比例
     * @param baseSize 基准的大小
     */
    void write(PrintWriter writer, String xy, float scale, int baseSize) {
        writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.println("<resources>");
        for (int i = 1; i <= baseSize; i++) {
            writer.println("<dimen name=\"" + xy + i + "\">" + computeSize(i, scale) + "px</dimen>");
        }
        writer.write("</resources>");
    }

    float computeSize(int size, float scale) {
        String value = decimalFormat.format(size * scale);
        return Float.valueOf(value);
    }
}
