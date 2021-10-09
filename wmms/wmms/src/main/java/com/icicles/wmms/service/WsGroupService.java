package com.icicles.wmms.service;

import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.entity.po.WsGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsGroupQueryParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icicles.wmms.exception.ApiException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 表井 服务类
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
public interface WsGroupService extends IService<WsGroup> {
      /**
       * 根据条件分页查询表井
       *
       * @param page 分页对象
       * @return IPage<WsGroup>   表井列表（分页）
       */
       IPage<WsGroup> findPage(Page page, WsGroupQueryParam wsGroupQueryParam, Principal principal) throws ApiException;

      /**
       * 增加表井
       *
       * @param wsGroup 表井
       * @throws ApiException 异常信息
       */
      void add(WsGroup wsGroup) throws ApiException;

      /**
      * 删除表井
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新表井
      *
      * @param wsGroup 表井
      * @throws ApiException 异常信息
      */
      void refresh(WsGroup wsGroup) throws ApiException;

      /**
      * 根据编号查询表井
      *
      * @param id 编号
      * @return WsGroup 表井
      * @throws ApiException 异常信息
      */
      WsGroup findById(String id) throws ApiException;

      /**
       * 根据名称查询表井
       *
       * @param name 组名称
       * @return villageId 村id
       * @return WsGroup   表井
       * @throws ApiException 异常信息
       */
      WsGroup findByName(String villageId,String name) throws ApiException;
      /**
       * 根据序列号查询表井
       *
       * @param sn 组名称
       * @return villageId 村id
       * @return WsGroup   表井
       * @throws ApiException 异常信息
       */
      WsGroup findBySn(String villageId,String sn) throws ApiException;

      /**
       * 根据序列号查询表井
       *
       * @param sn 组名称
       * @return villageId 村id
       * @return WsGroup   表井
       * @throws ApiException 异常信息
       */
      WsGroup findBySn(String sn) throws ApiException;

      /**
       * 导出excel
       */
      void exportExcel(List<String> items, HttpServletResponse response) throws IOException;

      /**
       * 导入excel
       */
      ExcelResultDTO importExcel(MultipartFile file);

      /**
       * 处理最后修改人的名称
       * @param groups
       * @return
       */
      List<WsGroup> showCreateUserName(List<WsGroup> groups);

      /**
       * 根据表井列表获取对应的表井信息
       * @param sn 表井列表
       * @return
       */
      List<WsGroup> getGroupsBySns(Collection<String> sn);

      /**
       * 为表井设置管理的村的名称
       * @param data 表井列表
       * @return
       */
      List<WsGroup> setVillageName(List<WsGroup> data);
}
