package com.icicles.wmms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diboot.core.entity.Dictionary;
import com.diboot.core.service.DictionaryService;
import com.diboot.core.vo.DictionaryVO;
import com.icicles.wmms.DAO.SysDictMapper;
import com.icicles.wmms.entity.param.SysDictQueryParam;
import com.icicles.wmms.entity.po.SysDict;
import com.icicles.wmms.entity.po.SysDictItem;
import com.icicles.wmms.entity.po.SysParam;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.SysDictItemService;
import com.icicles.wmms.service.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典 服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-04-28
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    private static final Logger logger = LoggerFactory.getLogger(SysDictServiceImpl.class);

    @Resource
    SysDictMapper sysDictMapper;

    private SysDictItemService sysDictItemService;

    @Autowired
    public void setSysDictItemService(SysDictItemService sysDictItemService) {
        this.sysDictItemService = sysDictItemService;
    }

    private DictionaryService dictionaryService;

    @Autowired
    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @Override
    public IPage<SysDict> findPage(Page page, SysDictQueryParam sysDictQueryParam) throws ApiException {
        IPage<SysDict> retPage;
        try {
            QueryWrapper<SysDict> queryWrapper = sysDictQueryParam.build();
            queryWrapper.like(StringUtils.isNotBlank(sysDictQueryParam.getName()), "name", sysDictQueryParam.getName());
            queryWrapper.eq(StringUtils.isNotBlank(sysDictQueryParam.getCode()), "code", sysDictQueryParam.getCode());
            queryWrapper.lambda().orderByAsc(SysDict::getSerial);
            retPage = this.page(page,queryWrapper);
            logger.debug("查询字典表列表成功");
        } catch (Exception e) {
            logger.error("查询字典表列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询字典表列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysDict sysDict) throws ApiException {
        try {
            QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", sysDict.getCode());
            SysDict sysDict1 = sysDictMapper.selectOne(queryWrapper);
            if (sysDict1 != null) {
                throw new ApiException("字典代码已存在", HttpStatus.BAD_REQUEST);
            }
            this.save(sysDict);
            // diboot字典处理
            DictionaryVO dictionaryVO = new DictionaryVO();
            dictionaryVO.setType("system");
            dictionaryVO.setParentId(0L);
            dictionaryVO.setDeletable(true);
            dictionaryVO.setItemValue(sysDict.getCode());
            dictionaryVO.setItemName(sysDict.getName());
            dictionaryVO.setDescription(sysDict.getRemark());
            dictionaryService.createDictAndChildren(dictionaryVO);
            logger.debug("添加字典成功" + sysDict.getId());
        } catch (Exception e) {
            logger.error("添加字典异常：" + e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException("添加字典异常：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            SysDict sysDict = sysDictMapper.selectById(id);
            if (sysDict != null) {
                List<SysDictItem> list = sysDictItemService.findByDictCode(sysDict.getCode());
                /*if (list != null && list.size() > 0) {
                    throw new ApiException("含子字典不允许删除", HttpStatus.BAD_REQUEST);
                }*/
                for (int i = 0; i < list.size(); i++) {
                    sysDictItemService.delete(list.get(i).getId().toString());
                }
            }
            this.removeById(id);
            // diboot字典处理
            QueryWrapper<Dictionary> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("item_value",sysDict.getCode());
            queryWrapper1.eq("parent_id",0L);
            queryWrapper1.eq("is_deleted",0);
            List<Dictionary> dictionaryList = dictionaryService.getEntityList(queryWrapper1);
            if(dictionaryList.size()!=1){
                throw new ApiException("字典表异常，请联系管理员", HttpStatus.BAD_REQUEST);
            }
            QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id",dictionaryList.get(0).getId());
            dictionaryService.deleteEntities(queryWrapper);
            dictionaryService.deleteEntities(queryWrapper1);
            logger.debug("删除字典表成功" + id);
        } catch (Exception e) {
            logger.error("删除字典表异常：" + e.getMessage(), e);
            e.printStackTrace();
            throw new ApiException("删除字典表异常：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(SysDict sysDict) throws ApiException {
        try {
            QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", sysDict.getCode());
            SysDict sysDict1 = sysDictMapper.selectOne(queryWrapper);
            if (sysDict1 != null && !sysDict1.getId().equals(sysDict.getId())) {
                throw new ApiException("字典代码已存在", HttpStatus.BAD_REQUEST);
            }
            UpdateWrapper<SysDict> wrapper = new UpdateWrapper();
            wrapper.eq("id", sysDict.getId());
            this.update(sysDict, wrapper);
            QueryWrapper<Dictionary> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("item_value",sysDict.getCode());
            queryWrapper1.eq("parent_id",0L);
            queryWrapper1.eq("is_deleted",0);
            List<Dictionary> dictionaryList = dictionaryService.getEntityList(queryWrapper1);
            if(dictionaryList.size()!=1){
                throw new ApiException("字典表异常，请联系管理员", HttpStatus.BAD_REQUEST);
            }
            DictionaryVO dictionaryVO = new DictionaryVO();
            dictionaryVO.setId(dictionaryList.get(0).getId());
            dictionaryVO.setDescription(sysDict.getRemark());
            dictionaryVO.setType("system");
            dictionaryVO.setParentId(0L);
            dictionaryVO.setItemValue(sysDict.getCode());
            dictionaryVO.setItemName(sysDict.getName());
            dictionaryService.updateDictAndChildren(dictionaryVO);
            logger.debug("更新字典成功" + sysDict.getId());
        } catch (Exception e) {
            logger.error("更新字典异常", e);
            e.printStackTrace();
            throw new ApiException("更新字典异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public SysDict findById(String id) throws ApiException {
        SysDict sysDict;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            sysDict = sysDictMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询字典成功");
        } catch (Exception e) {
            logger.error("根据编号查询字典异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询字典异常", HttpStatus.BAD_REQUEST);
        }
        return sysDict;
    }

}
