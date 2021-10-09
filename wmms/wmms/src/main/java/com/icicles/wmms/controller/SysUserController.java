package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import com.icicles.wmms.entity.dto.UseDefaultPwdResultDTO;
import com.icicles.wmms.entity.form.SysUserForm;
import com.icicles.wmms.entity.form.SysUserFormPwd;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.service.SysUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.form.SysUserQueryForm;
import com.icicles.wmms.entity.param.SysUserQueryParam;
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
 * 用水监测管理平台用户信息表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@RestController
@Api(tags = "用水监测管理平台用户信息表")
@RequestMapping("/sysUser")
public class SysUserController {

    private SysUserService sysUserService;

    @Autowired
    public void setSysUserService(SysUserService sysUserService) {
       this.sysUserService = sysUserService;
    }

    @ApiOperation(value = "新增用水监测管理平台用户信息表", notes = "新增用水监测管理平台用户信息表")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody SysUserForm sysUserForm) throws ApiException {
        sysUserService.add(sysUserForm.toPo(SysUser.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除用水监测管理平台用户信息表", notes = "删除用水监测管理平台用户信息表")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        sysUserService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新用水监测管理平台用户信息表", notes = "更新用水监测管理平台用户信息表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用水监测管理平台用户信息表ID", required = true, dataType = "long"),
        @ApiImplicitParam(name = "sysUserForm", value = "用水监测管理平台用户信息表实体", required = true, dataType = "SysUserForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody SysUserForm sysUserForm) throws ApiException {
        sysUserService.refresh(sysUserForm.toPo(id, SysUser.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="获取用水监测管理平台用户信息表", notes="获取指定用水监测管理平台用户信息表")
    @ApiImplicitParam(paramType = "path", name = "id", value = "用水监测管理平台用户信息表ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SysUser> get(@PathVariable String id) throws ApiException {
        SysUser user = sysUserService.findById(id);
        if(user!=null){
            //过滤敏感字段
            user.setLoginPass("");
        }
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @ApiOperation(value="获取当前登录人信息", notes="获取当前登录人信息")
    @GetMapping(value = "/userInfo")
    public ResponseEntity<SysUser> userInfo(Principal principal) throws ApiException {
        SysUser user = sysUserService.findByAccount(principal.getName());
        if(user!=null){
            //过滤敏感字段
            user.setLoginPass("");
        }
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有用水监测管理平台用户信息表", notes = "查询所有用水监测管理平台用户信息表：集管端获取全部，村委端仅获取本村数据库中的数据")
    @ApiResponses(
        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/all")
    public ResponseEntity<List<SysUser>> queryAll() throws ApiException {
        List<SysUser> sysUserList = sysUserService.getAll();
        return new ResponseEntity(sysUserList, HttpStatus.OK);
    }

    @ApiOperation(value = "搜索用水监测管理平台用户信息表(分页)", notes = "根据条件搜索用水监测管理平台用户信息表，集管端获取全部，村委端仅获取本村数据库中的数据")
    @ApiImplicitParam(name = "sysUserQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "SysUserQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<SysUser>> search(@Valid @RequestBody SysUserQueryForm sysUserQueryForm, Principal principal) throws ApiException {
        IPage<SysUser> page = sysUserService.findPage(sysUserQueryForm.getPage(), sysUserQueryForm.toParam(SysUserQueryParam.class),principal.getName());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value="判断当前登录人是否在使用默认密码", notes="判断当前登录人是否在使用默认密码，如果用户使用默认密码，isUserDefaultPwd为1，否则isUserDefaultPwd为0")
    @GetMapping(value = "/getDefaultPwd")
    public ResponseEntity<UseDefaultPwdResultDTO> getDefaultPwd(@ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        UseDefaultPwdResultDTO rs = sysUserService.checkDefaultPwd(securityLoginUserSn);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @ApiOperation(value="修改当前登录人的密码", notes="修改当前登录人的密码")
    @PostMapping("/changePwd")
    @Validated
    public ResponseEntity<HttpStatus> changePwd(@Valid @RequestBody SysUserFormPwd pwd, @ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        sysUserService.changePwd(securityLoginUserSn,pwd.getPassword());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除多个用户", notes = "前台选择多个用户，将用户id用数组的形式上传过程")
    @PostMapping("/multiDel")
    public ResponseEntity<HttpStatus> multiDel(@Valid @RequestBody @ApiParam(name = "ids",value = "选中的用户id") List<String> ids){
        sysUserService.multiDel(ids);
        return ResponseEntity.ok().build();
    }
}
