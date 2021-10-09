package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsFeeStandardItem;
import lombok.*;

/**
 * <p>
 * 水表的收费标准子项对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsFeeStandardItemQueryParam extends BaseParam<WsFeeStandardItem> {


    private String feeStandardId;

    private String startNum;

    private String endNum;

    private String isStep;

    private String price;


}
