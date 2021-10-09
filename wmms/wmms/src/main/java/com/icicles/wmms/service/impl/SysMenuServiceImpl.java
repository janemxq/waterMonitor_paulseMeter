package com.icicles.wmms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.form.SysMenuQueryForm;
import com.icicles.wmms.entity.po.SysMenu;
import com.icicles.wmms.entity.param.SysMenuQueryParam;
import com.icicles.wmms.DAO.SysMenuMapper;
import com.icicles.wmms.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.service.SysRoleMenusService;
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

import java.security.Principal;
import java.util.ArrayList;
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
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private static final Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);

    @Resource
    SysMenuMapper sysMenuMapper;
    @Resource
    private SysRoleMenusService roleMenusService;

    @Override
    public List<SysMenu> menus(SysMenuQueryForm sysMenuQueryParam, Principal principal) throws ApiException {

        List<SysMenu> retPage;
        //角色对应的菜单id
        Set<String> menus = roleMenusService.getMenuIds(principal.getName());
        if(menus==null||menus.size()<=0){
            throw new ApiException("无法获取菜单", HttpStatus.BAD_REQUEST);
        }
        try {
            QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
            if(sysMenuQueryParam!=null){
                queryWrapper.eq(StringUtils.isNotBlank(sysMenuQueryParam.getName()), "name", sysMenuQueryParam.getName());
                queryWrapper.eq(StringUtils.isNotBlank(sysMenuQueryParam.getParentId()), "parent_id", sysMenuQueryParam.getParentId());
                queryWrapper.eq(StringUtils.isNotBlank(sysMenuQueryParam.getIsValid()), "is_valid", sysMenuQueryParam.getIsValid());
                queryWrapper.like(StringUtils.isNotBlank(sysMenuQueryParam.getRemark()), "remark", sysMenuQueryParam.getRemark());
            }
            queryWrapper.lambda().in(SysMenu::getId,menus).eq(SysMenu::getIsValid,1).orderByAsc(SysMenu::getSerial);
            retPage = this.list(queryWrapper);
            logger.debug("查询列表成功");
        } catch (Exception e) {
            logger.error("查询列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    public IPage<SysMenu> findPage(Page page,SysMenuQueryParam sysMenuQueryParam) throws ApiException {
        IPage<SysMenu> retPage;
        //为了避免分页问题，只能查询一级菜单
        sysMenuQueryParam.setParentId("-1");
        try {
            QueryWrapper<SysMenu> queryWrapper = sysMenuQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(sysMenuQueryParam.getLabel()), "label", sysMenuQueryParam.getLabel());
            queryWrapper.like(StringUtils.isNotBlank(sysMenuQueryParam.getName()), "name", sysMenuQueryParam.getName());
            queryWrapper.eq(StringUtils.isNotBlank(sysMenuQueryParam.getParentId()), "parent_id", sysMenuQueryParam.getParentId());
            queryWrapper.like(StringUtils.isNotBlank(sysMenuQueryParam.getRemark()), "remark", sysMenuQueryParam.getRemark());
            retPage = this.page(page,queryWrapper);
            //处理成树状结构
            List<SysMenu> rs = new ArrayList<>();

            List<SysMenu> data = retPage.getRecords();
            if(!data.isEmpty()){
                for (SysMenu t:
                data) {
                    rs.add(t);
                    QueryWrapper<SysMenu> childsQuery = new QueryWrapper<>();
                    childsQuery.lambda().apply("FIND_IN_SET({0}, parent_ids)",t.getId());
                    List<SysMenu> dataTemp = sysMenuMapper.selectList(childsQuery);
                    if(dataTemp!=null&&dataTemp.size()>0){
                        for (SysMenu dbData:
                        dataTemp) {
                            rs.add(dbData);
                        }
                    }
                }
            }
            List<SysMenu> tree = buildTree(rs);
            retPage.setRecords(tree);
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
    public void add(SysMenu sysMenu) throws ApiException {
        try {
            //设置父级id
            sysMenu.setParentIds(this.setAllParentIds(sysMenu.getParentId()));
            this.save(sysMenu);
            logger.debug("添加成功" + sysMenu.getId());
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
    public void refresh(SysMenu sysMenu) throws ApiException {
        try {
            if(StringUtils.isNotBlank(sysMenu.getParentIds())){
                sysMenu.setParentIds(this.setAllParentIds(sysMenu.getParentIds()));
            }
            UpdateWrapper<SysMenu> wrapper = new UpdateWrapper();
            wrapper.eq("id",sysMenu.getId());
            this.update(sysMenu,wrapper);
            logger.debug("更新成功" + sysMenu.getId());
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
    public SysMenu findById(String id) throws ApiException {
        SysMenu sysMenu;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            sysMenu = sysMenuMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询成功");
        } catch (Exception e) {
            logger.error("根据编号查询异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询异常", HttpStatus.BAD_REQUEST);
        }
        return sysMenu;
    }

    @Override
    public List<SysMenu> getChildMenu(Long id) throws ApiException {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysMenu::getParentId,id);
        return sysMenuMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysMenu> getListByIds(List<String> ids) throws ApiException {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SysMenu::getId,ids);
        return sysMenuMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysMenu> menusTreeByRoleId(String roleId, Principal principal) throws ApiException {
        //登录用户拥有的菜单
        List<SysMenu> menus = this.menus(null,principal);
        //角色对应的菜单
        List<String> menusIds = roleMenusService.getMenuIdsByRoleId(roleId);

        if(menusIds.isEmpty()){
            for (SysMenu menu:
                    menus) {
                menu.setCheckedFlag(0);
            }
        }else{
            for (SysMenu menu:
                    menus) {
                String menuId = String.valueOf(menu.getId());
                if(menusIds.contains(menuId)){
                    menu.setCheckedFlag(1);
                }else{
                    menu.setCheckedFlag(0);
                }
            }
        }
        List<SysMenu> menus2 = this.buildTree(menus);
        //为父级添加CheckedFlag的值
        this.setCheckedForParent(menus2);
        return menus2;
    }

    @Override
    public List<SysMenu> menusTree(SysMenuQueryForm sysMenuQueryParam, Principal principal) throws ApiException {
        List<SysMenu> menus = this.menus(sysMenuQueryParam,principal);
        return this.buildTree(menus);
    }

    /**
     * 把一个List转成树
     * @param list
     * @return
     */
    private List<SysMenu> buildTree(List<SysMenu> list){
        List<SysMenu> tree= new ArrayList<>();
        for(SysMenu node:list){
            if(node.getParentId().equals(Constants.MENU_PID)){
                tree.add(findChild(node,list));
            }
        }
        return tree;
    }

    /**
     * 如果有子菜单，给父级添加CheckedFlag属性
     * @return
     */
    private void setCheckedForParent(SysMenu menu,List<SysMenu> menus){
        if(CollUtil.isEmpty(menus)){
            return;
        }
        for (SysMenu menu2 : menus) {
            if(menu2.getCheckedFlag()==1){
                menu.setCheckedFlag(1);
            }
            setCheckedForParent(menu2,menu2.getChildMenu());
        }
    }
    /**
     * 如果有子菜单，给父级添加CheckedFlag属性
     * @return
     */
    private void setCheckedForParent(List<SysMenu> menus){
        for (SysMenu menu : menus) {
            this.setCheckedForParent(menu,menu.getChildMenu());
        }
    }

    private SysMenu findChild(SysMenu node, List<SysMenu> list){
        for(SysMenu n:list){
            if(n.getParentId().equals(String.valueOf(node.getId()))){
                if(node.getChildMenu() == null){
                    node.setChildMenu(new ArrayList<>());
                }
                node.getChildMenu().add(findChild(n,list));
            }
        }
        return node;
    }



    /**
     * 将上级的所有id处理成用逗号隔开的字符串的形式
     * @param pid
     * @return
     */
    private String setAllParentIds(String pid){
        if(StringUtils.isBlank(pid)||Constants.MENU_PID.equals(pid)){
            return "";
        }
        SysMenu menu = this.findById(pid);
        if(menu==null){
            throw new ApiException("上级菜单不存在", HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(menu.getParentIds())){
            return pid;
        }else{
            return pid+","+menu.getParentIds();
        }
    }
}
