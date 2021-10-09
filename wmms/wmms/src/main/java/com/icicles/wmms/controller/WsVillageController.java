package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.impl.DataQueryAuthServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.entity.po.WsVillage;
import com.icicles.wmms.service.WsVillageService;
import com.icicles.wmms.entity.form.WsVillageForm;
import com.icicles.wmms.entity.form.WsVillageQueryForm;
import com.icicles.wmms.entity.param.WsVillageQueryParam;
import io.swagger.annotations.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * <p>
 * 村庄 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@RestController
 @Api(tags = "村庄")
@RequestMapping("/wsVillage")
public class WsVillageController {

    private WsVillageService wsVillageService;

    @Resource
    private DataQueryAuthServiceImpl dataQueryAuthService;

    @Autowired
    public void setWsVillageService(WsVillageService wsVillageService) {
       this.wsVillageService = wsVillageService;
    }

    @ApiOperation(value = "新增村庄", notes = "新增村庄")
    @PostMapping
    public ResponseEntity<HttpStatus> add(@Validated @RequestBody WsVillageForm wsVillageForm) throws ApiException {
        wsVillageService.add(wsVillageForm.toPo(WsVillage.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除村庄", notes = "删除村庄")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
        wsVillageService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "更新村庄", notes = "更新村庄")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "村庄ID", required = true, dataType = "long"),
        @ApiImplicitParam(name = "wsVillageForm", value = "村庄实体", required = true, dataType = "WsVillageForm")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody WsVillageForm wsVillageForm) throws ApiException {
        wsVillageService.refresh(wsVillageForm.toPo(id,WsVillage.class));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="获取村庄", notes="获取指定村庄")
    @ApiImplicitParam(paramType = "path", name = "id", value = "村庄ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<WsVillage> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(wsVillageService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有村庄", notes = "查询所有村庄。其中online代表是否在线，显示在集管。0代表不在线1代表在线")
    @ApiResponses(
        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
    )
    @GetMapping(value = "/all")
    public ResponseEntity<List<WsVillage>> queryAll(Principal principal) throws ApiException {
        QueryWrapper<WsVillage> queryWrapper = new QueryWrapper<>();
        SysUser u = dataQueryAuthService.getUser(principal);
        if(u!=null){
            boolean role = dataQueryAuthService.isSuperAdmin(u);
            if(!role){
                queryWrapper.lambda().eq(WsVillage::getSn,u.getVillageSn());
            }
        }
        List<WsVillage> wsVillageList = wsVillageService.list(queryWrapper);
        return new ResponseEntity(wsVillageList, HttpStatus.OK);
    }

    @ApiOperation(value = "搜索村庄(分页)", notes = "根据条件搜索村庄")
    @ApiImplicitParam(name = "wsVillageQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "WsVillageQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<WsVillage>> search(@Valid @RequestBody WsVillageQueryForm wsVillageQueryForm,Principal principal) throws ApiException {
        IPage<WsVillage> page = wsVillageService.findPage(wsVillageQueryForm.getPage(),wsVillageQueryForm.toParam(WsVillageQueryParam.class),principal);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation(value = "通过excel导入村庄", notes = "通过excel导入村庄,必须是.xlsx格式的文件，大小不超过30M（文件大小的判断前端不需要做）。通过编号（sn）来确保数据的唯一性，相同sn不允许导入。测试时，编写不同sn即可。")
    @PostMapping("/importExcel")
    public ResponseEntity<ExcelResultDTO> importExcel(@RequestParam("metersFiles") @ApiParam(name = "metersFiles",value = "上传excel,必须", required = true) MultipartFile file) throws ApiException {
        ExcelResultDTO rs = wsVillageService.importExcel(file);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @ApiOperation(value = "村庄信息导出到excel", notes = "村庄信息导出到excel")
    @PostMapping("/exportExcel")
    public ResponseEntity<String> exportExcel(@Valid @RequestBody @ApiParam(name = "ids",value = "选中的记录id（用户选择需要导出的数据，不传默认导出全部数据，非必须）") List<String> ids,
                                              HttpServletResponse response) throws IOException {
        wsVillageService.exportExcel(ids,response);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
