package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组（表井）
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ws_group")
@ApiModel(value="WsGroup", description="组（表井）实体类")
public class WsGroup extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 组名
     */
    @ApiModelProperty(name = "group_name", value = "组名")
    private String groupName;

    /**
     * 村id
     */
    @ApiModelProperty(name = "village_id", value = "村id")
    private Long villageId;

    /**
     * 村名
     */
    @ApiModelProperty(name = "village_name", value = "村名")
    private String villageName;
    /**
     * 表井标号
     */
    @ApiModelProperty(name = "sn", value = "表井标号")
    private String sn;

    /**
     * 表井地址
     */
    @ApiModelProperty(name = "address", value = "表井地址")
    private String address;

    /**
     * 村庄编码
     */
    @ApiModelProperty(name = "village_sn", value = "村庄编码")
    private String villageSn;
}
