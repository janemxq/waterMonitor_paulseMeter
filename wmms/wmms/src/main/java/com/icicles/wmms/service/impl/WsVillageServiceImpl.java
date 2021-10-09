package com.icicles.wmms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.entity.po.SysRole;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.entity.po.WsVillage;
import com.icicles.wmms.entity.param.WsVillageQueryParam;
import com.icicles.wmms.DAO.WsVillageMapper;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.SysUserService;
import com.icicles.wmms.service.WsVillageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.utils.MyExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


/**
 * <p>
 * 村庄 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Service
public class WsVillageServiceImpl extends ServiceImpl<WsVillageMapper, WsVillage> implements WsVillageService {

    private static final Logger logger = LoggerFactory.getLogger(WsVillageServiceImpl.class);

    @Resource
    WsVillageMapper wsVillageMapper;

    @Resource
    private SysUserService sysUserService;
    @Resource
    private DataQueryAuthServiceImpl dataQueryAuthService;


    @Override
    public IPage<WsVillage> findPage(Page page, WsVillageQueryParam wsVillageQueryParam,Principal principal) throws ApiException {
        IPage<WsVillage> retPage;
        try {
            QueryWrapper<WsVillage> queryWrapper = wsVillageQueryParam.build();
            queryWrapper.like(StringUtils.isNotBlank(wsVillageQueryParam.getVillageName()), "village_name", wsVillageQueryParam.getVillageName());
            queryWrapper.eq(StringUtils.isNotBlank(wsVillageQueryParam.getAddress()), "address", wsVillageQueryParam.getAddress());
            queryWrapper.eq(StringUtils.isNotBlank(wsVillageQueryParam.getSortId()), "sort_id", wsVillageQueryParam.getSortId());
            if(StringUtils.isNotBlank(wsVillageQueryParam.getVillageSn())){
                queryWrapper.eq("sn", wsVillageQueryParam.getVillageSn());
            }else{
                //如果不是超级管理员，只能看自己村的
                SysUser user = dataQueryAuthService.getUser(principal);
                if(user!=null){
                    boolean role = dataQueryAuthService.isSuperAdmin(user);
                    if(!role){
                        queryWrapper.eq("sn", user.getVillageSn());
                    }
                }
            }
            queryWrapper.lambda().orderByAsc(WsVillage::getSortId).orderByDesc(WsVillage::getId);

            retPage = this.page(page,queryWrapper);
            logger.debug("查询村庄列表成功");
        } catch (Exception e) {
            logger.error("查询村庄列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询村庄列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WsVillage wsVillage) throws ApiException {
        try {
            //确保sn不唯一
            WsVillage dbv = this.findBySn(wsVillage.getSn());
            if(dbv!=null){
                throw new ApiException("村落编号已经存在,确保村落编号唯一", HttpStatus.BAD_REQUEST);
            }
            WsVillage dbName = this.findByName(wsVillage.getVillageName());
            if(dbName!=null){
                throw new ApiException("村落名称已经存在,确保村落名称唯一", HttpStatus.BAD_REQUEST);
            }
            this.save(wsVillage);
            logger.debug("添加村庄成功" + wsVillage.getId());
        } catch (ApiException e) {
            logger.error("添加村庄错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加村庄异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加村庄异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            //如果有村民就不能删除了吧？
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysUser::getVillageId,id).last("limit 1");
            SysUser user = sysUserService.getOne(queryWrapper);
            if(user!=null){
                throw new ApiException("村委中还有村民数据，不允许删除", HttpStatus.BAD_REQUEST);
            }
            this.removeById(id);
            logger.debug("删除村庄成功" + id);
        } catch (Exception e) {
            logger.error("删除村庄异常", e);
            e.printStackTrace();
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsVillage wsVillage) throws ApiException {
        try {
            if(StringUtils.isNotBlank(wsVillage.getSn())){
                //确保sn不唯一
                WsVillage dbv = this.findBySn(wsVillage.getId(),wsVillage.getSn());
                if(dbv!=null){
                    throw new ApiException("村落编号已经存在,确保村落编号唯一", HttpStatus.BAD_REQUEST);
                }
            }
            if(StringUtils.isNotBlank(wsVillage.getVillageName())){
                //确保name唯一
                WsVillage dbN = this.findByName(wsVillage.getVillageName(),wsVillage.getId());
                if(dbN!=null){
                    throw new ApiException("村落名称已经存在,确保村落名称唯一", HttpStatus.BAD_REQUEST);
                }
            }

            UpdateWrapper<WsVillage> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsVillage.getId());
            this.update(wsVillage,wrapper);
            logger.debug("更新村庄成功" + wsVillage.getId());
        } catch (ApiException e) {
            logger.error("更新村庄错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新村庄异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新村庄异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsVillage findById(String id) throws ApiException {
        WsVillage wsVillage;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsVillage = wsVillageMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询村庄成功");
        } catch (Exception e) {
            logger.error("根据编号查询村庄异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询村庄异常", HttpStatus.BAD_REQUEST);
        }
        return wsVillage;
    }

    @Override
    public WsVillage findBySn(String sn) throws ApiException {
        WsVillage wsVillage;
        try {
            QueryWrapper<WsVillage> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WsVillage::getSn, sn).last("limit 1");
            wsVillage = wsVillageMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询村庄成功");
        } catch (Exception e) {
            logger.error("根据编号查询村庄异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询村庄异常", HttpStatus.BAD_REQUEST);
        }
        return wsVillage;
    }

    @Override
    public void exportExcel(List<String> items, HttpServletResponse response) throws IOException {
        QueryWrapper<WsVillage> villagesQuery = new QueryWrapper<>();
        if(items!=null&&items.size()>0){
            villagesQuery.lambda().in(WsVillage::getId,items);
        }
        List<WsVillage> data = wsVillageMapper.selectList(villagesQuery);
        List<List<String>> rows = new ArrayList<>();
        for (WsVillage villages:
                data) {
            List<String> tempRow = new ArrayList<>();
            tempRow.add(villages.getSn());
            tempRow.add(villages.getVillageName());
            tempRow.add(villages.getAddress());
            tempRow.add(String.valueOf(villages.getOnline()));
            tempRow.add(villages.getCreateTime());
            rows.add(tempRow);
        }
        List<String> title = CollUtil.newArrayList("村庄编号","村庄名称", "村庄地址", "是否在线", "创建时间");

        MyExcelUtils.exportExcel(response,"村庄信息",title,rows);
    }

    @Override
    public ExcelResultDTO importExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApiException("请上传文件", HttpStatus.BAD_REQUEST);
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if(!".xlsx".equals(suffixName)){
            throw new ApiException("格式不正确", HttpStatus.BAD_REQUEST);
        }

        // 文件上传路径
        String filePath = System.getProperty("user.dir");
        // 解决中文问题,liunx 下中文路径,图片显示问题
        String fileRename = UUID.randomUUID().toString().replace("-","") + suffixName;
        //全路径
        String fullFileName = filePath + "/excelfiles3/"+fileRename;

        File dest = new File(fullFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //导入结果
        ExcelResultDTO rs = new ExcelResultDTO();
        //读取excel文件
        Excel07SaxReader reader = new Excel07SaxReader(villageCreateRowHandler(rs));
        reader.read(fullFileName, -1);
        //删除文件
        dest.delete();

        return rs;
    }

    @Override
    public WsVillage findByName(String name) throws ApiException {
        QueryWrapper<WsVillage> queryWrap = new QueryWrapper<> ();
        queryWrap.lambda().eq(WsVillage::getVillageName,name).last("limit 1");
        return wsVillageMapper.selectOne(queryWrap);
    }

    @Override
    public List<WsVillage> getListBySns(Collection<String> sn) {
        QueryWrapper<WsVillage> queryWrap = new QueryWrapper<> ();
        queryWrap.lambda().in(WsVillage::getSn,sn);
        return wsVillageMapper.selectList(queryWrap);
    }

    private WsVillage findByName(String name,Long id) throws ApiException {
        QueryWrapper<WsVillage> queryWrap = new QueryWrapper<> ();
        queryWrap.lambda().eq(WsVillage::getVillageName,name).ne(WsVillage::getId,id).last("limit 1");
        return wsVillageMapper.selectOne(queryWrap);
    }

    /**
     * 处理excel数据
     * @return RowHandler 处理类
     */
    private RowHandler villageCreateRowHandler(ExcelResultDTO rs) {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
//                Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowList);
                //使用第一个sheet1
                if (sheetIndex == 0) {
                    //忽略第一行
                    if (rowIndex > 0) {
                        rs.setSum(rs.getSum()+1);
                        try {
                            int f = this.getVillageFromExcel(rowList);
                            if (f <= 0) {
                                rs.setError(rs.getError()+1);
                                logger.error("添加失败");
                            }
                        } catch (Exception e) {
                            rs.setError(rs.getError()+1);
                            e.printStackTrace();
                        }
                    }
                }
            }
            private int getVillageFromExcel(List<Object> rowList) throws ApiException{
                //判断唯一标识
                String sn = String.valueOf(rowList.get(0));
                if(StringUtils.isBlank(sn)){
                    throw new ApiException("唯一标识不存在", HttpStatus.BAD_REQUEST);
                }
                QueryWrapper<WsVillage> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(WsVillage::getSn,sn);
                WsVillage village = wsVillageMapper.selectOne(queryWrapper);
                if(village!=null){
                    throw new ApiException("村庄的编号已经存在，请确保村庄的编号唯一", HttpStatus.BAD_REQUEST);
                }

                WsVillage insertWsVillage = new WsVillage();
                insertWsVillage.setSn(sn);
                insertWsVillage.setVillageName(String.valueOf(rowList.get(1)));
                insertWsVillage.setAddress(String.valueOf(rowList.get(2)));
                insertWsVillage.setOnline(Integer.valueOf(rowList.get(3).toString()));
                insertWsVillage.setCreateTime(String.valueOf(rowList.get(4)));

                return wsVillageMapper.insert(insertWsVillage);
            }
        };
    }

    private WsVillage findBySn(Long id,String sn) throws ApiException {
        WsVillage wsVillage;
        try {
            QueryWrapper<WsVillage> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().ne(WsVillage::getId,id).eq(WsVillage::getSn, sn).last("limit 1");
            wsVillage = wsVillageMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询村庄成功");
        } catch (Exception e) {
            logger.error("根据编号查询村庄异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询村庄异常", HttpStatus.BAD_REQUEST);
        }
        return wsVillage;
    }
}
