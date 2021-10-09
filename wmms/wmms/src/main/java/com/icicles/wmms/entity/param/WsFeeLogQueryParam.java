package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsFeeLog;
import lombok.*;

/**
 * <p>
 * 缴费记录对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsFeeLogQueryParam extends BaseParam<WsFeeLog> {

    private String phone;

    private String payStatus;

    private String userId;

    private String userName;

    private String meterSn;

    private String userSn;

    private String villageSn;
}
