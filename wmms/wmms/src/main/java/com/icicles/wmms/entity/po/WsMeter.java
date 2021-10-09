package com.icicles.wmms.entity.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.diboot.core.binding.annotation.BindField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 水表设备
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
@TableName("ws_meter")
@ApiModel(value="WsMeter", description="水表实体类")
public class WsMeter extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(name = "sn", value = "水表编号")
    private String sn;

    /**
     * 水表初始值
     */
    @ApiModelProperty(name = "meter_init", value = "水表初始值")
    private BigDecimal meterInit;

    /**
     * 用户名
     */
    @BindField(entity= SysUser.class, field="name", condition="user_id=id")
    @ApiModelProperty(name = "user_name", value = "用户名")
    private String userName;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "user_id", value = "用户id")
    private Integer userId;

    /**
     * 村名
     */
    @ApiModelProperty(name = "village_name", value = "村名")
    private String villageName;

    /**
     * 村id
     */
    @ApiModelProperty(name = "village_id", value = "村id")
    private Integer villageId;

    /**
     * 组（表井）id
     */
    @ApiModelProperty(name = "group_id", value = "组（表井）id")
    private Integer groupId;

    /**
     * 组（表井）名称
     */
    @ApiModelProperty(name = "group_name", value = "组（表井）名称")
    private String groupName;

    /**
     * 是否在线
     */
    @ApiModelProperty(name = "online", value = "是否在线：0不在线1在线")
    private Integer online;

    /**
     * 脉冲（多少脉冲对应 1 方水）
     */
    @ApiModelProperty(name = "pulse", value = "脉冲")
    private String pulse;

    /**
     * 唯一标识
     */
    @ApiModelProperty(name = "mac_sn", value = "表井通道号")
    private String macSn;

    /**
     * 用水类型
     */
    @ApiModelProperty(name = "meter_type", value = "用水类型（新增或者更新时，从接口“/wsFeeStandard/all”中获取其对应的id。显示时直接使用meterTypeName）")
    private Integer meterType;

    /**
     * 累计用水量
     */
    @ApiModelProperty(name = "meter_num", value = "累计用水量")
    private BigDecimal meterNum;

    /**
     * 码盘值
     */
    @ApiModelProperty(name = "water_sum", value = "码盘值")
    private BigDecimal waterSum;

    /**
     * 水表最新抄表时间
     */
    @ApiModelProperty(name = "read_time", value = "水表最新抄表时间")
    private String readTime;

    /**
     * 设备启用时间
     */
    @ApiModelProperty(name = "activation_time", value = "设备启用时间")
    private String activationTime;

    /**
     * 表井唯一标识
     */
    @ApiModelProperty(name = "group_sn", value = "表井唯一标识")
    private String groupSn;

    /**
     * 用户唯一标识
     */
    @ApiModelProperty(name = "user_sn", value = "用户唯一标识")
    private String userSn;
    /**
     * 收费标准的唯一标识
     */
    @ApiModelProperty(name = "meter_type_sn", value = "收费标准的唯一标识")
    private String meterTypeSn;

    /**
     * 村庄的唯一标识
     */
    @ApiModelProperty(name = "village_sn", value = "村庄的唯一标识")
    private String villageSn;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 用于余额的乐观锁，没有实际意义
     */
    @ApiModelProperty(name = "balanceVersion", value = "用于余额的乐观锁，没有实际意义，前端不用理会")
    private Integer balanceVersion;

    /**
     * 余额
     */
    @ApiModelProperty(name = "balance", value = "余额")
    private BigDecimal balance;

    /**
     * 脉冲初始值（初始化水表时脉冲的值）
     */
    @ApiModelProperty(name = "pulseInit", value = "脉冲初始值（初始化水表时脉冲的值）")
    private Integer pulseInit;
}
