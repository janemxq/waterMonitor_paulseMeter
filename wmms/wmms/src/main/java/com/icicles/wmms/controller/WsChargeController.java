package com.icicles.wmms.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.entity.po.WsCharge;
import com.icicles.wmms.service.WsChargeService;
import com.icicles.wmms.entity.form.WsChargeForm;
import com.icicles.wmms.entity.form.WsChargeQueryForm;
import com.icicles.wmms.entity.param.WsChargeQueryParam;
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
 * 用户充值记录 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-07-02
 */
@RestController
@Api(tags = "用户充值记录(新)")
@RequestMapping("/wsCharge")
public class WsChargeController {

    private WsChargeService wsChargeService;

    @Autowired
    public void setWsChargeService(WsChargeService wsChargeService) {
       this.wsChargeService = wsChargeService;
    }
//
//    @ApiOperation(value = "新增用户充值记录", notes = "新增用户充值记录")
//    @PostMapping
//    public ResponseEntity<HttpStatus> add(@Validated @RequestBody WsChargeForm wsChargeForm) throws ApiException {
//        wsChargeService.add(wsChargeForm.toPo(WsCharge.class));
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value = "删除用户充值记录", notes = "删除用户充值记录")
//    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "long")
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> delete(@PathVariable String id) throws ApiException {
//        wsChargeService.delete(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @ApiOperation(value = "更新用户充值记录", notes = "更新用户充值记录")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "id", value = "用户充值记录ID", required = true, dataType = "long"),
//        @ApiImplicitParam(name = "wsChargeForm", value = "用户充值记录实体", required = true, dataType = "WsChargeForm")
//    })
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<HttpStatus> update(@PathVariable Long id,@Validated @RequestBody WsChargeForm wsChargeForm) throws ApiException {
//        wsChargeService.refresh(wsChargeForm.toPo(id,WsCharge.class));
//        return ResponseEntity.ok().build();
//    }

    @ApiOperation(value="获取用户充值记录(新)", notes="获取指定用户充值记录")
    @ApiImplicitParam(paramType = "path", name = "id", value = "用户充值记录ID", required = true, dataType = "long")
    @GetMapping(value = "/{id}")
    public ResponseEntity<WsCharge> get(@PathVariable String id) throws ApiException {
        return new ResponseEntity(wsChargeService.findById(id), HttpStatus.OK);
    }

//    @ApiOperation(value = "查询所有用户充值记录", notes = "查询所有用户充值记录")
//    @ApiResponses(
//        @ApiResponse(code = 200, message = "处理成功", response = JSON.class)
//    )
//    @GetMapping(value = "/all")
//    public ResponseEntity<List<WsCharge>> queryAll() throws ApiException {
//        List<WsCharge> wsChargeList = wsChargeService.list();
//        return new ResponseEntity(wsChargeList, HttpStatus.OK);
//    }

    @ApiOperation(value = "搜索用户充值记录(分页)(新)", notes = "根据条件搜索用户充值记录")
    @ApiImplicitParam(name = "wsChargeQueryForm", value = "{table.comment!}查询参数", required = true, dataType = "WsChargeQueryForm")
    @PostMapping("/page")
    public ResponseEntity<IPage<WsCharge>> search(@Valid @RequestBody WsChargeQueryForm wsChargeQueryForm,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        WsChargeQueryParam chargeQueryParam = wsChargeQueryForm.toParam(WsChargeQueryParam.class);
        if(StringUtils.isBlank(chargeQueryParam.getVillageSn())&&StringUtils.isNotBlank(securityLoginVillage)){
            chargeQueryParam.setVillageSn(securityLoginVillage);
        }
        IPage<WsCharge> page = wsChargeService.findPage(wsChargeQueryForm.getPage(),chargeQueryParam);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
