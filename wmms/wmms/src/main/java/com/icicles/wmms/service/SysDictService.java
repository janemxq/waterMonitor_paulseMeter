package com.icicles.wmms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.SysDictQueryParam;
import com.icicles.wmms.entity.po.SysDict;
import com.icicles.wmms.exception.ApiException;

/**
 * <p>
 * 字典 服务类
 * </p>
 *
 * @author auto
 * @since 2020-04-28
 */
public interface SysDictService extends IService<SysDict> {
    /**
     * 根据条件分页查询字典项
     *
     * @param page 分页对象
     * @return IPage<SysDictItem>   字典项列表（分页）
     */
    IPage<SysDict> findPage(Page page, SysDictQueryParam sysDictQueryParam) throws ApiException;

    /**
     * 增加字典
     *
     * @param sysDict 字典
     * @throws ApiException 异常信息
     */
    void add(SysDict sysDict) throws ApiException;

    /**
     * 删除字典
     *
     * @param id 编号
     * @throws ApiException 异常信息
     */
    void delete(String id) throws ApiException;

    /**
     * 更新字典
     *
     * @param sysDict 字典
     * @throws ApiException 异常信息
     */
    void refresh(SysDict sysDict) throws ApiException;

    /**
     * 根据编号查询字典
     *
     * @param id 编号
     * @return SysDict 字典
     * @throws ApiException 异常信息
     */
    SysDict findById(String id) throws ApiException;
}
