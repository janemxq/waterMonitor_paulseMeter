package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.SysRole;
import com.icicles.wmms.service.SysRoleService;
import com.icicles.wmms.entity.form.SysRoleForm;
import com.icicles.wmms.entity.form.SysRoleQueryForm;
import com.icicles.wmms.entity.param.SysRoleQueryParam;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-09
 */
@RestController
 @Api(tags = "角色")
@RequestMapping("/sysRole")
public class SysRoleController {

    private SysRoleService sysRoleService;

    @Autowired
    public void setSysRoleService(SysRoleService sysRoleService) {
       this.sysRoleService = sysRoleService;
    }

    @ApiOperation(value = "新增角色", notes = "新增角色")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody SysRoleForm sysRoleForm,@ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        SysRole role = sysRoleForm.toPo(SysRole.class);
        role.setUpdateUserId(securityLoginUserSn);
        sysRoleService.add(role);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        sysRoleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新角色", notes = "更新角色")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "long"),
        @ApiImplicitParam(name = "sysRoleForm", value = "角色实体", required = true, dataType = "SysRoleForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody SysRoleForm sysRoleForm,@ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        SysRole role = sysRoleForm.toPo(id,SysRole.class);
        role.setUpdateUserId(securityLoginUserSn);
        sysRoleService.refresh(role);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="获取角色", notes="获取指定角色")
    @ApiImplicitParam(paramType = "path", name = "id", value = "角色ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SysRole> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(sysRoleService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有角色", notes = "查询所有角色")
    @ApiResponses(
        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/all")
    public ResponseEntity<List<SysRole>> queryAll() throws ApiException {
        List<SysRole> sysRoleList = sysRoleService.list();
        return new ResponseEntity(sysRoleList, HttpStatus.OK);
    }

    @ApiOperation(value = "查询账号下角色", notes = "查询账号下角色")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/findByAccount")
    public ResponseEntity<List<SysRole>> findByAccount(Principal principal) throws ApiException {
        List<SysRole> sysRoleList = sysRoleService.findByAccount(principal.getName());
        return new ResponseEntity(sysRoleList, HttpStatus.OK);
    }

    @ApiOperation(value = "搜索角色(分页)", notes = "根据条件搜索角色")
    @ApiImplicitParam(name = "sysRoleQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "SysRoleQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<SysRole>> search(@Valid @RequestBody SysRoleQueryForm sysRoleQueryForm) throws ApiException {
        IPage<SysRole> page = sysRoleService.findPage(sysRoleQueryForm.getPage(),sysRoleQueryForm.toParam(SysRoleQueryParam.class));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
