package com.icicles.wmms.entity.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icicles.wmms.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 水表读数
 * </p>
 *
 * @author 
 * @since 2020-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ws_meter_reader")
@ApiModel(value="WsMeterReader", description="水表读数（用水记录）实体类")
public class WsMeterReader extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "user_id", value = "用户id")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(name = "user_name", value = "用户名")
    private String userName;

    /**
     * 设备id
     */
    @ApiModelProperty(name = "meter_id", value = "设备id")
    private Long meterId;

    /**
     * 设备标识
     */
    @ApiModelProperty(name = "meter_sn", value = "设备标识")
    private String meterSn;

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
     * 累计用水量
     */
    @ApiModelProperty(name = "num", value = "累计用水量")
    private String num;


    /**
     * 组（表井）id
     */
    @ApiModelProperty(name = "group_id", value = "组（表井）id")
    private Long groupId;

    /**
     * 组（表井）名称
     */
    @ApiModelProperty(name = "group_name", value = "组（表井）名称")
    private String groupName;

    /**
     * 码盘值
     */
    @ApiModelProperty(name = "volume", value = "码盘值")
    private String volume;
    /**
     * 表井标识
     */
    @ApiModelProperty(name = "groupSn", value = "表井标识")
    private String groupSn;

    /**
     * 用户标识
     */
    @ApiModelProperty(name = "user_sn", value = "用户标识")
    private String userSn;
    
    /**
     * 村标识
     */
    @ApiModelProperty(name = "village_sn", value = "村标识")
    private String villageSn;

    /**
     * 本次用水量
     */
    @ApiModelProperty(name = "useWater", value = "本次用水量")
    private BigDecimal useWater;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "phone", value = "手机号")
    @TableField(exist = false)
    private String phone;
}
