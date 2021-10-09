package com.icicles.wmms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.dto.WaterStatisticesDTO;
import com.icicles.wmms.entity.form.WsFeeLogExcelForm;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.entity.param.WsFeeLogQueryParam;
import com.icicles.wmms.DAO.WsFeeLogMapper;
import com.icicles.wmms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.service.impl.excel.CommonExport;
import com.icicles.wmms.utils.WebDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 缴费记录 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-12
 */
@Service
public class WsFeeLogServiceImpl extends ServiceImpl<WsFeeLogMapper, WsFeeLog> implements WsFeeLogService {

    private static final Logger logger = LoggerFactory.getLogger(WsFeeLogServiceImpl.class);

    @Resource
    WsFeeLogMapper wsFeeLogMapper;
    @Resource
    @Lazy
    private WsMeterService wsMeterService;
    @Resource
    private WsFeeStandardService wsFeeStandardService;
    @Resource
    private WaterFeeService waterFeeService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysDictItemService sysDictItemService;
    @Resource
    private WsChargeService wsChargeService;
    @Autowired
    private DataSourceTransactionManager txManager;
    @Resource
    private SysParamService sysParamService;

    @Override
    public IPage<WsFeeLog> findPage(Page page, WsFeeLogQueryParam wsFeeLogQueryParam) throws ApiException {
        IPage<WsFeeLog> retPage;
        try {
            QueryWrapper<WsFeeLog> queryWrapper = wsFeeLogQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeLogQueryParam.getUserId()), "user_id", wsFeeLogQueryParam.getUserId());
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeLogQueryParam.getPayStatus()), "pay_status", wsFeeLogQueryParam.getPayStatus());
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeLogQueryParam.getPhone()), "phone", wsFeeLogQueryParam.getUserName());
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeLogQueryParam.getMeterSn()), "meter_sn", wsFeeLogQueryParam.getMeterSn());
            queryWrapper.like(StringUtils.isNotBlank(wsFeeLogQueryParam.getUserName()), "user_name", wsFeeLogQueryParam.getUserName());
            queryWrapper.like(StringUtils.isNotBlank(wsFeeLogQueryParam.getVillageSn()), "village_sn", wsFeeLogQueryParam.getVillageSn());
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeLogQueryParam.getUserSn()), "user_sn", wsFeeLogQueryParam.getUserSn());

            queryWrapper.lambda().orderByDesc(WsFeeLog::getId);
            retPage = this.page(page,queryWrapper);
            logger.debug("查询缴费记录列表成功");
        } catch (Exception e) {
            logger.error("查询缴费记录列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询缴费记录列表异常", HttpStatus.BAD_REQUEST);
        }
        List<WsFeeLog> data = retPage.getRecords();
        this.setUpdateAdminName(data);
        return retPage;
   }

    @Override
    public synchronized WsFeeLog add(WsFeeLog wsFeeLog) throws ApiException {
        try {
            //查询对应的水表信息
            WsMeter meter = wsMeterService.getById(String.valueOf(wsFeeLog.getMeterId()));
            if(meter==null){
                throw new ApiException("设备信息不存在", HttpStatus.BAD_REQUEST);
            }
            return this.addFee(meter,wsFeeLog.getPayment(),wsFeeLog.getRealPay(),wsFeeLog.getUpdateUserId());
        } catch (ApiException e1) {
            logger.error("添加缴费记录异常", e1);
            e1.printStackTrace();
            throw e1;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除缴费记录成功" + id);
        } catch (Exception e) {
            logger.error("删除缴费记录异常", e);
            e.printStackTrace();
            throw new ApiException("删除缴费记录异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsFeeLog wsFeeLog) throws ApiException {
        try {
            UpdateWrapper<WsFeeLog> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsFeeLog.getId());
            this.update(wsFeeLog,wrapper);
            logger.debug("更新缴费记录成功" + wsFeeLog.getId());
        } catch (ApiException e) {
            logger.error("更新缴费记录错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新缴费记录异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新缴费记录异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsFeeLog findById(String id) throws ApiException {
        WsFeeLog wsFeeLog;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsFeeLog = wsFeeLogMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询缴费记录成功");
        } catch (Exception e) {
            logger.error("根据编号查询缴费记录异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询缴费记录异常", HttpStatus.BAD_REQUEST);
        }
        return wsFeeLog;
    }

    @Override
    public List<WaterStatisticesDTO> getAccountSumByMonth(String year, String userSn, String villageSn, String meterSn) {
        List<WaterStatisticesDTO> data = wsFeeLogMapper.getAccountSumByMonth(year,userSn,villageSn,meterSn);
        return this.formatMonthData(data,year);
    }

    @Override
    public List<WaterStatisticesDTO> getAccountSumByYear(String userSn, String villageSn, String meterSn) {
        return wsFeeLogMapper.getAccountSumByYear(userSn,villageSn,meterSn);
    }

    @Override
    public List<WaterStatisticesDTO> getAccountSumBySeason(String year, String userSn, String villageSn, String meterSn) {
        List<WaterStatisticesDTO> data = wsFeeLogMapper.getAccountSumBySeason(year,userSn,villageSn,meterSn);
        return this.formatSeasonData(data,year);
    }

    @Override
    public void exportMonthData(String year,String userSn,String villageSn,String meterSn, HttpServletResponse response) throws IOException {
        List<WaterStatisticesDTO> data = this.getAccountSumByMonth(year,userSn,villageSn,meterSn);

        //数据模板（设置excel头和导出的字段）
        ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp = new ArrayList<>();
        AbstractMap.SimpleEntry<String,String> data1 = new AbstractMap.SimpleEntry<>("date", "月份");
        AbstractMap.SimpleEntry<String,String> data2 = new AbstractMap.SimpleEntry<>("num", "金额");
        dataTemp.add(data1);
        dataTemp.add(data2);

        CommonExport<WaterStatisticesDTO> exportFun = new CommonExport<>(data,dataTemp);
        exportFun.exportExcel(year+"年缴费金额月度统计",response);
    }

    @Override
    public void exportSeasonData(String year,String userSn,String villageSn,String meterSn, HttpServletResponse response) throws IOException {
        List<WaterStatisticesDTO> data = this.getAccountSumBySeason(year,userSn,villageSn,meterSn);

        //数据模板（设置excel头和导出的字段）
        ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp = new ArrayList<>();
        AbstractMap.SimpleEntry<String,String> data1 = new AbstractMap.SimpleEntry<>("date", "季度");
        AbstractMap.SimpleEntry<String,String> data2 = new AbstractMap.SimpleEntry<>("num", "金额 ");
        dataTemp.add(data1);
        dataTemp.add(data2);

        CommonExport<WaterStatisticesDTO> exportFun = new CommonExport<>(data,dataTemp);
        exportFun.exportExcel(year+"年缴费金额季度统计",response);
    }

    @Override
    public void exportYearData(String userSn,String villageSn,String meterSn, HttpServletResponse response) throws IOException {
        List<WaterStatisticesDTO> data = this.getAccountSumByYear(userSn,villageSn,meterSn);

        //数据模板（设置excel头和导出的字段）
        ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp = new ArrayList<>();
        AbstractMap.SimpleEntry<String,String> data1 = new AbstractMap.SimpleEntry<>("date", "年份");
        AbstractMap.SimpleEntry<String,String> data2 = new AbstractMap.SimpleEntry<>("num", "金额 ");
        dataTemp.add(data1);
        dataTemp.add(data2);

        CommonExport<WaterStatisticesDTO> exportFun = new CommonExport<>(data,dataTemp);
        exportFun.exportExcel("年缴费金额季度统计",response);
    }

    @Override
    public void exportFeeLogData(WsFeeLogExcelForm wsFeeLogExcelForm,HttpServletResponse response) throws IOException {
        QueryWrapper<WsFeeLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNoneBlank(wsFeeLogExcelForm.getUserSn()),WsFeeLog::getUserSn,wsFeeLogExcelForm.getUserSn()).
                             eq(StringUtils.isNoneBlank(wsFeeLogExcelForm.getMeterSn()),WsFeeLog::getMeterSn,wsFeeLogExcelForm.getMeterSn());
        if(StringUtils.isNotBlank(wsFeeLogExcelForm.getCreatedTimeStart())&&StringUtils.isNotBlank(wsFeeLogExcelForm.getCreatedTimeEnd())){
            queryWrapper.lambda().ge(WsFeeLog::getCreateTime,wsFeeLogExcelForm.getCreatedTimeStart()).le(WsFeeLog::getCreateTime,wsFeeLogExcelForm.getCreatedTimeEnd());
        }else if(StringUtils.isNotBlank(wsFeeLogExcelForm.getCreatedTimeStart())){
            queryWrapper.lambda().ge(WsFeeLog::getCreateTime,wsFeeLogExcelForm.getCreatedTimeStart());
        }else if(StringUtils.isNotBlank(wsFeeLogExcelForm.getCreatedTimeEnd())){
            queryWrapper.lambda().le(WsFeeLog::getCreateTime,wsFeeLogExcelForm.getCreatedTimeEnd());
        }

        List<WsFeeLog> data = this.list(queryWrapper);

        //数据模板（设置excel头和导出的字段）
        ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp = new ArrayList<>();
        AbstractMap.SimpleEntry<String,String> data1 = new AbstractMap.SimpleEntry<>("userName", "用户名");
        AbstractMap.SimpleEntry<String,String> data2 = new AbstractMap.SimpleEntry<>("phone", "手机号");
        AbstractMap.SimpleEntry<String,String> data3 = new AbstractMap.SimpleEntry<>("volume", "用水量");
        AbstractMap.SimpleEntry<String,String> data4 = new AbstractMap.SimpleEntry<>("account", "缴费金额");
        AbstractMap.SimpleEntry<String,String> data5 = new AbstractMap.SimpleEntry<>("waterSum", "累计用水量");
        AbstractMap.SimpleEntry<String,String> data6 = new AbstractMap.SimpleEntry<>("payment", "支付方式");
        AbstractMap.SimpleEntry<String,String> data7 = new AbstractMap.SimpleEntry<>("payTime", "支付时间");
        AbstractMap.SimpleEntry<String,String> data8 = new AbstractMap.SimpleEntry<>("startNum", "上期水表示数");
        AbstractMap.SimpleEntry<String,String> data9 = new AbstractMap.SimpleEntry<>("endNum", "本期水表示数");
        AbstractMap.SimpleEntry<String,String> data10 = new AbstractMap.SimpleEntry<>("feeStartTime", "缴费开始时间");
        AbstractMap.SimpleEntry<String,String> data11 = new AbstractMap.SimpleEntry<>("feeEndTime", "缴费结束时间");
        dataTemp.add(data1);
        dataTemp.add(data2);
        dataTemp.add(data3);
        dataTemp.add(data4);
        dataTemp.add(data5);
        dataTemp.add(data6);
        dataTemp.add(data7);
        dataTemp.add(data8);
        dataTemp.add(data9);
        dataTemp.add(data10);
        dataTemp.add(data11);

        CommonExport<WsFeeLog> exportFun = new CommonExport<>(data,dataTemp);
        exportFun.exportExcel("缴费记录",response);
    }

    @Override
    public void autoFee(){
        //0值
        BigDecimal zeroNum = BigDecimal.ZERO;
        //查询余额大于0的设备
        QueryWrapper<WsMeter> wsMeterQueryWrapper = new QueryWrapper<>();
        wsMeterQueryWrapper.lambda().gt(WsMeter::getBalance,0);
        //所有水表设置
        List<WsMeter> data = wsMeterService.list(wsMeterQueryWrapper);
        for (WsMeter temp:
                data) {
            try {
                this.addFee(temp,"balance",zeroNum,"");
            }catch (ApiException apiE){
                logger.error(apiE.getMessage());
            }
        }
    }

    @Override
    public void addFeeByBalance(WsMeter meter) {
        this.addFee(meter,"balance",BigDecimal.ZERO,"");
    }
//    @Override
//    public void exportWaterLogData(String startTime, String endTime, HttpServletResponse response) throws IOException {
//        List<WsFeeLog> data = wsFeeLogMapper.selectList();
//
//        ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp = new ArrayList<>();
//        AbstractMap.SimpleEntry<String,String> data1 = new AbstractMap.SimpleEntry<>("userName", "用户名");
//        AbstractMap.SimpleEntry<String,String> data2 = new AbstractMap.SimpleEntry<>("phone", "手机号");
//        AbstractMap.SimpleEntry<String,String> data3 = new AbstractMap.SimpleEntry<>("volume", "用水量");
//        AbstractMap.SimpleEntry<String,String> data4 = new AbstractMap.SimpleEntry<>("account", "缴费金额");
//        AbstractMap.SimpleEntry<String,String> data5 = new AbstractMap.SimpleEntry<>("waterSum", "累计用水量");
//        dataTemp.add(data1);
//        dataTemp.add(data2);
//        dataTemp.add(data3);
//        dataTemp.add(data4);
//        dataTemp.add(data5);
//
//        CommonExport<WaterStatisticesDTO> exportFun = new CommonExport<>(data,dataTemp);
//        exportFun.exportExcel("年缴费金额月度统计",response);
//    }

    /**
     * 查询管理的最后更新人信息和用户信息
     * @param data
     * @return
     */
    private List<WsFeeLog> setUpdateAdminName(List<WsFeeLog> data){
        if(CollUtil.isEmpty(data)){
            return null;
        }
        //最后修改人
        List<SysUser> adminList = null;
        List<String> wsFeeLogUpdateId = data.stream().map(WsFeeLog::getUpdateUserId).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(wsFeeLogUpdateId)){
            wsFeeLogUpdateId = wsFeeLogUpdateId.stream().distinct().collect(Collectors.toList());
            adminList = sysUserService.getAllBySn(wsFeeLogUpdateId);
        }
        //记录对应的用户的详细信息
        List<String> userInfoIds = data.stream().map(WsFeeLog::getUserSn).collect(Collectors.toList());
        List<SysUser> userList = null;
        //去重
        if(CollUtil.isNotEmpty(userInfoIds)){
            userInfoIds = userInfoIds.stream().distinct().collect(Collectors.toList());
            userList = sysUserService.getAllBySn(userInfoIds);
        }
        boolean adminFlag = CollUtil.isNotEmpty(adminList);
        boolean userFlag = CollUtil.isNotEmpty(userList);
        for (WsFeeLog log:data){
            if(adminFlag){
                for (SysUser admin:adminList){
                    if(log.getUpdateUserId().equals(admin.getSn())){
                        log.setUpdateUserId(admin.getName());
                    }
                }
            }
            if(userFlag){
                for (SysUser u:userList){
                    if(log.getUserSn().equals(u.getSn())){
                        log.setUserName(u.getName());
                        log.setPhone(u.getPhone());
                    }
                }
            }
        }
        return data;
    }


    /**
     * 格式化月份数据（必须12个月）
     * @param dbData 数据库查询出来的数据
     * @param year   年份
     * @return
     */
    private List<WaterStatisticesDTO> formatMonthData(List<WaterStatisticesDTO> dbData,String year){
        List<WaterStatisticesDTO> yearData = new ArrayList<>();
        for (int i=1;i<=12;i++){
            WaterStatisticesDTO newData = new WaterStatisticesDTO();
            String month;
            if(i<=9){
                month = "0"+i;
            }else{
                month = String.valueOf(i);
            }
            newData.setDate(year+"-"+month);
            newData.setNum(BigDecimal.ZERO);
            yearData.add(newData);
        }
        for (WaterStatisticesDTO temp:
                dbData) {
            for (WaterStatisticesDTO formatData:
                    yearData) {
                if(formatData.getDate().equals(temp.getDate())){
                    formatData.setNum(temp.getNum());
                }
            }
        }
        return yearData;
    }

    /**
     * 格式化季度数据（必须四个季度）
     * @param dbData 数据库查询出来的数据
     * @param year   年份
     * @return
     */
    private List<WaterStatisticesDTO> formatSeasonData(List<WaterStatisticesDTO> dbData,String year){
        List<WaterStatisticesDTO> yearData = new ArrayList<>();
        for (int i=1;i<=4;i++){
            WaterStatisticesDTO newData = new WaterStatisticesDTO();
            newData.setDate(year+"-"+i);
            newData.setNum(BigDecimal.ZERO);
            yearData.add(newData);
        }
        for (WaterStatisticesDTO temp:
                dbData) {
            for (WaterStatisticesDTO formatData:
                    yearData) {
                if(formatData.getDate().equals(temp.getDate())){
                    formatData.setNum(temp.getNum());
                }
            }
        }
        return yearData;
    }
    /**
     * 缴费方法
     * 已经手动管理事务了，注意避免事务嵌套
     * @param meter          设备信息
     * @param paymentData    支付方式
     * @param realPay        前台收款金额
     * @param updateUserSn   最后更新人
     * @return
     */
    private synchronized WsFeeLog addFee(WsMeter meter,String paymentData,BigDecimal realPay,String updateUserSn){
        //手动管理事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def);
        try {
            //0值
            final BigDecimal zeroNumber = BigDecimal.ZERO;
            //用户的缴费金额不能小于0
            if(realPay.compareTo(zeroNumber)<0){
                throw new ApiException("缴费金额不正确", HttpStatus.BAD_REQUEST);
            }
            //查询最近一次付费的记录
            QueryWrapper<WsFeeLog> meterQuery = new QueryWrapper<>();
            meterQuery.lambda().eq(WsFeeLog::getMeterSn,meter.getSn()).
                    eq(WsFeeLog::getPayStatus,1).
                    orderByDesc(WsFeeLog::getId).
                    last("limit 1");
            WsFeeLog feeLog = this.getOne(meterQuery);

            //最新时间
            String lastTime = WebDateUtils.getWebDateBySchema("yyyy-MM-dd");

            //最近一次缴费的水表示数
            BigDecimal lastMeterNum = zeroNumber;
            //账单开始时间
            String feeStartTime;

            if(feeLog==null){
                //如果没有记录，账单开始即设备启用时间
                feeStartTime = meter.getActivationTime();
            }else{
                //如果有记录，最近一次缴费的水表示数即该记录的结束值(采用用水量的计算方式)
                lastMeterNum = feeLog.getEndNum();
                //如果有记录，账单开始时间上次缴费的时间
                feeStartTime = feeLog.getFeeEndTime();
            }

            //如果缴费记录中数水表示数=水表码盘值，则证明不需要缴费
//            if(meter.getMeterNum().compareTo(lastMeterNum)==0){
//                throw new ApiException("水费已经结清，不需要缴费。", HttpStatus.BAD_REQUEST);
//            }

            //缴费标准
            WsFeeStandard feeStandard = wsFeeStandardService.findBySn(meter.getMeterTypeSn());
            if(feeStandard==null){
                throw new ApiException("无法找到该设备对应的收费标准，请先做好对应。", HttpStatus.BAD_REQUEST);
            }

            /*读取关于计算用水量方式（用水量还是脉冲值）的配置*/
            SysParam param = sysParamService.findByKey(Constants.WaterBase.WATER_VOLUME_BASE);
            //计算用水量的标准（按照用水量或者安装脉冲值）
            String base;
            if(param==null){
                base = Constants.WaterBase.WATER_VOLUME_BASE_VOLUME;
            }else{
                base = param.getConfigValue();
                if(!Constants.WaterBase.WATER_VOLUME_BASE_PLUSE.equals(base)&&!Constants.WaterBase.WATER_VOLUME_BASE_VOLUME.equals(base)){
                    base = Constants.WaterBase.WATER_VOLUME_BASE_VOLUME;
                }
            }
            /*获取默认脉冲值*/
            //对应的方水/脉冲
            int defaultPluseInt=Integer.parseInt(meter.getPulse());
            if(Constants.WaterBase.WATER_VOLUME_BASE_PLUSE.equals(base)){
                if(defaultPluseInt<=0) {
                    //读取配置中的默认脉冲值
                    SysParam defaultPluseDbObj = sysParamService.findByKey(Constants.WaterBase.DEFAULT_PLUSE);
                    if(defaultPluseDbObj!=null){
                        defaultPluseInt = Integer.parseInt(defaultPluseDbObj.getConfigValue());
                        if(defaultPluseInt<=0){
                            throw new ApiException("默认多少脉冲对应一方水的值设置错误，请联系管理员设置", HttpStatus.BAD_REQUEST);
                        }
                    }
                }
            }
            /*获取前多少水免费*/
            int freeWater=0;
            //读取配置中的默认脉冲值
            SysParam freeWaterDbObj = sysParamService.findByKey(Constants.WaterBase.FREE_WATER);
            if(freeWaterDbObj!=null){
                if(StringUtils.isNotBlank(freeWaterDbObj.getConfigValue())){
                    freeWater = Integer.parseInt(freeWaterDbObj.getConfigValue());
                    if(freeWater<=0){
                        freeWater = 0;
                    }
                }
            }
            BigDecimal volume = zeroNumber;
            if(Constants.WaterBase.WATER_VOLUME_BASE_PLUSE.equals(base)){
                /*脉冲方式计算用水量时，重新设置最新示数*/
                if(feeLog==null){
                    //如果没有缴过费，就用初始脉冲值
                    lastMeterNum = BigDecimal.valueOf(meter.getPulseInit());
                    if(lastMeterNum.compareTo(zeroNumber)<=0){
                        lastMeterNum = zeroNumber;
                    }
                    //缴费区间的脉冲值为总的脉冲值-上次脉冲值
                    BigDecimal pulseSumTempNum = meter.getWaterSum().subtract(lastMeterNum);
                    //用水量 = 脉冲值/脉冲值对应的水方
                    volume = pulseSumTempNum.divide(BigDecimal.valueOf(defaultPluseInt),2,BigDecimal.ROUND_HALF_UP);
                    if(volume.compareTo(BigDecimal.valueOf(freeWater))<=0){
                        volume = zeroNumber;
                    }else{
                        volume = volume.subtract(BigDecimal.valueOf(freeWater));
                        if(volume.compareTo(zeroNumber)<0){
                            volume = zeroNumber;
                        }
                    }
                }else{
                    //如果有记录，最近一次缴费的水表示数即该记录的结束值
                    lastMeterNum = feeLog.getPulseSum();
                    //缴费区间的脉冲值为总的脉冲值-上次脉冲值
                    BigDecimal pulseSumTempNum = meter.getWaterSum().subtract(lastMeterNum);
                    volume = pulseSumTempNum.divide(BigDecimal.valueOf(defaultPluseInt),2,BigDecimal.ROUND_HALF_UP);
                }
            }else {
                /*总用水量方式计算用水量时*/

                //用水量=水表的最新示数-最近一次缴费示数
                if(feeLog==null){
                    lastMeterNum = zeroNumber;
                }
                volume = meter.getMeterNum().subtract(lastMeterNum);
                if(feeLog==null){
                    if(volume.compareTo(BigDecimal.valueOf(freeWater))<=0){
                        volume = zeroNumber;
                    }else{
                        volume = volume.subtract(BigDecimal.valueOf(freeWater));
                    }
                }
                if(volume.compareTo(zeroNumber)<=0){
//                throw new ApiException("总用水量必须大于0。", HttpStatus.BAD_REQUEST);
                    volume = zeroNumber;
                }
            }

            //收费金额
            BigDecimal cost = waterFeeService.getWaterFee(volume,feeStandard,feeStartTime,lastTime);
            if(cost.compareTo(zeroNumber)<=0){
//                throw new ApiException("缴费金额大于0时，才可以收费。", HttpStatus.BAD_REQUEST);
                cost = zeroNumber;
            }

            //用户信息
            SysUser u = sysUserService.findByUserSn(meter.getUserSn());
            //用户名
            String userName;
            //手机号
            String phone;
            if(u!=null){
                userName = u.getName();
                phone = u.getPhone();
            }else{
                userName = "";
                phone = "";
            }

            WsFeeLog wsFeeLog = new WsFeeLog();
            //保证支付方式
            SysDictItem payment = sysDictItemService.findByDictCode("payment",paymentData);
            if(payment==null){
//            throw new ApiException("系统不支持您选择的支付方式,请联系管理员添加新的支付方式", HttpStatus.BAD_REQUEST);
                wsFeeLog.setPayment("");
            }else{
                wsFeeLog.setPayment(payment.getValue());
            }

            /*处理收款金额和水费的关系*/

            //水表余额
            BigDecimal balance = meter.getBalance().add(realPay).subtract(cost);
            if(realPay.compareTo(cost)>0){
                BigDecimal sub = realPay.subtract(cost);
                //收款金额大于水费，保存一下余额
                wsMeterService.incAccount(meter.getSn(),sub);
            }else{
                //查询用户的余额
                BigDecimal balanceNum = meter.getBalance();

                //收款+用户余额
                BigDecimal userAccount = realPay.add(balanceNum);
                //收款+用户余额-水费
                BigDecimal subAccountNum = userAccount.subtract(cost);

                if(subAccountNum.compareTo(zeroNumber)<0){
                    throw new ApiException("收款金额加用户余额不足以支付水费，不允许缴费", HttpStatus.BAD_REQUEST);
                }else{
                    // 减去的余额数
                    BigDecimal subRealNum = cost.subtract(realPay);
                    //减去用户余额
                    if(subRealNum.compareTo(zeroNumber)>0){
                        //如果数据库余额不足，会抛出异常
                        wsMeterService.decAccount(meter.getSn(),subRealNum);
                    }
                }
            }


            wsFeeLog.setAccount(cost);
            wsFeeLog.setPayTime(lastTime);
            wsFeeLog.setFeeEndTime(lastTime);
            wsFeeLog.setFeeStartTime(feeStartTime);
            wsFeeLog.setFeeStandard(feeStandard.getFeeStandard());
            wsFeeLog.setMeterId(meter.getId());
            wsFeeLog.setUserId(meter.getUserId().longValue());
            wsFeeLog.setVolume(volume);
            wsFeeLog.setUserSn(meter.getUserSn());
            wsFeeLog.setUserName(userName);
            wsFeeLog.setPhone(phone);
            wsFeeLog.setMeterSn(meter.getSn());
            if(Constants.WaterBase.WATER_VOLUME_BASE_PLUSE.equals(base)){
                //总用水量
                BigDecimal waterVM = meter.getWaterSum().subtract(BigDecimal.valueOf(meter.getPulseInit()));
                waterVM = waterVM.divide(BigDecimal.valueOf(defaultPluseInt),2,BigDecimal.ROUND_HALF_UP);
                if(waterVM.compareTo(zeroNumber)<=0){
                    waterVM = zeroNumber;
                }
                wsFeeLog.setWaterSum(waterVM);
                //起码
                BigDecimal startNumTemp = lastMeterNum.divide(BigDecimal.valueOf(defaultPluseInt),2,BigDecimal.ROUND_HALF_UP);
                if(startNumTemp.compareTo(zeroNumber)<=0){
                    startNumTemp = zeroNumber;
                }
                wsFeeLog.setStartNum(startNumTemp);
                //止码
                BigDecimal endNumTemp = meter.getWaterSum().divide(BigDecimal.valueOf(defaultPluseInt),2,BigDecimal.ROUND_HALF_UP);
                if(endNumTemp.compareTo(zeroNumber)<=0){
                    endNumTemp = zeroNumber;
                }
                wsFeeLog.setEndNum(endNumTemp);
            }else{
                wsFeeLog.setWaterSum(meter.getMeterNum());
                wsFeeLog.setEndNum(meter.getMeterNum());
                wsFeeLog.setStartNum(lastMeterNum);
            }
            wsFeeLog.setVillageSn(meter.getVillageSn());
            wsFeeLog.setBalance(balance);
            wsFeeLog.setRealPay(realPay);
            wsFeeLog.setPulseSum(meter.getWaterSum());
            wsFeeLog.setUpdateUserId(updateUserSn);

            //保存水费记录
            if(volume.compareTo(zeroNumber)>0&&cost.compareTo(zeroNumber)>0){
                this.save(wsFeeLog);
            }

            //收款记录
            if(realPay.compareTo(zeroNumber)>0){
                WsCharge wsCharge = new WsCharge();
                wsCharge.setAccountNum(wsFeeLog.getRealPay());
                wsCharge.setBalance(balance);
                wsCharge.setPhone(phone);
                wsCharge.setUserSn(meter.getUserSn());
                wsCharge.setMeterSn(meter.getSn());
                wsCharge.setUserName(userName);
                wsCharge.setCost(cost);
                wsCharge.setVillageSn(meter.getVillageSn());
                wsCharge.setPayment(wsFeeLog.getPayment());
                wsCharge.setUpdateUserId(updateUserSn);
                wsChargeService.add(wsCharge);
            }
            txManager.commit(status);
            return wsFeeLog;
        } catch (ApiException e) {
            txManager.rollback(status);
            throw e;
        }
    }
}
