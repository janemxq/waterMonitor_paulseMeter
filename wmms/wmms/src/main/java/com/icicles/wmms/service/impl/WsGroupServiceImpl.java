package com.icicles.wmms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.entity.param.WsGroupQueryParam;
import com.icicles.wmms.DAO.WsGroupMapper;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.SysUserService;
import com.icicles.wmms.service.WsGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.service.WsMeterService;
import com.icicles.wmms.service.WsVillageService;
import com.icicles.wmms.utils.MyExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 表井 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Service
public class WsGroupServiceImpl extends ServiceImpl<WsGroupMapper, WsGroup> implements WsGroupService {

    private static final Logger logger = LoggerFactory.getLogger(WsGroupServiceImpl.class);

    @Resource
    WsGroupMapper wsGroupMapper;
    @Resource
    WsVillageService wsVillageService;
    @Resource
    WsMeterService wsMeterService;
    @Resource
    private SysUserService userService;
    @Resource
    private DataQueryAuthServiceImpl dataQueryAuthService;

    @Override
    public IPage<WsGroup> findPage(Page page, WsGroupQueryParam wsGroupQueryParam, Principal principal) throws ApiException {
        IPage<WsGroup> retPage;
        try {
            QueryWrapper<WsGroup> queryWrapper = wsGroupQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(wsGroupQueryParam.getSn()), "sn", wsGroupQueryParam.getSn());
            queryWrapper.eq(StringUtils.isNotBlank(wsGroupQueryParam.getGroupName()), "group_name", wsGroupQueryParam.getGroupName());
            queryWrapper.eq(StringUtils.isNotBlank(wsGroupQueryParam.getVillageId()), "village_id", wsGroupQueryParam.getVillageId());
            queryWrapper.eq(StringUtils.isNotBlank(wsGroupQueryParam.getVillageName()), "village_name", wsGroupQueryParam.getVillageName());
            queryWrapper.eq(StringUtils.isNotBlank(wsGroupQueryParam.getAddress()), "address", wsGroupQueryParam.getAddress());

            if(StringUtils.isNotBlank(wsGroupQueryParam.getVillageSn())){
                queryWrapper.eq(StringUtils.isNotBlank(wsGroupQueryParam.getVillageSn()), "village_sn", wsGroupQueryParam.getVillageSn());
            }else{
                //如果是村管理员，只能看本村的
                SysUser u = userService.findByAccount(principal.getName());
                if(u!=null){
                    SysRole r = userService.getRoleByAccount(principal);
                    if(!dataQueryAuthService.isSuperAdmin(u)){
                        queryWrapper.eq("village_sn",u.getVillageSn());
                    }
                }

            }


            retPage = this.page(page,queryWrapper);
            logger.debug("查询表井列表成功");
        } catch (Exception e) {
            logger.error("查询表井列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询表井列表异常", HttpStatus.BAD_REQUEST);
        }
        List<WsGroup> list = retPage.getRecords();
        //显示最后修改人姓名
        list = this.showCreateUserName(list);
        list = this.setVillageName(list);
        retPage.setRecords(list);
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WsGroup wsGroup) throws ApiException {
        try {
            WsGroup g = this.findByName(wsGroup.getVillageSn(),wsGroup.getGroupName());

            if(g!=null){
                throw new ApiException("表井名称已经存在", HttpStatus.BAD_REQUEST);
            }
            WsGroup snFlag = this.findBySn(wsGroup.getSn());
            if(snFlag!=null){
                throw new ApiException("表井编号已经存在", HttpStatus.BAD_REQUEST);
            }

            //村信息
            WsVillage wsVillageNew = wsVillageService.findBySn(wsGroup.getVillageSn());
            if(wsVillageNew==null){
                throw new ApiException("表井关联的村信息不存在", HttpStatus.BAD_REQUEST);
            }
            wsGroup.setVillageId(wsVillageNew.getId());

            this.save(wsGroup);
            logger.debug("添加表井成功" + wsGroup.getId());
        } catch (ApiException e) {
            logger.error("添加表井错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加表井异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加表井异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            //如果绑定了设备，不让删除
            QueryWrapper<WsMeter> wsMeterQueryWrapper = new QueryWrapper<>();
            wsMeterQueryWrapper.lambda().eq(WsMeter::getGroupId,id).last("limit 1");
            WsMeter wsMeter = wsMeterService.getOne(wsMeterQueryWrapper);
            if(wsMeter!=null){
                throw new ApiException("还有设备绑定在该表井上，请先更改绑定或删除设备后再删除", HttpStatus.BAD_REQUEST);
            }

            this.removeById(id);
            logger.debug("删除表井成功" + id);
        } catch (ApiException e){
            logger.error("删除表井失败:" + e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            logger.error("删除表井异常", e);
            e.printStackTrace();
            throw new ApiException("删除表井异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsGroup wsGroup) throws ApiException {
        try {
            //如果集管端只传了一个id，可能导致其他属性都是空，这个地方查一下
            WsGroup dbInfo = this.findById(wsGroup.getId().toString());
            if(dbInfo==null){
                throw new ApiException("更新的内容不存在", HttpStatus.BAD_REQUEST);
            }

            if(StringUtils.isNotBlank(wsGroup.getVillageSn())){
                WsVillage wsVillageNew = wsVillageService.findBySn(wsGroup.getVillageSn());
                if(wsVillageNew==null){
                    throw new ApiException("表井关联的村信息不存在", HttpStatus.BAD_REQUEST);
                }
                wsGroup.setVillageId(wsVillageNew.getId());
            }

            if(StringUtils.isNotBlank(wsGroup.getGroupName())){
                WsGroup i = this.findByName(wsGroup.getSn(),wsGroup.getGroupName(),wsGroup.getId());
                if(i!=null){
                    throw new ApiException("表井名称已经存在", HttpStatus.BAD_REQUEST);
                }
            }
            if(StringUtils.isNotBlank(wsGroup.getSn())){
                WsGroup snFlag = this.findBySnAndId(wsGroup.getSn(),String.valueOf(wsGroup.getId()));
                if(snFlag!=null){
                    throw new ApiException("表井编码已经存在", HttpStatus.BAD_REQUEST);
                }
            }

            UpdateWrapper<WsGroup> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsGroup.getId());
            this.update(wsGroup,wrapper);
            logger.debug("更新表井成功" + wsGroup.getId());
        } catch (ApiException e) {
            logger.error("更新表井错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新表井异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新表井异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsGroup findById(String id) throws ApiException {
        WsGroup wsGroup;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsGroup = wsGroupMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询表井成功");
        } catch (Exception e) {
            logger.error("根据编号查询表井异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询表井异常", HttpStatus.BAD_REQUEST);
        }
        return wsGroup;
    }

    @Override
    public WsGroup findByName(String villageSn,String name){
        QueryWrapper<WsGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsGroup::getVillageSn,villageSn).eq(WsGroup::getGroupName,name).last("limit 1");
        return wsGroupMapper.selectOne(queryWrapper);
    }

    @Override
    public WsGroup findBySn(String villageSn, String sn) throws ApiException {
        QueryWrapper<WsGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsGroup::getVillageSn,villageSn).eq(WsGroup::getSn,sn).last("limit 1");
        return wsGroupMapper.selectOne(queryWrapper);
    }

    @Override
    public WsGroup findBySn(String sn) throws ApiException {
        QueryWrapper<WsGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsGroup::getSn,sn).last("limit 1");
        return wsGroupMapper.selectOne(queryWrapper);
    }

    @Override
    public void exportExcel(List<String> items, HttpServletResponse response) throws IOException {
        QueryWrapper<WsGroup> queryWrapper = new QueryWrapper<>();
        if(items!=null&&items.size()>0){
            queryWrapper.lambda().in(WsGroup::getId,items);
        }

        List<WsGroup> data = wsGroupMapper.selectList(queryWrapper);
        List<List<String>> rows = new ArrayList<>();
        for (WsGroup group:
        data) {
            List<String> tempRow = new ArrayList<>();
            tempRow.add(group.getSn());
            tempRow.add(group.getAddress());
            tempRow.add(group.getGroupName());
            tempRow.add(group.getVillageSn());
            tempRow.add(group.getVillageName());
            tempRow.add(group.getCreateTime());
            rows.add(tempRow);
        }
        List<String> title = CollUtil.newArrayList("表井编号","表井通讯地址", "表井名称", "村庄编号", "村落名称","创建时间");

        MyExcelUtils.exportExcel(response,"所有表井",title,rows);
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
        String fullFileName = filePath + "/excelfiles2/"+fileRename;

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
        Excel07SaxReader reader = new Excel07SaxReader(groupCreateRowHandler(rs));
        reader.read(fullFileName, -1);
        //删除文件
        dest.delete();

        return rs;
    }

    @Override
    public List<WsGroup> showCreateUserName(List<WsGroup> groups) {
       if(groups==null||groups.size() == 0){
           return null;
       }
        Set<String> set = new LinkedHashSet<>();
        for (WsGroup group : groups) {
            if(StrUtil.isNotBlank(group.getUpdateUserId())){
                set.add(group.getUpdateUserId());
            }
        }
        if(set.size() > 0){
            List<SysUser> userList = userService.list(new QueryWrapper<SysUser> ().lambda().in(SysUser::getSn,set));
            if(userList!=null&&userList.size() > 0){
                for (WsGroup group : groups) {
                    for (SysUser user : userList) {
                        if(Objects.equals(String.valueOf(user.getSn()),group.getUpdateUserId())){
                            group.setUpdateUserId(user.getName());
                        }
                    }
                }
            }
        }
        return groups;
    }

    @Override
    public List<WsGroup> getGroupsBySns(Collection<String> sn) {
        QueryWrapper<WsGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(WsGroup::getSn,sn);
        return wsGroupMapper.selectList(queryWrapper);
    }

    @Override
    public List<WsGroup> setVillageName(List<WsGroup> data) {
        if(CollUtil.isEmpty(data)){
            return null;
        }
        //涉及到的村信息
        List<WsVillage> villageList=null;
        //所有村的标识
        List<String> villageSnList = data.stream().map(WsGroup::getVillageSn).collect(Collectors.toList());
        //去重
        if(CollUtil.isNotEmpty(villageSnList)){
            villageSnList = villageSnList.stream().distinct().collect(Collectors.toList());
            villageList = wsVillageService.getListBySns(villageSnList);
        }
        if(CollUtil.isNotEmpty(villageList)){
            for (WsGroup temp:data){
                for (WsVillage v:villageList){
                    if(temp.getVillageSn().equals(v.getSn())){
                        temp.setVillageName(v.getVillageName());
                    }
                }
            }
        }
        return data;
    }


    /**
     * 处理excel数据
     * @return RowHandler 处理类
     */
    private RowHandler groupCreateRowHandler(ExcelResultDTO rs) {
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
                            int f = this.getGroupFromExcel(rowList);
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
            private int getGroupFromExcel(List<Object> rowList) throws ApiException{
                //判断唯一标识
                String sn = String.valueOf(rowList.get(0));
                if(StringUtils.isBlank(sn)){
                    throw new ApiException("唯一标识不存在", HttpStatus.BAD_REQUEST);
                }
                QueryWrapper<WsGroup> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(WsGroup::getSn,sn);
                WsGroup group = wsGroupMapper.selectOne(queryWrapper);
                if(group!=null){
                    throw new ApiException("标识已经存在，请确保表井标识唯一", HttpStatus.BAD_REQUEST);
                }

                WsGroup insertGroup = new WsGroup();
                //村落编号
                String villageSn = String.valueOf(rowList.get(3));
                if(StringUtils.isBlank(villageSn)){
                    throw new ApiException("没有村落标识，无法对应村落", HttpStatus.BAD_REQUEST);
                }else{
                    WsVillage tempVillage = wsVillageService.findBySn(villageSn);
                    if(tempVillage==null){
                        throw new ApiException("无法查询到村落信息，无法对应村落", HttpStatus.BAD_REQUEST);
                    }
                    insertGroup.setVillageId(tempVillage.getId());
                    insertGroup.setVillageName(tempVillage.getVillageName());
                    insertGroup.setVillageSn(tempVillage.getSn());
                }
                insertGroup.setSn(sn);
                insertGroup.setAddress(String.valueOf(rowList.get(1)));
                insertGroup.setGroupName(String.valueOf(rowList.get(2)));
                insertGroup.setCreateTime(String.valueOf(rowList.get(5)));
                return wsGroupMapper.insert(insertGroup);
            }
        };
    }

    private WsGroup findByName(String villageSn,String name,Long id){
        QueryWrapper<WsGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsGroup::getVillageSn,villageSn).eq(WsGroup::getGroupName,name).ne(WsGroup::getId,id).last("limit 1");
        return wsGroupMapper.selectOne(queryWrapper);
    }

    private WsGroup findBySnAndId(String sn,String id) throws ApiException {
        QueryWrapper<WsGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsGroup::getSn,sn).ne(WsGroup::getId,id).last("limit 1");
        return wsGroupMapper.selectOne(queryWrapper);
    }
}
