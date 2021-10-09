package com.icicles.wmms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.icicles.wmms.entity.dto.WaterStatisticesDTO;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.entity.param.WsMeterReaderQueryParam;
import com.icicles.wmms.DAO.WsMeterReaderMapper;
import com.icicles.wmms.service.SysUserService;
import com.icicles.wmms.service.WsGroupService;
import com.icicles.wmms.service.WsMeterReaderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.service.WsVillageService;
import com.icicles.wmms.service.impl.excel.CommonExport;
import org.springframework.stereotype.Service;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 水表读数 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-06
 */
@Service
public class WsMeterReaderServiceImpl extends ServiceImpl<WsMeterReaderMapper, WsMeterReader> implements WsMeterReaderService {

    private static final Logger logger = LoggerFactory.getLogger(WsMeterReaderServiceImpl.class);

    @Resource
    WsMeterReaderMapper wsMeterReaderMapper;
    @Resource
    private WsVillageService villageService;
    @Resource
    private WsGroupService groupService;
    @Resource
    private SysUserService sysUserService;

    @Override
    public IPage<WsMeterReader> findPage(Page page, WsMeterReaderQueryParam wsMeterReaderQueryParam) throws ApiException {
        IPage<WsMeterReader> retPage;
        try {
            QueryWrapper<WsMeterReader> queryWrapper = wsMeterReaderQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getUserId()), "user_id", wsMeterReaderQueryParam.getUserId());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getUserSn()), "user_sn", wsMeterReaderQueryParam.getUserSn());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getUserName()), "user_name", wsMeterReaderQueryParam.getUserName());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getMeterId()), "meter_id", wsMeterReaderQueryParam.getMeterId());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getMeterSn()), "meter_sn", wsMeterReaderQueryParam.getMeterSn());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getVillageId()), "village_id", wsMeterReaderQueryParam.getVillageId());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getVillageName()), "village_name", wsMeterReaderQueryParam.getVillageName());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getNum()), "num", wsMeterReaderQueryParam.getNum());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getCreateId()), "create_id", wsMeterReaderQueryParam.getCreateId());
            queryWrapper.eq(StringUtils.isNotBlank(wsMeterReaderQueryParam.getUpdateId()), "update_id", wsMeterReaderQueryParam.getUpdateId());

            queryWrapper.lambda().orderByDesc(WsMeterReader::getId);
            retPage = this.page(page,queryWrapper);

            List<WsMeterReader> data = retPage.getRecords();

            //处理村名和表井名
            if(data!=null){
                //涉及到的村信息
                List<WsVillage> villageList=null;
                //涉及到的表井信息
                List<WsGroup> groupList=null;
                //涉及到的表井信息
                List<SysUser> userList=null;

                //所有村的标识
                List<String> villageSnList = data.stream().map(WsMeterReader::getVillageSn).collect(Collectors.toList());
                //去重
                if(CollUtil.isNotEmpty(villageSnList)){
                    villageSnList = villageSnList.stream().distinct().collect(Collectors.toList());
                    villageList = villageService.getListBySns(villageSnList);
                }
                //所有表井的标识
                List<String> groupSnList = data.stream().map(WsMeterReader::getGroupSn).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(groupSnList)){
                    groupSnList = groupSnList.stream().distinct().collect(Collectors.toList());
                    groupList = groupService.getGroupsBySns(groupSnList);
                }
                //所有用户标识
                List<String> userSnList = data.stream().map(WsMeterReader::getUserSn).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(userSnList)){
                    userSnList = userSnList.stream().distinct().collect(Collectors.toList());
                    userList = sysUserService.getAllBySn(userSnList);
                }

                for (WsMeterReader temp:data){
                    if(CollUtil.isNotEmpty(groupList)){
                        for (WsGroup g:groupList){
                            if(g.getSn().equals(temp.getGroupSn())){
                                temp.setGroupName(g.getGroupName());
                            }
                        }
                    }
                    if(CollUtil.isNotEmpty(villageList)){
                        for (WsVillage v:villageList){
                            if(v.getSn().equals(temp.getVillageSn())){
                                temp.setVillageName(v.getVillageName());
                            }
                        }
                    }
                    if(CollUtil.isNotEmpty(userList)){
                        for (SysUser u:userList){
                            if(u.getSn().equals(temp.getUserSn())){
                                temp.setUserName(u.getName());
                            }
                        }
                    }
                }
            }

            logger.debug("查询水表读数列表成功");
        } catch (Exception e) {
            logger.error("查询水表读数列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询水表读数列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WsMeterReader wsMeterReader) throws ApiException {
        try {
            this.save(wsMeterReader);
            logger.debug("添加水表读数成功" + wsMeterReader.getId());
        } catch (ApiException e) {
            logger.error("添加水表读数错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加水表读数异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加水表读数异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除水表读数成功" + id);
        } catch (Exception e) {
            logger.error("删除水表读数异常", e);
            e.printStackTrace();
            throw new ApiException("删除水表读数异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsMeterReader wsMeterReader) throws ApiException {
        try {
            UpdateWrapper<WsMeterReader> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsMeterReader.getId());
            this.update(wsMeterReader,wrapper);
            logger.debug("更新水表读数成功" + wsMeterReader.getId());
        } catch (ApiException e) {
            logger.error("更新水表读数错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新水表读数异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新水表读数异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsMeterReader findById(String id) throws ApiException {
        WsMeterReader wsMeterReader;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsMeterReader = wsMeterReaderMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询水表读数成功");
        } catch (Exception e) {
            logger.error("根据编号查询水表读数异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询水表读数异常", HttpStatus.BAD_REQUEST);
        }
        return wsMeterReader;
    }

    @Override
    public List<WaterStatisticesDTO> getWaterSumByMonth(String year,String userSn,String villageSn,String meterSn) {
        List<WaterStatisticesDTO> data = wsMeterReaderMapper.getWaterSumByMonth(year,userSn,villageSn,meterSn);
        return this.formatMonthData(data,year);
    }
    @Override
    public List<WaterStatisticesDTO> getWaterSumByYear(String userSn,String villageSn,String meterSn) {
        return wsMeterReaderMapper.getWaterSumByYear(userSn,villageSn,meterSn);
    }

    @Override
    public List<WaterStatisticesDTO> getWaterSumBySeason(String year,String userSn,String villageSn,String meterSn) {
        List<WaterStatisticesDTO> data = wsMeterReaderMapper.getWaterSumBySeason(year,userSn,villageSn,meterSn);
        return this.formatSeasonData(data,year);
    }

    @Override
    public void exportWaterSumByMonth(String year, String userSn, String villageSn, String meterSn, HttpServletResponse response) throws IOException {
        List<WaterStatisticesDTO> data = wsMeterReaderMapper.getWaterSumByMonth(year,userSn,villageSn,meterSn);
        //数据模板（设置excel头和导出的字段）
        ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp = new ArrayList<>();
        AbstractMap.SimpleEntry<String,String> data3 = new AbstractMap.SimpleEntry<>("date", "月份");
        AbstractMap.SimpleEntry<String,String> data4 = new AbstractMap.SimpleEntry<>("num", "用水量");
        dataTemp.add(data3);
        dataTemp.add(data4);

        CommonExport<WaterStatisticesDTO> exportFun = new CommonExport<>(data,dataTemp);
        exportFun.exportExcel(year+"月用水量统计",response);
    }

    @Override
    public void exportWaterSumBySeason(String year, String userSn, String villageSn, String meterSn,HttpServletResponse response) throws IOException {
        List<WaterStatisticesDTO> data = wsMeterReaderMapper.getWaterSumBySeason(year,userSn,villageSn,meterSn);
        //数据模板（设置excel头和导出的字段）
        ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp = new ArrayList<>();
        AbstractMap.SimpleEntry<String,String> data3 = new AbstractMap.SimpleEntry<>("date", "季度");
        AbstractMap.SimpleEntry<String,String> data4 = new AbstractMap.SimpleEntry<>("num", "用水量");
        dataTemp.add(data3);
        dataTemp.add(data4);

        CommonExport<WaterStatisticesDTO> exportFun = new CommonExport<>(data,dataTemp);
        exportFun.exportExcel(year+"季度用水量统计",response);
    }
    @Override
    public void exportWaterSumByYear(String userSn, String villageSn, String meterSn,HttpServletResponse response) throws IOException {
        List<WaterStatisticesDTO> data = wsMeterReaderMapper.getWaterSumByYear(userSn,villageSn,meterSn);
        //数据模板（设置excel头和导出的字段）
        ArrayList<AbstractMap.SimpleEntry<String,String>> dataTemp = new ArrayList<>();
        AbstractMap.SimpleEntry<String,String> data3 = new AbstractMap.SimpleEntry<>("date", "年份");
        AbstractMap.SimpleEntry<String,String> data4 = new AbstractMap.SimpleEntry<>("num", "用水量");
        dataTemp.add(data3);
        dataTemp.add(data4);

        CommonExport<WaterStatisticesDTO> exportFun = new CommonExport<>(data,dataTemp);
        exportFun.exportExcel("年度用水量统计",response);
    }

    /**
     * 获取用水记录表（ws_meter_reader）中某个设备的最新记录
     * @param sn 设备的唯一标识
     * @return 最新记录
     */
    private WsMeterReader getLastRecord(String sn){
        QueryWrapper<WsMeterReader> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WsMeterReader::getMeterSn,sn).orderByDesc(WsMeterReader::getId).last("limit 1");
        return wsMeterReaderMapper.selectOne(queryWrapper);
    }

    /**
     * 格式化月份数据（必须12个月）
     * @param dbData 数据库查询出来的数据
     * @param year   年份
     * @return
     */
    private List<WaterStatisticesDTO> formatMonthData(List<WaterStatisticesDTO> dbData,String year){
        List<WaterStatisticesDTO> yearData = new ArrayList<>();
        for (int i=1;i<=12;i++){
            WaterStatisticesDTO newData = new WaterStatisticesDTO();
            String month;
            if(i<=9){
                month = "0"+i;
            }else{
                month = String.valueOf(i);
            }
            newData.setDate(year+"-"+month);
            newData.setNum(BigDecimal.ZERO);
            yearData.add(newData);
        }
        for (WaterStatisticesDTO temp:
                dbData) {
            for (WaterStatisticesDTO formatData:
                    yearData) {
                if(formatData.getDate().equals(temp.getDate())){
                    formatData.setNum(temp.getNum());
                }
            }
        }
        return yearData;
    }

    /**
     * 格式化季度数据（必须四个季度）
     * @param dbData 数据库查询出来的数据
     * @param year   年份
     * @return
     */
    private List<WaterStatisticesDTO> formatSeasonData(List<WaterStatisticesDTO> dbData,String year){
        List<WaterStatisticesDTO> yearData = new ArrayList<>();
        for (int i=1;i<=4;i++){
            WaterStatisticesDTO newData = new WaterStatisticesDTO();
            newData.setDate(year+i);
            newData.setNum(BigDecimal.ZERO);
            yearData.add(newData);
        }
        for (WaterStatisticesDTO temp:
                dbData) {
            for (WaterStatisticesDTO formatData:
                    yearData) {
                if(formatData.getDate().equals(temp.getDate())){
                    formatData.setNum(temp.getNum());
                }
            }
        }
        return yearData;
    }
}
