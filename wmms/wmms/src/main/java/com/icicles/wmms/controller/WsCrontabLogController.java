package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.WsCrontabLog;
import com.icicles.wmms.service.WsCrontabLogService;
import com.icicles.wmms.entity.form.WsCrontabLogForm;
import com.icicles.wmms.entity.form.WsCrontabLogQueryForm;
import com.icicles.wmms.entity.param.WsCrontabLogQueryParam;
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
 * 定时任务执行日志 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-09-04
 */
@RestController
 @Api(tags = "自动执行的任务日志")
@RequestMapping("/wsCrontabLog")
public class WsCrontabLogController {

    private WsCrontabLogService wsCrontabLogService;

    @Autowired
    public void setWsCrontabLogService(WsCrontabLogService wsCrontabLogService) {
       this.wsCrontabLogService = wsCrontabLogService;
    }

//    @ApiOperation(value = "新增定时任务执行日志", notes = "新增定时任务执行日志")
//    @PostMapping
//    public ResponseEntity<HttpStatus> add(@Validated @RequestBody WsCrontabLogForm wsCrontabLogForm) throws ApiException {
//        wsCrontabLogService.add(wsCrontabLogForm.toPo(WsCrontabLog.class));
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value = "删除定时任务执行日志", notes = "删除定时任务执行日志")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
//        wsCrontabLogService.delete(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value = "更新定时任务执行日志", notes = "更新定时任务执行日志")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "id", value = "定时任务执行日志ID", required = true, dataType = "long"),
//        @ApiImplicitParam(name = "wsCrontabLogForm", value = "定时任务执行日志实体", required = true, dataType = "WsCrontabLogForm")
//    })
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody WsCrontabLogForm wsCrontabLogForm) throws ApiException {
//        wsCrontabLogService.refresh(wsCrontabLogForm.toPo(id,WsCrontabLog.class));
//        return ResponseEntity.ok().build();
//    }

    @ApiOperation(value="获取定时任务执行日志", notes="获取指定定时任务执行日志")
    @ApiImplicitParam(paramType = "path", name = "id", value = "定时任务执行日志ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<WsCrontabLog> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(wsCrontabLogService.findById(id), HttpStatus.OK);
    }
//
//    @ApiOperation(value = "查询所有定时任务执行日志", notes = "查询所有定时任务执行日志")
//    @ApiResponses(
//        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
//    )
//    @GetMapping(value = "/all")
//    public ResponseEntity<List<WsCrontabLog>> queryAll() throws ApiException {
//        List<WsCrontabLog> wsCrontabLogList = wsCrontabLogService.list();
//        return new ResponseEntity(wsCrontabLogList, HttpStatus.OK);
//    }

    @ApiOperation(value = "搜索定时任务执行日志(分页)", notes = "根据条件搜索定时任务执行日志")
    @ApiImplicitParam(name = "wsCrontabLogQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "WsCrontabLogQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<WsCrontabLog>> search(@Valid @RequestBody WsCrontabLogQueryForm wsCrontabLogQueryForm) throws ApiException {
        IPage<WsCrontabLog> page = wsCrontabLogService.findPage(wsCrontabLogQueryForm.getPage(),wsCrontabLogQueryForm.toParam(WsCrontabLogQueryParam.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
