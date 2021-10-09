package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.param.WsMeterQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 搜索设备信息、设备水费信息表单实体
 * </p>
 *
 * @author lisy
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsMeterSearchForm", description="搜索设备信息、设备水费信息表单实体")
public class WsMeterSearchForm extends BaseQueryForm<WsMeterQueryParam> {

    @ApiModelProperty(name = "user_name", value = "用户名||非必须")
    private String userName;

    @Min(value = 1, message = "用户标识格式不正确||非必须")
    @ApiModelProperty(name = "userId", value = "用户id")
    private String userId;

    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    @ApiModelProperty(name = "phone", value = "手机号||非必须")
    private String phone;

    @Min(value = 1, message = "收费类型格式不正确")
    @ApiModelProperty(name = "meter_type", value = "收费类型||非必须")
    private String meterType;

    @Min(value = 1, message = "村id")
    @ApiModelProperty(name = "village_id", value = "村id||非必须")
    private String villageId;

    @Min(value = 1, message = "组（表井）id")
    @ApiModelProperty(name = "group_id", value = "组（表井）id||非必须")
    private String groupId;

    @ApiModelProperty(name = "village_sn", value = "村的唯一标识")
    private String villageSn;
    @ApiModelProperty(name = "group_sn", value = "表井的唯一标识")
    private String groupSn;
    @ApiModelProperty(name = "user_sn", value = "用户的唯一标识||varchar(32)")
    private String userSn;
    @ApiModelProperty(name = "meter_type_sn", value = "收费标准唯一标识||varchar(32)")
    private String meterTypeSn;
}
