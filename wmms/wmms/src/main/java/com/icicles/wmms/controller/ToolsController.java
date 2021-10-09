package com.icicles.wmms.controller;

import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.impl.SysDataClient;
import com.icicles.wmms.utils.WebDateUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 对外提供的工具
 * @author lisy
 */
@RestController
@Api(tags = "对外提供的工具")
@RequestMapping("/wmms/tools")
public class ToolsController {
    @Resource
    private SysDataClient sysDataClient;

    @ApiOperation(value = "获取网络时间", notes = "获取网络时间")
    @GetMapping("/webDate")
    public ResponseEntity<String> getWebDate() throws ApiException {
        return new ResponseEntity<>(WebDateUtils.getWebDateString(),HttpStatus.OK);
    }

    @ApiOperation(value = "保存同步过来的数据", notes = "保存同步过来的数据")
    @PostMapping("/save")
    public ResponseEntity<String> js(@RequestBody String data) throws ApiException {
        sysDataClient.save(data);
        return new ResponseEntity<>("success",HttpStatus.OK);
    }
    @ApiOperation(value = "测试用的", notes = "测试用的")
    @GetMapping("/test")
    public ResponseEntity<String> test(HttpServletResponse response) throws Exception {

        return new ResponseEntity<>("success",HttpStatus.OK);
    }
}
