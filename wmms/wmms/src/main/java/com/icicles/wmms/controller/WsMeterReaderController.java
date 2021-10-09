package com.icicles.wmms.controller;

import com.icicles.wmms.entity.dto.WaterStatisticesDTO;
import com.icicles.wmms.entity.form.StatisticsForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.WsMeterReader;
import com.icicles.wmms.service.WsMeterReaderService;
import com.icicles.wmms.entity.form.WsMeterReaderQueryForm;
import com.icicles.wmms.entity.param.WsMeterReaderQueryParam;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 水表读数 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-06
 */
@RestController
@Api(tags = "水表读数")
@RequestMapping("/wsMeterReader")
@Slf4j
public class WsMeterReaderController {

    private WsMeterReaderService wsMeterReaderService;

    @Autowired
    public void setWsMeterReaderService(WsMeterReaderService wsMeterReaderService) {
       this.wsMeterReaderService = wsMeterReaderService;
    }

//    @ApiOperation(value = "新增水表读数（仅测试用，前端不用对接）", notes = "新增水表读数，仅测试用，测试完成删除。数据是由系统通过和水表交互产生的，不是人为录入的。")
//    @PostMapping
//    public ResponseEntity<HttpStatus> add(@Validated @RequestBody WsMeterReaderForm wsMeterReaderForm) throws ApiException {
//        wsMeterReaderService.add(wsMeterReaderForm.toPo(WsMeterReader.class));
//        return ResponseEntity.ok().build();
//    }

//    @ApiOperation(value = "删除水表读数（仅测试用，前端不用对接）", notes = "删除水表读数，仅测试用，不能删除")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
//        wsMeterReaderService.delete(id);
//        return ResponseEntity.ok().build();
//    }

//    @ApiOperation(value = "更新水表读数（仅测试用，前端不用对接）", notes = "！！！更新支付状态！！！")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "id", value = "水表读数ID", required = true, dataType = "long"),
//        @ApiImplicitParam(name = "wsMeterReaderForm", value = "水表读数实体", required = true, dataType = "WsMeterReaderForm")
//    })
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody WsMeterReaderForm wsMeterReaderForm) throws ApiException {
//        wsMeterReaderService.refresh(wsMeterReaderForm.toPo(id,WsMeterReader.class));
//        return ResponseEntity.ok().build();
//    }

    @ApiOperation(value="获取水表读数", notes="获取指定水表读数")
    @ApiImplicitParam(paramType = "path", name = "id", value = "水表读数ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<WsMeterReader> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(wsMeterReaderService.findById(id), HttpStatus.OK);
    }

//    @ApiOperation(value = "查询所有水表读数", notes = "查询所有水表读数")
//    @ApiResponses(
//        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
//    )
//    @GetMapping(value = "/all")
//    public ResponseEntity<List<WsMeterReader>> queryAll() throws ApiException {
//        List<WsMeterReader> wsMeterReaderList = wsMeterReaderService.list();
//        return new ResponseEntity(wsMeterReaderList, HttpStatus.OK);
//    }

    @ApiOperation(value = "搜索水表读数(分页)", notes = "根据条件搜索水表读数")
    @ApiImplicitParam(name = "wsMeterReaderQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "WsMeterReaderQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<WsMeterReader>> search(@Valid @RequestBody WsMeterReaderQueryForm wsMeterReaderQueryForm) throws ApiException {
        IPage<WsMeterReader> page = wsMeterReaderService.findPage(wsMeterReaderQueryForm.getPage(),wsMeterReaderQueryForm.toParam(WsMeterReaderQueryParam.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value = "用水量按月统计", notes = "用水量按月统计,不传其他参数代表查询平台信息。传某个标识代表查询某个具体信息。只能按年查询")
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
        List<WaterStatisticesDTO> data = wsMeterReaderService.getWaterSumByMonth(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "用水量按季度统计", notes = "用水量按季度统计,不传其他参数代表查询平台信息。传某个标识代表查询某个具体信息。只能按年查询")
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
        List<WaterStatisticesDTO> data = wsMeterReaderService.getWaterSumBySeason(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "导出用水量月份统计的数据", notes = "导出用水量月份统计的数据")
    @PostMapping("/exportWaterSumByMonth")
    public ResponseEntity<String> exportWaterSumByMonth(@Valid @RequestBody StatisticsForm form, HttpServletResponse response) throws ApiException, IOException {
        String year;
        if(StringUtils.isBlank(form.getYear())){
            year = DateFormatUtils.format(new Date(), "yyyy");
        }else{
            year = form.getYear();
        }
        wsMeterReaderService.exportWaterSumByMonth(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn(),response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @ApiOperation(value = "导出用水量季度统计的数据", notes = "导出用水量季度统计的数据")
    @PostMapping("/exportWaterSumBySeason")
    public ResponseEntity<String> exportWaterSumBySeason(@Valid @RequestBody StatisticsForm form, HttpServletResponse response) throws ApiException, IOException {
        String year;
        if(StringUtils.isBlank(form.getYear())){
            year = DateFormatUtils.format(new Date(), "yyyy");
        }else{
            year = form.getYear();
        }
        wsMeterReaderService.exportWaterSumBySeason(year,form.getUserSn(),form.getVillageSn(),form.getMeterSn(),response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @ApiOperation(value = "导出用水量年度统计的数据", notes = "导出用水量年度统计的数据")
    @PostMapping("/exportWaterSumByYear")
    public ResponseEntity<String> exportWaterSumByYear(@Valid @RequestBody StatisticsForm form, HttpServletResponse response) throws ApiException, IOException {
        wsMeterReaderService.exportWaterSumByYear(form.getUserSn(),form.getVillageSn(),form.getMeterSn(),response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
