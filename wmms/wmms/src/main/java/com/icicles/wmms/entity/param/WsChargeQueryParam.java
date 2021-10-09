package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsCharge;
import lombok.*;

/**
 * <p>
 * 用户充值记录对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsChargeQueryParam extends BaseParam<WsCharge> {


    private String userSn;

    private String userName;

    private String phone;

    private String accountNum;

    private String balance;

    private String villageSn;

}
