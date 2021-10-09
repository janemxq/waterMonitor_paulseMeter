package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * excel导入结果
 * @author lisy
 */
@Data
@ApiModel(value="excel导入结果", description="excel导入结果实体类")
public class ExcelResultDTO {
    @ApiModelProperty(name = "sum", value = "导入的总数据量")
    private Integer sum = 0;
    @ApiModelProperty(name = "error", value = "失败数量")
    private Integer error = 0;
}
