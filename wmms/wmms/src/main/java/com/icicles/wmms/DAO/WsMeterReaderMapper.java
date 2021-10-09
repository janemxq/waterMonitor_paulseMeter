package com.icicles.wmms.DAO;

import com.icicles.wmms.entity.dto.WaterStatisticesDTO;
import com.icicles.wmms.entity.po.WsMeterReader;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 水表读数 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-06-06
 */
public interface WsMeterReaderMapper extends BaseMapper<WsMeterReader> {

    /**
     * 查询年用水量，userSn为空时查询平台用水量
     * @param userSn 用户标识
     * @return
     */
    List<WaterStatisticesDTO> getWaterSumByYear(@Param("userSn") String userSn,@Param("villageSn") String villageSn,@Param("meterSn") String meterSn);

    /**
     * 查询月用水量，userSn为空时查询平台用水量
     * @param userSn 用户标识
     * @return
     */
    List<WaterStatisticesDTO> getWaterSumByMonth(@Param("year") String year,@Param("userSn") String userSn,@Param("villageSn") String villageSn,@Param("meterSn") String meterSn);

    /**
     * 查询季度用水量，userSn为空时查询平台用水量
     * @param userSn 用户标识
     * @return
     */
    List<WaterStatisticesDTO> getWaterSumBySeason(@Param("year") String year,@Param("userSn") String userSn,@Param("villageSn") String villageSn,@Param("meterSn") String meterSn);
}
