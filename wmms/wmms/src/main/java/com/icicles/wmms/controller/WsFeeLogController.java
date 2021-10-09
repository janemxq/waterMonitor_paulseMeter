package com.icicles.wmms.controller;

import com.icicles.wmms.entity.dto.WaterStatisticesDTO;
import com.icicles.wmms.entity.form.StatisticsForm;
import com.icicles.wmms.entity.form.WsFeeLogExcelForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.WsFeeLog;
import com.icicles.wmms.service.WsFeeLogService;
import com.icicles.wmms.entity.form.WsFeeLogForm;
import com.icicles.wmms.entity.form.WsFeeLogQueryForm;
import com.icicles.wmms.entity.param.WsFeeLogQueryParam;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 缴费记录 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-12
 */
@RestController
 @Api(tags = "缴费操作")
@RequestMapping("/wsFeeLog")
public class WsFeeLogController {

    private WsFeeLogService wsFeeLogService;

    @Autowired
    public void setWsFeeLogService(WsFeeLogService wsFeeLogService) {
       this.wsFeeLogService = wsFeeLogService;
    }

    @ApiOperation(value = "新增缴费", notes = "新增缴费")
    @PostMapping
    public ResponseEntity<WsFeeLog> add(@Validated @RequestBody WsFeeLogForm wsFeeLogForm,@ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        WsFeeLog feeLog = wsFeeLogForm.toPo(WsFeeLog.class);
        feeLog.setUpdateUserId(securityLoginUserSn);
        WsFeeLog wsFeeLog = wsFeeLogService.add(feeLog);
        return new ResponseEntity<>(wsFeeLog, HttpStatus.OK);
    }

//    @ApiOperation(value = "删除缴费记录", notes = "删除缴费记录")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
//        wsFeeLogService.delete(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value = "更新缴费记录", notes = "更新缴费记录")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "id", value = "缴费记录ID", required = true, dataType = "long"),
//        @ApiImplicitParam(name = "wsFeeLogForm", value = "缴费记录实体", required = true, dataType = "WsFeeLogForm")
//    })
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody WsFeeLogForm wsFeeLogForm) throws ApiException {
//        wsFeeLogService.refresh(wsFeeLogForm.toPo(id,WsFeeLog.class));
//        return ResponseEntity.ok().build();
//    }

    @ApiOperation(value="获取缴费记录", notes="获取指定缴费记录")
    @ApiImplicitParam(paramType = "path", name = "id", value = "缴费记录ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<WsFeeLog> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(wsFeeLogService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "缴费按月统计", notes = "缴费按月统计,不传其他参数代表查询平台信息。传某个标识代表查询某个具体信息。只能按年查询")
    @PostMapping("/getWaterSumByMonth")
    public ResponseEntity<List<WaterStatisticesDTO>> getWaterSumByMonth(@Valid @RequestBody StatisticsForm form,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        String year;
        if(StringUtils.isBlank(form.getYear())){
            year = DateFormatUtils.format(new Date(), "yyyy");
        }else{
            year = form.getYear();
        }
        if(StringUtils.isBlank(form.getVillageSn())&&StringUtils.isNotBlank(securityLoginVillage)){
            //如果用户没有上传村的标识，并且当前登录人为某个村的管理员，只查询该村的记录
            form.setVillageSn(securityLoginVillage);
        }
        List<WaterStatisticesDTO> data = wsFeeLogService.getAccountSumByMonth(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    @ApiOperation(value = "缴费按季度统计", notes = "缴费按月统计,不传其他参数代表查询平台信息。传某个标识代表查询某个具体信息。只能按年查询")
    @PostMapping("/getWaterSumBySeason")
    public ResponseEntity<List<WaterStatisticesDTO>> getWaterSumBySeason(@Valid @RequestBody StatisticsForm form,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        String year;
        if(StringUtils.isBlank(form.getYear())){
            year = DateFormatUtils.format(new Date(), "yyyy");
        }else{
            year = form.getYear();
        }
        if(StringUtils.isBlank(form.getVillageSn())&&StringUtils.isNotBlank(securityLoginVillage)){
            //如果用户没有上传村的标识，并且当前登录人为某个村的管理员，只查询该村的记录
            form.setVillageSn(securityLoginVillage);
        }
        List<WaterStatisticesDTO> data = wsFeeLogService.getAccountSumBySeason(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

//    @ApiOperation(value = "查询所有缴费记录", notes = "查询所有缴费记录")
//    @ApiResponses(
//        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
//    )
//    @GetMapping(value = "/all")
//    public ResponseEntity<List<WsFeeLog>> queryAll() throws ApiException {
//        List<WsFeeLog> wsFeeLogList = wsFeeLogService.list();
//        return new ResponseEntity(wsFeeLogList, HttpStatus.OK);
//    }

    @ApiOperation(value = "搜索缴费记录(分页)", notes = "根据条件搜索缴费记录")
    @ApiImplicitParam(name = "wsFeeLogQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "WsFeeLogQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<WsFeeLog>> search(@Valid @RequestBody WsFeeLogQueryForm wsFeeLogQueryForm,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        WsFeeLogQueryParam queryParam = wsFeeLogQueryForm.toParam(WsFeeLogQueryParam.class);
        if(StringUtils.isBlank(queryParam.getVillageSn())&&StringUtils.isNotBlank(securityLoginVillage)){
            queryParam.setVillageSn(securityLoginVillage);
        }
        IPage<WsFeeLog> page = wsFeeLogService.findPage(wsFeeLogQueryForm.getPage(),queryParam);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value = "导出缴费记录", notes = "导出缴费记录")
    @PostMapping("/exportFeeLogData")
    public ResponseEntity<String> exportFeeLogData(@Valid @RequestBody WsFeeLogExcelForm wsFeeLogExcelForm,
                                              HttpServletResponse response) throws IOException {
        wsFeeLogService.exportFeeLogData(wsFeeLogExcelForm,response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @ApiOperation(value = "导出缴费记录的月统计", notes = "导出缴费记录的月统计")
    @PostMapping("/exportMonthData")
    public ResponseEntity<String> exportMonthData(@Valid @RequestBody StatisticsForm form,
                                              HttpServletResponse response) throws IOException {
        String year;
        if(StringUtils.isBlank(form.getYear())){
            year = DateFormatUtils.format(new Date(), "yyyy");
        }else{
            year = form.getYear();
        }
        wsFeeLogService.exportMonthData(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn(),response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @ApiOperation(value = "导出缴费记录的季度统计", notes = "导出缴费记录的季度统计")
    @PostMapping("/exportSeasonData")
    public ResponseEntity<String> exportSeasonData(@Valid @RequestBody StatisticsForm form,
                                                  HttpServletResponse response) throws IOException {
        String year;
        if(StringUtils.isBlank(form.getYear())){
            year = DateFormatUtils.format(new Date(), "yyyy");
        }else{
            year = form.getYear();
        }
        wsFeeLogService.exportSeasonData(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn(),response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @ApiOperation(value = "导出缴费记录的年度统计", notes = "导出缴费记录的年度统计")
    @PostMapping("/exportYearData")
    public ResponseEntity<String> exportYearData(@Valid @RequestBody StatisticsForm form,
                                                   HttpServletResponse response) throws IOException {
        wsFeeLogService.exportYearData(form.getUserSn(),form.getVillageSn(),form.getMeterSn(),response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
