package com.icicles.wmms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diboot.core.entity.Dictionary;
import com.diboot.core.mapper.DictionaryMapper;
import com.diboot.core.service.DictionaryService;
import com.icicles.wmms.DAO.SysDictItemMapper;
import com.icicles.wmms.entity.param.SysDictItemQueryParam;
import com.icicles.wmms.entity.po.SysDictItem;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.SysDictItemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 字典项 服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-05-13
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

    private static final Logger logger = LoggerFactory.getLogger(SysDictItemServiceImpl.class);

    @Resource
    SysDictItemMapper sysDictItemMapper;

    @Resource
    DictionaryMapper dictionaryMapper;


    private DictionaryService dictionaryService;

    @Autowired
    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @Override
    public IPage<SysDictItem> findPage(Page page, SysDictItemQueryParam sysDictItemQueryParam) throws ApiException {
        IPage<SysDictItem> retPage;
        try {
            QueryWrapper<SysDictItem> queryWrapper = sysDictItemQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(sysDictItemQueryParam.getDictId()), "dict_id", sysDictItemQueryParam.getDictId());
            queryWrapper.eq(StringUtils.isNotBlank(sysDictItemQueryParam.getDictCode()), "dict_code", sysDictItemQueryParam.getDictCode());
            queryWrapper.eq(StringUtils.isNotBlank(sysDictItemQueryParam.getValue()), "value", sysDictItemQueryParam.getValue());
            queryWrapper.like(StringUtils.isNotBlank(sysDictItemQueryParam.getLabel()), "label", sysDictItemQueryParam.getLabel());
            queryWrapper.eq(StringUtils.isNotBlank(sysDictItemQueryParam.getSerial()), "serial", sysDictItemQueryParam.getSerial());
            queryWrapper.eq(StringUtils.isNotBlank(sysDictItemQueryParam.getRemark()), "remark", sysDictItemQueryParam.getRemark());
            queryWrapper.orderByDesc("create_time");
            retPage = this.page(page, queryWrapper);
            logger.debug("查询字典项列表成功");
        } catch (Exception e) {
            logger.error("查询字典项列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询字典项列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysDictItem sysDictItem) throws ApiException {
        try {
            if (sysDictItem.getDictId() == null) {
                throw new ApiException("字典编号不能为空", HttpStatus.BAD_REQUEST);
            }
            this.save(sysDictItem);
            // diboot字典处理
            // 获取父id
            QueryWrapper<Dictionary> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("item_value",sysDictItem.getDictCode());
            queryWrapper1.eq("parent_id",0L);
            queryWrapper1.eq("is_deleted",0);
            List<Dictionary> dictionaryList = dictionaryService.getEntityList(queryWrapper1);
            if(dictionaryList.size()!=1){
                throw new ApiException("字典表异常，请联系管理员", HttpStatus.BAD_REQUEST);
            }
            Dictionary dictionary = new Dictionary();
            dictionary.setType("system");
            dictionary.setParentId(dictionaryList.get(0).getId());
            dictionary.setItemValue(sysDictItem.getValue());
            dictionary.setItemName(sysDictItem.getLabel());
            dictionary.setDeletable(true);
            dictionary.setDescription(sysDictItem.getRemark());
            dictionaryService.createEntity(dictionary);
            logger.debug("添加字典项成功" + sysDictItem.getId());
        } catch (Exception e) {
            logger.error("添加字典项异常：" + e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException("添加字典项异常：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            // diboot字典处理
            // 获取父id
            SysDictItem sysDictItem = findById(id);
            QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_value",sysDictItem.getDictCode());
            queryWrapper.eq("parent_id",0L);
            queryWrapper.eq("is_deleted",0);
            List<Dictionary> dictionaryList = dictionaryService.getEntityList(queryWrapper);
            if(dictionaryList.size()!=1){
                throw new ApiException("字典表异常，请联系管理员", HttpStatus.BAD_REQUEST);
            }
            QueryWrapper<Dictionary> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("item_value",sysDictItem.getValue());
            queryWrapper1.eq("parent_id",dictionaryList.get(0).getId());
            dictionaryMapper.delete(queryWrapper1);
            this.removeById(id);
            logger.debug("删除字典项成功" + id);
        } catch (Exception e) {
            logger.error("删除字典项异常", e);
            e.printStackTrace();
            throw new ApiException("删除字典项异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(SysDictItem sysDictItem) throws ApiException {
        try {
            if (sysDictItem.getDictId() == null) {
                throw new ApiException("字典编号不能为空", HttpStatus.BAD_REQUEST);
            }
            UpdateWrapper<SysDictItem> wrapper = new UpdateWrapper();
            wrapper.eq("id", sysDictItem.getId());
            this.update(sysDictItem, wrapper);
            QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_value",sysDictItem.getDictCode());
            queryWrapper.eq("parent_id",0L);
            queryWrapper.eq("is_deleted",0);
            List<Dictionary> dictionaryList = dictionaryService.getEntityList(queryWrapper);
            if(dictionaryList.size()!=1){
                throw new ApiException("字典表异常，请联系管理员", HttpStatus.BAD_REQUEST);
            }
            logger.debug("更新字典项成功" + sysDictItem.getId());
        } catch (Exception e) {
            logger.error("更新字典项异常：" + e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException("更新字典项异常：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public SysDictItem findById(String id) throws ApiException {
        SysDictItem sysDictItem;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            sysDictItem = sysDictItemMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询字典项成功");
        } catch (Exception e) {
            logger.error("根据编号查询字典项异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询字典项异常", HttpStatus.BAD_REQUEST);
        }
        return sysDictItem;
    }

    @Override
    public List<SysDictItem> findByDictCode(String dictCode) throws ApiException {
        List<SysDictItem> sysDictItemList;
        try {
            QueryWrapper<SysDictItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dict_code", dictCode);
            sysDictItemList = sysDictItemMapper.selectList(queryWrapper);
            logger.debug("查询字典项信息列表成功");
        } catch (Exception e) {
            logger.error("查询字典项信息列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询字典项信息列表异常", HttpStatus.BAD_REQUEST);
        }
        return sysDictItemList;

    }

    @Override
    public SysDictItem findByDictCode(String dictCode, String label) throws ApiException {
        QueryWrapper<SysDictItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDictItem::getDictCode,dictCode).eq(SysDictItem::getLabel,label).last("limit 1");
        return sysDictItemMapper.selectOne(queryWrapper);
    }

}
