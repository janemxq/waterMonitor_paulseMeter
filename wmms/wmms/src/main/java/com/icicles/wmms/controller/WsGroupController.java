package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.impl.DataQueryAuthServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.entity.po.WsGroup;
import com.icicles.wmms.service.WsGroupService;
import com.icicles.wmms.entity.form.WsGroupForm;
import com.icicles.wmms.entity.form.WsGroupQueryForm;
import com.icicles.wmms.entity.param.WsGroupQueryParam;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * <p>
 * 表井 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@RestController
 @Api(tags = "组（表井）管理")
@RequestMapping("/wsGroup")
public class WsGroupController {

    private WsGroupService wsGroupService;

    @Resource
    private DataQueryAuthServiceImpl dataQueryAuthService;

    @Autowired
    public void setWsGroupService(WsGroupService wsGroupService) {
       this.wsGroupService = wsGroupService;
    }

    @ApiOperation(value = "新增组（表井）", notes = "新增组（表井）")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody WsGroupForm wsGroupForm,@ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        WsGroup wsGroup = wsGroupForm.toPo(WsGroup.class);
        wsGroup.setCreateUserId(securityLoginUserSn);
        wsGroup.setUpdateUserId(securityLoginUserSn);
        wsGroupService.add(wsGroup);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除组（表井）", notes = "删除组（表井）")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        wsGroupService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新组（表井）", notes = "更新组（表井）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "表井ID", required = true, dataType = "long"),
        @ApiImplicitParam(name = "wsGroupForm", value = "组（表井）实体", required = true, dataType = "WsGroupForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody WsGroupForm wsGroupForm,@ApiIgnore @RequestAttribute String securityLoginUserSn) throws ApiException {
        WsGroup wsGroup = wsGroupForm.toPo(id,WsGroup.class);
        wsGroup.setUpdateUserId(securityLoginUserSn);
        wsGroupService.refresh(wsGroup);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="获取组（表井）", notes="获取指定组（表井）")
    @ApiImplicitParam(paramType = "path", name = "id", value = "组（表井）ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<WsGroup> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(wsGroupService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有组（表井）", notes = "查询所有组（表井）")
    @ApiResponses(
        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/all")
    public ResponseEntity<List<WsGroup>> queryAll(Principal principal) throws ApiException {
        QueryWrapper<WsGroup> queryWrapper = new QueryWrapper<>();
        SysUser u = dataQueryAuthService.getUser(principal);
        if(u!=null){
            boolean role = dataQueryAuthService.isSuperAdmin(u);
            if(!role){
                queryWrapper.lambda().eq(WsGroup::getVillageSn,u.getVillageSn());
            }
        }
        List<WsGroup> wsGroupList = wsGroupService.list(queryWrapper);
        wsGroupList = wsGroupService.showCreateUserName(wsGroupList);
        wsGroupList = wsGroupService.setVillageName(wsGroupList);
        return new ResponseEntity(wsGroupList, HttpStatus.OK);
    }

    @ApiOperation(value = "搜索组（表井）(分页)", notes = "根据条件搜索组（表井）")
    @ApiImplicitParam(name = "wsGroupQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "WsGroupQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<WsGroup>> search(@Valid @RequestBody WsGroupQueryForm wsGroupQueryForm,Principal principal) throws ApiException {
        IPage<WsGroup> page = wsGroupService.findPage(wsGroupQueryForm.getPage(),wsGroupQueryForm.toParam(WsGroupQueryParam.class),principal);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value = "通过excel导入表井", notes = "通过excel导入表井,必须是.xlsx格式的文件，大小不超过30M（文件大小的判断前端不需要做）。通过编号（sn）来确保数据的唯一性，相同sn不允许导入。测试时，编写不同sn即可。")
    @PostMapping("/importExcel")
    public ResponseEntity<ExcelResultDTO> importExcel(@RequestParam("metersFiles") @ApiParam(name = "metersFiles",value = "上传excel,必须", required = true) MultipartFile file) throws ApiException {
        ExcelResultDTO rs = wsGroupService.importExcel(file);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @ApiOperation(value = "表井信息导出到excel", notes = "表井信息导出到excel")
    @PostMapping("/exportExcel")
    public ResponseEntity<String> exportExcel(@Valid @RequestBody @ApiParam(name = "ids",value = "选中的记录id（用户选择需要导出的数据，不传默认导出全部数据，非必须）") List<String> ids,
                                              HttpServletResponse response) throws IOException {
        wsGroupService.exportExcel(ids,response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
