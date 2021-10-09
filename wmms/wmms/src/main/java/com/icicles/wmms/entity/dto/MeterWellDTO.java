package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author lisy
 */
@Data
@ApiModel(value="MeterWellDTO", description="表井")
public class MeterWellDTO {

    /**
     * 表井编号
     */
    private String meterWellSn;
    /**
     * 表井名称
     */
    private String meterWellName;

    /**
     *  表井地址
     */
    private String address;

    /**
     * 水表
     */
    private List<MeterDTO> meterDTOList;

}
