package com.icicles.wmms.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 同步数据的模板类
 * @author lisy
 */
@Data
public class SynDataDTO<T> {
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 签名
     */
    private String sign;
    /**
     * 数据
     */
    private List<T> data;
}
