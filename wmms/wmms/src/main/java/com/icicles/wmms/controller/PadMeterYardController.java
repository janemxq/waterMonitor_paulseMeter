package com.icicles.wmms.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icicles.wmms.entity.dto.CommonResultShowDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.PadMeterYard;
import com.icicles.wmms.service.PadMeterYardService;
import com.icicles.wmms.entity.form.PadMeterYardForm;
import com.icicles.wmms.entity.form.PadMeterYardQueryForm;
import com.icicles.wmms.entity.param.PadMeterYardQueryParam;
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
 * pad端表井 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@RestController
 @Api(tags = "pad端表井操作")
@RequestMapping("/padMeterYard")
public class PadMeterYardController {

    private PadMeterYardService padMeterYardService;

    @Autowired
    public void setPadMeterYardService(PadMeterYardService padMeterYardService) {
       this.padMeterYardService = padMeterYardService;
    }

    @ApiOperation(value = "新增pad端表井", notes = "新增pad端表井")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody PadMeterYardForm padMeterYardForm) throws ApiException {
        padMeterYardService.add(padMeterYardForm.toPo(PadMeterYard.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除pad端表井", notes = "删除pad端表井")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        padMeterYardService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新pad端表井", notes = "更新pad端表井")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "pad端表井ID", required = true, dataType = "long"),
        @ApiImplicitParam(name = "padMeterYardForm", value = "pad端表井实体", required = true, dataType = "PadMeterYardForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody PadMeterYardForm padMeterYardForm) throws ApiException {
        padMeterYardService.refresh(padMeterYardForm.toPo(id,PadMeterYard.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="获取pad端表井", notes="获取指定pad端表井")
    @ApiImplicitParam(paramType = "path", name = "id", value = "pad端表井ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PadMeterYard> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(padMeterYardService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value="表井获取数据", notes="表井获取水表数据，一下子获取表井下所有水表的数据")
    @ApiImplicitParam(paramType = "path", name = "sn", value = "pad端表井sn", required = true, dataType = "String")
    @GetMapping(value = "/getMeterNum/{sn}")
    public ResponseEntity<CommonResultShowDTO> getYardNum(@PathVariable String sn) throws ApiException {
        CommonResultShowDTO rs = padMeterYardService.getYardNum(sn);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @ApiOperation(value="表井上传数据（暂时没有完成）", notes="提交一个或者多个表井的sn，将对应的表井的数据上传村委端")
    @PostMapping("/upload")
    public ResponseEntity<HttpStatus> getYardNum(@RequestBody @ApiParam(name = "sns",value = "表井的sn值") List<String> sns) throws ApiException {
        if(CollUtil.isEmpty(sns)){
            throw new ApiException("至少上传一个表井标识", HttpStatus.BAD_REQUEST);
        }
        padMeterYardService.upload(sns);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询所有pad端表井", notes = "查询所有pad端表井")
    @ApiResponses(
        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/all")
    public ResponseEntity<List<PadMeterYard>> queryAll() throws ApiException {
        QueryWrapper<PadMeterYard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(PadMeterYard::getId);
        List<PadMeterYard> padMeterYardList = padMeterYardService.list(queryWrapper);
        return new ResponseEntity(padMeterYardList, HttpStatus.OK);
    }

    @ApiOperation(value = "搜索pad端表井(分页)", notes = "根据条件搜索pad端表井")
    @ApiImplicitParam(name = "padMeterYardQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "PadMeterYardQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<PadMeterYard>> search(@Valid @RequestBody PadMeterYardQueryForm padMeterYardQueryForm) throws ApiException {
        IPage<PadMeterYard> page = padMeterYardService.findPage(padMeterYardQueryForm.getPage(),padMeterYardQueryForm.toParam(PadMeterYardQueryParam.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
