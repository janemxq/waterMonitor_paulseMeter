package com.icicles.wmms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.icicles.wmms.entity.dto.PayDataExceptionDTO;
import com.icicles.wmms.entity.form.FeeExceptionForm;
import com.icicles.wmms.entity.form.NopayUsersForm;
import com.icicles.wmms.entity.po.WsFeeLog;
import com.icicles.wmms.entity.po.WsMeterReader;

/**
 * 用水量、缴费等超过阈值而且引起的数据异常，展示给管理员查看
 * @author lisy
 */
public interface DataExceptionService {
    /**
     * 获取缴费异常
     * @param feeExceptionForm  缴费异常查询表单
     * @return 缴费记录
     */
    IPage<WsFeeLog> getFeeException(FeeExceptionForm feeExceptionForm);

    /**
     * 获取用水量异常
     * @param feeExceptionForm  缴费异常查询表单
     * @return
     */
    IPage<WsMeterReader> getWaterCostException(FeeExceptionForm feeExceptionForm);

    /**
     * 查询未交费用户(超过一定时间没有缴费的)
     * @param nopayUsersForm 缴费异常查询表单
     * @return
     */
    IPage<PayDataExceptionDTO> getNopayUsers(NopayUsersForm nopayUsersForm);
}
