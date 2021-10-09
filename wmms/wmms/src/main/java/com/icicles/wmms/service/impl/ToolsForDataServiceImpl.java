package com.icicles.wmms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用于数据数据的工具
 * 1、为了演示，清空业务数据(暂时不用)
 * @author lisy
 */
@Service
public class ToolsForDataServiceImpl {
    @Resource
    private PadMeterInfoService padMeterInfoService;
    @Resource
    private PadMeterYardService padMeterYardService;
    @Resource
    private WsChargeService wsChargeService;
    @Resource
    private WsFeeLogService wsFeeLogService;
    @Resource
    private WsGroupService wsGroupService;
    @Resource
    private WsMeterService wsMeterService;
    @Resource
    private WsMeterReaderService wsMeterReaderService;
    @Resource
    private WsSynService wsSynService;

    public void delData(){
        /*删除pad端表井记录*/
        QueryWrapper<PadMeterInfo> padMeterInfoQueryWrapper = new QueryWrapper<> ();
        padMeterInfoQueryWrapper.lambda().gt(PadMeterInfo::getId,0);
        padMeterInfoService.remove(padMeterInfoQueryWrapper);
        /*删除pad端水表记录*/
        QueryWrapper<PadMeterYard> padMeterYardQueryWrapper = new QueryWrapper<> ();
        padMeterYardQueryWrapper.lambda().gt(PadMeterYard::getId,0);
        padMeterYardService.remove(padMeterYardQueryWrapper);
        /*删除充值记录*/
        QueryWrapper<WsCharge> wsChargeQueryWrapper = new QueryWrapper<> ();
        wsChargeQueryWrapper.lambda().gt(WsCharge::getId,0);
        wsChargeService.remove(wsChargeQueryWrapper);
        /*删除缴费记录*/
        QueryWrapper<WsFeeLog> wsFeeLogQueryWrapper = new QueryWrapper<> ();
        wsFeeLogQueryWrapper.lambda().gt(WsFeeLog::getId,0);
        wsFeeLogService.remove(wsFeeLogQueryWrapper);
        /*删除表井记录*/
        QueryWrapper<WsGroup> wsGroupQueryWrapper = new QueryWrapper<> ();
        wsGroupQueryWrapper.lambda().gt(WsGroup::getId,0);
        wsGroupService.remove(wsGroupQueryWrapper);
        /*删除水表记录*/
        QueryWrapper<WsMeter> wsMeterQueryWrapper = new QueryWrapper<> ();
        wsMeterQueryWrapper.lambda().gt(WsMeter::getId,0);
        wsMeterService.remove(wsMeterQueryWrapper);
        /*删除抄表记录*/
        QueryWrapper<WsMeterReader> wsMeterReaderQueryWrapper = new QueryWrapper<> ();
        wsMeterReaderQueryWrapper.lambda().gt(WsMeterReader::getId,0);
        wsMeterReaderService.remove(wsMeterReaderQueryWrapper);
        /*删除同步数据记录*/
        QueryWrapper<WsSyn> wsSynQueryWrapper = new QueryWrapper<> ();
        wsSynQueryWrapper.lambda().gt(WsSyn::getId,0);
        wsSynService.remove(wsSynQueryWrapper);
    }
}
