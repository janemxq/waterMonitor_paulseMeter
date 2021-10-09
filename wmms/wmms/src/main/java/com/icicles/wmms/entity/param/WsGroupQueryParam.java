package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsGroup;
import lombok.*;

/**
 * <p>
 * 表井对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsGroupQueryParam extends BaseParam<WsGroup> {


    private String groupName;

    private String villageId;

    private String villageName;

    private String villageSn;

    private String address;

    private String sn;
}
