package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.icicles.wmms.entity.form.SysDictItemForm;
import com.icicles.wmms.entity.form.SysDictItemQueryForm;
import com.icicles.wmms.entity.param.SysDictItemQueryParam;
import com.icicles.wmms.entity.po.SysDictItem;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.SysDictItemService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 字典项 前端控制器
 * </p>
 *
 * @author auto
 * @since 2020-05-13
 */
@RestController
 @Api(tags = "字典项")
@RequestMapping("/sysDictItem")
public class SysDictItemController {

    protected SysDictItemService sysDictItemService;

    @Autowired
    public void setSysDictItemService(SysDictItemService sysDictItemService) {
       this.sysDictItemService = sysDictItemService;
    }

    @ApiOperation(value = "新增字典项", notes = "新增字典项")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody SysDictItemForm sysDictItemForm) throws ApiException {
        sysDictItemService.add(sysDictItemForm.toPo(SysDictItem.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除字典项", notes = "删除字典项")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        sysDictItemService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新字典项", notes = "更新字典项")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "字典项ID", required = true, dataType = "long"),
        @ApiImplicitParam(name = "sysDictItemForm", value = "字典项实体", required = true, dataType = "SysDictItemForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody SysDictItemForm sysDictItemForm) throws ApiException {
        sysDictItemService.refresh(sysDictItemForm.toPo(id,SysDictItem.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="获取字典项", notes="获取指定字典项")
    @ApiImplicitParam(paramType = "path", name = "id", value = "字典项ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SysDictItem> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(sysDictItemService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有字典项", notes = "查询所有字典项")
    @ApiResponses(
        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/all")
    public ResponseEntity<List<SysDictItem>> queryAll() throws ApiException {
        List<SysDictItem> sysDictItemList = sysDictItemService.list();
        return new ResponseEntity(sysDictItemList, HttpStatus.OK);
    }

    @ApiOperation(value = "搜索字典项(分页)", notes = "根据条件搜索字典项")
    @ApiImplicitParam(name = "sysDictItemQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "SysDictItemQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<SysDictItem>> search(@Valid @RequestBody SysDictItemQueryForm sysDictItemQueryForm) throws ApiException {
        IPage<SysDictItem> page = sysDictItemService.findPage(sysDictItemQueryForm.getPage(),sysDictItemQueryForm.toParam(SysDictItemQueryParam.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value="根据字典代码查询字典项", notes="根据字典代码查询字典项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictCode", paramType = "query", required = true, value = "字典代码")
    })
    @GetMapping({"/findByDictCode"})
    public ResponseEntity<List<SysDictItem>> findByDictCode(@Param("dictCode") String dictCode) throws ApiException {
        List<SysDictItem> sysDictItemList = sysDictItemService.findByDictCode(dictCode);
        return new ResponseEntity(sysDictItemList, HttpStatus.OK);
    }

}
