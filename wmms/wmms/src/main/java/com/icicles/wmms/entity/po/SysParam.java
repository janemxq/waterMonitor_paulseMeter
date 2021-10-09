package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统参数
 * </p>
 *
 * @author auto
 * @since 2020-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sys_param")
public class SysParam extends BasePo {

    private static final long serialVersionUID = 1L;

    private String name;

    private String configKey;

    private String configValue;

    private Integer isValid;

    private Integer serial;

    private String remark;
}
