package com.icicles.wmms.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icicles.wmms.entity.po.SysDictItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典项 Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2020-05-13
 */
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    List<SysDictItem> selectBySysDictCode(@Param("dictCode") String dictCode);


}
