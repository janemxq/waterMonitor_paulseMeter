package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import com.icicles.wmms.entity.dto.WsFeeStandardDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.WsFeeStandard;
import com.icicles.wmms.service.WsFeeStandardService;
import com.icicles.wmms.entity.form.WsFeeStandardForm;
import com.icicles.wmms.entity.form.WsFeeStandardQueryForm;
import com.icicles.wmms.entity.param.WsFeeStandardQueryParam;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 水表的收费标准 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-10
 */
@RestController
 @Api(tags = "水表的收费标准")
@RequestMapping("/wsFeeStandard")
public class WsFeeStandardController {

    private WsFeeStandardService wsFeeStandardService;

    @Autowired
    public void setWsFeeStandardService(WsFeeStandardService wsFeeStandardService) {
       this.wsFeeStandardService = wsFeeStandardService;
    }

    @ApiOperation(value = "新增水表的收费标准", notes = "新增水表的收费标准")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody WsFeeStandardForm wsFeeStandardForm,@ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        WsFeeStandard standard = wsFeeStandardForm.toPo(WsFeeStandard.class);
        standard.setCreateUserId(securityLoginUserSn);
        standard.setUpdateUserId(securityLoginUserSn);
        wsFeeStandardService.add(standard);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除水表的收费标准", notes = "删除水表的收费标准")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        wsFeeStandardService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新水表的收费标准", notes = "更新水表的收费标准")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "水表的收费标准ID", required = true, dataType = "long"),
        @ApiImplicitParam(name = "wsFeeStandardForm", value = "水表的收费标准实体", required = true, dataType = "WsFeeStandardForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody WsFeeStandardForm wsFeeStandardForm,@ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        WsFeeStandard standard = wsFeeStandardForm.toPo(id,WsFeeStandard.class);
        standard.setUpdateUserId(securityLoginUserSn);
        wsFeeStandardService.refresh(standard);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="获取水表的收费标准", notes="获取指定水表的收费标准")
    @ApiImplicitParam(paramType = "path", name = "id", value = "水表的收费标准ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<WsFeeStandardDTO> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(wsFeeStandardService.findDetailById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有水表的收费标准", notes = "查询所有水表的收费标准")
    @ApiResponses(
        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/all")
    public ResponseEntity<List<WsFeeStandardDTO>> queryAll() throws ApiException {
        List<WsFeeStandardDTO> wsFeeStandardList = wsFeeStandardService.getAll();
        return new ResponseEntity(wsFeeStandardList, HttpStatus.OK);
    }

    @ApiOperation(value = "搜索水表的收费标准(分页)", notes = "根据条件搜索水表的收费标准")
    @ApiImplicitParam(name = "wsFeeStandardQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "WsFeeStandardQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<WsFeeStandardDTO>> search(@Valid @RequestBody WsFeeStandardQueryForm wsFeeStandardQueryForm) throws ApiException {
        IPage<WsFeeStandardDTO> page = wsFeeStandardService.findPage(wsFeeStandardQueryForm.getPage(),wsFeeStandardQueryForm.toParam(WsFeeStandardQueryParam.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
