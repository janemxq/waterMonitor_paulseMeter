package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计欠费用户
 * @author lisy
 */
@Data
@ApiModel(value="统计欠费用户", description="统计欠费用户结果实体类")
public class NopayInfoExceptionDTO {
    /**
     * 用户名
     */
    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;
    /**
     * 水表编号
     */
    @ApiModelProperty(name = "meterSn", value = "水表编号")
    private String meterSn;
    /**
     * 累计用水量
     */
    @ApiModelProperty(name = "meterNum", value = "累计用水量")
    private String meterNum;
    /**
     * 码盘值
     */
    @ApiModelProperty(name = "waterSum", value = "码盘值")
    private String waterSum;
    /**
     * 手机号
     */
    @ApiModelProperty(name = "phone", value = "手机号")
    private String phone;
    /**
     * 未交费水量
     */
    @ApiModelProperty(name = "volume", value = "未交费水量")
    private String volume;

    /**
     * 用户标识
     */
    @ApiModelProperty(name = "userSn", value = "用户标识")
    private String userSn;

    /**
     * 村庄名
     */
    @ApiModelProperty(name = "villageName", value = "村庄名")
    private String villageName;
}
