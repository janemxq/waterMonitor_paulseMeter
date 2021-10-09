package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author lisy
 */
@Data
@ApiModel(value="PcTerminalDTO", description="PC端")
public class PcTerminalDTO {

    /**
     * 源地址
     */
    private String fromAddress;

    /**
     *  lora环境是否测试
     */
    private String isTest;

    /**
     *  串口名
     */
    private String commName;

    /**
     *  串口通信频率
     */
    private String baudrate;

    /**
     *  帧头
     */
    private String header;

}
