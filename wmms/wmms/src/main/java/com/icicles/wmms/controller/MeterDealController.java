package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSONObject;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.MeterDealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统参数 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
@RestController
 @Api(tags = "水表处理")
@RequestMapping("/meterDeal")
public class MeterDealController {

    private MeterDealService meterDealService;

    @Autowired
    public void setMeterDealService(MeterDealService meterDealService) {
       this.meterDealService = meterDealService;
    }

    @ApiOperation(value = "根据水表编号更新水表数据", notes = "根据水表编号更新水表数据")
    @ApiImplicitParam(paramType = "path", name = "id", value = "水表编号", required = true, dataType = "long")
    @PostMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> updateMeterData(@PathVariable String id) throws ApiException {
        meterDealService.updateMetersDateCrontab(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "配置终端设备地址码", notes = "配置终端设备地址码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dAddr", value = "目的地址(十六进制，例如：0101)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "nAddr", value = "新地址(十六进制，例如：0101)", required = true, paramType = "query")
    })
    @PostMapping(value = "/testConfigAddr")
    public ResponseEntity<Boolean> configAddr(@Param("dAddr") String dAddr,@Param("nAddr") String nAddr) throws ApiException {
        return ResponseEntity.ok(meterDealService.configAddr(dAddr,nAddr));
    }

    @ApiOperation(value = "配置累计用水量", notes = "配置终端每个脉冲通道上脉冲表的累计用水量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dAddr", value = "目的地址(十六进制，例如：0101)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "index", value = "通道(十六进制，例如：F)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "val", value = "累计用水量(十进制，例如：100)", required = true, paramType = "query")
    })
    @PostMapping(value = "/testConfigVal")
    public ResponseEntity<Boolean> configVal(@Param("dAddr") String dAddr,@Param("index") String index,@Param("val") String val) throws ApiException {
        return ResponseEntity.ok(meterDealService.configVal(dAddr,index,val));
    }

    @ApiOperation(value = "配置脉冲", notes = "配置终端每个脉冲通道上脉冲表，多少脉冲对应 1 方水")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dAddr", value = "目的地址(十六进制，例如：0101)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "index", value = "通道(十六进制，例如：F)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pulse", value = "脉冲(十进制，例如：1000)", required = true, paramType = "query")
    })
    @PostMapping(value = "/testConfigPulse")
    public ResponseEntity<Boolean> configPulse(@Param("dAddr") String dAddr,@Param("index") String index,@Param("pulse") String pulse) throws ApiException {
        return ResponseEntity.ok(meterDealService.configPulse(dAddr,index,pulse));
    }

    @ApiOperation(value = "配置脉冲", notes = "配置终端每个脉冲通道上脉冲表，多少脉冲对应 1 方水")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dAddr", value = "目的地址(十六进制，例如：0101)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pulse", value = "脉冲(十进制，例如：1000)", required = true, paramType = "query")
    })
    @PostMapping(value = "/testConfigAllPulse")
    public ResponseEntity<Boolean> configPulse(@Param("dAddr") String dAddr,@Param("pulse") String pulse) throws ApiException {
        return ResponseEntity.ok(meterDealService.configPulse(dAddr,pulse));
    }

    @ApiOperation(value = "配置当前码盘的码值", notes = "配置终端每个脉冲通道上脉冲表的当前码盘的码值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dAddr", value = "目的地址(十六进制，例如：0101)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "index", value = "通道(十六进制，例如：F)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "codeVal", value = "码盘值(十进制，例如：1000)", required = true, paramType = "query")
    })
    @PostMapping(value = "/testConfigCodeVal")
    public ResponseEntity<Boolean> configCodeVal(@Param("dAddr") String dAddr,@Param("index") String index,@Param("codeVal") String codeVal) throws ApiException {
        return ResponseEntity.ok(meterDealService.configCodeVal(dAddr,index,codeVal));
    }

    @ApiOperation(value = "读取终端设备数据", notes = "读取终端设备指定通道(x)上水表码盘值、累计用水量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dAddr", value = "目的地址(十六进制，例如：0101)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "index", value = "通道(十六进制，例如：F)", required = true, paramType = "query")
    })
    @PostMapping(value = "/testReadSingle")
    public ResponseEntity<JSONObject> readSingle(@Param("dAddr") String dAddr, @Param("index") String index) throws ApiException {
        return ResponseEntity.ok(meterDealService.readSingle(dAddr,index));
    }
}
