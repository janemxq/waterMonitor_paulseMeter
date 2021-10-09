package com.icicles.wmms.entity.dto;

import com.icicles.wmms.entity.po.WsMeter;
import lombok.Data;

import java.util.List;
@Data
public class GroupMetersDTO {
    /**
     * 组（表井）id
     */
    private Long groupId;
    /**
     * 组（表井）名称
     */
    private String groupName;
    /**
     * 组（表井）包含的设备
     */
    private List<WsMeter> meters;
}
