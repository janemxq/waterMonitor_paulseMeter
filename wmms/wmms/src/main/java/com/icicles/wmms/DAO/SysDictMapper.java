package com.icicles.wmms.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.icicles.wmms.entity.po.SysDict;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 字典 Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2020-04-28
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    IPage<SysDict> findPage(@Param("code") String code, @Param("name") String name, IPage<SysDict> page) throws Exception;


}
