package com.icicles.wmms.DAO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.icicles.wmms.entity.dto.WsFeeStandardDTO;
import com.icicles.wmms.entity.param.WsFeeStandardQueryParam;
import com.icicles.wmms.entity.po.WsFeeStandard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 水表的收费标准 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-06-10
 */
public interface WsFeeStandardMapper extends BaseMapper<WsFeeStandard> {
    /**
     * 分页查询
     * @param page
     * @param wsFeeStandardQueryParam
     * @return
     */
    List<WsFeeStandardDTO> getPageList(IPage<WsFeeStandardDTO> page, @Param("wsFeeStandardQueryParam") WsFeeStandardQueryParam wsFeeStandardQueryParam);
}
