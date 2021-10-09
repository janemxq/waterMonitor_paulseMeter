package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author lisy
 */
@Data
@ApiModel(value="MeterDTO", description="水表")
public class MeterDTO {

    /**
     * 水表编号
     */
    private String meterSn;

    /**
     *  水表通道
     */
    private String address;

    /**
     *  水表累计水量
     */
    private String val;

    /**
     *  水表通道
     */
    private String pulse;


    /**
     *  水表码盘值
     */
    private String codeVal;

}
