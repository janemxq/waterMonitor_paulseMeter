package com.icicles.wmms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icicles.wmms.DAO.StatisticsMapper;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.dto.PayDataExceptionDTO;
import com.icicles.wmms.entity.form.FeeExceptionForm;
import com.icicles.wmms.entity.form.NopayUsersForm;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author lisy
 */
@Service
public class DataExceptionServiceImpl implements DataExceptionService {
    @Resource
    private SysDictItemService itemService;
    @Resource
    private WsFeeLogService feeLogService;
    @Resource
    private WsMeterService wsMeterService;
    @Resource
    private WsGroupService wsGroupService;
    @Resource
    private StatisticsMapper statisticsMapper;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private WsMeterReaderService wsMeterReaderService;

    @Override
    public IPage<WsFeeLog> getFeeException(FeeExceptionForm feeExceptionForm) {
        Page<WsFeeLog> page = new Page<>(feeExceptionForm.getCurrent(), feeExceptionForm.getSize());
        //阈值
        Threshold threshold = new Threshold().initPayException();
        //缴费异常的最小阈值
        String min = threshold.getPayExceptionMin();
        //缴费异常的最大阈值
        String max = threshold.getPayExceptionMax();
        if(StringUtils.isBlank(min)||StringUtils.isBlank(max)){
            throw new ApiException("参数异常，请联系管理员设置好缴费异常的阈值", HttpStatus.BAD_REQUEST);
        }

        String month = feeExceptionForm.getMonth();
        if(StringUtils.isBlank(month)){
            month = DateFormatUtils.format(new Date(), "yyyy-MM");
        }

        QueryWrapper<WsFeeLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNoneBlank(feeExceptionForm.getUserSn()),WsFeeLog::getUserSn,feeExceptionForm.getUserSn())
                              .eq(StringUtils.isNoneBlank(feeExceptionForm.getVillageSn()),WsFeeLog::getVillageSn,feeExceptionForm.getVillageSn())
                             .apply("(account < {0} or account >{1})",min,max).apply("DATE_FORMAT(pay_time,'%Y-%m')={0}",month);
        return feeLogService.page(page,queryWrapper);
    }

    @Override
    public IPage<WsMeterReader> getWaterCostException(FeeExceptionForm feeExceptionForm) {
        Page<WsMeterReader> page = new Page<>(feeExceptionForm.getCurrent(), feeExceptionForm.getSize());
        //阈值
        Threshold threshold = new Threshold().initWaterException();
        //缴费异常的最小阈值
        String min = threshold.getWaterExceptionMin();
        //缴费异常的最大阈值
        String max = threshold.getWaterExceptionMax();
        if(StringUtils.isBlank(min)||StringUtils.isBlank(max)){
            throw new ApiException("参数异常，请联系管理员设置好缴费异常的阈值", HttpStatus.BAD_REQUEST);
        }

        String month = feeExceptionForm.getMonth();
        if(StringUtils.isBlank(month)){
            month = DateFormatUtils.format(new Date(), "yyyy-MM");
        }

        QueryWrapper<WsMeterReader> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNoneBlank(feeExceptionForm.getUserSn()),WsMeterReader::getUserSn,feeExceptionForm.getUserSn())
                .eq(StringUtils.isNoneBlank(feeExceptionForm.getVillageSn()),WsMeterReader::getVillageSn,feeExceptionForm.getVillageSn())
                .apply("(use_water>0 and (use_water < {0} or use_water >{1}))",min,max).apply("DATE_FORMAT(create_time,'%Y-%m')={0}",month);
        IPage<WsMeterReader> rs = wsMeterReaderService.page(page,queryWrapper);
        List<WsMeterReader> data = rs.getRecords();
        for (WsMeterReader temp:
        data) {
            SysUser user = sysUserService.findByUserSn(temp.getUserSn());
            if(user==null){
                temp.setPhone("");
            }else{
                temp.setPhone(user.getPhone());
                temp.setUserName(user.getName());
            }
            WsMeter meter = wsMeterService.findBySn(temp.getMeterSn());
            if(meter!=null){
                WsGroup group = wsGroupService.findBySn(meter.getGroupSn());
                if(group != null){
                    temp.setGroupName(group.getGroupName());
                }
            }
            //为了兼容前端，为volume字段赋值，使其代表用水量
            temp.setVolume(temp.getUseWater().toString());
        }
        return rs;
    }

    @Override
    public IPage<PayDataExceptionDTO> getNopayUsers(NopayUsersForm nopayUsersForm) {
        Page<PayDataExceptionDTO> page = new Page<>(nopayUsersForm.getCurrent(), nopayUsersForm.getSize());
        //阈值
        Threshold threshold = new Threshold().initNoPayException();

        List<PayDataExceptionDTO> data = statisticsMapper.getNopayUsers(page,nopayUsersForm.getVillageSn(),nopayUsersForm.getUserSn(),Integer.valueOf(threshold.getVolumeException()),Integer.valueOf(threshold.getHowLongException()));
        return page.setRecords(data);
    }

    /**
     * 各个阈值
     */
    private class Threshold{

        /**
         * 用水异常最小值，小于该值正常说明异常
         */
        private String waterExceptionMin;
        /**
         * 用水异常最大值，大于该值正常说明异常
         */
        private String waterExceptionMax;
        /**
         * 缴费异常的最小阈值，小于该值说明缴费异常
         */
        private String payExceptionMin;
        /**
         * 缴费异常的最大阈值，大于该值说明缴费异常
         */
        private String payExceptionMax;

        /**
         * 查询使用了多少水但是没有缴费的用户
         */
        private String volumeException;


        /**
         * 查询使用了多少天但是没有缴费的用户
         */
        private String howLongException;

        /**
         * 初始化用水异常的阈值
         * @return 异常阈值类
         */
        private Threshold initWaterException(){
            SysDictItem min = itemService.findByDictCode(Constants.WATER_EXCEPTION,Constants.WATER_EXCEPTION_MIN);
            if(min!=null){
                this.setWaterExceptionMin(min.getValue());
            }
            SysDictItem max = itemService.findByDictCode(Constants.WATER_EXCEPTION,Constants.WATER_EXCEPTION_MAX);
            if(max!=null){
                this.setWaterExceptionMax(max.getValue());
            }
            return this;
        }

        /**
         * 初始化缴费异常的阈值
         * @return 异常阈值类
         */
        private Threshold initPayException(){
            SysDictItem min = itemService.findByDictCode(Constants.PAYMENT_EXCEPTION,Constants.PAY_EXCEPTION_MIN);
            if(min!=null){
                this.setPayExceptionMin(min.getValue());
            }
            SysDictItem max = itemService.findByDictCode(Constants.PAYMENT_EXCEPTION,Constants.PAY_EXCEPTION_MAX);
            if(max!=null){
                this.setPayExceptionMax(max.getValue());
            }
            return this;
        }

        /**
         * 初始化未交费异常的阈值
         * @return 异常阈值类
         */
        private Threshold initNoPayException(){
            SysDictItem volume = itemService.findByDictCode(Constants.NO_PAY_EXCEPTION,Constants.NO_PAY_EXCEPTION_VOLUME);
            if(volume!=null){
                this.setVolumeException(volume.getValue());
            }
            SysDictItem longValue = itemService.findByDictCode(Constants.NO_PAY_EXCEPTION,Constants.NO_PAY_EXCEPTION_VOLUME_LONG);
            if(longValue!=null){
                this.setHowLongException(longValue.getValue());
            }
            return this;
        }



        private String getWaterExceptionMin() {
            return waterExceptionMin;
        }

        private void setWaterExceptionMin(String waterExceptionMin) {
            this.waterExceptionMin = waterExceptionMin;
        }

        private String getWaterExceptionMax() {
            return waterExceptionMax;
        }

        private void setWaterExceptionMax(String waterExceptionMax) {
            this.waterExceptionMax = waterExceptionMax;
        }

        private String getPayExceptionMin() {
            return payExceptionMin;
        }

        private void setPayExceptionMin(String payExceptionMin) {
            this.payExceptionMin = payExceptionMin;
        }

        private String getPayExceptionMax() {
            return payExceptionMax;
        }

        private void setPayExceptionMax(String payExceptionMax) {
            this.payExceptionMax = payExceptionMax;
        }

        private String getVolumeException() {
            return volumeException;
        }

        private void setVolumeException(String volumeException) {
            this.volumeException = volumeException;
        }

        private String getHowLongException() {
            return howLongException;
        }

        private void setHowLongException(String howLongException) {
            this.howLongException = howLongException;
        }
    }
}
