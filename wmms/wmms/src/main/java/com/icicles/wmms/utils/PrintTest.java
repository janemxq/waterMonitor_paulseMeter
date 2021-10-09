package com.icicles.wmms.utils;

import cn.hutool.json.JSONObject;
import com.google.zxing.WriterException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PrintTest implements Printable {
    private String water  = "";
    private String pay  = "";
    private String must  = "";
    private String balances= "";
    private String name  = "";
    private String time  = "";
    public PrintTest(JSONObject param){
        this.water = param.getStr("water");
        this.pay = param.getStr("pay");
        this.must = param.getStr("must");
        this.balances = param.getStr("balances");
        this.name = param.getStr("name");
        this.time = param.getStr("time");
    }
    public int print(Graphics gra, PageFormat pf, int pageIndex) throws PrinterException {
        System.out.println("pageIndex="+pageIndex);
        Component c = null;
       //转换成Graphics2D
       Graphics2D g2 = (Graphics2D) gra;
       //设置打印颜色为黑色
       g2.setColor(Color.black);
  
       //打印起点坐标
       double x = pf.getImageableX();
       double y = pf.getImageableY();
         
       switch(pageIndex){
          case 0:
            //设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）
            //Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和 DialogInput
            Font font = new Font("新宋体", Font.BOLD, 10);
            g2.setFont(font);//设置字体
            //BasicStroke   bs_3=new   BasicStroke(0.5f);   
            float[]   dash1   =   {2.0f}; 
            //设置打印线的属性。
            //1.线宽 2、3、不知道，4、空白的宽度，5、虚线的宽度，6、偏移量
            g2.setStroke(new   BasicStroke(0.5f,   BasicStroke.CAP_BUTT,   BasicStroke.JOIN_MITER,   2.0f,   dash1,   0.0f));
            //g2.setStroke(bs_3);//设置线宽
            float heigth = font.getSize2D();//字体高度
            System.out.println("x="+x);
            // -1- 用Graphics2D直接输出
            //首字符的基线(右下部)位于用户空间中的 (x, y) 位置处
            //g2.drawLine(10,10,10,300);
            //g2.drawLine(1,1,580,1);
            //g2.drawLine(1,290,580,290);
              Image image1=null;
              Image image2=null;

              try {
                  File file = new File(System.getProperty("user.home")+ File.separator+"wmms");
                  if(!file.exists()){
                      file.mkdirs();
                  }
                  QrUtils.generateQRCodeImage("用水总量："+this.water+"方" +
                          "\n交费金额："+this.pay+"元"+
                          "\n应交水费："+this.must+"元"+
                          "\n账户剩余："+this.balances+"元"+
                          "\n缴费表户："+this.name+
                          "\n交费时间："+this.time, 300, 300,
                          System.getProperty("user.home")+ File.separator+"wmms"+ File.separator+this.time.replaceAll(":","").replaceAll(" ","")+".png");
                  //image1 = ImageIO.read(new FileInputStream(ResourceUtils.getFile("classpath:image/icon.png")));
                  image1 = ImageIO.read(PrintTest.class.getClassLoader().getResourceAsStream("image/icon.png"));
                  image2 = ImageIO.read(new FileInputStream(System.getProperty("user.home")+ File.separator+"wmms"+ File.separator+this.time.replaceAll(":","").replaceAll(" ","")+".png"));
              } catch (IOException | WriterException e) {
                  e.printStackTrace();
              }
             g2.drawImage(image1,50,2,50,50,null);
             //g2.setFont(new Font("宋体", Font.BOLD, 40));
             g2.drawString("山东朝启用水缴费凭条",20,65);
             g2.drawImage(image2,1,70,150,150,null);

             g2.setFont(new Font("新宋体", Font.BOLD, 7));//设置字体
             g2.drawString("用水总量：" + this.water + "方",10,230);
             g2.drawString("交费金额：" + this.pay + "元",10,240);
             g2.drawString("应交水费：" + this.must + "元",10,250);
             g2.drawString("账户剩余：" + this.balances + "元",10,260);
             g2.drawString("缴费表户：" + this.name + "",10,270);
             g2.drawString("交费时间：" + this.time + "",10,280);
            //Image src = Toolkit.getDefaultToolkit().getImage("D:\\test.png");
            //g2.drawImage(src,(int)x,(int)y,c);
            //int img_Height=src.getHeight(c);
            //int img_width=src.getWidth(c);
            //System.out.println("img_Height="+img_Height+"img_width="+img_width) ;

            //g2.drawLine((int)x,(int)(y+1*heigth+img_Height+10),(int)x+200,(int)(y+1*heigth+img_Height+10));


          return PAGE_EXISTS;
          default:
          return NO_SUCH_PAGE;
       }
        
    }
    public static void main(String[] args) {
      
     //    通俗理解就是书、文档
     Book book = new Book();
     //    设置成竖打
     PageFormat pf = new PageFormat();
     pf.setOrientation(PageFormat.PORTRAIT);
     //    通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
     Paper p = new Paper();
     p.setSize(580,830);//纸张大小
     p.setImageableArea(0,0, 580,830);//A4(595 X 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
        pf.setPaper(p);
        Graphics gra;
     //    把 PageFormat 和 Printable 添加到书中，组成一个页面
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("water","100");
        jsonObject.set("pay","100");
        jsonObject.set("must","50");
        jsonObject.set("balances","50");
        jsonObject.set("name","张山");
        jsonObject.set("time","2020-01-01 01:02:02");
     book.append(new PrintTest(jsonObject), pf);
  
      //获取打印服务对象
      PrinterJob job = PrinterJob.getPrinterJob();      
      // 设置打印类
      job.setPageable(book);
      try {
          //可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印
          //boolean a=job.printDialog();
          //if(a)
          //{        
          job.print();
          //}
      } catch (PrinterException e) {
          e.printStackTrace();
      }
    }
}
