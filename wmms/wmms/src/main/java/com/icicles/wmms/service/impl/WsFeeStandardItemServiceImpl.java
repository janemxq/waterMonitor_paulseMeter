package com.icicles.wmms.service.impl;

import com.icicles.wmms.entity.po.WsFeeStandardItem;
import com.icicles.wmms.entity.param.WsFeeStandardItemQueryParam;
import com.icicles.wmms.DAO.WsFeeStandardItemMapper;
import com.icicles.wmms.service.WsFeeStandardItemService;
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
 * 水表的收费标准子项 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-11
 */
@Service
public class WsFeeStandardItemServiceImpl extends ServiceImpl<WsFeeStandardItemMapper, WsFeeStandardItem> implements WsFeeStandardItemService {

    private static final Logger logger = LoggerFactory.getLogger(WsFeeStandardItemServiceImpl.class);

    @Resource
    WsFeeStandardItemMapper wsFeeStandardItemMapper;

    @Override
    public IPage<WsFeeStandardItem> findPage(Page page, WsFeeStandardItemQueryParam wsFeeStandardItemQueryParam) throws ApiException {
        IPage<WsFeeStandardItem> retPage;
        try {
            QueryWrapper<WsFeeStandardItem> queryWrapper = wsFeeStandardItemQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeStandardItemQueryParam.getFeeStandardId()), "fee_standard_id", wsFeeStandardItemQueryParam.getFeeStandardId());
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeStandardItemQueryParam.getStartNum()), "start_num", wsFeeStandardItemQueryParam.getStartNum());
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeStandardItemQueryParam.getEndNum()), "end_num", wsFeeStandardItemQueryParam.getEndNum());
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeStandardItemQueryParam.getIsStep()), "is_step", wsFeeStandardItemQueryParam.getIsStep());
            queryWrapper.eq(StringUtils.isNotBlank(wsFeeStandardItemQueryParam.getPrice()), "price", wsFeeStandardItemQueryParam.getPrice());
            retPage = this.page(page,queryWrapper);
            logger.debug("查询水表的收费标准子项列表成功");
        } catch (Exception e) {
            logger.error("查询水表的收费标准子项列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询水表的收费标准子项列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WsFeeStandardItem wsFeeStandardItem) throws ApiException {
        try {
            this.save(wsFeeStandardItem);
            logger.debug("添加水表的收费标准子项成功" + wsFeeStandardItem.getId());
        } catch (ApiException e) {
            logger.error("添加水表的收费标准子项错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加水表的收费标准子项异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加水表的收费标准子项异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除水表的收费标准子项成功" + id);
        } catch (Exception e) {
            logger.error("删除水表的收费标准子项异常", e);
            e.printStackTrace();
            throw new ApiException("删除水表的收费标准子项异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsFeeStandardItem wsFeeStandardItem) throws ApiException {
        try {
            UpdateWrapper<WsFeeStandardItem> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsFeeStandardItem.getId());
            this.update(wsFeeStandardItem,wrapper);
            logger.debug("更新水表的收费标准子项成功" + wsFeeStandardItem.getId());
        } catch (ApiException e) {
            logger.error("更新水表的收费标准子项错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新水表的收费标准子项异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新水表的收费标准子项异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsFeeStandardItem findById(String id) throws ApiException {
        WsFeeStandardItem wsFeeStandardItem;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsFeeStandardItem = wsFeeStandardItemMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询水表的收费标准子项成功");
        } catch (Exception e) {
            logger.error("根据编号查询水表的收费标准子项异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询水表的收费标准子项异常", HttpStatus.BAD_REQUEST);
        }
        return wsFeeStandardItem;
    }

}
