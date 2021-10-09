package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用于统计很长时间不缴费的用户
 * @author lisy
 */
@Data
@ApiModel(value="未缴费的查询结果", description="未缴费的查询结果实体类")
public class PayDataExceptionDTO {
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
     * 码盘值
     */
    @ApiModelProperty(name = "meterNum", value = "码盘值")
    private String meterNum;
    /**
     * 总的用水量
     */
    @ApiModelProperty(name = "waterSum", value = "总的用水量")
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
     * 多长时间没有缴费了
     */
    @ApiModelProperty(name = "howLong", value = "长时间没有缴费了（天）")
    private String howLong;

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
    /**
     * 表井名
     */
    @ApiModelProperty(name = "groupName", value = "表井名")
    private String  groupName;
}
