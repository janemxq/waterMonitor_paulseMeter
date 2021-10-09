package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.icicles.wmms.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * pad端水表信息
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pad_meter_info")
@ApiModel(value="pad端水表设备信息", description="pad端水表设备对应的实体类")
public class PadMeterInfo extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 表井标识（pad_meter_yard表sn）
     */
    @ApiModelProperty(name = "meterYardSn", value = "表井标识")
    private String meterYardSn;

    /**
     * 表井id（pad_meter_yard表id）
     */
    @ApiModelProperty(name = "meterYardId", value = "表井id")
    private Long meterYardId;

    /**
     * 水表通道号
     */
    @ApiModelProperty(name = "meterMac", value = "水表通道号")
    private String meterMac;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 总脉冲
     */
    @ApiModelProperty(name = "pulseSum", value = "总的脉冲")
    private Integer pulseSum;

    /**
     * 总的用水量
     */
    @ApiModelProperty(name = "waterSum", value = "总的用水量")
    private Integer waterSum;

    /**
     * 码盘值
     */
    @ApiModelProperty(name = "meterNum", value = "码盘值")
    private Integer meterNum;

    /**
     * 最后获取数据的时间
     */
    @ApiModelProperty(name = "getDataTime", value = "最后获取数据的时间")
    private String getDataTime;

    /**
     * 水表的唯一标识
     */
    @ApiModelProperty(name = "sn", value = "水表的唯一标识")
    private String sn;

    /**
     * 表井地址
     */
    @ApiModelProperty(name = "yardAddress", value = "表井地址")
    private String yardAddress;
}
