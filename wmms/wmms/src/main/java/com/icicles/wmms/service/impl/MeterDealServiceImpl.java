package com.icicles.wmms.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.DAO.SysParamMapper;
import com.icicles.wmms.config.AppRunSchema;
import com.icicles.wmms.entity.dto.PcTerminalDTO;
import com.icicles.wmms.entity.param.SysParamQueryParam;
import com.icicles.wmms.entity.po.SysParam;
import com.icicles.wmms.entity.po.WsGroup;
import com.icicles.wmms.entity.po.WsMeter;
import com.icicles.wmms.entity.po.WsMeterReader;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.exception.CustomException;
import com.icicles.wmms.service.*;
import com.icicles.wmms.utils.CommixUtil;
import javafx.scene.input.DataFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统参数 服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-04-27
 */
@Service
public class MeterDealServiceImpl implements MeterDealService {

    private static final Logger logger = LoggerFactory.getLogger(MeterDealServiceImpl.class);

    private WsMeterService wsMeterService;

    @Autowired
    public void setWsMeterService(WsMeterService wsMeterService) {
        this.wsMeterService = wsMeterService;
    }

    private SysParamService sysParamService;

    @Autowired
    public void setSysParamService(SysParamService sysParamService) {
        this.sysParamService = sysParamService;
    }

    private WsGroupService wsGroupService;

    @Autowired
    public void setWsGroupService(WsGroupService wsGroupService) {
        this.wsGroupService = wsGroupService;
    }

    private WsMeterReaderService wsMeterReaderService;

    @Autowired
    public void setWsMeterReaderService(WsMeterReaderService wsMeterReaderService) {
        this.wsMeterReaderService = wsMeterReaderService;
    }

    private WsFeeLogService feeLogService;

    @Autowired
    public void setWsMeterReaderService(WsFeeLogService feeLogService) {
        this.feeLogService = feeLogService;
    }


    @Override
    public synchronized boolean updateMeterData(String id) throws ApiException {
        // 获取水表数据
        WsMeter wsMeter = wsMeterService.findById(id);
        try {
            // 获取表井数据
            WsGroup wsGroup = wsGroupService.findById(wsMeter.getGroupId().toString());

            // 读取指定表数据
            JSONObject jsonObject =  readSingle(wsGroup.getAddress(),wsMeter.getMacSn());
            // 记录执行数据

            // 更新指定表数据
            wsMeter.setMeterNum(new BigDecimal(jsonObject.get("codeVal").toString()));
            wsMeter.setWaterSum(new BigDecimal(jsonObject.get("val").toString()));
            wsMeter.setReadTime(DateUtil.now());
            wsMeter.setOnline(1);
            wsMeterService.refresh(wsMeter);

            //查询上次的抄表记录，计算两次抄表之间的用水量
            QueryWrapper<WsMeterReader> lastReaderQuery = new QueryWrapper<> ();
            lastReaderQuery.lambda().eq(WsMeterReader::getMeterId,id).orderByDesc(WsMeterReader::getId).last("limit 1");
            WsMeterReader lastReader = wsMeterReaderService.getOne(lastReaderQuery);

            WsMeterReader wsMeterReader = new WsMeterReader();
            wsMeterReader.setUserId(Long.parseLong(wsMeter.getUserId().toString()));
            wsMeterReader.setUserName(wsMeter.getUserName());
            wsMeterReader.setUserSn(wsMeter.getUserSn());
            wsMeterReader.setMeterId(wsMeter.getId());
            wsMeterReader.setMeterSn(wsMeter.getSn());
            wsMeterReader.setGroupId(wsGroup.getId());
            wsMeterReader.setGroupName(wsGroup.getGroupName());
            wsMeterReader.setGroupSn(wsGroup.getSn());
            wsMeterReader.setVolume(String.valueOf(jsonObject.get("val").toString()));
            wsMeterReader.setNum(String.valueOf(jsonObject.get("codeVal").toString()));
            wsMeterReader.setVillageId(wsGroup.getVillageId());
            wsMeterReader.setVillageName(wsGroup.getVillageName());
            wsMeterReader.setVillageSn(wsMeter.getVillageSn());

            /**
             * 计算用水量
             */
            if(lastReader==null){
                wsMeterReader.setUseWater(new BigDecimal(String.valueOf(jsonObject.get("codeVal"))));
            }else{
                BigDecimal zeroNumTo = BigDecimal.ZERO;
                BigDecimal useWater = new BigDecimal(String.valueOf(jsonObject.get("codeVal")));
                BigDecimal oldUserWaterSum = zeroNumTo;
                if(StringUtils.isNotBlank(lastReader.getNum())){
                    oldUserWaterSum = new BigDecimal(lastReader.getNum());
                }
                BigDecimal uw = useWater.subtract(oldUserWaterSum);
                if(uw.compareTo(zeroNumTo)<=0){
                    uw = zeroNumTo;
                }
                wsMeterReader.setUseWater(uw);
            }

            wsMeterReaderService.add(wsMeterReader);

            logger.debug("远程获取水表数据成功");
        } catch (Exception e) {
            wsMeter.setReadTime(DateUtil.now());
            wsMeter.setOnline(0);
            wsMeterService.refresh(wsMeter);

            logger.error("远程获取水表数据异常", e);
            e.printStackTrace();
            throw new ApiException("远程获取水表数据异常", HttpStatus.BAD_REQUEST);
        }
        return false;
    }

