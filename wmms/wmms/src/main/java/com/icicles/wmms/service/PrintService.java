package com.icicles.wmms.service;

import com.alibaba.fastjson.JSONObject;
import com.icicles.wmms.exception.ApiException;

import java.math.BigDecimal;

/**
 * <p>
 * 系统参数 服务类
 * </p>
 *
 * @author auto
 * @since 2020-04-27
 */
public interface PrintService {

      /**
       * 打印凭条
       *
       * @param volume
       * @throws ApiException 异常信息
       */
      boolean print(BigDecimal volume,BigDecimal balances,BigDecimal realPay,BigDecimal accountNum, String userName) throws ApiException;

}
