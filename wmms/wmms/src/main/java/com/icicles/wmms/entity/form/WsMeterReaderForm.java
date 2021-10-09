package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.WsMeterReader;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 水表读数(用水记录)对外查询保存实体
 * </p>
 *
 * @author lisy
 * @since 2020-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsMeterReaderForm", description="水表读数对外查询保存实体")
public class WsMeterReaderForm extends BaseForm<WsMeterReader> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "user_id", value = "用户id||bigint(20)")
    @NotNull(message = "user_id不能为空")
    @Min(value = 0, message = "用户id格式不正确")
    private Long userId;

    @ApiModelProperty(name = "user_name", value = "用户名||varchar(50)")
    @NotBlank(message = "user_name不能为空")
    @Length(min = 2, max = 50, message = "user_name长度在3到20个字符")
    private String userName;

    @ApiModelProperty(name = "meter_id", value = "设备id||bigint(20)")
    @NotNull(message = "meter_id不能为空")
    @Min(value = 0, message = "meter_id格式不正确")
    private Long meterId;

    @ApiModelProperty(name = "meter_sn", value = "设备标识||varchar(80)")
    @NotBlank(message = "meter_sn不能为空")
    @Length(min = 2, max = 80, message = "meter_sn长度在2到80个字符")
    private String meterSn;

    @ApiModelProperty(name = "village_id", value = "村id||bigint(20)")
    @NotNull(message = "village_id不能为空")
    @Min(value = 0, message = "village_id格式不正确")
    private Long villageId;

    @ApiModelProperty(name = "village_name", value = "村名||varchar(50)")
    @NotBlank(message = "village_name不能为空")
    @Length(min = 2, max = 50, message = "village_name长度在2到50个字符")
    private String villageName;


    @ApiModelProperty(name = "num", value = "水表读数||decimal(15,2)")
    @NotNull(message = "水表读数不能为空")
    @Min(value = 0, message = "水表读数大于等于0")
    private BigDecimal num;
}
