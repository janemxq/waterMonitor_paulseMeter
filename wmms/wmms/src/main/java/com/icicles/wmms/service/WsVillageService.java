package com.icicles.wmms.service;

import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.entity.dto.GroupMetersDTO;
import com.icicles.wmms.entity.dto.VillageMetersDTO;
import com.icicles.wmms.entity.po.WsVillage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsVillageQueryParam;
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
 * 村庄 服务类
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
public interface WsVillageService extends IService<WsVillage> {
      /**
       * 根据条件分页查询村庄
       *
       * @param page 分页对象
       * @return IPage<WsVillage>   村庄列表（分页）
       */
       IPage<WsVillage> findPage(Page page, WsVillageQueryParam wsVillageQueryParam, Principal principal) throws ApiException;

      /**
       * 增加村庄
       *
       * @param wsVillage 村庄
       * @throws ApiException 异常信息
       */
      void add(WsVillage wsVillage) throws ApiException;

      /**
      * 删除村庄
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新村庄
      *
      * @param wsVillage 村庄
      * @throws ApiException 异常信息
      */
      void refresh(WsVillage wsVillage) throws ApiException;

      /**
      * 根据编号查询村庄
      *
      * @param id 编号
      * @return WsVillage 村庄
      * @throws ApiException 异常信息
      */
      WsVillage findById(String id) throws ApiException;

      /**
       * 根据序列号查询村庄
       *
       * @param sn 序列号
       * @return WsVillage 村庄
       * @throws ApiException 异常信息
       */
      WsVillage findBySn(String sn) throws ApiException;

      /**
       * 导出excel
       */
      void exportExcel(List<String> items, HttpServletResponse response) throws IOException;

      /**
       * 导入excel
       */
      ExcelResultDTO importExcel(MultipartFile file);

      /**
       * 根据名称查询信息
       *
       * @param name 编号
       * @return WsVillage 村庄
       * @throws ApiException 异常信息
       */
      WsVillage findByName(String name) throws ApiException;

      /**
       * 根据村的sn列表对应的村的信息
       * @param sn 村标识列表
       * @return
       */
      List<WsVillage> getListBySns(Collection<String> sn);
}
