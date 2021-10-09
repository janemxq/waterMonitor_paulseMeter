package com.icicles.wmms.service;

import com.icicles.wmms.entity.dto.StatisticsDTO;
import com.icicles.wmms.entity.form.NopayInfoForm;
import com.icicles.wmms.entity.form.StatisticsForm;
import com.itextpdf.text.DocumentException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 统计相关的服务类
 * @author lisy
 */
public interface StatisticsService {
    /**
     * 统计系统人数
     * @return
     */
    Integer userCount(String villageSn);
    /**
     * 统计设备数量
     * @return
     */
    Integer meterCount(String villageSn);

    /**
     * 统计抄表次数
     * @param startTime 统计开始时间
     * @param endTime   统计结束时间
     * @return
     */
    Integer meterReaderCount(String startTime,String endTime,String sn,String villageSn);
    /**
     * 统计支付次数
     * @param startTime 统计开始时间
     * @param endTime   统计结束时间
     * @return
     */
    Integer paymentCount(String startTime,String endTime,String sn,String villageSn);

    /**
     * 统计支付总额
     * @param startTime 统计开始时间
     * @param endTime   统计结束时间
     * @return
     */
    BigDecimal paymentSum(String startTime,String endTime,String sn,String villageSn);
    /**
     * 统计用水总数
     * @param startTime 统计开始时间
     * @param endTime   统计结束时间
     * @return
     */
    BigDecimal waterSum(String startTime,String endTime,String sn,String villageSn);

    /**
     * 按月查询用水和缴费信息
     * @param form
     * @return
     */
    StatisticsDTO getMonthPayinfoAndWaterinfo(StatisticsForm form);
    /**
     * 按季度查询用水和缴费信息
     * @param form
     * @return
     */
    StatisticsDTO getSeasonPayinfoAndWaterinfo(StatisticsForm form);

    /**
     * 导出报表
     * @param response   响应
     * @param year       年，默认今年
     * @param userSn     用户sn
     * @param villageSn  村庄sn
     * @throws IOException io异常
     */
    void exportDashboard(HttpServletResponse response,String year,String userSn,String villageSn) throws IOException;

    /**
     * 导出未缴费报表
     * 一定时间内没有缴费的用户查询表单
     * @throws IOException io异常
     */
    void exportNoPay(HttpServletResponse response, NopayInfoForm nopayUsersForm) throws IOException;

    /**
     * 根据用户id 按照时间（年）统计用户缴费和用水
     * @param response   响应
     * @param villageSn  村庄sn 超级管理员时，villageSn为空
     * @throws IOException io异常
     */
    void exportUserCostAndWaterSumToPdf(HttpServletResponse response,String villageSn,String year) throws Exception;
}
