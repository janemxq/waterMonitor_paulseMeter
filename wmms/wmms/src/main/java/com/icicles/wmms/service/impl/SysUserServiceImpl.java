package com.icicles.wmms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.icicles.wmms.DAO.SysUserMapper;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.dto.UseDefaultPwdResultDTO;
import com.icicles.wmms.entity.param.SysUserQueryParam;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.SysParamService;
import com.icicles.wmms.service.SysRoleService;
import com.icicles.wmms.service.SysUserService;
import com.icicles.wmms.service.WsVillageService;
import com.icicles.wmms.utils.BLUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 人员信息 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2020-03-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Resource
    SysUserMapper sysUserMapper;
    @Resource
    private DataQueryAuthServiceImpl dataQueryAuthService;

    private SysParamService sysParamService;

    @Autowired
    public void setSysParamService(SysParamService sysParamService) {
        this.sysParamService = sysParamService;
    }
    @Resource
    private WsVillageService villageService;

    private SysRoleService sysRoleService;

    @Autowired
    public void setSysRoleService(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @Override
    public IPage<SysUser> findPage(Page page, SysUserQueryParam sysUserQueryParam,String account) throws ApiException {
        IPage<SysUser> retPage;
        try {
            SysUser sysUser = this.findByAccount(account);

            QueryWrapper<SysUser> queryWrapper = sysUserQueryParam.build();

            if(sysUser!=null){
                if(!dataQueryAuthService.isSuperAdmin(sysUser)){
                    queryWrapper.eq("village_id", sysUser.getVillageId());
                }
            }
//            queryWrapper.eq(StringUtils.isNotBlank(sysUserQueryParam.getName()), "name", sysUserQueryParam.getName());
            queryWrapper.like(StringUtils.isNotBlank(sysUserQueryParam.getName()),"name",sysUserQueryParam.getName());
            queryWrapper.like(StringUtils.isNotBlank(sysUserQueryParam.getPhone()), "phone", sysUserQueryParam.getPhone());
            queryWrapper.eq(StringUtils.isNotBlank(sysUserQueryParam.getLoginAccount()), "login_account", sysUserQueryParam.getLoginAccount());
            queryWrapper.eq(StringUtils.isNotBlank(sysUserQueryParam.getIdCard()), "id_card", sysUserQueryParam.getIdCard());
            queryWrapper.eq(StringUtils.isNotBlank(sysUserQueryParam.getIsValid()), "is_valid", sysUserQueryParam.getIsValid());
            queryWrapper.eq(StringUtils.isNotBlank(sysUserQueryParam.getType()), "type", sysUserQueryParam.getType());
            queryWrapper.eq(StringUtils.isNotBlank(sysUserQueryParam.getRemark()), "remark", sysUserQueryParam.getRemark());
            queryWrapper.eq(StringUtils.isNotBlank(sysUserQueryParam.getVillageSn()), "village_sn", sysUserQueryParam.getVillageSn());
            queryWrapper.lambda().select(
                    SysUser::getId, SysUser::getIsValid, SysUser::getLoginAccount, SysUser::getName,
                    SysUser::getPhone, SysUser::getRemark, SysUser::getType, SysUser::getVillageId,SysUser::getVillageSn,
                    SysUser::getIdCard,SysUser::getSn,
                    SysUser::getVillageName, SysUser::getCreateTime, SysUser::getUpdateTime
            );
            //处理村id
            retPage = this.page(page,queryWrapper);
            for(SysUser sysUser1:retPage.getRecords()){
                String typeNameTemp = "";
                if(StringUtils.isNotBlank(sysUser1.getType().toString())){
                    SysRole roleTemp = sysRoleService.findById(sysUser1.getType().toString());
                    if(roleTemp!=null){
                        typeNameTemp = roleTemp.getRoleName();
                    }
                }
                sysUser1.setTypeName(typeNameTemp);
            }
            logger.debug("查询用水监测管理平台用户信息表列表成功");
        } catch (Exception e) {
            logger.error("查询用水监测管理平台用户信息表列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询用水监测管理平台用户信息表列表异常", HttpStatus.BAD_REQUEST);
        }
        List<SysUser> u = retPage.getRecords();
        u = this.setVillageName(u);
        retPage.setRecords(u);
        return retPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUser sysUser) throws ApiException {
        try {
            if(StringUtils.isBlank(sysUser.getLoginAccount())){
                throw new ApiException("登录账号不能为空", HttpStatus.BAD_REQUEST);
            }
            if(this.findByAccount(sysUser.getLoginAccount())!=null){
                throw new ApiException("登录账号重复", HttpStatus.BAD_REQUEST);
            }
            // 密码加盐储存
            BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
            SysParam sysParam = sysParamService.findByKey("resetPassword");
            String password = sysUser.getLoginPass()==null?sysParam.getConfigValue():sysUser.getLoginPass();
            sysUser.setLoginPass(bcryptPasswordEncoder.encode(password));
            sysUser.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            sysUser.setSn(BLUtil.getUUID(true));
            if(sysUser.getVillageId()!=null&&sysUser.getVillageId()!=0){
                WsVillage village = villageService.findById(String.valueOf(sysUser.getVillageId()));
                if(village==null){
                    throw new ApiException("选择的村的信息不正确", HttpStatus.BAD_REQUEST);
                }
                sysUser.setVillageSn(village.getSn());
            }
            this.save(sysUser);
            logger.debug("添加人员信息成功" + sysUser.getId());
        } catch (ApiException e) {
            logger.error("添加人员信息错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加人员信息异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加人员信息异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            if(id.equals("1")){
                throw new ApiException("不允许删除管理员", HttpStatus.BAD_REQUEST);
            }
            this.removeById(id);
            logger.debug("删除人员信息成功" + id);
        } catch (Exception e) {
            logger.error("删除人员信息异常", e);
            e.printStackTrace();
            throw new ApiException("删除人员信息异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(SysUser sysUser) throws ApiException {
        try {
            if (sysUser.getId()==null) {
                throw new ApiException("编号不能为空", HttpStatus.BAD_REQUEST);
            }

            //把数据查出来
            QueryWrapper<SysUser> dbQuery = new QueryWrapper<>();
            dbQuery.lambda().eq(SysUser::getId,sysUser.getId()).select(SysUser::getLoginPass).last("limit 1");
            SysUser dbSys = sysUserMapper.selectOne(dbQuery);
            if(dbSys==null){
                throw new ApiException("用户信息不存在", HttpStatus.BAD_REQUEST);
            }

            if(StringUtils.isNotBlank(sysUser.getLoginPass())){
                BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
                SysParam sysParam = sysParamService.findByKey("resetPassword");
                String password = sysUser.getLoginPass()==null?sysParam.getConfigValue():sysUser.getLoginPass();
                sysUser.setLoginPass(bcryptPasswordEncoder.encode(password));
            }else{
                sysUser.setLoginPass(dbSys.getLoginPass());
            }

            if(sysUser.getVillageId()!=null&&sysUser.getVillageId()!=0){
                if(sysUser.getType()!=null){
                    SysRole role = sysRoleService.findById(sysUser.getType().toString());
                    if (role!=null){
                        if(!role.getRoleCode().equals(Constants.SUPER_ADMIN)){
                            WsVillage village = villageService.findById(String.valueOf(sysUser.getVillageId()));
                            if(village==null){
                                throw new ApiException("选择的村的信息不正确", HttpStatus.BAD_REQUEST);
                            }
                            sysUser.setVillageSn(village.getSn());
                        }
                    }
                }
            }

            UpdateWrapper<SysUser> wrapper = new UpdateWrapper();
            wrapper.eq("id",sysUser.getId());
            this.update(sysUser,wrapper);
            logger.debug("更新人员信息成功" + sysUser.getId());
        } catch (ApiException e) {
            logger.error("更新人员信息错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新人员信息异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新人员信息异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public SysUser findByAccount(String account) throws ApiException {
        SysUser sysUser;
        try {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("login_account",account);
            sysUser = this.getOne(queryWrapper);
            logger.debug("查询人员信息成功");
        } catch (Exception e) {
            logger.error("查询人员信息异常", e);
            e.printStackTrace();
            throw new ApiException("查询人员信息异常", HttpStatus.BAD_REQUEST);
        }
        return sysUser;
    }

    @Override
    public UseDefaultPwdResultDTO checkDefaultPwd(String userSn) throws ApiException {
        //根据用户标识获取用户信息
        SysUser u = this.findByUserSn(userSn);


        //是否在使用默认密码1在使用0未使用
        int defaultPwd = 0;
        //登录账号
        String account = "";

        if(u == null){
            throw new ApiException("查询人员信息异常", HttpStatus.BAD_REQUEST);
        }
        //获取默认密码
        SysParam param = sysParamService.findByKey(Constants.RESET_PASSWORD);
        if(param != null){
            // 密码加盐储存
            BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
            if(bcryptPasswordEncoder.matches(param.getConfigValue(),u.getLoginPass())){
                defaultPwd = 1;
            }
        }

        account = u.getLoginAccount();
        UseDefaultPwdResultDTO rs = new UseDefaultPwdResultDTO();
        rs.setLoginAccount(account);
        rs.setIsUserDefaultPwd(defaultPwd);
        return rs;
    }

    @Override
    public SysUser findById(String id) throws ApiException {
        SysUser sysUser;
        try {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysUser::getId, id);
            sysUser = sysUserMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询用水监测管理平台用户信息表成功");
        } catch (Exception e) {
            logger.error("根据编号查询用水监测管理平台用户信息表异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询用水监测管理平台用户信息表异常", HttpStatus.BAD_REQUEST);
        }
        //去掉敏感信息
        if(sysUser !=null){
            sysUser.setLoginPass("");
        }
        return sysUser;
    }

    @Override
    public List<SysUser> getAll() throws ApiException {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(
                SysUser::getId, SysUser::getIsValid, SysUser::getLoginAccount, SysUser::getName,
                SysUser::getPhone, SysUser::getRemark, SysUser::getType, SysUser::getVillageId,SysUser::getIdCard,SysUser::getSn,
                SysUser::getVillageName,SysUser::getVillageSn, SysUser::getCreateTime, SysUser::getUpdateTime
        );
        List<SysUser> u = sysUserMapper.selectList(queryWrapper);
        u = this.setVillageName(u);
        return u;
    }

    @Override
    public SysUser findByUserSn(String userSn) throws ApiException {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getSn,userSn).last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SysUser> setVillageName(List<SysUser> data) throws ApiException {
        if(CollUtil.isEmpty(data)){
            return null;
        }
        //涉及到的村信息
        List<WsVillage> villageList=null;
        //所有村的标识
        List<String> villageSnList = data.stream().map(SysUser::getVillageSn).collect(Collectors.toList());
        //去重
        if(CollUtil.isNotEmpty(villageSnList)){
            villageSnList = villageSnList.stream().distinct().collect(Collectors.toList());
            villageList = villageService.getListBySns(villageSnList);
        }
        if(CollUtil.isNotEmpty(villageList)){
            for (SysUser u:data){
                for (WsVillage v:villageList){
                    if(v.getSn().equals(u.getVillageSn())){
                        u.setVillageName(v.getVillageName());
                    }
                }
            }
        }
        return data;
    }

    @Override
    public List<SysUser> getAllBySn(Collection<String> sn) throws ApiException {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysUser::getSn,sn);
        return sysUserMapper.selectList(queryWrapper);
    }

    @Override
    public <T extends BasePo> void setUpdateUser(List<T> data) {
        if(CollUtil.isEmpty(data)){
            return;
        }
        //获取对象列表中的updateUserId
        List<String> userSns = data.stream().map(BasePo::getUpdateUserId).collect(Collectors.toList());
        //获取对象列表中的updateUserId
        List<String> createUserSns = data.stream().map(BasePo::getCreateUserId).collect(Collectors.toList());
        /*所有涉及到的用户*/
        //更新人
        List<SysUser> userList=null;
        //创建人
        List<SysUser> createUserList=null;
        //去重并查询
        if(CollUtil.isNotEmpty(userSns)){
            userSns = userSns.stream().distinct().collect(Collectors.toList());
            userList = this.getAllBySn(userSns);
            createUserList = this.getAllBySn(createUserSns);
        }

        //更新人是否为空
        boolean updateUsersFlag = CollUtil.isNotEmpty(userList);
        //创建人是否为空
        boolean createUserFlag = CollUtil.isNotEmpty(createUserList);

        for (T temp:data){
            if(updateUsersFlag){
                for (SysUser ut : userList) {
                    if(ut.getSn().equals(temp.getUpdateUserId())){
                        temp.setUpdateUserId(ut.getName());
                    }
                }
            }
            if(createUserFlag){
                for (SysUser ut2 : createUserList) {
                    if(ut2.getSn().equals(temp.getCreateUserId())){
                        temp.setCreateUserId(ut2.getName());
                    }
                }
            }
        }
    }

    @Override
    public SysRole getRoleByAccount(String account) {
        if(StringUtils.isBlank(account)){
            return null;
        }
        SysUser u = this.findByAccount(account);
        if(u==null||u.getType()==null){
            return null;
        }
        return sysRoleService.getById(u.getType());
    }

    @Override
    public SysRole getRoleByAccount(Principal principal) {
        return this.getRoleByAccount(principal.getName());
    }

    @Override
    public void changePwd(String sn,String password) {
        if(StringUtils.isBlank(password)){
            throw new ApiException("密码不能为空", HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(sn)){
            throw new ApiException("请先登录", HttpStatus.BAD_REQUEST);
        }
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        UpdateWrapper<SysUser> updateWrap = new UpdateWrapper<> ();
        updateWrap.lambda().eq(SysUser::getSn,sn);
        SysUser updateData = new SysUser();
        updateData.setLoginPass(bcryptPasswordEncoder.encode(password));
        sysUserMapper.update(updateData,updateWrap);
    }

    @Override
    public void addLoginNum(String loginAccount) {
        UpdateWrapper<SysUser> queryWrap = new UpdateWrapper<> ();
        queryWrap.lambda().eq(SysUser::getLoginAccount,loginAccount);
        queryWrap.setSql("login_number = login_number+"+1);
        SysUser user = new SysUser();
        sysUserMapper.update(user,queryWrap);
    }

    @Override
    public void multiDel(List<String> ids) {
        if(CollUtil.isEmpty(ids)){
            throw new ApiException("请选择需要删除的用户", HttpStatus.BAD_REQUEST);
        }
        if(ids.contains("1")){
            throw new ApiException("不允许删除管理员", HttpStatus.BAD_REQUEST);
        }
        QueryWrapper<SysUser> queryWrap = new QueryWrapper<> ();
        queryWrap.lambda().in(SysUser::getId,ids);
        sysUserMapper.delete(queryWrap);
    }
}
