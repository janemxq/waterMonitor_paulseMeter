package com.icicles.wmms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.exception.ApiException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * Excel处理模板类
 * @author lisy
 */
public abstract class AbstractExcelDealService<T>{
    /**
     * 导出/入的数据
     */
    private List<T> data;

    /**
     * 数据模板,key代表字段值,value代表说明（可以作为excel标题）
     */
    private ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp;

    /**
     * 构造函数，传入两个必须的数据
     * @param data        导入或者导出的数据
     * @param dataTemp    数据模板：其中，key代表属性名称，value代表导出的excel的头
     */
    public AbstractExcelDealService(List<T> data,ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp){
        this.data = data;
        this.dataTemp = dataTemp;
    }

    /**
     * Excel 文件上传解析
     * @param file Excel文件
     * @return
     */
    public ExcelResultDTO importExcel(MultipartFile file){
        if (file.isEmpty()) {
            throw new ApiException("请上传文件", HttpStatus.BAD_REQUEST);
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if(!".xlsx".equals(suffixName)){
            throw new ApiException("格式不正确", HttpStatus.BAD_REQUEST);
        }

        // 文件上传路径
        String filePath = System.getProperty("user.dir");
        // 解决中文问题,liunx 下中文路径,图片显示问题
        String fileRename = UUID.randomUUID().toString().replace("-","") + suffixName;
        //全路径
        String fullFileName = filePath + "/excelfiles/"+fileRename;

        File dest = new File(fullFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //导入结果
        ExcelResultDTO rs = new ExcelResultDTO();
        //读取excel文件
        Excel07SaxReader reader = new Excel07SaxReader(createRowHandler(rs));
        reader.read(fullFileName, -1);
        //删除文件
        dest.delete();

        return rs;
    }

    /**
     * 导入Excel处理
     * @param rs
     * @return
     */
    protected abstract RowHandler createRowHandler(ExcelResultDTO rs);

    /**
     * 导出excel（从浏览器下载）
     * @param fileName        文件名
     * @param response        响应类
     * @throws IOException    io异常
     */
    public void exportExcel(String fileName, HttpServletResponse response) throws IOException{
        //要导出的数据
        List<List<Object>> rows = this.getExcelData();
        //excel头
        List<Object> title = new ArrayList<>();
        //数据模板
        ArrayList<AbstractMap.SimpleEntry<String, String>> dataTemp = this.getDataTemp();
        for (AbstractMap.SimpleEntry<String, String> titleTemp:
                dataTemp) {
            title.add(titleTemp.getValue());
        }
        this.exportExcel(response,fileName,title,rows);
    }

    /**
     * 通过反射获取指定属性的值
     * @param obj       数据对象
     * @param filed     属性名称
     * @return         属性值
     */
    private Object method(Object obj, String filed) {
        Method method = getGetMethod(obj.getClass(), filed);
        try {
            return method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取get方法
     * @param objectClass  数据对象的class
     * @param fieldName    属性名称
     * @return             get方法
     */
    private Method getGetMethod(Class objectClass, String fieldName) {
        StringBuilder sb = new StringBuilder();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getMethod(sb.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 导出的数据
     * @return
     */
    private List<T> getData(){
        return this.data;
    }
    private void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 将要导出的数据整理成特定的形式
     * @return 数据
     */
    private List<List<Object>> getExcelData(){
        //数据模板
        List<T> realData = this.getData();
        //excel数据
        List<List<Object>> excelTemp = new ArrayList<>();
        //数据模板
        ArrayList<AbstractMap.SimpleEntry<String, String>> dateTemp = this.getDataTemp();
        for (T t:
        realData) {
            List<Object> innerTemp = new ArrayList<>();
            for (AbstractMap.SimpleEntry<String,String> titleTemp:
                    dateTemp) {
                Object data = this.method(t,titleTemp.getKey());
                innerTemp.add(data);
            }
            excelTemp.add(innerTemp);
        }
        return excelTemp;
    }


    private ArrayList<AbstractMap.SimpleEntry<String, String>> getDataTemp() {
        return dataTemp;
    }

    private void setDataTemp(ArrayList<AbstractMap.SimpleEntry<String, String>> dataTemp) {
        this.dataTemp = dataTemp;
    }


    /**
     * 导出excel到数据流（从浏览器下载）
     * @param response  响应类
     * @param fileName  文件名
     * @param title     excel头标题
     * @param rows      数据
     * @throws IOException
     */
    private void exportExcel(HttpServletResponse response,String fileName,List<Object> title,List<List<Object>> rows) throws IOException {
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
        fileName = fileName+ DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName, "utf-8")+".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }
}
