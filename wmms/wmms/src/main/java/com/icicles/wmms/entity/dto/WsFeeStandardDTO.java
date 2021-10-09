package com.icicles.wmms.entity.dto;

import com.icicles.wmms.entity.po.WsFeeStandard;
import com.icicles.wmms.entity.po.WsFeeStandardItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
/**
 * @author lisy
 */
@ApiModel(value="WsFeeStandardDTO", description="收费标准实体说明")
@EqualsAndHashCode(callSuper = true)
@Data
public class WsFeeStandardDTO extends WsFeeStandard {
    /**
     * 收费标准子项
     */
    @ApiModelProperty(name = "items", value = "收费标准子项")
    private List<WsFeeStandardItem> items;
}
