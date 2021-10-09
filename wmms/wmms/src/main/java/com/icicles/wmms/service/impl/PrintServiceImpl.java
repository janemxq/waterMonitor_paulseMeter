package com.icicles.wmms.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.icicles.wmms.entity.dto.PcTerminalDTO;
import com.icicles.wmms.entity.po.SysParam;
import com.icicles.wmms.entity.po.WsGroup;
import com.icicles.wmms.entity.po.WsMeter;
import com.icicles.wmms.entity.po.WsMeterReader;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.exception.CustomException;
import com.icicles.wmms.service.*;
import com.icicles.wmms.utils.CommixUtil;
import com.icicles.wmms.utils.PrintTest;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.print.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 打印 服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-04-27
 */
@Service
public class PrintServiceImpl implements PrintService {

    private static final Logger logger = LoggerFactory.getLogger(PrintServiceImpl.class);

    @Override
    @Transactional
    public boolean print(BigDecimal volume, BigDecimal balances,BigDecimal realPay,BigDecimal accountNum, String userName) throws ApiException {
        try {
            if(volume==null){
                volume = BigDecimal.valueOf(0);
            }
            if(balances==null){
                balances = BigDecimal.valueOf(0);
            }
            if(realPay==null){
                realPay = BigDecimal.valueOf(0);
            }
            if(accountNum==null){
                accountNum = BigDecimal.valueOf(0);
            }
            //    通俗理解就是书、文档
            Book book = new Book();
            //    设置成竖打
            PageFormat pf = new PageFormat();
            pf.setOrientation(PageFormat.PORTRAIT);
            //    通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
            Paper p = new Paper();
            p.setSize(580,830);//纸张大小
            p.setImageableArea(0,0, 580,830);//A4(595 X 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
            pf.setPaper(p);
            Graphics gra;
            //    把 PageFormat 和 Printable 添加到书中，组成一个页面
            cn.hutool.json.JSONObject jsonObject = new JSONObject();
            jsonObject.set("water",volume==null?"0":volume.toString());
            jsonObject.set("pay",realPay==null?"0":realPay.toString());
            jsonObject.set("must",accountNum==null?"0":accountNum.toString());
            jsonObject.set("balances",realPay.add(balances).subtract(accountNum));
            jsonObject.set("name",userName);
            jsonObject.set("time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            book.append(new PrintTest(jsonObject), pf);

            //获取打印服务对象
            PrinterJob job = PrinterJob.getPrinterJob();
            // 设置打印类
            job.setPageable(book);
            try {
                //可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印
                //boolean a=job.printDialog();
                //if(a)
                //{
                job.print();
                //}
            } catch (PrinterException e) {
                e.printStackTrace();
            }
            logger.debug("打印凭条成功");
            return true;
        } catch (Exception e) {
            logger.error("打印凭条异常", e);
            e.printStackTrace();
            throw new ApiException("打印凭条异常", HttpStatus.BAD_REQUEST);
        }
    }
}
