package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 同步记录
 * </p>
 *
 * @author 
 * @since 2020-07-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ws_syn")
public class WsSyn extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 数据类型（class name）
     */
    private String dataType;

    /**
     * 对应的数据记录的id
     */
    private Long branchId;


}
