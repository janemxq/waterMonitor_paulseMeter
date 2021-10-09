package com.icicles.wmms.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.icicles.wmms.entity.dto.CommonResultShowDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.PadMeterInfo;
import com.icicles.wmms.service.PadMeterInfoService;
import com.icicles.wmms.entity.form.PadMeterInfoForm;
import com.icicles.wmms.entity.form.PadMeterInfoQueryForm;
import com.icicles.wmms.entity.param.PadMeterInfoQueryParam;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * pad端水表信息 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@RestController
 @Api(tags = "pad端水表操作")
@RequestMapping("/padMeterInfo")
public class PadMeterInfoController {

    private PadMeterInfoService padMeterInfoService;

    @Autowired
    public void setPadMeterInfoService(PadMeterInfoService padMeterInfoService) {
       this.padMeterInfoService = padMeterInfoService;
    }

//    @ApiOperation(value = "新增pad端水表信息", notes = "新增pad端水表信息")
//    @PostMapping
//    public ResponseEntity<HttpStatus> add(@Validated @RequestBody PadMeterInfoForm padMeterInfoForm) throws ApiException {
//        padMeterInfoService.add(padMeterInfoForm.toPo(PadMeterInfo.class));
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value = "删除pad端水表信息", notes = "删除pad端水表信息")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
//        padMeterInfoService.delete(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value = "更新pad端水表信息", notes = "更新pad端水表信息")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "id", value = "pad端水表信息ID", required = true, dataType = "long"),
//        @ApiImplicitParam(name = "padMeterInfoForm", value = "pad端水表信息实体", required = true, dataType = "PadMeterInfoForm")
//    })
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody PadMeterInfoForm padMeterInfoForm) throws ApiException {
//        padMeterInfoService.refresh(padMeterInfoForm.toPo(id,PadMeterInfo.class));
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value="获取pad端水表信息", notes="获取指定pad端水表信息")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "pad端水表信息ID", required = true, dataType = "long")
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<PadMeterInfo> get(@PathVariable String id) throws ApiException {
//        return new ResponseEntity(padMeterInfoService.findById(id), HttpStatus.OK);
//    }
//
//    @ApiOperation(value = "查询所有pad端水表信息", notes = "查询所有pad端水表信息")
//    @ApiResponses(
//        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
//    )
//    @GetMapping(value = "/all")
//    public ResponseEntity<List<PadMeterInfo>> queryAll() throws ApiException {
//        List<PadMeterInfo> padMeterInfoList = padMeterInfoService.list();
//        return new ResponseEntity(padMeterInfoList, HttpStatus.OK);
//    }
//
//    @ApiOperation(value = "搜索pad端水表信息(分页)", notes = "根据条件搜索pad端水表信息")
//    @ApiImplicitParam(name = "padMeterInfoQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "PadMeterInfoQueryForm")
//    @PostMapping("/page")
//    public ResponseEntity<IPage<PadMeterInfo>> search(@Valid @RequestBody PadMeterInfoQueryForm padMeterInfoQueryForm) throws ApiException {
//        IPage<PadMeterInfo> page = padMeterInfoService.findPage(padMeterInfoQueryForm.getPage(),padMeterInfoQueryForm.toParam(PadMeterInfoQueryParam.class));
//        return new ResponseEntity<>(page, HttpStatus.OK);
//    }
    @ApiOperation(value = "根据表井标识获取水表列表", notes = "根据表井标识获取水表列表")
    @ApiImplicitParam(paramType = "path", name = "sn", value = "表井标识", required = true, dataType = "String")
    @GetMapping(value = "/getMeters/{sn}")
    public ResponseEntity<List<PadMeterInfo>> search(@PathVariable String sn) throws ApiException {
        List<PadMeterInfo> data = padMeterInfoService.getMetersByYardSn(sn);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    @ApiOperation(value="获取（同步）水表数据", notes="提供一个或者多个水表的sn，后台程序同步最新水表数据，如果服务器返回的状态是200,调用查询表井下所有水表接口")
    @PostMapping("/getMetersNum")
    public ResponseEntity<CommonResultShowDTO> getMetersNum(@Valid @RequestBody @ApiParam(name = "sns",value = "水表的sn值") List<String> sns) throws ApiException {
        if(CollUtil.isEmpty(sns)){
            throw new ApiException("至少上传一个水表标识", HttpStatus.BAD_REQUEST);
        }
        CommonResultShowDTO rs = padMeterInfoService.getMetersNum(sns);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
    @ApiOperation(value="上传数据（暂时没有完成）", notes="提供一个或者多个水表的sn，后台程序将对应的最新数据上传村委端（设备）")
    @PostMapping("/upload")
    public ResponseEntity<HttpStatus> upload(@Valid @RequestBody @ApiParam(name = "sns",value = "水表的sn值") List<String> sns) throws ApiException {
        if(CollUtil.isEmpty(sns)){
            throw new ApiException("至少上传一个水表标识", HttpStatus.BAD_REQUEST);
        }
        padMeterInfoService.upload(sns);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value="初始化水表", notes="提供某个水表的sn，后台程序调用硬件接口，完成水表的初始化")
    @PostMapping("/init")
    public ResponseEntity<HttpStatus> init(@Valid @RequestBody PadMeterInfoForm form) throws ApiException {
        padMeterInfoService.init(form);
        return ResponseEntity.ok().build();
    }
}
