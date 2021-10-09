package com.icicles.wmms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.dto.SynDataDTO;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.service.*;
import com.icicles.wmms.service.impl.syn.*;
import com.icicles.wmms.utils.ApiSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步数据的客户端
 * 方法syn用于同步数据，如果新增同步数据的类型，先注入service，然后在方法syn中的map里面添加对应
 * @author lisy
 */
@Service
@Slf4j
public class SysDataClient {
    @Resource
    private SysUserService userService;
    @Resource
    private WsMeterService wsMeterService;
    @Resource
    private WsGroupService wsGroupService;
    @Resource
    private WsFeeLogService wsFeeLogService;
    @Resource
    private WsChargeService wsChargeService;
    @Resource
    private SysRoleService roleService;
    @Resource
    private WsVillageService wsVillageService;
    @Resource
    private WsFeeStandardService wsFeeStandService;
    @Resource
    private WsMeterReaderService wsMeterReaderService;


    @Resource
    private ApiSign apiSign;
    @Resource
    private DataSourceTransactionManager txManager;
    @Resource
    private WsSynService wsSynService;
    @Resource
    private SysParamService sysParamService;


    private Map<String,IService<? extends BasePo>> initType(){
        /*初始化同步的数据类型，可以根据需求添加 */
        Map<String,IService<? extends BasePo>> type = new HashMap<>();
        //用户数据
        type.put(SysUser.class.getName(),userService);
        //用水设备
        type.put(WsMeter.class.getName(),wsMeterService);
        //表井
        type.put(WsGroup.class.getName(),wsGroupService);
        //缴费记录
        type.put(WsFeeLog.class.getName(),wsFeeLogService);
        //充值记录
        type.put(WsCharge.class.getName(),wsChargeService);
        //村记录
        type.put(WsVillage.class.getName(),wsVillageService);
        //缴费标准
        type.put(WsFeeStandard.class.getName(),wsFeeStandService);
        //同步读表记录
        type.put(WsMeterReader.class.getName(),wsMeterReaderService);
        return type;
    }


    /**
     * 异步方式将本地数据发送到服务器
     */
    @Async("task-queue")
    public void syn(){
        /*初始化同步的数据类型，可以根据需求添加 */
        Map<String,IService<? extends BasePo>> type = this.initType();
        /*同步数据*/
        for (Map.Entry<String,IService<? extends BasePo>> t: type.entrySet()) {
            singleSyn(t.getValue(),t.getKey());
        }
    }

    /**
     * 异步方式保存数据
     */
    @Async("task-queue")
    public void save(String data){
        if(StringUtils.isBlank(data)){
            log.error("数据为空，不用保存同步的数据");
            return;
        }

        //转成对应的实体
        SynDataDTO vo = JSON.parseObject(data,SynDataDTO.class);
        String dataType = vo.getDataType();
        //验签
        boolean signFlag = apiSign.verifySign(vo.getTimestamp(),vo.getSign());
        if(!signFlag){
            log.error("签名异常，请进行核实");
            return;
        }else{
            log.info("验签成功，开始同步数据");
        }

        //同步用户信息
        if(SysUser.class.getName().equals(dataType)){
            Type type = new TypeReference<SynDataDTO<SysUser>>(){}.getType();
            SynDataDTO<SysUser> real = JSON.parseObject(data,type);
            List<SysUser> db = real.getData();
            AbstractSynData<SysUser> synData = new SynWsUserDataServiceImpl(userService,wsSynService,roleService);
            synData.saveData(db);
            return;
        }

        //同步水表设备
        if(WsMeter.class.getName().equals(dataType)){
            Type type = new TypeReference<SynDataDTO<WsMeter>>(){}.getType();
            SynDataDTO<WsMeter> real = JSON.parseObject(data,type);
            List<WsMeter> db = real.getData();
            AbstractSynData<WsMeter> synData = new SynWsMeterDataServiceImpl(wsMeterService,wsSynService);
            synData.saveData(db);
            return;
        }

        //同步表井设备
        if(WsGroup.class.getName().equals(dataType)){
            Type type = new TypeReference<SynDataDTO<WsGroup>>(){}.getType();
            SynDataDTO<WsGroup> real = JSON.parseObject(data,type);
            List<WsGroup> db = real.getData();
            AbstractSynData<WsGroup> synData = new SynWsGroupDataServiceImpl(wsGroupService,wsSynService);
            synData.saveData(db);
            return;
        }

        //同步缴费记录
        if(WsFeeLog.class.getName().equals(dataType)){
            Type type = new TypeReference<SynDataDTO<WsFeeLog>>(){}.getType();
            SynDataDTO<WsFeeLog> real = JSON.parseObject(data,type);
            List<WsFeeLog> db = real.getData();
            wsFeeLogService.saveBatch(db);
            return;
        }

        //同步充值记录
        if(WsCharge.class.getName().equals(dataType)){
            Type type = new TypeReference<SynDataDTO<WsCharge>>(){}.getType();
            SynDataDTO<WsCharge> real = JSON.parseObject(data,type);
            List<WsCharge> db = real.getData();
            wsChargeService.saveBatch(db);
            return;
        }

        //同步村数据
        if(WsVillage.class.getName().equals(dataType)){
            Type type = new TypeReference<SynDataDTO<WsVillage>>(){}.getType();
            SynDataDTO<WsVillage> real = JSON.parseObject(data,type);
            List<WsVillage> db = real.getData();
            AbstractSynData<WsVillage> synData = new SynWsVillageDataServiceImpl(wsVillageService,wsSynService);
            synData.saveData(db);
            return;
        }
        //同步缴费标准
        if(WsFeeStandard.class.getName().equals(dataType)){
            Type type = new TypeReference<SynDataDTO<WsFeeStandard>>(){}.getType();
            SynDataDTO<WsFeeStandard> real = JSON.parseObject(data,type);
            List<WsFeeStandard> db = real.getData();
            AbstractSynData<WsFeeStandard> synData = new SynFeeStandardDataServiceImpl(wsFeeStandService,wsSynService);
            synData.saveData(db);
            return;
        }

        //同步抄表记录（要不然没有办法统计）
        if(WsMeterReader.class.getName().equals(dataType)){
            Type type = new TypeReference<SynDataDTO<WsMeterReader>>(){}.getType();
            SynDataDTO<WsMeterReader> real = JSON.parseObject(data,type);
            List<WsMeterReader> db = real.getData();
            wsMeterReaderService.saveBatch(db);
        }
    }

    /**
     * 同步单一类型的数据
     * @param service  服务类
     * @param pojo     pojo的名称
     * @param <T>      pojo的类型
     */
    private <T extends BasePo> void singleSyn(IService<T> service, String pojo){
        //手动管理事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def);
        try{
            SysDateImpl<T> sysUserSysDate = new SysDateImpl<>(service,wsSynService,apiSign,this.getCenterServerUrl());
            sysUserSysDate.syn(pojo);
            txManager.commit(status);
        }catch (Exception e){
            log.error("出错了："+e.getMessage());
            txManager.rollback(status);
        }
    }

    /**
     * 获取集管端的服务器地址
     */
    private String getCenterServerUrl() {
        SysParam s = sysParamService.findByKey(Constants.CENTER_SERVER_URL_KEY);
        String centerServerUrl;
        if(s == null){
            centerServerUrl = Constants.CENTER_SERVER_URL;
        }else{
            centerServerUrl = s.getConfigValue();
        }
        return centerServerUrl;
    }
}
