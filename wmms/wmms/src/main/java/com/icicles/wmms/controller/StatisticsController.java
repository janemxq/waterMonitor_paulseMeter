package com.icicles.wmms.controller;

import com.icicles.wmms.entity.dto.StatisticsDTO;
import com.icicles.wmms.entity.dto.StatisticsHomeDTO;
import com.icicles.wmms.entity.dto.WaterStatisticesDTO;
import com.icicles.wmms.entity.form.NopayInfoForm;
import com.icicles.wmms.entity.form.PdfFeeAndWaterForm;
import com.icicles.wmms.entity.form.StatisticsCommonForm;
import com.icicles.wmms.entity.form.StatisticsForm;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 统计相关的接口
 * @author lisy
 */
@RestController
@Api(tags = "统计相关的接口")
@RequestMapping("/Statistics")
public class StatisticsController {
    @Resource
    private StatisticsService statisticsService;

    @ApiOperation(value = "综合统计类", notes = "综合统计类，统计用户总数、设备总数、抄表次数、支付次数、支付总额、用水总数")
    @PostMapping("/statisticsHome")
    public ResponseEntity<StatisticsHomeDTO> statisticsHome(@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        StatisticsHomeDTO data = new StatisticsHomeDTO();
        //用户总数
        data.setUserCount(statisticsService.userCount(securityLoginVillage));
        //设备总数
        data.setMeterCount(statisticsService.meterCount(securityLoginVillage));
        //抄表次数
        data.setMeterReaderCount(statisticsService.meterReaderCount("","","",securityLoginVillage));
        //支付次数
        data.setPaymentCount(statisticsService.paymentCount("","","",securityLoginVillage));
        //支付总额
        data.setPaymentSum(statisticsService.paymentSum("","","",securityLoginVillage));
        //用水总数
        data.setWaterSum(statisticsService.waterSum("","","",securityLoginVillage));

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "同时获取某年（默认今年）的月用水量和缴费金额数据", notes = "同时获取某年（默认今年）的月用水量和缴费金额数据,不传年份时，默认当前年份。其他条件不传，默认为平台数据。查询单个用户数据，可以上传用户sn")
    @PostMapping("/getMonthPayinfoAndWaterinfo")
    public ResponseEntity<StatisticsDTO> getMonthPayinfoAndWaterinfo(@Valid @RequestBody StatisticsForm form,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        if(StringUtils.isBlank(form.getVillageSn())&&StringUtils.isNotBlank(securityLoginVillage)){
            //如果用户没有上传村的标识，并且当前登录人为某个村的管理员，只查询该村的记录
            form.setVillageSn(securityLoginVillage);
        }else{
            //如果用户上传了村的标识，但是不是超级管理员（超级管理员村标识为空），他只能看他自己村的信息
            if(StringUtils.isNotBlank(form.getVillageSn())){
                if(StringUtils.isNotBlank(securityLoginVillage)){
                    if(!form.getVillageSn().equals(securityLoginVillage)){
                        form.setVillageSn(securityLoginVillage);
                    }
                }
            }
        }
        StatisticsDTO data = statisticsService.getMonthPayinfoAndWaterinfo(form);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "同时获取某年（默认今年）的各个季度用水量和缴费金额数据", notes = "同时获取某年（默认今年）的各个季度用水量和缴费金额数据,不传年份时，默认当前年份。其他条件不传，默认为平台数据。查询单个用户数据，可以上传用户sn")
    @PostMapping("/getSeasonPayinfoAndWaterinfo")
    public ResponseEntity<StatisticsDTO> getSeasonPayinfoAndWaterinfo(@Valid @RequestBody StatisticsForm form,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        if(StringUtils.isBlank(form.getVillageSn())&&StringUtils.isNotBlank(securityLoginVillage)){
            //如果用户没有上传村的标识，并且当前登录人为某个村的管理员，只查询该村的记录
            form.setVillageSn(securityLoginVillage);
        }
        StatisticsDTO data = statisticsService.getSeasonPayinfoAndWaterinfo(form);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "获取平台内用户数量", notes = "获取平台内用户数量，所有参数为空时，代表获取平台全时间数据。取数据中的num值")
    @PostMapping("/getUserNum")
    public ResponseEntity<WaterStatisticesDTO> getUserNum(@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        Integer userCount = statisticsService.userCount(securityLoginVillage);
        WaterStatisticesDTO data = new WaterStatisticesDTO();
        data.setDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        data.setNum(new BigDecimal(userCount));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "获取平台内设备数量", notes = "获取平台内设备数量，所有参数为空时，代表获取平台全时间数据。取数据中的num值")
    @PostMapping("/getMeterNum")
    public ResponseEntity<WaterStatisticesDTO> getMeterNum(@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        Integer userCount = statisticsService.meterCount(securityLoginVillage);
        WaterStatisticesDTO data = new WaterStatisticesDTO();
        data.setDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        data.setNum(new BigDecimal(userCount));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "获取抄表次数", notes = "获取获取抄表次数，所有参数为空时，代表获取平台全时间数据。取数据中的num值")
    @PostMapping("/meterReaderCount")
    public ResponseEntity<WaterStatisticesDTO> meterReaderCount(@Valid @RequestBody StatisticsCommonForm form,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        Integer userCount = statisticsService.meterReaderCount(form.getStartTime(),form.getEndTime(),form.getUserSn(),securityLoginVillage);
        WaterStatisticesDTO data = new WaterStatisticesDTO();
        data.setDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        data.setNum(new BigDecimal(userCount));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "获取支付次数。", notes = "获取支付次数，所有参数为空时，代表获取平台全时间数据。取数据中的num值")
    @PostMapping("/paymentCount")
    public ResponseEntity<WaterStatisticesDTO> paymentCount(@Valid @RequestBody StatisticsCommonForm form,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        Integer userCount = statisticsService.paymentCount(form.getStartTime(),form.getEndTime(),form.getUserSn(),securityLoginVillage);
        WaterStatisticesDTO data = new WaterStatisticesDTO();
        data.setDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        data.setNum(new BigDecimal(userCount));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "获取支付总额。", notes = "获取获取支付总额，所有参数为空时，代表获取平台全时间数据。取数据中的num值")
    @PostMapping("/paymentSum")
    public ResponseEntity<WaterStatisticesDTO> paymentSum(@Valid @RequestBody StatisticsCommonForm form,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        BigDecimal userCount = statisticsService.paymentSum(form.getStartTime(),form.getEndTime(),form.getUserSn(),securityLoginVillage);
        WaterStatisticesDTO data = new WaterStatisticesDTO();
        data.setDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        data.setNum(userCount);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "统计用水总数。", notes = "统计用水总数，所有参数为空时，代表获取平台全时间数据。取数据中的num值")
    @PostMapping("/waterSum")
    public ResponseEntity<WaterStatisticesDTO> waterSum(@Valid @RequestBody StatisticsCommonForm form,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        BigDecimal userCount = statisticsService.waterSum(form.getStartTime(),form.getEndTime(),form.getUserSn(),securityLoginVillage);
        WaterStatisticesDTO data = new WaterStatisticesDTO();
        data.setDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        data.setNum(userCount);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "导出报表", notes = "导出报表")
    @PostMapping("/exportDashboard")
    public ResponseEntity<String> dao(HttpServletResponse response,@Valid @RequestBody StatisticsForm form, @ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException, IOException {
        if(StringUtils.isNotBlank(securityLoginVillage)){
            form.setVillageSn(securityLoginVillage);
        }
        statisticsService.exportDashboard(response,form.getYear(),"",form.getVillageSn());
        return new ResponseEntity<>("success",HttpStatus.OK);
    }

    @ApiOperation(value = "导出欠费信息", notes = "导出欠费信息")
    @PostMapping("/exportNoPay")
    public ResponseEntity<String> exportNoPay(HttpServletResponse response, @Valid @RequestBody NopayInfoForm form, @ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException, IOException {
        if(StringUtils.isNotBlank(securityLoginVillage)){
            form.setVillageSn(securityLoginVillage);
        }
        statisticsService.exportNoPay(response,form);
        return new ResponseEntity<>("success",HttpStatus.OK);
    }
    @ApiOperation(value = "导出根据水费和时间生成的报表导出pdf", notes = "导出根据水费和时间生成的报表导出pdf")
    @PostMapping("/exportUserCostAndWaterSumToPdf")
    public ResponseEntity<String> exportUserCostAndWaterSumToPdf(HttpServletResponse response, @Valid @RequestBody PdfFeeAndWaterForm form, @ApiIgnore @RequestAttribute String securityLoginVillage) throws Exception {
        statisticsService.exportUserCostAndWaterSumToPdf(response,securityLoginVillage,form.getYear());
        return new ResponseEntity<>("success",HttpStatus.OK);
    }
}
