package com.icicles.wmms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.icicles.wmms.entity.dto.PayDataExceptionDTO;
import com.icicles.wmms.entity.form.FeeExceptionForm;
import com.icicles.wmms.entity.form.NopayUsersForm;
import com.icicles.wmms.entity.po.WsFeeLog;
import com.icicles.wmms.entity.po.WsMeterReader;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.DataExceptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 查询缴费或者用水异常
 * @author lisy
 */
@RestController
@Api(tags = "查询缴费或者用水异常")
@RequestMapping("/DataException")
public class DataExceptionController {
    @Resource
    private DataExceptionService dataExceptionService;

    @ApiOperation(value = "查询缴费异常（缴费记录中小于或者大于某个阈值的记录）", notes = "查询缴费异常：缴费记录中小于或者大于某个阈值的记录")
    @PostMapping("/getFeeException")
    public ResponseEntity<IPage<WsFeeLog>> getFeeException(@Validated @RequestBody FeeExceptionForm feeExceptionForm,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        if(StringUtils.isBlank(feeExceptionForm.getVillageSn())&&StringUtils.isNotBlank(securityLoginVillage)){
            //如果用户没有上传村的标识，并且当前登录人为某个村的管理员，只查询该村的记录
            feeExceptionForm.setVillageSn(securityLoginVillage);
        }
        IPage<WsFeeLog> data = dataExceptionService.getFeeException(feeExceptionForm);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "查询用水异常（水表读数记录中小于或者大于某个阈值的记录）", notes = "查询用水异常：水表读数记录中小于或者大于某个阈值的记录")
    @PostMapping("/getWaterCostException")
    public ResponseEntity<IPage<WsMeterReader>> getWaterCostException(@Validated @RequestBody FeeExceptionForm feeExceptionForm,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        if(StringUtils.isBlank(feeExceptionForm.getVillageSn())&&StringUtils.isNotBlank(securityLoginVillage)){
            //如果用户没有上传村的标识，并且当前登录人为某个村的管理员，只查询该村的记录
            feeExceptionForm.setVillageSn(securityLoginVillage);
        }
        IPage<WsMeterReader> data = dataExceptionService.getWaterCostException(feeExceptionForm);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "查询一定时间内没有缴费的用户（查欠费情况）", notes = "查询一定时间内没有缴费的用户，即查询欠费情况")
    @PostMapping("/getNopayUsers")
    public ResponseEntity<IPage<PayDataExceptionDTO>> getNopayUsers(@Validated @RequestBody NopayUsersForm nopayUsersForm,@ApiIgnore @RequestAttribute String securityLoginVillage) throws ApiException {
        if(StringUtils.isBlank(nopayUsersForm.getVillageSn())&&StringUtils.isNotBlank(securityLoginVillage)){
            //如果用户没有上传村的标识，并且当前登录人为某个村的管理员，只查询该村的记录
            nopayUsersForm.setVillageSn(securityLoginVillage);
        }
        IPage<PayDataExceptionDTO> data = dataExceptionService.getNopayUsers(nopayUsersForm);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
