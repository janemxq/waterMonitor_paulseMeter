package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.SysRoleMenus;
import com.icicles.wmms.service.SysRoleMenusService;
import com.icicles.wmms.entity.form.SysRoleMenusForm;
import com.icicles.wmms.entity.form.SysRoleMenusQueryForm;
import com.icicles.wmms.entity.param.SysRoleMenusQueryParam;
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
 *  前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@RestController
@Api(tags = "角色对应菜单")
@RequestMapping("/sysRoleMenus")
public class SysRoleMenusController {

    private SysRoleMenusService sysRoleMenusService;

    @Autowired
    public void setSysRoleMenusService(SysRoleMenusService sysRoleMenusService) {
       this.sysRoleMenusService = sysRoleMenusService;
    }

//    @ApiOperation(value = "新增", notes = "新增")
//    @PostMapping
//    public ResponseEntity<HttpStatus> add(@Validated @RequestBody SysRoleMenusForm sysRoleMenusForm) throws ApiException {
//        sysRoleMenusService.add(sysRoleMenusForm);
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
//        sysRoleMenusService.delete(id);
//        return ResponseEntity.ok().build();
//    }

    @ApiOperation(value = "添加/更新角色对应的菜单", notes = "添加/更新角色对应的菜单")
    @PostMapping("/save")
    public ResponseEntity<HttpStatus> update(@Validated @RequestBody SysRoleMenusForm sysRoleMenusForm,@ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        sysRoleMenusService.refresh(sysRoleMenusForm,securityLoginUserSn);
        return ResponseEntity.ok().build();
    }

//    @ApiOperation(value="获取", notes="获取指定")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "ID", required = true, dataType = "long")
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<SysRoleMenus> get(@PathVariable String id) throws ApiException {
//        return new ResponseEntity(sysRoleMenusService.findById(id), HttpStatus.OK);
//    }

//    @ApiOperation(value = "查询所有", notes = "查询所有")
//    @ApiResponses(
//        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
//    )
//    @GetMapping(value = "/all")
//    public ResponseEntity<List<SysRoleMenus>> queryAll() throws ApiException {
//        List<SysRoleMenus> sysRoleMenusList = sysRoleMenusService.list();
//        return new ResponseEntity(sysRoleMenusList, HttpStatus.OK);
//    }
//
//    @ApiOperation(value = "搜索(分页)", notes = "根据条件搜索")
//    @ApiImplicitParam(name = "sysRoleMenusQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "SysRoleMenusQueryForm")
//    @PostMapping("/page")
//    public ResponseEntity<IPage<SysRoleMenus>> search(@Valid @RequestBody SysRoleMenusQueryForm sysRoleMenusQueryForm) throws ApiException {
//        IPage<SysRoleMenus> page = sysRoleMenusService.findPage(sysRoleMenusQueryForm.getPage(),sysRoleMenusQueryForm.toParam(SysRoleMenusQueryParam.class));
//        return new ResponseEntity<>(page, HttpStatus.OK);
//    }
}