    @Override
    public boolean configAddr(String dAddr, String nAddr) throws ApiException {
        try {
            CommixUtil commixUtil = new CommixUtil();
            PcTerminalDTO pcTerminalDTO = getTerminalParam();

            Boolean ret =  commixUtil.configAddress(pcTerminalDTO.getCommName(),Integer.parseInt(pcTerminalDTO.getBaudrate()),Integer.valueOf(pcTerminalDTO.getHeader(),16),
                    Integer.valueOf(dAddr,16), Integer.valueOf(pcTerminalDTO.getFromAddress(),16),Integer.valueOf(nAddr,16),pcTerminalDTO.getIsTest());
            logger.debug("配置终端设备地址码成功");
            return ret;
        } catch (CustomException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.error("配置终端设备地址码异常", e);
            e.printStackTrace();
            throw new ApiException("配置终端设备地址码异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean configVal(String dAddr,String index, String val) throws ApiException {
        try {
            CommixUtil commixUtil = new CommixUtil();

            PcTerminalDTO pcTerminalDTO = getTerminalParam();
            Map<Integer,Integer> map = new HashMap<>();
            map.put(Integer.parseInt(index,16),Integer.parseInt(val));
            Boolean ret =  commixUtil.configVal(pcTerminalDTO.getCommName(),Integer.parseInt(pcTerminalDTO.getBaudrate()),Integer.valueOf(pcTerminalDTO.getHeader(),16),
                    Integer.valueOf(dAddr,16), Integer.valueOf(pcTerminalDTO.getFromAddress(),16),map,pcTerminalDTO.getIsTest());
            logger.debug("配置水表累计水量成功");
            return ret;
        } catch (CustomException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.error("配置水表累计水量异常", e);
            e.printStackTrace();
            throw new ApiException("配置水表累计水量异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean configPulse(String dAddr,String index, String pulse) throws ApiException {
        try {
            CommixUtil commixUtil = new CommixUtil();

            PcTerminalDTO pcTerminalDTO = getTerminalParam();
            Map<Integer,Integer> map = new HashMap<>();
            map.put(Integer.parseInt(index,16),Integer.parseInt(pulse));
            Boolean ret =  commixUtil.configPulse(pcTerminalDTO.getCommName(),Integer.parseInt(pcTerminalDTO.getBaudrate()),Integer.valueOf(pcTerminalDTO.getHeader(),16),
                    Integer.valueOf(dAddr,16), Integer.valueOf(pcTerminalDTO.getFromAddress(),16),map,pcTerminalDTO.getIsTest());
            logger.debug("配置水表脉冲值成功");
            return ret;
        } catch (CustomException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.error("配置水表脉冲值异常", e);
            e.printStackTrace();
            throw new ApiException("配置水表脉冲值异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean configPulse(String dAddr, String pulse) throws ApiException {
        try {
            CommixUtil commixUtil = new CommixUtil();

            PcTerminalDTO pcTerminalDTO = getTerminalParam();
            Map<Integer,Integer> map = new HashMap<>();
            for(int i = 0;i<16;i++){
                map.put(i,Integer.parseInt(pulse));
            }
            Boolean ret =  commixUtil.configPulse(pcTerminalDTO.getCommName(),Integer.parseInt(pcTerminalDTO.getBaudrate()),Integer.valueOf(pcTerminalDTO.getHeader(),16),
                    Integer.valueOf(dAddr,16), Integer.valueOf(pcTerminalDTO.getFromAddress(),16),map,pcTerminalDTO.getIsTest());
            logger.debug("配置水表脉冲值成功");
            return ret;
        } catch (CustomException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.error("配置水表脉冲值异常", e);
            e.printStackTrace();
            throw new ApiException("配置水表脉冲值异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean configCodeVal(String dAddr,String index, String codeVal) throws ApiException {
        try {
            CommixUtil commixUtil = new CommixUtil();
            PcTerminalDTO pcTerminalDTO = getTerminalParam();
            Map<Integer,Integer> map = new HashMap<>();
            map.put(Integer.parseInt(index,16),Integer.parseInt(codeVal));
            Boolean ret =  commixUtil.configCodeVal(pcTerminalDTO.getCommName(),Integer.parseInt(pcTerminalDTO.getBaudrate()),Integer.valueOf(pcTerminalDTO.getHeader(),16),
                    Integer.valueOf(dAddr,16), Integer.valueOf(pcTerminalDTO.getFromAddress(),16),map,pcTerminalDTO.getIsTest());
            logger.debug("配置码盘值成功");
            return ret;
        } catch (CustomException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.error("配置码盘值异常", e);
            e.printStackTrace();
            throw new ApiException("配置码盘值异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public JSONObject readSingle(String dAddr, String index) throws ApiException {
        try {
            CommixUtil commixUtil = new CommixUtil();
            PcTerminalDTO pcTerminalDTO = getTerminalParam();
            JSONObject ret =  commixUtil.read(pcTerminalDTO.getCommName(),Integer.parseInt(pcTerminalDTO.getBaudrate()),Integer.valueOf(pcTerminalDTO.getHeader(),16),
                    Integer.valueOf(dAddr,16), Integer.valueOf(pcTerminalDTO.getFromAddress(),16),Integer.parseInt(index,16),pcTerminalDTO.getIsTest());
            logger.debug("读取单个表数据成功");
            return ret;
        } catch (CustomException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.error("读取单个表数据异常", e);
            e.printStackTrace();
            throw new ApiException("读取单个表数据异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public JSONArray read(String dAddr) throws ApiException {
        try {
            CommixUtil commixUtil = new CommixUtil();
            PcTerminalDTO pcTerminalDTO = getTerminalParam();

            JSONArray ret = commixUtil.readList(pcTerminalDTO.getCommName(),Integer.parseInt(pcTerminalDTO.getBaudrate()),Integer.valueOf(pcTerminalDTO.getHeader(),16),
                    Integer.valueOf(dAddr,16), Integer.valueOf(pcTerminalDTO.getFromAddress(),16),16,pcTerminalDTO.getIsTest());
            logger.debug("读取表井所有表数据成功");
            return ret;
        } catch (CustomException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.error("读取表井所有表数据异常", e);
            e.printStackTrace();
            throw new ApiException("读取表井所有表数据异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void updateMetersDataCrontab() {
        List<WsMeter> meters = wsMeterService.list();
        for (WsMeter m : meters) {
            try {
                this.updateMeterData(String.valueOf(m.getId()));
            }catch (Exception e) {
                logger.error("自动抄表异常", e);
            }
        }
    }

    @Override
    public void updateMetersDateCrontab(String meterId) {
        //水表信息
        WsMeter meter = wsMeterService.findById(meterId);
        if(meter!=null){
            //更新总用水量和码盘值
            boolean meterUpdateFlag = this.updateMeterData(meterId);
            //再查一次
            meter = wsMeterService.findById(meterId);
            //自动缴费
            feeLogService.addFeeByBalance(meter);
//            if(meterUpdateFlag){
//                //自动缴费
//                feeLogService.addFeeByBalance(meter);
//            }
        }else{
            throw new ApiException("水表设备不存在", HttpStatus.BAD_REQUEST);
        }
    }

    private PcTerminalDTO getTerminalParam(){
        PcTerminalDTO pcTerminalDTO = new PcTerminalDTO();
        SysParam sysParam = sysParamService.findByKey("header");
        String header_str = sysParam==null?"FDDF":sysParam.getConfigValue();
        pcTerminalDTO.setHeader(header_str);
        sysParam = sysParamService.findByKey("fromAddress");
        String from_address = sysParam==null?"0000":sysParam.getConfigValue();
        pcTerminalDTO.setFromAddress(from_address);
        sysParam = sysParamService.findByKey("isTest");
        String is_test = sysParam==null?"1":sysParam.getConfigValue();
        pcTerminalDTO.setIsTest(is_test);
        sysParam = sysParamService.findByKey("commName");
        String commName = sysParam==null?"COM4":sysParam.getConfigValue();
        pcTerminalDTO.setCommName(commName);
        sysParam = sysParamService.findByKey("baudrate");
        String baudrate = sysParam==null?"9600":sysParam.getConfigValue();
        pcTerminalDTO.setBaudrate(baudrate);
        return pcTerminalDTO;
    }
}
