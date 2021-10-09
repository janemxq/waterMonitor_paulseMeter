package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsVillage;
import lombok.*;

/**
 * <p>
 * 村庄对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsVillageQueryParam extends BaseParam<WsVillage> {


    private String villageName;

    private String address;

    private String sortId;

    private String sn;

    private String villageSn;

}
