package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.param.WsMeterQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * <p>
 * 根据用户名查询水表设备的查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsMeterQueryByNameForm", description="根据用户名查询水表设备")
public class WsMeterQueryByNameForm extends BaseQueryForm<WsMeterQueryParam> {

    @ApiModelProperty(name = "user_name", value = "用户名")
    private String userName;

    @Min(value = 1, message = "用户标识格式不正确")
    @ApiModelProperty(name = "userId", value = "用户id")
    private String userId;
}
