package com.icicles.wmms.service.impl;

import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.form.SysRoleMenusForm;
import com.icicles.wmms.entity.po.SysMenu;
import com.icicles.wmms.entity.po.SysRole;
import com.icicles.wmms.entity.po.SysRoleMenus;
import com.icicles.wmms.entity.param.SysRoleMenusQueryParam;
import com.icicles.wmms.DAO.SysRoleMenusMapper;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.service.SysMenuService;
import com.icicles.wmms.service.SysRoleMenusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.service.SysRoleService;
import com.icicles.wmms.service.SysUserService;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@Service
public class SysRoleMenusServiceImpl extends ServiceImpl<SysRoleMenusMapper, SysRoleMenus> implements SysRoleMenusService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleMenusServiceImpl.class);

    @Resource
    SysRoleMenusMapper sysRoleMenusMapper;
    @Resource
    private SysRoleService roleService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysMenuService menuService;

    @Override
    public IPage<SysRoleMenus> findPage(Page page, SysRoleMenusQueryParam sysRoleMenusQueryParam) throws ApiException {
        IPage<SysRoleMenus> retPage;
        try {
            QueryWrapper<SysRoleMenus> queryWrapper = sysRoleMenusQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(sysRoleMenusQueryParam.getRoleId()), "role_id", sysRoleMenusQueryParam.getRoleId());
            queryWrapper.eq(StringUtils.isNotBlank(sysRoleMenusQueryParam.getMenusId()), "menus_id", sysRoleMenusQueryParam.getMenusId());
            retPage = this.page(page,queryWrapper);
            logger.debug("查询列表成功");
        } catch (Exception e) {
            logger.error("查询列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysRoleMenusForm sysRoleMenus,String updateUserId) throws ApiException {
        try {
            //角色id
            String roleId = sysRoleMenus.getRoleId();
            //是否已经有记录
            SysRoleMenus dbFlag = this.getDataByRoleId(roleId);
            if(dbFlag!=null){
                this.deleteDataByRoleId(roleId);
            }

            //所有菜单
            List<String> menuIds = sysRoleMenus.getMenusIds();
            if(menuIds==null||menuIds.isEmpty()){
                throw new ApiException("请选择角色对应的菜单", HttpStatus.BAD_REQUEST);
            }

            //保存数据
            this.saveAllData(roleId,menuIds);

            //更新最后操作人
            SysRole role = new SysRole();
            role.setId(Long.valueOf(roleId));
            role.setUpdateUserId(updateUserId);
            roleService.refresh(role);

            logger.debug("添加成功");
        } catch (ApiException e) {
            logger.error("添加错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除成功" + id);
        } catch (Exception e) {
            logger.error("删除异常", e);
            e.printStackTrace();
            throw new ApiException("删除异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(SysRoleMenusForm sysRoleMenus,String updateUserId) throws ApiException {
        try {
            this.add(sysRoleMenus,updateUserId);
            logger.debug("更新成功");
        } catch (ApiException e) {
            logger.error("更新错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public SysRoleMenus findById(String id) throws ApiException {
        SysRoleMenus sysRoleMenus;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            sysRoleMenus = sysRoleMenusMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询成功");
        } catch (Exception e) {
            logger.error("根据编号查询异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询异常", HttpStatus.BAD_REQUEST);
        }
        return sysRoleMenus;
    }

    @Override
    public Set<String> getMenuIds(String loginUserName) throws ApiException {
        //根据登录者的用户名获取角色
        SysRole role = this.getSysRoleInfo(loginUserName);
        if(role==null){
            throw new ApiException("账号角色异常，请联系管理员确认信息是否正确", HttpStatus.BAD_REQUEST);
        }
        Set<String> menuIds = new HashSet<>();
        if(role.getRoleCode().equals(Constants.SUPER_ADMIN)){
            //超级管理员展示所有菜单
            List<SysMenu> sysMenus = menuService.list();
            if(sysMenus!=null&&sysMenus.size()>0){
                for (SysMenu temp:
                        sysMenus) {
                    menuIds.add(String.valueOf(temp.getId()));
                }
            }
        }else{
            QueryWrapper<SysRoleMenus> queryWrapperRoleMenus = new QueryWrapper<>();
            queryWrapperRoleMenus.lambda().eq(SysRoleMenus::getRoleId,role.getId()).select(SysRoleMenus::getMenusId);
            List<SysRoleMenus> menus = sysRoleMenusMapper.selectList(queryWrapperRoleMenus);
            if(menus!=null&&menus.size()>0){
                for (SysRoleMenus temp:
                        menus) {
                    menuIds.add(temp.getMenusId());
                }
                /*还需要考虑，仅仅只指定了低级菜单，为了到达这个菜单，需要把菜单的所有父级都查询出来*/
                QueryWrapper<SysMenu> menuQueryWrapper = new QueryWrapper<>();
                menuQueryWrapper.lambda().in(SysMenu::getId,menuIds);
                List<SysMenu> menusInfo = menuService.list(menuQueryWrapper);
                for (SysMenu info:
                menusInfo) {
                    if(StringUtils.isNotBlank(info.getParentIds())){
                        String[] parentIdsTemp = info.getParentIds().split(",");
                        for (String idTemp:
                        parentIdsTemp) {
                            menuIds.add(idTemp);
                        }
                    }
                }
            }
        }
        return menuIds;
    }

    @Override
    public List<String> getMenuIdsByRoleId(String roleId) throws ApiException {
        List<String> data = new ArrayList<>();
        List<SysRoleMenus> roleMenus = this.getSysRoleMenusByRoleId(roleId);
        for (SysRoleMenus temp:
        roleMenus) {
            data.add(temp.getMenusId());
        }
        return data;
    }

    @Override
    public List<SysRoleMenus> getSysRoleMenusByRoleId(String roleId) throws ApiException {
        QueryWrapper<SysRoleMenus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRoleMenus::getRoleId,roleId).select(SysRoleMenus::getRoleId,SysRoleMenus::getMenusId);
        return sysRoleMenusMapper.selectList(queryWrapper);
    }

    /**
     * 批量保存信息
     * @param roleId 角色id
     * @param menus  菜单id
     */
    private void saveAllData(String roleId,List<String> menus){
        List<SysRoleMenus> data = new ArrayList<>();
        for (String id:
                menus) {
            SysRoleMenus newObj = new SysRoleMenus();
            if(StringUtils.isBlank(id)){
                throw new ApiException("菜单数据存在异常，请检查是否有空数据", HttpStatus.BAD_REQUEST);
            }
            newObj.setMenusId(id);
            newObj.setRoleId(roleId);
            data.add(newObj);
        }
        this.saveBatch(data);
    }

    private SysRoleMenus getDataByRoleId(String roleId){
        QueryWrapper<SysRoleMenus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRoleMenus::getRoleId,roleId).select(SysRoleMenus::getMenusId,SysRoleMenus::getRoleId).last("limit 1");
        return sysRoleMenusMapper.selectOne(queryWrapper);
    }
    private int deleteDataByRoleId(String roleId){
        QueryWrapper<SysRoleMenus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRoleMenus::getRoleId,roleId);
        return sysRoleMenusMapper.delete(queryWrapper);
    }

    /**
     * 根据登录者的用户名获取角色
     * @param loginUserName 用户名
     * @return 角色对象
     */
    private SysRole getSysRoleInfo(String loginUserName){
        if(StringUtils.isBlank(loginUserName)){
            throw new ApiException("请确保已经登录", HttpStatus.BAD_REQUEST);
        }
        SysUser user = sysUserService.findByAccount(loginUserName);
        if(user==null){
            throw new ApiException("账号异常，请联系管理员确认信息是否正确", HttpStatus.BAD_REQUEST);
        }
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRole::getId,user.getType());
        return roleService.getOne(queryWrapper);
    }
}
