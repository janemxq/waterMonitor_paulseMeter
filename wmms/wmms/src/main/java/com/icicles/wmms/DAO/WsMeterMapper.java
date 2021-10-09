package com.icicles.wmms.DAO;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icicles.wmms.entity.dto.MeterFeeDTO;
import com.icicles.wmms.entity.dto.WsMeterDTO;
import com.icicles.wmms.entity.form.WsMeterQueryForm;
import com.icicles.wmms.entity.form.WsMeterSearchForm;
import com.icicles.wmms.entity.po.WsMeter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 水表设备 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
public interface WsMeterMapper extends BaseMapper<WsMeter> {
    /**
     * 分页查询内容
     * @param page      分页
     * @param wsMeterQueryForm  查询表单
     * @return
     */
    List<WsMeterDTO> getMeterList(Page<WsMeterDTO> page, @Param("wsMeterQueryForm") WsMeterQueryForm wsMeterQueryForm);
    /**
     * 分页查询内容（根据用户id去重）
     * @param page      分页
     * @param wsMeterQueryForm  查询表单
     * @return
     */
    List<WsMeter> getMeterUserList(Page<WsMeter> page, @Param("wsMeterQueryForm") WsMeterQueryForm wsMeterQueryForm);

    /**
     * 根据用户名查询设备信息
     * @param page      分页
     * @param wsMeterSearchForm  查询表单
     * @return
     */
    List<MeterFeeDTO> getMetersFeeInfo(Page<MeterFeeDTO> page, @Param("wsMeterSearchForm") WsMeterSearchForm wsMeterSearchForm,@Param("volumeBase") String base);
}
