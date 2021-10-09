package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import com.diboot.core.binding.RelationsBinder;
import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.entity.dto.MeterFeeDTO;
import com.icicles.wmms.entity.dto.WsMeterDTO;
import com.icicles.wmms.entity.form.*;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.impl.excel.CoreDataExport;
import com.icicles.wmms.utils.MyExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.entity.po.WsMeter;
import com.icicles.wmms.service.WsMeterService;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

/**
 * <p>
 * 水表设备 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@RestController
@Api(tags = "水表设备")
@RequestMapping("/wsMeter")
public class WsMeterController {

    private WsMeterService wsMeterService;

    @Resource
    private CoreDataExport coreDataExport;

    @Autowired
    public void setWsMeterService(WsMeterService wsMeterService) {
       this.wsMeterService = wsMeterService;
    }

    @ApiOperation(value = "新增水表设备", notes = "新增水表设备")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody WsMeterForm wsMeterForm) throws ApiException {
        WsMeter meter = wsMeterForm.toPo(WsMeter.class);
        if(StringUtils.isNotBlank(meter.getPulse())){
            try {
                BigDecimal p = new BigDecimal(meter.getPulse());
                if(p.compareTo(BigDecimal.ZERO)<=0){
                    throw new ApiException("请填写正确的'方水/脉冲'值（填写大于0的数字）", HttpStatus.BAD_REQUEST);
                }
            }catch (Exception e) {
                throw new ApiException("请填写正确的'方水/脉冲'值（填写大于0的数字）", HttpStatus.BAD_REQUEST);
            }
        }
        wsMeterService.add(meter);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除水表设备", notes = "删除水表设备")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        wsMeterService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新水表设备", notes = "更新水表设备")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "水表设备ID", required = true, dataType = "long"),
        @ApiImplicitParam(name = "wsMeterForm", value = "水表设备实体", required = true, dataType = "WsMeterForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody WsMeterForm wsMeterForm) throws ApiException {
        WsMeter meter = wsMeterForm.toPo(WsMeter.class);
        if(StringUtils.isNotBlank(meter.getPulse())){
            try {
                BigDecimal p = new BigDecimal(meter.getPulse());
                if(p.compareTo(BigDecimal.ZERO)<=0){
                    throw new ApiException("请填写正确的'方水/脉冲'值（填写大于0的数字）", HttpStatus.BAD_REQUEST);
                }
            }catch (Exception e) {
                throw new ApiException("请填写正确的'方水/脉冲'值（填写大于0的数字）", HttpStatus.BAD_REQUEST);
            }
        }
        wsMeterService.refresh(wsMeterForm.toPo(id,WsMeter.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="获取水表设备", notes="获取指定水表设备")
    @ApiImplicitParam(paramType = "path", name = "id", value = "水表设备ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<WsMeter> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(wsMeterService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有水表设备", notes = "查询所有水表设备")
    @ApiResponses(
        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/all")
    public ResponseEntity<List<WsMeter>> queryAll() throws ApiException {
        List<WsMeter> wsMeterList = wsMeterService.list();
        RelationsBinder.bind(wsMeterList);
        wsMeterList = wsMeterService.setVillageNameAndYardName(wsMeterList);
        return new ResponseEntity(wsMeterList, HttpStatus.OK);
    }

    @ApiOperation(value = "搜索水表设备(分页)", notes = "根据条件搜索水表设备")
    @ApiImplicitParam(name = "wsMeterQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "WsMeterQueryForm")

    @PostMapping("/page")
    public ResponseEntity<IPage<WsMeterDTO>> search(@Valid @RequestBody WsMeterQueryForm wsMeterQueryForm,Principal principal) throws ApiException {
        IPage<WsMeterDTO> page = wsMeterService.getList(wsMeterQueryForm,principal);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value = "用户设备水费管理", notes = "获取设备及水费信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wsMeterSearchForm", value = "查询表单", required = true, dataType = "WsMeterSearchForm"),
            @ApiImplicitParam(name = "name", value = "登录信息", dataType = "Principal")
    })
    @PostMapping("/getMetersFeeInfo")
    public ResponseEntity<IPage<MeterFeeDTO>> getMeters(@Valid @RequestBody WsMeterSearchForm wsMeterSearchForm, Principal principal) throws ApiException {

        IPage<MeterFeeDTO> page = wsMeterService.getMetersFeeInfo(wsMeterSearchForm,principal);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value = "excel导入设备", notes = "excel导入设备,必须是.xlsx格式的文件，大小不超过30M（文件大小的判断前端不需要做）导入后会完成水表和用户的添加或更新操作")
    @PostMapping("/importExcel")
    public ResponseEntity<ExcelResultDTO> importExcel(@RequestParam("metersFiles") @ApiParam(name = "metersFiles",value = "上传excel,必须", required = true) MultipartFile file) throws ApiException {
        ExcelResultDTO rs = coreDataExport.importExcel(file);
        return new ResponseEntity<>(rs,HttpStatus.OK);
    }
    @ApiOperation(value = "设备信息导出到excel", notes = "设备信息导出到excel。必须先保证对应的表井存在，导入才能成功")
    @PostMapping("/exportExcel")
    public ResponseEntity<String> exportExcel(@Valid @RequestBody @ApiParam(name = "ids",value = "选中的记录id（用户选择需要导出的数据，不传默认导出全部数据，非必须）") List<String> ids,
                                              HttpServletResponse response,@ApiIgnore @RequestAttribute String securityLoginVillage) throws IOException {
        coreDataExport.exportExcel(response,securityLoginVillage);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @ApiOperation(value = "下载模板", notes = "下载模板")
    @GetMapping("/downloadTemplate")
    public ResponseEntity<String> downloadTemplate(HttpServletResponse response) throws Exception {
        MyExcelUtils.downloadTemplate("excel/userandmeter.xlsx",response);
        return new ResponseEntity<>("success",HttpStatus.OK);
    }

//    @ApiOperation(value = "通过excel初始化用户和水表", notes = "通过excel初始化用户和水表")
//    @PostMapping("/initData")
//    public ResponseEntity<ExcelResultDTO> initData(@RequestParam("metersFiles") @ApiParam(name = "metersFiles",value = "上传excel,必须", required = true) MultipartFile file) throws Exception {
//        ExcelResultDTO rs = dataInitService.importExcel(file);
//        return new ResponseEntity<>(rs,HttpStatus.OK);
//    }

//    @ApiOperation(value = "搜索水表设备(根据用户分组)", notes = "根据条件搜索水表设备，并根据用户分组")
//    @ApiImplicitParam(name = "wsMeterQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "WsMeterQueryForm")
//    @PostMapping("/groupUser")
//    public ResponseEntity<IPage<WsMeter>> searchGroupUser(@Valid @RequestBody WsMeterQueryForm wsMeterQueryForm) throws ApiException {
//        IPage<WsMeter> page = wsMeterService.getUserList(wsMeterQueryForm);
//        return new ResponseEntity<>(page, HttpStatus.OK);
//    }
//    @ApiOperation(value = "根据用户名搜索水表设备", notes = "根据用户名搜索水表设备,搜索时用户名和用户id不能同时为空，可以单独上传用户id获取用户名称")
//    @ApiImplicitParam(name = "wsMeterQueryByNameForm", value = "{table.comment!}查询参数", required = true, dataType = "WsMeterQueryByNameForm")
//    @PostMapping("/searchByUserName")
//    public ResponseEntity<IPage<MeterFeeDTO>> searchByUserName(@Valid @RequestBody WsMeterQueryByNameForm wsMeterQueryByNameForm) throws ApiException {
//        if(StringUtils.isBlank(wsMeterQueryByNameForm.getUserId())&&StringUtils.isBlank(wsMeterQueryByNameForm.getUserName())){
//            throw new ApiException("用于搜索的用户名和用户标识不能同时为空",HttpStatus.BAD_REQUEST);
//        }
//        IPage<MeterFeeDTO> page = wsMeterService.getUserMeterInfo(wsMeterQueryByNameForm);
//        return new ResponseEntity<>(page, HttpStatus.OK);
//    }
//
//    @ApiOperation(value = "查询某村某组下的设备和村民信息)", notes = "查询某村某组下的设备和村民信息，可以先获取所有村信息、再根据村id获取所有组信息，进而调用本接口获取设备和村民信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "villageId", value = "村id", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "groupId", value = "组id", required = true, dataType = "String")
//    })
//    @GetMapping("/getMetersByVillageAndGroup")
//    public ResponseEntity<GroupMetersDTO> getGroupMeters(String villageId,String groupId) throws ApiException {
//        GroupMetersDTO data = wsMeterService.getGroupMeters(villageId,groupId);
//        return new ResponseEntity<>(data, HttpStatus.OK);
//    }
}
