package com.icicles.wmms.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PrintImgeUtils {

    private String getImage(){
        int width = 580, height = 830;
        //创建图片对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        //基于图片对象打开绘图
        Graphics2D graphics = image.createGraphics();
        //绘图逻辑 START （基于业务逻辑进行绘图处理）……

        //绘制圆形
        graphics.setColor(Color.BLACK);
        Image image1=null;
        Image image2=null;
        try {
            image1 = ImageIO.read(new FileInputStream("D://111.png"));
            image2 = ImageIO.read(new FileInputStream("D://MyQRCode.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //buffImg = image.getScaledInstance(width,height,BufferedImage.SCALE_SMOOTH);
        graphics.drawImage(image1,30,50,70,70,null);
        graphics.setFont(new Font("宋体", Font.BOLD, 40));
        graphics.drawString("山东朝启用水缴费凭条",120,100);

        graphics.drawImage(image2,30,120,500,500,null);

        graphics.setFont(new Font("宋体", Font.BOLD, 30));
        graphics.drawString("用水总量：10方",50,610);
        graphics.drawString("交费金额：200元",50,650);
        graphics.drawString("应交水费：21元",50,690);
        graphics.drawString("账户剩余：179元",50,730);
        graphics.drawString("缴费表户：张三",50,770);
        graphics.drawString("交费时间：2020-07-24 14:20:20",50,810);
        // 绘图逻辑 END
        //处理绘图
        graphics.dispose();
        //将绘制好的图片写入到图片
        try {
            ImageIO.write(image, "png", new File("D://test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

        public static void main(String[] args) {
            PrintImgeUtils printImgeUtils = new PrintImgeUtils();
            printImgeUtils.getImage();

        }


}
