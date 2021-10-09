package com.icicles.wmms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icicles.wmms.DAO.StatisticsMapper;
import com.icicles.wmms.entity.dto.NopayInfoExceptionDTO;
import com.icicles.wmms.entity.dto.PaymentStatisticsDTO;
import com.icicles.wmms.entity.dto.StatisticsDTO;
import com.icicles.wmms.entity.dto.WaterStatisticesDTO;
import com.icicles.wmms.entity.form.NopayInfoForm;
import com.icicles.wmms.entity.form.StatisticsForm;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.service.*;
import com.icicles.wmms.service.impl.excel.CommonExport;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

/**
 * 统计服务类
 * @author lisy
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Resource
    private SysUserService userService;
    @Resource
    private WsMeterService meterService;
    @Resource
    private WsMeterReaderService meterReaderService;
    @Resource
    private WsFeeLogService feeLogService;
    @Resource
    private WsChargeService wsChargeService;
    @Resource
    private WsVillageService villageService;
    @Resource
    private StatisticsMapper statisticsMapper;

    @Override
    public Integer userCount(String villageSn) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(villageSn)){
            queryWrapper.lambda().eq(SysUser::getVillageSn,villageSn);
        }
        return userService.count(queryWrapper);
    }

    @Override
    public Integer meterCount(String villageSn) {
        QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(villageSn)){
            queryWrapper.lambda().eq(WsMeter::getVillageSn,villageSn);
        }
        return meterService.count(queryWrapper);
    }

    @Override
    public Integer meterReaderCount(String startTime, String endTime,String sn,String villageSn) {
        QueryWrapper<WsMeterReader> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotBlank(sn),WsMeterReader::getUserSn,sn);
        queryWrapper.lambda().eq(StringUtils.isNotBlank(villageSn),WsMeterReader::getVillageSn,villageSn);
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            queryWrapper.
                    apply("DATE_FORMAT(create_time,'%Y-%m-%d') >={0}",startTime).
                    apply("create_time < DATE_FORMAT(DATE_ADD({0}, INTERVAL 1 DAY),'%Y-%m-%d')",endTime);
        }else if(StringUtils.isNotBlank(startTime)){
            queryWrapper.apply("DATE_FORMAT(create_time,'%Y-%m-%d') >={0}",startTime);
        }else if(StringUtils.isNotBlank(endTime)){
            queryWrapper.apply("create_time < DATE_FORMAT(DATE_ADD({0}, INTERVAL 1 DAY),'%Y-%m-%d')",endTime);
        }
        queryWrapper.select("IFNULL(count(id),0) as total ");
        Map<String, Object> map = meterReaderService.getMap(queryWrapper);
        return Integer.valueOf(String.valueOf(map.get("total")));
    }

    @Override
    public Integer paymentCount(String startTime, String endTime,String sn,String villageSn) {
        QueryWrapper<WsCharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotBlank(sn),WsCharge::getUserSn,sn);
        queryWrapper.lambda().eq(StringUtils.isNotBlank(villageSn),WsCharge::getVillageSn,villageSn);
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            queryWrapper.
                    apply("DATE_FORMAT(create_time,'%Y-%m-%d') >={0}",startTime).
                    apply("create_time < DATE_FORMAT(DATE_ADD({0}, INTERVAL 1 DAY),'%Y-%m-%d')",endTime)
                    ;
        }else if(StringUtils.isNotBlank(startTime)){
            queryWrapper.apply("DATE_FORMAT(create_time,'%Y-%m-%d') >={0}",startTime);
        }else if(StringUtils.isNotBlank(endTime)){
            queryWrapper.apply("create_time < DATE_FORMAT(DATE_ADD({0}, INTERVAL 1 DAY),'%Y-%m-%d')",endTime);
        }
        queryWrapper.select("IFNULL(count(account_num),0) as total ");
        Map<String, Object> map = wsChargeService.getMap(queryWrapper);
        return Integer.valueOf(String.valueOf(map.get("total")));
    }

    @Override
    public BigDecimal paymentSum(String startTime, String endTime, String sn,String villageSn) {
        QueryWrapper<WsCharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotBlank(sn),WsCharge::getUserSn,sn);
        queryWrapper.lambda().eq(StringUtils.isNotBlank(villageSn),WsCharge::getVillageSn,villageSn);
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            queryWrapper.apply("DATE_FORMAT(create_time,'%Y-%m-%d') >={0}",startTime).apply("create_time < DATE_FORMAT(DATE_ADD({0}, INTERVAL 1 DAY),'%Y-%m-%d')",endTime);
        }else if(StringUtils.isNotBlank(startTime)){
            queryWrapper.apply("DATE_FORMAT(create_time,'%Y-%m-%d') >={0}",startTime);
        }else if(StringUtils.isNotBlank(endTime)){
            queryWrapper.apply("create_time < DATE_FORMAT(DATE_ADD({0}, INTERVAL 1 DAY),'%Y-%m-%d')",endTime);
        }
        queryWrapper.select("IFNULL(sum(account_num),0) as total ");
        Map<String, Object> map = wsChargeService.getMap(queryWrapper);
        return new BigDecimal(String.valueOf(map.get("total")));
    }


    @Override
    public BigDecimal waterSum(String startTime, String endTime,String sn,String villageSn) {
        QueryWrapper<WsFeeLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotBlank(sn),WsFeeLog::getMeterSn,sn);
        queryWrapper.lambda().eq(StringUtils.isNotBlank(villageSn),WsFeeLog::getVillageSn,villageSn);
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            queryWrapper.
                    apply("DATE_FORMAT(create_time,'%Y-%m-%d') >={0}",startTime).
                    apply("create_time < DATE_FORMAT(DATE_ADD({0}, INTERVAL 1 DAY),'%Y-%m-%d')",endTime);
        }else if(StringUtils.isNotBlank(startTime)){
            queryWrapper.apply("DATE_FORMAT(create_time,'%Y-%m-%d') >={0}",startTime);
        }else if(StringUtils.isNotBlank(endTime)){
            queryWrapper.apply("create_time < DATE_FORMAT(DATE_ADD({0}, INTERVAL 1 DAY),'%Y-%m-%d')",endTime);
        }
        queryWrapper.select("IFNULL(sum(volume),0) as total ");
        Map<String, Object> map = feeLogService.getMap(queryWrapper);
        return new BigDecimal(String.valueOf(map.get("total")));
    }

    @Override
    public StatisticsDTO getMonthPayinfoAndWaterinfo(StatisticsForm form) {
        String year;
        if(StringUtils.isBlank(form.getYear())){
            year = DateFormatUtils.format(new Date(), "yyyy");
        }else{
            year = form.getYear();
        }
        StatisticsDTO data = new StatisticsDTO();
        List<WaterStatisticesDTO> waterData = meterReaderService.getWaterSumByMonth(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn());
        List<WaterStatisticesDTO> feeLogData = feeLogService.getAccountSumByMonth(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn());
        data.setWaterData(waterData);
        data.setFeeLogData(feeLogData);
        return data;
    }

    @Override
    public StatisticsDTO getSeasonPayinfoAndWaterinfo(StatisticsForm form) {
        String year;
        if(StringUtils.isBlank(form.getYear())){
            year = DateFormatUtils.format(new Date(), "yyyy");
        }else{
            year = form.getYear();
        }
        StatisticsDTO data = new StatisticsDTO();
        List<WaterStatisticesDTO> waterData = meterReaderService.getWaterSumBySeason(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn());
        List<WaterStatisticesDTO> feeLogData = feeLogService.getAccountSumBySeason(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn());
        data.setWaterData(waterData);
        data.setFeeLogData(feeLogData);
        return data;
    }

    /**
     * 导出报表
     * @param year       年，默认今年
     * @param userSn     用户sn
     * @param villageSn  村庄sn
     */
    @Override
    public void exportDashboard(HttpServletResponse response,String year, String userSn, String villageSn) throws IOException {
        if(StringUtils.isBlank(year)){
            year = DateFormatUtils.format(new Date(), "yyyy");
        }

        //零值
        BigDecimal zeroNum = BigDecimal.ZERO;
        //按季度导出用水量
        List<WaterStatisticesDTO> waterData = new ArrayList<> ();
        BigDecimal waterDataSum = zeroNum;
        //按季度导出缴费金额
        List<WaterStatisticesDTO> feeLogData = new ArrayList<> ();
        BigDecimal feeLogDataSum = zeroNum;
        //按月导出用水量
        List<WaterStatisticesDTO> waterDataMonth = new ArrayList<> ();
        BigDecimal waterDataMonthSum = zeroNum;
        //按月导出用缴费金额
        List<WaterStatisticesDTO> feeLogDataMonth = new ArrayList<> ();
        BigDecimal feeLogDataMonthSum = zeroNum;

        if(StringUtils.isBlank(villageSn)){
            List<WsVillage> villageList = villageService.list();
            for (WsVillage village : villageList) {
                //按季度导出用水量
                List<WaterStatisticesDTO> waterDataTemp = meterReaderService.getWaterSumBySeason(year,userSn,village.getSn(),"");
                //按季度导出缴费金额
                List<WaterStatisticesDTO> feeLogDataTemp = feeLogService.getAccountSumBySeason(year,userSn,village.getSn(),"");

                //按月导出用水量
                List<WaterStatisticesDTO> waterDataMonthTemp = meterReaderService.getWaterSumByMonth(year,userSn,village.getSn(),"");
                //按月导出用缴费金额
                List<WaterStatisticesDTO> feeLogDataMonthTemp = feeLogService.getAccountSumByMonth(year,userSn,village.getSn(),"");

                String villageName = village.getVillageName();

                for (WaterStatisticesDTO t:waterDataTemp){
                    waterDataSum = waterDataSum.add(t.getNum());

                    t.setVillageName(villageName);
                    waterData.add(t);
                }
                for (WaterStatisticesDTO t:feeLogDataTemp){
                    feeLogDataSum = feeLogDataSum.add(t.getNum());

                    t.setVillageName(villageName);
                    feeLogData.add(t);
                }
                for (WaterStatisticesDTO t:waterDataMonthTemp){
                    waterDataMonthSum = waterDataMonthSum.add(t.getNum());

                    t.setVillageName(villageName);
                    waterDataMonth.add(t);
                }
                for (WaterStatisticesDTO t:feeLogDataMonthTemp){
                    feeLogDataMonthSum = feeLogDataMonthSum.add(t.getNum());

                    t.setVillageName(villageName);
                    feeLogDataMonth.add(t);
                }
            }
        }else{
            WsVillage village = villageService.findBySn(villageSn);

            //按季度导出用水量
            waterData = meterReaderService.getWaterSumBySeason(year,userSn,villageSn,"");
            //按季度导出缴费金额
            feeLogData = feeLogService.getAccountSumBySeason(year,userSn,villageSn,"");

            //按月导出用水量
            waterDataMonth = meterReaderService.getWaterSumByMonth(year,userSn,villageSn,"");
            //按月导出用缴费金额
            feeLogDataMonth = feeLogService.getAccountSumByMonth(year,userSn,villageSn,"");
            if(village != null){
                String villageName = village.getVillageName();

                for (WaterStatisticesDTO t:waterData){
                    waterDataSum = waterDataSum.add(t.getNum());
                    t.setVillageName(villageName);
                }
                for (WaterStatisticesDTO t:feeLogData){
                    feeLogDataSum = feeLogDataSum.add(t.getNum());
                    t.setVillageName(villageName);
                }
                for (WaterStatisticesDTO t:waterDataMonth){
                    waterDataMonthSum = waterDataMonthSum.add(t.getNum());
                    t.setVillageName(villageName);
                }
                for (WaterStatisticesDTO t:feeLogDataMonth){
                    feeLogDataMonthSum = feeLogDataMonthSum.add(t.getNum());
                    t.setVillageName(villageName);
                }
            }
        }



        //通过工具类创建writer
        ExcelWriter writer= ExcelUtil.getBigWriter();
        //删除第一个sheet
        writer.getWorkbook().removeSheetAt(0);
        writer.addHeaderAlias("villageName", "村庄名称");

        writer.addHeaderAlias("date", "季度");
        writer.addHeaderAlias("num", "用水数量");
        writer.setSheet(year+"季度用水量统计").write(waterData, true);
        writer.merge(2, "总用水数量:"+waterDataSum.toString());

        writer.addHeaderAlias("date", "季度");
        writer.addHeaderAlias("num", "缴费金额");
        writer.setSheet(year+"季度缴费金额统计").write(feeLogData, true);
        writer.merge(2, "总缴费金额:"+feeLogDataSum.toString());

        writer.addHeaderAlias("date", "月份");
        writer.addHeaderAlias("num", "用水数量");
        writer.setSheet(year+"每个月用水量统计").write(waterDataMonth, true);
        writer.merge(2, "用水数量:"+waterDataMonthSum.toString());

        writer.addHeaderAlias("date", "月份");
        writer.addHeaderAlias("num", "缴费金额");
        writer.setSheet(year+"每个月缴费金额").write(feeLogDataMonth, true);
        writer.merge(2, "总缴费金额:"+feeLogDataMonthSum.toString());

        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        String fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+"_"+ RandomUtil.randomInt(1000,9999);

        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName, "utf-8")+".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //关闭输出Servlet流
        IoUtil.close(out);
    }

    @Override
    public void exportNoPay(HttpServletResponse response, NopayInfoForm nopayUsersForm) throws IOException {
        //欠费用户数据
        List<NopayInfoExceptionDTO> data = statisticsMapper.getNopayInfos(nopayUsersForm.getVillageSn(),nopayUsersForm.getUserSn());

        ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp = new ArrayList<>();
        AbstractMap.SimpleEntry<String,String> data1 = new AbstractMap.SimpleEntry<>("userName", "用户名");
        AbstractMap.SimpleEntry<String,String> data2 = new AbstractMap.SimpleEntry<>("meterSn", "水表编号");
        AbstractMap.SimpleEntry<String,String> data3 = new AbstractMap.SimpleEntry<>("meterNum", "累计用水量");
        AbstractMap.SimpleEntry<String,String> data4 = new AbstractMap.SimpleEntry<>("waterSum", "码盘值");
        AbstractMap.SimpleEntry<String,String> data5 = new AbstractMap.SimpleEntry<>("phone", "手机号");
        AbstractMap.SimpleEntry<String,String> data6 = new AbstractMap.SimpleEntry<>("volume", "未交费水量");
        AbstractMap.SimpleEntry<String,String> data8 = new AbstractMap.SimpleEntry<>("villageName", "村庄名");

        dataTemp.add(data1);
        dataTemp.add(data2);
        dataTemp.add(data3);
        dataTemp.add(data4);
        dataTemp.add(data5);
        dataTemp.add(data6);
        dataTemp.add(data8);

        CommonExport<NopayInfoExceptionDTO> exportFun = new CommonExport<>(data,dataTemp);
        exportFun.exportExcel("缴费记录",response);
    }

    @Override
    public void exportUserCostAndWaterSumToPdf(HttpServletResponse response, String villageSn,String year) throws Exception {
        QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(villageSn)){
            userQueryWrapper.lambda().eq(SysUser::getVillageSn,villageSn);
        }
        List<SysUser> users = userService.list(userQueryWrapper);
        List<List<PaymentStatisticsDTO>> data = new ArrayList<>();
        for(SysUser u:users){
            List<PaymentStatisticsDTO> tempData = statisticsMapper.paymentStatisticsByUser(u.getSn(),year);
            if(CollUtil.isNotEmpty(tempData)){
                data.add(tempData);
            }
        }
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/pdf");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=user.pdf");
        float[] widths = {100, 100, 100,100,100};

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        writer.setViewerPreferences(PdfWriter.PageModeUseThumbs);
        document.setPageSize(PageSize.A4);//设置A4
        document.open();

        PdfPTable table = new PdfPTable(widths);
        table.setTotalWidth(500);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(new PdfPCell(new Paragraph("用户名", getPdfChineseFont())));
        table.addCell(new PdfPCell(new Paragraph("手机号", getPdfChineseFont())));
        table.addCell(new PdfPCell(new Paragraph("用水量", getPdfChineseFont())));
        table.addCell(new PdfPCell(new Paragraph("缴费金额", getPdfChineseFont())));
        table.addCell(new PdfPCell(new Paragraph("年份", getPdfChineseFont())));
        for(int i = 0; i < data.size(); i++) {
            for(int j = 0; j < data.get(i).size(); j++) {
                    PaymentStatisticsDTO tttt = data.get(i).get(j);
                    table.addCell(new Paragraph(tttt.getUserName(), getPdfChineseFont()));
                    table.addCell(new Paragraph(tttt.getPhone(), getPdfChineseFont()));
                    table.addCell(new Paragraph(dealString20(tttt.getWaterSum()), getPdfChineseFont()));
                    table.addCell(new Paragraph(dealString20(tttt.getAccount()), getPdfChineseFont()));
                    table.addCell(new Paragraph(tttt.getYear(), getPdfChineseFont()));
            }
        }

        document.add(table); //pdf文档中加入table
        document.close();
    }
    private static Font getPdfChineseFont() throws Exception {
        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
                BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfChinese, 12, Font.NORMAL);
        return fontChinese;
    }
    private String dealString20(Object d){
        return null == d ? "0" : d.toString();
    }
}
