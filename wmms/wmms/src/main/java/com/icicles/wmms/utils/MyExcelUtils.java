package com.icicles.wmms.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author lisy
 */
public class MyExcelUtils {
    /**
     * 导出excel到数据流（从浏览器下载）
     * @param response  响应类
     * @param fileName  文件名
     * @param title     excel头标题
     * @param rows      数据
     * @throws IOException
     */
    public static void exportExcel(HttpServletResponse response,String fileName,List<String> title,List<List<String>> rows) throws IOException {
        if(CollUtil.isNotEmpty(title)){
            rows.add(0,title);
        }
        //通过工具类创建writer
        ExcelWriter writer= ExcelUtil.getBigWriter();
        //一次性写出内容，强制输出标题
        writer.write(rows, false);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //下载的文件名
        if(StringUtils.isBlank(fileName)){
            fileName = fileName+ DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+"_"+ RandomUtil.randomInt(1000,9999);
        }
        response.setHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(fileName, "utf-8")+".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    /**
     * 导出excel到文件
     * @param fileNamePath  导出的文件路径
     * @param title        excel头标题
     * @param rows         数据
     * @throws IOException
     */
    public static void exportExcelToFile(String fileNamePath,List<String> title,List<List<String>> rows){
        if(CollUtil.isNotEmpty(title)){
            rows.add(0,title);
        }
        //通过工具类创建writer
        ExcelWriter writer= ExcelUtil.getBigWriter(fileNamePath);
        //一次性写出内容
        writer.write(rows);
        // 关闭writer，释放内存
        writer.close();
    }

    /**
     * excel模板下载
     * @param templatePath excel模板文件（在resource文件夹下面）
     * @param response 响应
     * @throws Exception 异常
     */
    public static void downloadTemplate(String templatePath,HttpServletResponse response) throws Exception{
        ServletOutputStream out = null;
        try {
            InputStream fis = MyExcelUtils.class.getClassLoader().getResourceAsStream(templatePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            response.setContentType("application/binary;charset=ISO8859-1");
            String fileName = java.net.URLEncoder.encode(DateUtil.format(new Date(),"yyyyMMddHHmmss"), "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件输出流
            if(out!=null){
                out.flush();
                out.close();
            }
        }
    }
}
