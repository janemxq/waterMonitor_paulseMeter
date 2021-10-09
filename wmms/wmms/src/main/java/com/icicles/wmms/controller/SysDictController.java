package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.icicles.wmms.entity.form.SysDictForm;
import com.icicles.wmms.entity.form.SysDictQueryForm;
import com.icicles.wmms.entity.param.SysDictQueryParam;
import com.icicles.wmms.entity.po.SysDict;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.SysDictService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 字典 前端控制器
 * </p>
 *
 * @author auto
 * @since 2020-04-28
 */
@RestController
@Api(tags = "字典")
@RequestMapping("/sysDict")
public class SysDictController {

    private SysDictService sysDictService;

    @Autowired
    public void setSysDictService(SysDictService sysDictService) {
        this.sysDictService = sysDictService;
    }

    @ApiOperation(value = "新增字典", notes = "新增字典")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody SysDictForm sysDictForm) throws ApiException {
        sysDictService.add(sysDictForm.toPo(SysDict.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除字典", notes = "删除字典")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        sysDictService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新字典", notes = "更新字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典ID", required = true, dataType = "long"),
            @ApiImplicitParam(name = "sysDictForm", value = "字典实体", required = true, dataType = "SysDictForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @Validated @RequestBody SysDictForm sysDictForm) throws ApiException {
        sysDictService.refresh(sysDictForm.toPo(id, SysDict.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "获取字典", notes = "获取指定字典")
    @ApiImplicitParam(paramType = "path", name = "id", value = "字典ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SysDict> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(sysDictService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有字典", notes = "查询所有字典")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/all")
    public ResponseEntity<List<SysDict>> queryAll() throws ApiException {
        List<SysDict> sysDictList = sysDictService.list();
        return new ResponseEntity(sysDictList, HttpStatus.OK);
    }

    @ApiOperation(value = "搜索字典(分页)", notes = "根据条件搜索字典")
    @ApiImplicitParam(name = "sysDictQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "SysDictQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<SysDict>> search(@Valid @RequestBody SysDictQueryForm sysDictQueryForm) throws ApiException {
        IPage<SysDict> page = sysDictService.findPage(sysDictQueryForm.getPage(), sysDictQueryForm.toParam(SysDictQueryParam.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
