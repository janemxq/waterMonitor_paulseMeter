package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 村庄
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
@TableName("ws_village")
@ApiModel(value="WsVillage", description="村委实体类")
public class WsVillage extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @ApiModelProperty(name = "village_name", value = "名称")
    private String villageName;

    /**
     * 地址
     */
    @ApiModelProperty(name = "address", value = "地址")
    private String address;

    /**
     * 排序字段
     */
    @ApiModelProperty(name = "sortId", value = "用于排序的字段")
    private Integer sortId;

    /**
     * 是否在线
     */
    @ApiModelProperty(name = "online", value = "是否在线0不在线1在线")
    private Integer online;

    /**
     * 村落编号
     */
    @ApiModelProperty(name = "sn", value = "村落编号")
    private String sn;
}
