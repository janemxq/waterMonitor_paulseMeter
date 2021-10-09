package com.icicles.wmms.controller;

import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.PrintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * <p>
 * 打印 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@RestController
 @Api(tags = "打印")
@RequestMapping("/print")
public class PrintController {

    private PrintService printService;

    @Autowired
    public void setPrintService(PrintService printService) {
        this.printService = printService;
    }


    @ApiOperation(value = "打印", notes = "打印凭条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "volume", value = "待付用水量", required = true, paramType = "query"),
            @ApiImplicitParam(name = "realPay", value = "缴费金额", required = true, paramType = "query"),
            @ApiImplicitParam(name = "balances", value = "账户余额", required = true, paramType = "query"),
            @ApiImplicitParam(name = "accountNum", value = "欠费金额", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户", required = true, paramType = "query")
    })
    @PostMapping(value = "")
    public ResponseEntity<Boolean> configPulse(@Param("volume") BigDecimal volume,@Param("balances") BigDecimal balances, @Param("realPay") BigDecimal realPay, @Param("accountNum") BigDecimal accountNum,@Param("userName") String userName) throws ApiException {
        return ResponseEntity.ok(printService.print(volume,balances,realPay,accountNum,userName));
    }
}
