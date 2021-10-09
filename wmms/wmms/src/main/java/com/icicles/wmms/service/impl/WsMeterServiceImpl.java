package com.icicles.wmms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.dto.*;
import com.icicles.wmms.entity.form.WsMeterQueryForm;
import com.icicles.wmms.entity.form.WsMeterSearchForm;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.entity.param.WsMeterQueryParam;
import com.icicles.wmms.DAO.WsMeterMapper;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.utils.MyExcelUtils;
import com.icicles.wmms.utils.WebDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 水表设备 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Service
public class WsMeterServiceImpl extends ServiceImpl<WsMeterMapper, WsMeter> implements WsMeterService {

    private static final Logger logger = LoggerFactory.getLogger(WsMeterServiceImpl.class);

    @Resource
    WsMeterMapper wsMeterMapper;
    @Resource
    private WsVillageService villageService;
    @Resource
    private WsGroupService groupService;
    @Resource
    private SysUserService userService;
    @Resource
    private WsFeeStandardService standardService;
    @Resource
    private WaterFeeService waterFeeService;
    @Resource
    private DataQueryAuthServiceImpl dataQueryAuthService;
    @Resource
    private SysParamService sysParamService;
    @Resource
    @Lazy
    private WsFeeLogService logService;

    @Override
    public IPage<WsMeter> findPage(Page page, WsMeterQueryParam wsMeterQueryParam) throws ApiException {
        IPage<WsMeter> retPage;
        try {
            QueryWrapper<WsMeter> queryWrapper = wsMeterQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterQueryParam.getSn()), "sn", wsMeterQueryParam.getSn());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterQueryParam.getUserName()), "user_name", wsMeterQueryParam.getUserName());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterQueryParam.getVillageName()), "village_name", wsMeterQueryParam.getVillageName());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterQueryParam.getGroupName()), "group_name", wsMeterQueryParam.getGroupName());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterQueryParam.getOnline()), "online", wsMeterQueryParam.getOnline());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterQueryParam.getPulse()), "pulse", wsMeterQueryParam.getPulse());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterQueryParam.getMacSn()), "mac_sn", wsMeterQueryParam.getMacSn());
            retPage = this.page(page,queryWrapper);
            logger.debug("查询水表设备列表成功");
        } catch (Exception e) {
            logger.error("查询水表设备列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询水表设备列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WsMeter wsMeter) throws ApiException {
        try {
            //保证标识唯一
            this.dbOnly(wsMeter.getSn());

            if(StringUtils.isNotBlank(wsMeter.getGroupSn())){
                WsGroup groupInfoNew = groupService.findBySn(wsMeter.getGroupSn());
                if(groupInfoNew==null){
                    throw new ApiException("添加表井信息不存在", HttpStatus.BAD_REQUEST);
                }
                //同一个表井下，通道号不能重复
                this.macOnly(groupInfoNew.getSn(),wsMeter.getMacSn(),null);

                wsMeter.setGroupName(groupInfoNew.getGroupName());
                wsMeter.setGroupId(groupInfoNew.getId().intValue());
                wsMeter.setVillageName(groupInfoNew.getVillageName());
                wsMeter.setVillageId(groupInfoNew.getVillageId().intValue());
                wsMeter.setVillageSn(groupInfoNew.getVillageSn());
            }


            //用户信息
            if(wsMeter.getUserId()!=null&&wsMeter.getUserId()!=0){
                SysUser user = userService.findById(String.valueOf(wsMeter.getUserId()));
                if(user==null){
                    throw new ApiException("用户信息不存在", HttpStatus.BAD_REQUEST);
                }
                wsMeter.setUserName(user.getName());
                wsMeter.setUserSn(user.getSn());
            }
            if(StringUtils.isNotBlank(wsMeter.getUserSn())){
                QueryWrapper<SysUser> queryWrapperUser = new QueryWrapper<>();
                queryWrapperUser.lambda().eq(SysUser::getSn,wsMeter.getUserSn()).last("limit 1");
                SysUser user = userService.getOne(queryWrapperUser);
                if(user==null){
                    throw new ApiException("用户信息不存在", HttpStatus.BAD_REQUEST);
                }
                wsMeter.setUserName(user.getName());
                wsMeter.setUserId(user.getId().intValue());
            }

            //收费标准
            if(wsMeter.getMeterType()!=null&&wsMeter.getMeterType()!=0){
                WsFeeStandard feeStandard = standardService.findById(String.valueOf(wsMeter.getMeterType()));
                if(feeStandard==null){
                    throw new ApiException("收费标准设置不正确", HttpStatus.BAD_REQUEST);
                }
                wsMeter.setMeterTypeSn(feeStandard.getSn());
            }
            if(StringUtils.isNotBlank(wsMeter.getMeterTypeSn())){
                WsFeeStandard feeStandardNew = standardService.findBySn(wsMeter.getMeterTypeSn());
                if(feeStandardNew==null){
                    throw new ApiException("收费标准设置不正确", HttpStatus.BAD_REQUEST);
                }
                wsMeter.setMeterType(feeStandardNew.getId().intValue());
            }

            //保存数据
            this.save(wsMeter);
            logger.debug("添加水表设备成功" + wsMeter.getId());
        } catch (ApiException e) {
            logger.error("添加水表设备错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加水表设备异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加水表设备异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除水表设备成功" + id);
        } catch (Exception e) {
            logger.error("删除水表设备异常", e);
            e.printStackTrace();
            throw new ApiException("删除水表设备异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsMeter wsMeter) throws ApiException {
        try {
            //保证标识唯一
            this.dbOnly(wsMeter.getSn(),wsMeter.getId());

            if(StringUtils.isNotBlank(wsMeter.getGroupSn())){
                WsGroup groupInfoNew = groupService.findBySn(wsMeter.getGroupSn());
                if(groupInfoNew==null){
                    throw new ApiException("添加表井信息不存在", HttpStatus.BAD_REQUEST);
                }

                //同一个表井下，通道号不能重复
                if(StringUtils.isNotBlank(wsMeter.getMacSn())){
                    this.macOnly(groupInfoNew.getSn(),wsMeter.getMacSn(),wsMeter.getId());
                }

                wsMeter.setGroupName(groupInfoNew.getGroupName());
                wsMeter.setGroupId(groupInfoNew.getId().intValue());
                if(StringUtils.isBlank(wsMeter.getVillageSn())){
                    wsMeter.setVillageSn(groupInfoNew.getVillageSn());
                    wsMeter.setVillageName(groupInfoNew.getVillageName());
                    wsMeter.setVillageId(groupInfoNew.getVillageId().intValue());
                }else{
                    wsMeter.setVillageName(groupInfoNew.getVillageName());
                    wsMeter.setVillageId(groupInfoNew.getVillageId().intValue());
                }
            }else{
                //同一个表井下，通道号不能重复
                if(StringUtils.isNotBlank(wsMeter.getMacSn())){
                    WsMeter recordFromDbTemp = wsMeterMapper.selectById(wsMeter.getId());
                    if(recordFromDbTemp==null){
                        throw new ApiException("编辑的内容不存在，请刷新页面", HttpStatus.BAD_REQUEST);
                    }else{
                        this.macOnly(recordFromDbTemp.getGroupSn(),wsMeter.getMacSn(),wsMeter.getId());
                    }
                }
            }

            //用户信息
            if(wsMeter.getUserId()!=null&&wsMeter.getUserId()!=0){
                SysUser user = userService.findById(String.valueOf(wsMeter.getUserId()));
                if(user==null){
                    throw new ApiException("用户信息不存在", HttpStatus.BAD_REQUEST);
                }
                wsMeter.setUserSn(user.getSn());
                wsMeter.setUserName(user.getName());
            }
            if(StringUtils.isNotBlank(wsMeter.getUserSn())){
                QueryWrapper<SysUser> queryWrapperUser = new QueryWrapper<>();
                queryWrapperUser.lambda().eq(SysUser::getSn,wsMeter.getUserSn()).last("limit 1");
                SysUser user = userService.getOne(queryWrapperUser);
                if(user==null){
                    throw new ApiException("用户信息不存在", HttpStatus.BAD_REQUEST);
                }
                wsMeter.setUserName(user.getName());
                wsMeter.setUserId(user.getId().intValue());
            }

            //村信息
            if (wsMeter.getVillageId()!=null&&wsMeter.getVillageId()>0){
                WsVillage village = villageService.findById(String.valueOf(wsMeter.getVillageId()));
                if(village==null){
                    throw new ApiException("对应的村信息不存在", HttpStatus.BAD_REQUEST);
                }
                wsMeter.setVillageName(village.getVillageName());
            }

            //收费标准
            if(wsMeter.getMeterType()!=null&&wsMeter.getMeterType()!=0){
                WsFeeStandard feeStandard = standardService.findById(String.valueOf(wsMeter.getMeterType()));
                if(feeStandard==null){
                    throw new ApiException("收费标准设置不正确", HttpStatus.BAD_REQUEST);
                }
                wsMeter.setMeterTypeSn(feeStandard.getSn());
            }
            if(StringUtils.isNotBlank(wsMeter.getMeterTypeSn())){
                WsFeeStandard feeStandardNew = standardService.findBySn(wsMeter.getMeterTypeSn());
                if(feeStandardNew==null){
                    throw new ApiException("收费标准设置不正确", HttpStatus.BAD_REQUEST);
                }
                wsMeter.setMeterType(feeStandardNew.getId().intValue());
            }

            //更新
            UpdateWrapper<WsMeter> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsMeter.getId());
            this.update(wsMeter,wrapper);
            logger.debug("更新水表设备成功" + wsMeter.getId());
        } catch (ApiException e) {
            logger.error("更新水表设备错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新水表设备异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新水表设备异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsMeter findById(String id) throws ApiException {
        WsMeter wsMeter;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsMeter = wsMeterMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询水表设备成功");
        } catch (Exception e) {
            logger.error("根据编号查询水表设备异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询水表设备异常", HttpStatus.BAD_REQUEST);
        }
        return wsMeter;
    }


    @Override
    public IPage<WsMeterDTO> getList(WsMeterQueryForm wsMeterQueryForm, Principal principal) {

        Page<WsMeterDTO> page = new Page<>(wsMeterQueryForm.getCurrent(), wsMeterQueryForm.getSize());

        //登录者只是普通管理员，则这能看本村的
        SysUser sys = dataQueryAuthService.getUser(principal);
        if(sys!=null){
            boolean role = dataQueryAuthService.isSuperAdmin(sys);
            if(!role){
                wsMeterQueryForm.setVillageSn(sys.getVillageSn());
            }
        }

        List<WsMeterDTO> data = wsMeterMapper.getMeterList(page,wsMeterQueryForm);

        //涉及到的村信息
        List<WsVillage> villageList=null;
        //设计到的表井信息
        List<WsGroup> groupList=null;

        //所有村的标识
        List<String> villageSnList = data.stream().map(WsMeter::getVillageSn).collect(Collectors.toList());
        //去重
        if(CollUtil.isNotEmpty(villageSnList)){
            villageSnList = villageSnList.stream().distinct().collect(Collectors.toList());
            villageList = villageService.getListBySns(villageSnList);
        }
        //所有表井的标识
        List<String> groupSnList = data.stream().map(WsMeter::getGroupSn).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(groupSnList)){
            groupSnList = groupSnList.stream().distinct().collect(Collectors.toList());
            groupList = groupService.getGroupsBySns(groupSnList);
        }

        //处理收费标准的名称，村的名称，表井名称
        List<WsFeeStandard> standards = standardService.list();
        if(data!=null&&standards!=null){
            for (WsMeterDTO meterDTO:
                    data) {
                for (WsFeeStandard standard:
                        standards) {
                    if(standard.getId()==meterDTO.getMeterType().longValue()){
                        meterDTO.setMeterTypeName(standard.getDisplayName());
                    }
                }
                if(CollUtil.isNotEmpty(villageList)){
                    for (WsVillage villageTemp:villageList){
                        if(villageTemp.getSn().equals(meterDTO.getVillageSn())){
                            meterDTO.setVillageName(villageTemp.getVillageName());
                        }
                    }
                }
                if (CollUtil.isNotEmpty(groupList)){
                    for (WsGroup groupTemp:groupList){
                        if(groupTemp.getSn().equals(meterDTO.getGroupSn())){
                            meterDTO.setGroupName(groupTemp.getGroupName());
                        }
                    }
                }
            }
        }

        return page.setRecords(data);
    }

    @Override
    public IPage<MeterFeeDTO> getMetersFeeInfo(WsMeterSearchForm wsMeterSearchForm, Principal principal) {
        Page<MeterFeeDTO> page = new Page<>(wsMeterSearchForm.getCurrent(), wsMeterSearchForm.getSize());
        //登录者只是普通管理员，则这能看本村的
        SysUser sys = dataQueryAuthService.getUser(principal);
        if(sys!=null){
            boolean role = dataQueryAuthService.isSuperAdmin(sys);
            if(!role){
                wsMeterSearchForm.setVillageSn(sys.getVillageSn());
            }
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

        //查询数据
        List<MeterFeeDTO> data = wsMeterMapper.getMetersFeeInfo(page,wsMeterSearchForm,base);

        /*获取默认脉冲值*/
        int defaultPluseInt=0;
        if(Constants.WaterBase.WATER_VOLUME_BASE_PLUSE.equals(base)){
            //读取配置中的默认脉冲值
            SysParam defaultPluseDbObj = sysParamService.findByKey(Constants.WaterBase.DEFAULT_PLUSE);
            if(defaultPluseDbObj!=null){
                defaultPluseInt = Integer.parseInt(defaultPluseDbObj.getConfigValue());
                if(defaultPluseInt<=0){
                    throw new ApiException("默认多少脉冲对应一方水的值设置错误，请联系管理员设置", HttpStatus.BAD_REQUEST);
                }
            }
        }
        /*获取前多少水免费*/
        int freeWater=0;
        //读取配置中的默认脉冲值
        SysParam freeWaterDbObj = sysParamService.findByKey(Constants.WaterBase.FREE_WATER);
        if(freeWaterDbObj!=null){
            freeWater = Integer.parseInt(freeWaterDbObj.getConfigValue());
            if(freeWater<=0){
                freeWater = 0;
            }
        }

        //涉及到的村信息
        List<WsVillage> villageList=null;
        //设计到的表井信息
        List<WsGroup> groupList=null;

        //所有村的标识
        List<String> villageSnList = data.stream().map(WsMeter::getVillageSn).collect(Collectors.toList());
        //去重
        if(CollUtil.isNotEmpty(villageSnList)){
            villageSnList = villageSnList.stream().distinct().collect(Collectors.toList());
            villageList = villageService.getListBySns(villageSnList);
        }
        //所有表井的标识
        List<String> groupSnList = data.stream().map(WsMeter::getGroupSn).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(groupSnList)){
            groupSnList = groupSnList.stream().distinct().collect(Collectors.toList());
            groupList = groupService.getGroupsBySns(groupSnList);
        }

        //当前时间
        String day = WebDateUtils.getWebDateBySchema("yyyy-MM-dd");
        //所有的收费标准
        Map<String,WsFeeStandard> standards = this.getAllStandard();
        //0值
        final BigDecimal zeroNumber = BigDecimal.ZERO;
        //获取默认脉冲值的BigDecimal值
        BigDecimal defaultPluseBigDecimal = BigDecimal.valueOf(defaultPluseInt);
        //获取前多少方水免费的BigDecimal值
        BigDecimal freeWaterBigDecimal = BigDecimal.valueOf(freeWater);
        //处理总的用水量、总的缴费金额
        for (MeterFeeDTO temp:
                data) {
            //缴费记录
            QueryWrapper<WsFeeLog> feeLogQueryWrapper = new QueryWrapper<>();
            feeLogQueryWrapper.lambda().eq(WsFeeLog::getMeterSn,temp.getSn()).last("limit 1");
            WsFeeLog lllg = logService.getOne(feeLogQueryWrapper);
            //最新的码盘值
            BigDecimal lastNumPulseTemp = temp.getLastNumber();
            //该表对应的方水/脉冲的BigDecial值（用于基于脉冲计算用水量时，做除数）
            BigDecimal thisPulseValue = new BigDecimal(temp.getPulse());
            if(thisPulseValue.compareTo(zeroNumber)<=0){
                thisPulseValue = defaultPluseBigDecimal;
            }

            //总的用水量(=最新读数-水表初始值)
            BigDecimal WaterSumIntTemp = temp.getLastNumber().subtract(temp.getPreNumber());
            if(Constants.WaterBase.WATER_VOLUME_BASE_PLUSE.equals(base)){
                //如果根据脉冲计算用水量，除以脉冲值
                WaterSumIntTemp = WaterSumIntTemp.divide(thisPulseValue,2,BigDecimal.ROUND_HALF_UP);
            }
            if(WaterSumIntTemp.compareTo(zeroNumber)<=0){
                WaterSumIntTemp = zeroNumber;
            }
            temp.setWaterSum(WaterSumIntTemp);
            //计算缴费金额使用的标准
            WsFeeStandard feeStandard = standards.get(temp.getMeterTypeSn());
            //上次缴费时间
            String prePayDate = temp.getPrePayDay();
            if(StringUtils.isBlank(prePayDate)){
                prePayDate = temp.getActivationTime();
            }
            //缴费金额
            BigDecimal account = zeroNumber;
            if(feeStandard!=null){
                BigDecimal tempToPayWaterVolume = temp.getToPayWaterVolume();
                if(Constants.WaterBase.WATER_VOLUME_BASE_PLUSE.equals(base)){
                    //如果根据脉冲计算用水量，除以脉冲值
                    tempToPayWaterVolume = tempToPayWaterVolume.divide(thisPulseValue,2,BigDecimal.ROUND_HALF_UP);
                }

                if(lllg==null){
                    tempToPayWaterVolume = tempToPayWaterVolume.subtract(freeWaterBigDecimal);
                    if(tempToPayWaterVolume.compareTo(zeroNumber)<=0){
                        tempToPayWaterVolume = zeroNumber;
                    }
                    if(WaterSumIntTemp.compareTo(freeWaterBigDecimal)<=0){
                        tempToPayWaterVolume = zeroNumber;
                    }
                }
                if(tempToPayWaterVolume.compareTo(zeroNumber)<=0){
                    tempToPayWaterVolume = zeroNumber;
                }
                temp.setToPayWaterVolume(tempToPayWaterVolume);
                account = waterFeeService.getWaterFee(tempToPayWaterVolume,feeStandard,prePayDate,day);
                temp.setMeterTypeName(feeStandard.getDisplayName());
            }else{
                temp.setMeterTypeName("");
            }
            //设置缴费金额
            temp.setToPayAccount(account);

            if(CollUtil.isNotEmpty(villageList)){
                for (WsVillage villageTemp:villageList){
                    if(villageTemp.getSn().equals(temp.getVillageSn())){
                        temp.setVillageName(villageTemp.getVillageName());
                    }
                }
            }
            if (CollUtil.isNotEmpty(groupList)){
                for (WsGroup groupTemp:groupList){
                    if(groupTemp.getSn().equals(temp.getGroupSn())){
                        temp.setGroupName(groupTemp.getGroupName());
                    }
                }
            }

            if(Constants.WaterBase.WATER_VOLUME_BASE_PLUSE.equals(base)){
                //起码
                BigDecimal startNumTemp = temp.getPreNumber().divide(BigDecimal.valueOf(defaultPluseInt),2,BigDecimal.ROUND_HALF_UP);
                if(startNumTemp.compareTo(zeroNumber)<=0){
                    startNumTemp = zeroNumber;
                }
                temp.setPreNumber(startNumTemp);
                //止码
                BigDecimal endNumTemp = lastNumPulseTemp.divide(BigDecimal.valueOf(defaultPluseInt),2,BigDecimal.ROUND_HALF_UP);
                if(endNumTemp.compareTo(zeroNumber)<=0){
                    endNumTemp = zeroNumber;
                }
                temp.setLastNumber(endNumTemp);
            }
        }
        return page.setRecords(data);
    }

    @Override
    public WsMeter findBySn(String sn) throws ApiException {
        QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsMeter::getSn,sn);
        return wsMeterMapper.selectOne(queryWrapper);
    }

    @Override
    public void exportExcel(List<String> items,HttpServletResponse response,String villageSn) throws IOException {
        QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<>();
        if(items!=null&&items.size()>0){
            queryWrapper.lambda().in(WsMeter::getId,items);
        }
        if(StringUtils.isNotBlank(villageSn)){
            queryWrapper.lambda().in(WsMeter::getVillageSn,villageSn);
        }
        List<WsMeter> data = wsMeterMapper.selectList(queryWrapper);

        List<List<String>> rows = new ArrayList<>();
        for (WsMeter tempData:
        data) {
            List<String> tempRow = new ArrayList<>();
            tempRow.add(tempData.getSn());
            tempRow.add(String.valueOf(tempData.getPulseInit()));
            tempRow.add(String.valueOf(tempData.getMeterNum()));
            tempRow.add(String.valueOf(tempData.getWaterSum()));
            tempRow.add(tempData.getUserName());
            tempRow.add(String.valueOf(tempData.getUserId()));
            tempRow.add(tempData.getVillageName());
            tempRow.add(String.valueOf(tempData.getVillageId()));
            tempRow.add(String.valueOf(tempData.getGroupSn()));
            tempRow.add(String.valueOf(tempData.getMeterType()));
            tempRow.add(tempData.getActivationTime());
            tempRow.add(tempData.getGroupName());
            tempRow.add(String.valueOf(tempData.getOnline()));
            tempRow.add(tempData.getPulse());
            tempRow.add(tempData.getMacSn());
            tempRow.add(tempData.getReadTime());
            tempRow.add(tempData.getCreateTime());

            tempRow.add(tempData.getUserSn());
            tempRow.add(tempData.getVillageSn());
            tempRow.add(tempData.getMeterTypeSn());
            tempRow.add(tempData.getRemark());
            tempRow.add(tempData.getBalance().toString());

            rows.add(tempRow);
        }
        List<String> title = CollUtil.newArrayList("设备编号","初始码盘值", "累计用水量", "码盘值", "用户名","用户id",
                "村名称","村id","表井唯一标识","缴费类型","设备起效时间","表井名称","是否在线","脉冲值","设备地址","抄表时间","创建时间",
                "用户标识","村庄标识","计费标识","备注","余额"
                );

        MyExcelUtils.exportExcel(response,"所有设备",title,rows);
    }

    @Override
    public ExcelResultDTO importExcel(MultipartFile file) {
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

    @Override
    public synchronized void decAccount(String meterSn, BigDecimal account) throws ApiException {
        if(account.compareTo(BigDecimal.ZERO)<=0){
            throw new ApiException("金额应该大于0", HttpStatus.BAD_REQUEST);
        }
        WsMeter dbAccount = this.getOneBySn(meterSn);
        if(dbAccount==null){
            throw new ApiException("缴费信息不存在", HttpStatus.BAD_REQUEST);
        }
        if(dbAccount.getBalance().compareTo(account)<0){
            throw new ApiException("余额不足", HttpStatus.BAD_REQUEST);
        }
        UpdateWrapper<WsMeter> update = new UpdateWrapper<>();

        //减少金额，乐观锁，还需要余额不能小于减少的金额
        update.lambda().eq(WsMeter::getBalanceVersion,dbAccount.getBalanceVersion()).ge(WsMeter::getBalance,account).eq(WsMeter::getId,dbAccount.getId()).setSql("balance = balance-"+account).setSql("balance_version = balance_version+1");
        wsMeterMapper.update(dbAccount,update);
    }

    @Override
    public void incAccount(String meterSn, BigDecimal account) throws ApiException {
        if(account.compareTo(BigDecimal.ZERO)<=0){
            throw new ApiException("金额应该大于0", HttpStatus.BAD_REQUEST);
        }
        WsMeter dbAccount = this.getOneBySn(meterSn);
        if(dbAccount==null){
            throw new ApiException("缴费信息不存在", HttpStatus.BAD_REQUEST);
        }
        UpdateWrapper<WsMeter> update = new UpdateWrapper<>();

        //增加金额，乐观锁
        update.lambda().eq(WsMeter::getBalanceVersion,dbAccount.getBalanceVersion()).eq(WsMeter::getId,dbAccount.getId()).setSql("balance = balance+"+account).setSql("balance_version = balance_version+1");
        wsMeterMapper.update(dbAccount,update);
    }

    /**
     * 用来处理水表设备村和表井的名称显示
     * @param data
     * @return
     */
    @Override
    public List<WsMeter> setVillageNameAndYardName(List<WsMeter> data) {
        if (data == null) {
            return null;
        }
        //涉及到的村信息
        List<WsVillage> villageList=null;
        //设计到的表井信息
        List<WsGroup> groupList=null;

        //所有村的标识
        List<String> villageSnList = data.stream().map(WsMeter::getVillageSn).collect(Collectors.toList());
        //去重
        if(CollUtil.isNotEmpty(villageSnList)){
            villageSnList = villageSnList.stream().distinct().collect(Collectors.toList());
            villageList = villageService.getListBySns(villageSnList);
        }
        //所有表井的标识
        List<String> groupSnList = data.stream().map(WsMeter::getGroupSn).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(groupSnList)){
            groupSnList = groupSnList.stream().distinct().collect(Collectors.toList());
            groupList = groupService.getGroupsBySns(groupSnList);
        }
        if(CollUtil.isNotEmpty(villageList)){
            for (WsMeter temp : data) {
                for (WsVillage villageTemp:villageList){
                    if(villageTemp.getSn().equals(temp.getVillageSn())){
                        temp.setVillageName(villageTemp.getVillageName());
                    }
                }
            }
        }
        if (CollUtil.isNotEmpty(groupList)){
            for (WsMeter temp : data) {
                for (WsGroup groupTemp:groupList){
                    if(groupTemp.getSn().equals(temp.getGroupSn())){
                        temp.setGroupName(groupTemp.getGroupName());
                    }
                }
            }
        }
        return data;
    }

    /**
     * 处理excel数据
     * @return RowHandler 处理类
     */
    private RowHandler createRowHandler(ExcelResultDTO rs) {
        return new MeterExcelDeal(rs);
    }

    private class MeterExcelDeal implements RowHandler{
        private ExcelResultDTO rs;
        private MeterExcelDeal(ExcelResultDTO rs){
            this.rs = rs;
        }
        @Override
        public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
            //使用第一个sheet1
            if(sheetIndex==0){
                //忽略第一行
                if(rowIndex>0){
                    rs.setSum(rs.getSum()+1);
//                        Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowList);
                    try {
                        int f = this.getMeterFromExcel(rowList);
                        if(f<=0){
                            rs.setError(rs.getError()+1);
                            logger.error("添加失败");
                        }
                    } catch (Exception e){
                        rs.setError(rs.getError()+1);
                        e.printStackTrace();
                    }
                }
            }
        }

        private int getMeterFromExcel(List<Object> rowList) throws ApiException{
            //判断唯一标识
            String sn = String.valueOf(rowList.get(0));
            if(StringUtils.isBlank(sn)){
                throw new ApiException("唯一标识不存在", HttpStatus.BAD_REQUEST);
            }

            //判断表井是否存在（导入设备时，必须确定表井存在）
            String groupSnString = rowList.get(8).toString();
            if(StringUtils.isBlank(groupSnString)){
                throw new ApiException("必须绑定表井", HttpStatus.BAD_REQUEST);
            }
            WsGroup group = groupService.findBySn(groupSnString);
            if(group==null){
                throw new ApiException("确认表井是否存在", HttpStatus.BAD_REQUEST);
            }

            /*如果水表设备已经存在，更新读表数据，如果不存在插入记录*/
            QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WsMeter::getSn,sn).last("limit 1");
            WsMeter tempMeter = wsMeterMapper.selectOne(queryWrapper);
            if(tempMeter==null){
                //水表设备不存在，插入记录
                WsMeter wsMeter = new WsMeter();
                wsMeter.setSn(sn);
                if(StringUtils.isBlank(String.valueOf(rowList.get(1)))){
                    wsMeter.setPulseInit(0);
                }else {
                    wsMeter.setPulseInit(Integer.parseInt(String.valueOf(rowList.get(1))));
                }
                wsMeter.setMeterNum(new BigDecimal(String.valueOf(rowList.get(2))));
                wsMeter.setWaterSum(new BigDecimal(String.valueOf(rowList.get(3))));
                wsMeter.setUserName(String.valueOf(rowList.get(4)));
                wsMeter.setUserId(Integer.valueOf(rowList.get(5).toString()));
                wsMeter.setVillageName(group.getVillageName());
                wsMeter.setVillageId(group.getVillageId().intValue());
                wsMeter.setGroupId(group.getId().intValue());
                wsMeter.setGroupSn(group.getSn());
                wsMeter.setMeterType(Integer.valueOf(rowList.get(9).toString()));
                wsMeter.setActivationTime(String.valueOf(rowList.get(10)));
                wsMeter.setGroupName(group.getGroupName());
                wsMeter.setOnline(Integer.valueOf(rowList.get(12).toString()));
                wsMeter.setPulse(String.valueOf(rowList.get(13)));
                wsMeter.setMacSn(String.valueOf(rowList.get(14)));
                wsMeter.setReadTime(String.valueOf(rowList.get(15)));
                wsMeter.setCreateTime(String.valueOf(rowList.get(16)));
                wsMeter.setUserSn(String.valueOf(rowList.get(17)));
                wsMeter.setVillageSn(String.valueOf(rowList.get(18)));
                wsMeter.setMeterTypeSn(String.valueOf(rowList.get(19)));
                wsMeter.setRemark(String.valueOf(rowList.get(20)));
                wsMeter.setBalance(new BigDecimal(String.valueOf(rowList.get(21))));

                return wsMeterMapper.insert(wsMeter);
            }else{
                /*水表设备已经存在，更新读表数据*/
                //excel中对应的读表时间
                String readTimeFromFile = String.valueOf(rowList.get(15));
                //数据库中对应的读表时间
                String readTimeFromDb = tempMeter.getReadTime();
                if(StringUtils.isNotBlank(readTimeFromFile)){
                    Date d1 = DateUtil.parse(readTimeFromFile);
                    Date d2 = DateUtil.parse(readTimeFromDb);
                    //只有导入的数据的读表时间大于数据库中的读表时间，才更新读数
                    if(DateUtil.compare(d1,d2)==1){
                        BigDecimal meterNumTemp = new BigDecimal(String.valueOf(rowList.get(2)));
                        BigDecimal waterSumTemp = new BigDecimal(String.valueOf(rowList.get(3)));
                        BigDecimal zeroNum = BigDecimal.ZERO;
                        //只有表盘示数和总用水量大于0时才更新，要不没有意义
                        if(meterNumTemp.compareTo(zeroNum)>0&&waterSumTemp.compareTo(zeroNum)>0){
                            WsMeter wsMeterUpdate = new WsMeter();
                            wsMeterUpdate.setId(tempMeter.getId());
                            wsMeterUpdate.setReadTime(readTimeFromFile);
                            wsMeterUpdate.setBalance(null);
                            wsMeterUpdate.setMeterNum(meterNumTemp);
                            wsMeterUpdate.setWaterSum(waterSumTemp);
                            wsMeterMapper.updateById(wsMeterUpdate);
                        }
                    }
                }
                return 1;
            }
        }
    }
    /**
     * 判断sn的唯一性
     * @param sn       唯一标识
     */
    private void dbOnly(String sn){
        //保证标识唯一
        QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsMeter::getSn,sn);
        WsMeter dbFlag = this.baseMapper.selectOne(queryWrapper);
        if(dbFlag!=null){
            throw new ApiException("设备标识已经存在", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 判断sn的唯一性
     * @param sn       唯一标识
     * @param id       记录id
     */
    private void dbOnly(String sn,Long id){
        //保证标识唯一
        QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(WsMeter::getId,id);
        queryWrapper.lambda().eq(WsMeter::getSn,sn);
        WsMeter dbFlag = this.baseMapper.selectOne(queryWrapper);
        if(dbFlag!=null){
            throw new ApiException("设备标识已经存在", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 判断表井通道号是否唯一
     * @param groupSn 表井标识
     * @param mac     表井通道号
     * @param id      记录id
     */
    private void macOnly(String groupSn,String mac,Long id){
        QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<>();
        if(id!=null&&id>0){
            queryWrapper.lambda().ne(WsMeter::getId,id);
        }
        queryWrapper.lambda().eq(WsMeter::getGroupSn,groupSn);
        queryWrapper.lambda().eq(WsMeter::getMacSn,mac).last("limit 1");
        WsMeter dbFlag = wsMeterMapper.selectOne(queryWrapper);
        if(dbFlag!=null){
            throw new ApiException("表井通道号已经存在", HttpStatus.BAD_REQUEST);
        }
    }

    private WsMeter getOneBySn(String sn){
        QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsMeter::getSn,sn).select(WsMeter::getId,WsMeter::getBalanceVersion,WsMeter::getBalance).last("limit 1");
        return wsMeterMapper.selectOne(queryWrapper);
    }

    /**
     * 获取所有的收费标准，以sn为值，以po为健
     * @return
     */
    private Map<String,WsFeeStandard> getAllStandard(){
        List<WsFeeStandard> standards = standardService.list();
        Map<String,WsFeeStandard> rs = new HashMap<>();
        if(standards!=null&&standards.size()>0){
            for (WsFeeStandard standard:
            standards) {
                rs.put(standard.getSn(),standard);
            }
        }
        return rs;
    }
}
