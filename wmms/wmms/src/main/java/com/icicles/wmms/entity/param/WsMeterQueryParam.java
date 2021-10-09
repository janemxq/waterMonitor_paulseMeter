package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsMeter;
import lombok.*;

/**
 * <p>
 * 水表设备对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsMeterQueryParam extends BaseParam<WsMeter> {


    private String sn;

    private String meterSum;

    private String userName;

//    private String userId;

    private String villageName;

//    private String villageId;

//    private String groupId;

    private String groupName;

    private String online;

    private String pulse;

    private String macSn;


}
