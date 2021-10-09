package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.SysMenu;
import com.icicles.wmms.service.SysMenuService;
import com.icicles.wmms.entity.form.SysMenuForm;
import com.icicles.wmms.entity.form.SysMenuQueryForm;
import com.icicles.wmms.entity.param.SysMenuQueryParam;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.security.Principal;
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
@Api(tags = "系统菜单")
@RequestMapping("/sysMenu")
public class SysMenuController {

    private SysMenuService sysMenuService;

    @Autowired
    public void setSysMenuService(SysMenuService sysMenuService) {
       this.sysMenuService = sysMenuService;
    }

    @ApiOperation(value = "新增菜单", notes = "新增菜单：菜单上级没有菜单时，上传-1")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody SysMenuForm sysMenuForm) throws ApiException {
        sysMenuService.add(sysMenuForm.toPo(SysMenu.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        sysMenuService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新", notes = "更新")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "long"),
        @ApiImplicitParam(name = "sysMenuForm", value = "实体", required = true, dataType = "SysMenuForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody SysMenuForm sysMenuForm) throws ApiException {
        sysMenuService.refresh(sysMenuForm.toPo(id,SysMenu.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="获取", notes="获取指定")
    @ApiImplicitParam(paramType = "path", name = "id", value = "ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SysMenu> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(sysMenuService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "登录用户获取菜单", notes = "登录用户获取菜单，返回树形结构数据，分页不会起作用")
    @PostMapping("/menus")
    public ResponseEntity<List<SysMenu>> menus(@Valid @RequestBody SysMenuQueryForm sysMenuQueryForm, Principal principal) throws ApiException {
        List<SysMenu> page = sysMenuService.menusTree(sysMenuQueryForm,principal);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value = "菜单列表(包含分页)", notes = "菜单列表，有分页，可以根据条件搜索")
    @PostMapping("/page")
    public ResponseEntity<IPage<SysMenu>> search(@Valid @RequestBody SysMenuQueryForm sysMenuQueryForm) throws ApiException {
        IPage<SysMenu> page = sysMenuService.findPage(sysMenuQueryForm.getPage(),sysMenuQueryForm.toParam(SysMenuQueryParam.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value="根据角色id获取菜单，用于角色和菜单绑定管理", notes="根据角色id获取菜单，用于角色和菜单绑定管理。修改和新增都调用/sysRoleMenus/save即可")
    @ApiImplicitParam(paramType = "path", name = "roleId", value = "角色id", required = true, dataType = "String")
    @GetMapping(value = "/menusTreeByRoleId/{roleId}")
    public ResponseEntity<List<SysMenu>> menusTreeByRoleId(@PathVariable String roleId,Principal principal) throws ApiException {
        List<SysMenu> data = sysMenuService.menusTreeByRoleId(roleId,principal);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
