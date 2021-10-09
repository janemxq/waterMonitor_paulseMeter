package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="通用的执行结果", description="通用的执行结果实体类")
public class CommonResultShowDTO {
    @ApiModelProperty(name = "sum", value = "总记录")
    private Integer sum = 0;
    @ApiModelProperty(name = "error", value = "失败数量")
    private Integer error = 0;
    public CommonResultShowDTO(){}
    public CommonResultShowDTO(Integer sum,Integer error){
        this.sum = sum;
        this.error = error;
    }
    public static CommonResultShowDTO build(Integer sum,Integer error){
        return new CommonResultShowDTO(sum,error);
    }
}
