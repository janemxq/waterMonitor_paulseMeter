package com.icicles.wmms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.icicles.wmms.entity.dto.CommonResultShowDTO;
import com.icicles.wmms.entity.dto.ReadMeterNumDTO;
import com.icicles.wmms.entity.form.PadMeterInfoForm;
import com.icicles.wmms.entity.po.PadMeterInfo;
import com.icicles.wmms.entity.param.PadMeterInfoQueryParam;
import com.icicles.wmms.DAO.PadMeterInfoMapper;
import com.icicles.wmms.entity.po.PadMeterYard;
import com.icicles.wmms.service.MeterDealService;
import com.icicles.wmms.service.PadMeterInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.service.PadMeterYardService;
import org.springframework.stereotype.Service;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * pad端水表信息 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Service
public class PadMeterInfoServiceImpl extends ServiceImpl<PadMeterInfoMapper, PadMeterInfo> implements PadMeterInfoService {

    private static final Logger logger = LoggerFactory.getLogger(PadMeterInfoServiceImpl.class);

    @Resource
    PadMeterInfoMapper padMeterInfoMapper;
    @Resource
    private PadMeterYardService padMeterYardService;
    @Resource
    /**
     * 与硬件通讯的服务类
     */
    private MeterDealService meterDealService;

    @Override
    public IPage<PadMeterInfo> findPage(Page page, PadMeterInfoQueryParam padMeterInfoQueryParam) throws ApiException {
        IPage<PadMeterInfo> retPage;
        try {
            QueryWrapper<PadMeterInfo> queryWrapper = padMeterInfoQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(padMeterInfoQueryParam.getMeterYardSn()), "meter_yard_sn", padMeterInfoQueryParam.getMeterYardSn());
            queryWrapper.eq(StringUtils.isNotBlank(padMeterInfoQueryParam.getMeterYardId()), "meter_yard_id", padMeterInfoQueryParam.getMeterYardId());
            queryWrapper.eq(StringUtils.isNotBlank(padMeterInfoQueryParam.getMeterMac()), "meter_mac", padMeterInfoQueryParam.getMeterMac());
            queryWrapper.eq(StringUtils.isNotBlank(padMeterInfoQueryParam.getRemark()), "remark", padMeterInfoQueryParam.getRemark());
            queryWrapper.eq(StringUtils.isNotBlank(padMeterInfoQueryParam.getPulseSum()), "pulse_sum", padMeterInfoQueryParam.getPulseSum());
            queryWrapper.eq(StringUtils.isNotBlank(padMeterInfoQueryParam.getWaterSum()), "water_sum", padMeterInfoQueryParam.getWaterSum());
            queryWrapper.eq(StringUtils.isNotBlank(padMeterInfoQueryParam.getMeterNum()), "meter_num", padMeterInfoQueryParam.getMeterNum());
            retPage = this.page(page,queryWrapper);
            logger.debug("查询pad端水表信息列表成功");
        } catch (Exception e) {
            logger.error("查询pad端水表信息列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询pad端水表信息列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PadMeterInfo padMeterInfo) throws ApiException {
        try {
            PadMeterYard yard = padMeterYardService.findBySn(padMeterInfo.getMeterYardSn());
            if(yard==null){
                throw new ApiException("没有对应的表井信息", HttpStatus.BAD_REQUEST);
            }
            padMeterInfo.setMeterYardId(yard.getId());
            this.save(padMeterInfo);
            logger.debug("添加pad端水表信息成功" + padMeterInfo.getId());
        } catch (ApiException e) {
            logger.error("添加pad端水表信息错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加pad端水表信息异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加pad端水表信息异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除pad端水表信息成功" + id);
        } catch (Exception e) {
            logger.error("删除pad端水表信息异常", e);
            e.printStackTrace();
            throw new ApiException("删除pad端水表信息异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(PadMeterInfo padMeterInfo) throws ApiException {
        try {
            UpdateWrapper<PadMeterInfo> wrapper = new UpdateWrapper();
            wrapper.eq("id",padMeterInfo.getId());
            this.update(padMeterInfo,wrapper);
            logger.debug("更新pad端水表信息成功" + padMeterInfo.getId());
        } catch (ApiException e) {
            logger.error("更新pad端水表信息错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新pad端水表信息异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新pad端水表信息异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PadMeterInfo findById(String id) throws ApiException {
        PadMeterInfo padMeterInfo;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            padMeterInfo = padMeterInfoMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询pad端水表信息成功");
        } catch (Exception e) {
            logger.error("根据编号查询pad端水表信息异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询pad端水表信息异常", HttpStatus.BAD_REQUEST);
        }
        return padMeterInfo;
    }

    @Override
    public List<PadMeterInfo> getMetersByYardSn(String yardSn) {
        QueryWrapper<PadMeterInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PadMeterInfo::getMeterYardSn,yardSn);
        return padMeterInfoMapper.selectList(queryWrapper);
    }

    /**
     * 水表初始化（配置当前码盘的码值、配置脉冲、配置累计用水量）
     * @param meterInfoForm 一些表单数据
     */
    @Override
    public void init(PadMeterInfoForm meterInfoForm) {
        QueryWrapper<PadMeterInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PadMeterInfo::getSn,meterInfoForm.getSn()).last("limit 1");
        PadMeterInfo dbInfo = padMeterInfoMapper.selectOne(queryWrapper);
        if(dbInfo==null){
            throw new ApiException("无法查询到水表数据", HttpStatus.BAD_REQUEST);
        }
        //表井地址
        String address = dbInfo.getYardAddress();
        if(StrUtil.isBlank(address)){
            throw new ApiException("水表对应的表井地址为空，无法完成初始化", HttpStatus.BAD_REQUEST);
        }
        //配置当前码盘的码值
        boolean rs = meterDealService.configCodeVal(address,dbInfo.getMeterMac(),String.valueOf(meterInfoForm.getMeterNum()));
        if(!rs){
            throw new ApiException("配置当前码盘的码值失败", HttpStatus.BAD_REQUEST);
        }
        //配置脉冲
        boolean rs2 = meterDealService.configPulse(address,dbInfo.getMeterMac(),String.valueOf(meterInfoForm.getPulseSum()));
        if(!rs2){
            throw new ApiException("配置脉冲失败", HttpStatus.BAD_REQUEST);
        }
        //配置累计用水量
        boolean rs3 = meterDealService.configPulse(address,dbInfo.getMeterMac(),String.valueOf(meterInfoForm.getWaterSum()));
        if(!rs3){
            throw new ApiException("配置累计用水量失败", HttpStatus.BAD_REQUEST);
        }

        //更新本地表
        PadMeterInfo meterInfo = new PadMeterInfo();
        //码盘值
        meterInfo.setMeterNum(meterInfoForm.getMeterNum());
        //脉冲
        meterInfo.setPulseSum(meterInfoForm.getPulseSum());
        //码盘值
        meterInfo.setWaterSum(meterInfoForm.getWaterSum());
        UpdateWrapper<PadMeterInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PadMeterInfo::getSn,meterInfoForm.getSn());
        padMeterInfoMapper.update(meterInfo,updateWrapper);
    }

    @Override
    public void upload(List<String> sns) {
        //TODO 上传一个或者多个数据到设备
    }

    /**
     * 获取最新的设备读数
     * @param sns 一个或者多个设备
     */
    @Override
    public CommonResultShowDTO getMetersNum(List<String> sns) {
        if(sns==null){
            throw new ApiException("上传数据异常", HttpStatus.BAD_REQUEST);
        }
        //处理结果
        CommonResultShowDTO rs = CommonResultShowDTO.build(sns.size(),0);
        //错误数据
        int error = 0;
        for (String sn:
        sns) {
            if(StrUtil.isBlank(sn)){
                //数据数量加1
                error++;
                continue;
            }
            QueryWrapper<PadMeterInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PadMeterInfo::getSn,sn).last("limit 1");
            PadMeterInfo dbInfo = padMeterInfoMapper.selectOne(queryWrapper);
            if(dbInfo==null){
                logger.error("没有对应的水表数据");
                //数据数量加1
                error++;
                continue;
            }
            JSONObject readData;
            //读取水表读数
            try {
                readData = meterDealService.readSingle(dbInfo.getYardAddress(),dbInfo.getMeterMac());
            }catch (Exception e){
                error++;
                logger.error("读取水表读数失败");
                continue;
            }
            //将读取水表读数转成水表读数对象
            ReadMeterNumDTO readMeterNum = new ReadMeterNumDTO(readData);
            if(StrUtil.isBlank(readMeterNum.getCodeVal())||StrUtil.isBlank(readMeterNum.getVal())){
                logger.error("无法读取id为{}的水表读数",dbInfo.getId());
                //数据数量加1
                error++;
                continue;
            }
            //累计用水量
            Integer waterSum = Integer.valueOf(readMeterNum.getCodeVal());
            //码盘值
            Integer meterNum = Integer.valueOf(readMeterNum.getVal());
            if(waterSum<=0||dbInfo.getWaterSum()>waterSum){
                logger.error("累计用水量错误");
                //数据数量加1
                error++;
                continue;
            }
            if(meterNum<=0||dbInfo.getMeterNum()>meterNum){
                logger.error("码盘值错误");
                //数据数量加1
                error++;
                continue;
            }
            //更新数据库中的内容
            PadMeterInfo updateInfo = new PadMeterInfo();
            updateInfo.setWaterSum(waterSum);
            updateInfo.setMeterNum(meterNum);
            UpdateWrapper<PadMeterInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(PadMeterInfo::getId,dbInfo.getId());
            padMeterInfoMapper.update(updateInfo,updateWrapper);
        }
        //设置错误数量
        rs.setError(error);
        return rs;
    }
}
