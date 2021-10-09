package com.icicles.wmms.service.impl.excel;

import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.service.AbstractExcelDealService;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用的excel导出工具
 * @author lisy
 */
public class CommonExport<T> extends AbstractExcelDealService<T> {

    public CommonExport(List<T> data,ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp) {
        super(data, dataTemp);
    }

    @Override
    protected RowHandler createRowHandler(ExcelResultDTO rs) {
        return null;
    }

}
