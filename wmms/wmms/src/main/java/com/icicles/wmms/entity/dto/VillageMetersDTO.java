package com.icicles.wmms.entity.dto;
import lombok.Data;

import java.util.List;

/**
 * @author lisy
 */
@Data
public class VillageMetersDTO {
    /**
     * 村id
     */
    private Long villageId;
    /**
     * 村名称
     */
    private String villageName;
    /**
     * 村包含的组（表井）
     */
    private List<GroupMetersDTO> villageMeters;
}
