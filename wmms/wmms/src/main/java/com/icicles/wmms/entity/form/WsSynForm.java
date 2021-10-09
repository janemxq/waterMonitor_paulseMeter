package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.WsSyn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 同步记录对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-07-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsSynForm", description="同步记录对外查询保存实体")
public class WsSynForm extends BaseForm<WsSyn> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "data_type", value = "数据类型（class name）||varchar(200)")
    @NotBlank(message = "data_type不能为空")
    @Length(min = 2, max = 20, message = "data_type长度在3到20个字符")
    private String dataType;


}
