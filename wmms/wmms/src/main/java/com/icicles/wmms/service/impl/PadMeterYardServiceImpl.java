package com.icicles.wmms.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icicles.wmms.entity.dto.CommonResultShowDTO;
import com.icicles.wmms.entity.po.PadMeterInfo;
import com.icicles.wmms.entity.po.PadMeterYard;
import com.icicles.wmms.entity.param.PadMeterYardQueryParam;
import com.icicles.wmms.DAO.PadMeterYardMapper;
import com.icicles.wmms.service.PadMeterInfoService;
import com.icicles.wmms.service.PadMeterYardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * pad端表井 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Service
public class PadMeterYardServiceImpl extends ServiceImpl<PadMeterYardMapper, PadMeterYard> implements PadMeterYardService {

    private static final Logger logger = LoggerFactory.getLogger(PadMeterYardServiceImpl.class);

    @Resource
    PadMeterYardMapper padMeterYardMapper;
    @Resource
    private PadMeterInfoService padMeterInfoService;
    @Resource
    private MeterDealServiceImpl meterDealService;

    @Override
    public IPage<PadMeterYard> findPage(Page page, PadMeterYardQueryParam padMeterYardQueryParam) throws ApiException {
        IPage<PadMeterYard> retPage;
        try {
            QueryWrapper<PadMeterYard> queryWrapper = padMeterYardQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(padMeterYardQueryParam.getName()), "name", padMeterYardQueryParam.getName());
            queryWrapper.eq(StringUtils.isNotBlank(padMeterYardQueryParam.getSn()), "sn", padMeterYardQueryParam.getSn());
            queryWrapper.eq(StringUtils.isNotBlank(padMeterYardQueryParam.getRemark()), "remark", padMeterYardQueryParam.getRemark());
            retPage = this.page(page,queryWrapper);
            logger.debug("查询pad端表井列表成功");
        } catch (Exception e) {
            logger.error("查询pad端表井列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询pad端表井列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PadMeterYard padMeterYard) throws ApiException {
        try {
            padMeterYard.setSn(IdUtil.simpleUUID());
            this.save(padMeterYard);
            this.addMeterInfo(padMeterYard.getId(),padMeterYard.getSn(),padMeterYard.getAddress());
            logger.debug("添加pad端表井成功" + padMeterYard.getId());
        } catch (ApiException e) {
            logger.error("添加pad端表井错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加pad端表井异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加pad端表井异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除pad端表井成功" + id);
        } catch (Exception e) {
            logger.error("删除pad端表井异常", e);
            e.printStackTrace();
            throw new ApiException("删除pad端表井异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(PadMeterYard padMeterYard) throws ApiException {
        try {
            UpdateWrapper<PadMeterYard> wrapper = new UpdateWrapper<>();
            //不要更新sn
            padMeterYard.setSn(null);
            //避免表井地址修改影响使用
            this.updateMeterInfo(padMeterYard);
            wrapper.eq("id",padMeterYard.getId());
            this.update(padMeterYard,wrapper);
            logger.debug("更新pad端表井成功" + padMeterYard.getId());
        } catch (ApiException e) {
            logger.error("更新pad端表井错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新pad端表井异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新pad端表井异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PadMeterYard findById(String id) throws ApiException {
        PadMeterYard padMeterYard;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            padMeterYard = padMeterYardMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询pad端表井成功");
        } catch (Exception e) {
            logger.error("根据编号查询pad端表井异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询pad端表井异常", HttpStatus.BAD_REQUEST);
        }
        return padMeterYard;
    }

    @Override
    public PadMeterYard findBySn(String sn) throws ApiException {
        QueryWrapper<PadMeterYard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PadMeterYard::getSn,sn).last("limit 1");
        return padMeterYardMapper.selectOne(queryWrapper);
    }

    @Override
    public CommonResultShowDTO getYardNum(String sn) {
        PadMeterYard yard = this.findBySn(sn);
        if(yard==null){
            logger.error("无法查询到表井信息");
            return CommonResultShowDTO.build(0,0);
        }
        String address = yard.getAddress();
        if(StrUtil.isBlank(address)){
            logger.error("表井地址异常");
            return CommonResultShowDTO.build(0,0);
        }
        int errorNum = 0;
        JSONArray rs = meterDealService.read(address);
        for (Object r : rs) {
            JSONObject obj = (JSONObject) r;
            //码盘值
            String waterNum = obj.get("val").toString();
            //累计用水量
            String waterSum = obj.get("codeVal").toString();
            //水表通道号
            String meterMac = obj.get("index").toString();

            //累计用水量
            int waterSumNum = 0;
            if(StrUtil.isNotBlank(waterSum)){
                waterSumNum = Integer.parseInt(waterSum);
            }
            //码盘值
            int waterNumInt = 0;
            if(StrUtil.isNotBlank(waterNum)){
                waterNumInt = Integer.parseInt(waterNum);
            }
            QueryWrapper<PadMeterInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PadMeterInfo::getMeterYardSn,address)
                                 .eq(PadMeterInfo::getMeterMac,meterMac)
                                 .last("limit 1");
            PadMeterInfo dbData = padMeterInfoService.getOne(queryWrapper);
            if(dbData==null){
                errorNum++;
                continue;
            }
            //更新
            UpdateWrapper<PadMeterInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(PadMeterInfo::getId,dbData.getId());
            PadMeterInfo dbUp = new PadMeterInfo();
            if(dbData.getMeterNum()<waterNumInt){
                dbUp.setMeterNum(waterNumInt);
            }
            if(dbData.getWaterSum()<waterSumNum){
                dbUp.setMeterNum(waterSumNum);
            }
            padMeterInfoService.update(dbUp,updateWrapper);
        }
        return CommonResultShowDTO.build(rs.size(),errorNum);
    }

    @Override
    public void upload(List<String> sns) {
        //TODO 调用硬件接口上传数据
    }

    private void addMeterInfo(Long yardId,String sn,String address){
        List<PadMeterInfo> list = new ArrayList<>();
        String now = DateUtil.now();
        for (int i=0;i<16;i++){
            PadMeterInfo temp = new PadMeterInfo();
            temp.setMeterYardSn(sn);
            temp.setMeterYardId(yardId);
            //水表通道号(十六进制)
            temp.setMeterMac(Integer.toHexString(i));
            temp.setRemark("");
            temp.setSn(IdUtil.simpleUUID());
            temp.setPulseSum(0);
            temp.setWaterSum(0);
            temp.setMeterNum(0);
            temp.setGetDataTime("");
            temp.setYardAddress(address);
            temp.setCreateTime(now);
            temp.setUpdateTime(now);
            list.add(temp);
        }
        padMeterInfoService.saveBatch(list);
    }
    private void updateMeterInfo(PadMeterYard padMeterYard){
        if(StrUtil.isNotBlank(padMeterYard.getSn())){
            UpdateWrapper<PadMeterInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(PadMeterInfo::getMeterYardSn,padMeterYard.getSn());
            PadMeterInfo info = new PadMeterInfo();
            if(StrUtil.isNotBlank(padMeterYard.getAddress())){
                info.setYardAddress(padMeterYard.getAddress());
            }
            padMeterInfoService.update(info,updateWrapper);
        }
    }
}
