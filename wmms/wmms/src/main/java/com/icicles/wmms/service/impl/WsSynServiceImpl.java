package com.icicles.wmms.service.impl;

import com.icicles.wmms.entity.po.WsSyn;
import com.icicles.wmms.entity.param.WsSynQueryParam;
import com.icicles.wmms.DAO.WsSynMapper;
import com.icicles.wmms.service.WsSynService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
/**
 * <p>
 * 同步记录 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-07-15
 */
@Service
public class WsSynServiceImpl extends ServiceImpl<WsSynMapper, WsSyn> implements WsSynService {

    private static final Logger logger = LoggerFactory.getLogger(WsSynServiceImpl.class);

    @Resource
    WsSynMapper wsSynMapper;

    @Override
    public IPage<WsSyn> findPage(Page page, WsSynQueryParam wsSynQueryParam) throws ApiException {
        IPage<WsSyn> retPage;
        try {
            QueryWrapper<WsSyn> queryWrapper = wsSynQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(wsSynQueryParam.getDataType()), "data_type", wsSynQueryParam.getDataType());
            retPage = this.page(page,queryWrapper);
            logger.debug("查询同步记录列表成功");
        } catch (Exception e) {
            logger.error("查询同步记录列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询同步记录列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WsSyn wsSyn) throws ApiException {
        try {
            this.save(wsSyn);
            logger.debug("添加同步记录成功" + wsSyn.getId());
        } catch (ApiException e) {
            logger.error("添加同步记录错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加同步记录异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加同步记录异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除同步记录成功" + id);
        } catch (Exception e) {
            logger.error("删除同步记录异常", e);
            e.printStackTrace();
            throw new ApiException("删除同步记录异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsSyn wsSyn) throws ApiException {
        try {
            UpdateWrapper<WsSyn> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsSyn.getId());
            this.update(wsSyn,wrapper);
            logger.debug("更新同步记录成功" + wsSyn.getId());
        } catch (ApiException e) {
            logger.error("更新同步记录错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新同步记录异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新同步记录异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsSyn findById(String id) throws ApiException {
        WsSyn wsSyn;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsSyn = wsSynMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询同步记录成功");
        } catch (Exception e) {
            logger.error("根据编号查询同步记录异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询同步记录异常", HttpStatus.BAD_REQUEST);
        }
        return wsSyn;
    }

    @Override
    public WsSyn findByType(String type) throws ApiException {
        QueryWrapper<WsSyn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsSyn::getDataType,type).orderByDesc(WsSyn::getId).last("limit 1");
        return wsSynMapper.selectOne(queryWrapper);
    }

}
