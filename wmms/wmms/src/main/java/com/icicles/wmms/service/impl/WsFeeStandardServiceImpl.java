package com.icicles.wmms.service.impl;

import com.icicles.wmms.entity.dto.WsFeeStandardDTO;
import com.icicles.wmms.entity.po.WsFeeStandard;
import com.icicles.wmms.entity.param.WsFeeStandardQueryParam;
import com.icicles.wmms.DAO.WsFeeStandardMapper;
import com.icicles.wmms.entity.po.WsFeeStandardItem;
import com.icicles.wmms.entity.po.WsMeter;
import com.icicles.wmms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.utils.BLUtil;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 水表的收费标准 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-10
 */
@Service
public class WsFeeStandardServiceImpl extends ServiceImpl<WsFeeStandardMapper, WsFeeStandard> implements WsFeeStandardService {

    private static final Logger logger = LoggerFactory.getLogger(WsFeeStandardServiceImpl.class);

    @Resource
    WsFeeStandardMapper wsFeeStandardMapper;
    @Resource
    private WsMeterService meterService;
    @Resource
    private WsFeeStandardItemService feeStandardItemService;
    @Resource
    private WaterFeeService waterFeeService;
    @Resource
    private SysUserService sysUserService;

    @Override
    public IPage<WsFeeStandardDTO> findPage(Page<WsFeeStandardDTO> page, WsFeeStandardQueryParam wsFeeStandardQueryParam) throws ApiException {
        try {
            List<WsFeeStandardDTO> data = wsFeeStandardMapper.getPageList(page,wsFeeStandardQueryParam);
            for (WsFeeStandardDTO temp:
                    data) {
                temp.setItems(waterFeeService.getStandards(temp));
            }
            logger.debug("查询水表的收费标准列表成功");
            sysUserService.setUpdateUser(data);
            return page.setRecords(data);
        } catch (Exception e) {
            logger.error("查询水表的收费标准列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询水表的收费标准列表异常", HttpStatus.BAD_REQUEST);
        }
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WsFeeStandard wsFeeStandard) throws ApiException {
        try {
            //确保名称唯一
            QueryWrapper<WsFeeStandard> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WsFeeStandard::getDisplayName,wsFeeStandard.getDisplayName()).last("limit 1");
            WsFeeStandard dbFlag = wsFeeStandardMapper.selectOne(queryWrapper);
            if(dbFlag!=null){
                throw new ApiException("名称已经存在，请尝试更换其他名称", HttpStatus.BAD_REQUEST);
            }
            //如果这个值前端没有给传，默认0
            if(wsFeeStandard.getIsStep()==null){
                wsFeeStandard.setIsStep(0);
            }
            wsFeeStandard.setSn(BLUtil.getUUID(true));
            this.save(wsFeeStandard);
            //保存收费子项
            List<WsFeeStandardItem> items = waterFeeService.getStandards(wsFeeStandard);
            feeStandardItemService.saveBatch(items);

            logger.debug("添加水表的收费标准成功" + wsFeeStandard.getId());
        } catch (ApiException e) {
            logger.error("添加水表的收费标准错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加水表的收费标准异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加水表的收费标准异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            //确保没有水表在使用该标准
            QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WsMeter::getMeterType,id).last("limit 1");
            WsMeter meter = meterService.getOne(queryWrapper);
            if(meter!=null){
                throw new ApiException("还有水表使用该标准，请先更改水表标准再删除", HttpStatus.BAD_REQUEST);
            }
            //删除记录
            this.removeById(id);
            logger.debug("删除水表的收费标准成功" + id);
        } catch (ApiException e) {
            logger.error("删除水表的收费标准异常:" + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("删除水表的收费标准异常", e);
            e.printStackTrace();
            throw new ApiException("删除水表的收费标准异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsFeeStandard wsFeeStandard) throws ApiException {
        try {
            QueryWrapper<WsFeeStandard> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WsFeeStandard::getDisplayName,wsFeeStandard.getDisplayName()).ne(WsFeeStandard::getId,wsFeeStandard.getId()).last("limit 1");
            WsFeeStandard dbFlag = wsFeeStandardMapper.selectOne(queryWrapper);
            if(dbFlag!=null){
                throw new ApiException("名称已经存在,请尝试更换其他名称", HttpStatus.BAD_REQUEST);
            }

            UpdateWrapper<WsFeeStandard> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsFeeStandard.getId());
            this.update(wsFeeStandard,wrapper);

            //删除对应的子项
            Map<String,Object> delCondition = new HashMap<>(1);
            delCondition.put("fee_standard_id",wsFeeStandard.getId());
            boolean dr = feeStandardItemService.removeByMap(delCondition);
            if(!dr){
                throw new ApiException("更新异常，请联系管理员", HttpStatus.BAD_REQUEST);
            }
            //保存收费子项
            List<WsFeeStandardItem> items = waterFeeService.getStandards(wsFeeStandard);
            feeStandardItemService.saveBatch(items);

            logger.debug("更新水表的收费标准成功" + wsFeeStandard.getId());
        } catch (ApiException e) {
            logger.error("更新水表的收费标准错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新水表的收费标准异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新水表的收费标准异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsFeeStandard findById(String id) throws ApiException {
        WsFeeStandard wsFeeStandard;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsFeeStandard = wsFeeStandardMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询水表的收费标准成功");
        } catch (Exception e) {
            logger.error("根据编号查询水表的收费标准异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询水表的收费标准异常", HttpStatus.BAD_REQUEST);
        }
        return wsFeeStandard;
    }

    @Override
    public WsFeeStandardDTO findDetailById(String id) throws ApiException {
        WsFeeStandardDTO rs = new WsFeeStandardDTO();
        WsFeeStandard wsFeeStandard;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsFeeStandard = wsFeeStandardMapper.selectOne(queryWrapper);
            if(wsFeeStandard==null){
                return rs;
            }

            rs.setItems(waterFeeService.getStandards(wsFeeStandard));
            rs.setDisplayName(wsFeeStandard.getDisplayName());
            rs.setCreateTime(wsFeeStandard.getCreateTime());
            rs.setFeeStandard(wsFeeStandard.getFeeStandard());
            rs.setId(wsFeeStandard.getId());
            rs.setUpdateTime(wsFeeStandard.getUpdateTime());
            rs.setCreateUserId(wsFeeStandard.getCreateUserId());
            rs.setUpdateUserId(wsFeeStandard.getUpdateUserId());
            rs.setIsStep(wsFeeStandard.getIsStep());
            rs.setSortId(wsFeeStandard.getSortId());

            logger.debug("根据编号查询水表的收费标准成功");
        } catch (Exception e) {
            logger.error("根据编号查询水表的收费标准异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询水表的收费标准异常", HttpStatus.BAD_REQUEST);
        }
        return rs;
    }

    @Override
    public List<WsFeeStandardDTO> getAll() throws ApiException {
        WsFeeStandardQueryParam ws = new WsFeeStandardQueryParam();
        List<WsFeeStandardDTO> data = wsFeeStandardMapper.getPageList(null,ws);
        for (WsFeeStandardDTO temp:
                data) {
            temp.setItems(waterFeeService.getStandards(temp));
        }
        sysUserService.setUpdateUser(data);
        return data;
    }

    @Override
    public WsFeeStandard findBySn(String sn) throws ApiException {
        QueryWrapper<WsFeeStandard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsFeeStandard::getSn, sn).last("limit 1");
        return wsFeeStandardMapper.selectOne(queryWrapper);
    }
}
