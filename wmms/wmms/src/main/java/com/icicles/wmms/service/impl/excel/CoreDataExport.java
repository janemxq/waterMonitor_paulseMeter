package com.icicles.wmms.service.impl.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.*;
import com.icicles.wmms.utils.MyExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 2020.09.23
 * 核心数据（通过excel）导入导出功能
 * @author lisy
 */
@Service
public class CoreDataExport extends AbstractExcelDealService {
    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 村 服务类
     */
    @Resource
    private WsVillageService villageService;
    /**
     * 水表设备服务类
     */
    @Resource
    private WsMeterService meterService;
    /**
     * 表井服务类
     */
    @Resource
    private WsGroupService groupService;
    /**
     * 用户服务类
     */
    @Resource
    private SysUserService userService;
    /**
     * 系统参数服务
     */
    @Resource
    private SysParamService paramService;
    /**
     * 收费标准（用水类型）
     */
    @Resource
    private WsFeeStandardService standardService;

    /**
     * 构造函数，传入两个必须的数据
     *
     * @param data     导入或者导出的数据
     * @param dataTemp 数据模板：其中，key代表属性名称，value代表导出的excel的头
     */
    public CoreDataExport(List data, ArrayList<AbstractMap.SimpleEntry<String, String>> dataTemp) {
        super(data, dataTemp);
    }
    public CoreDataExport(){
        super(null,null);
    }

    /**
     * 导出excel（从浏览器下载）
     * @param response        响应类
     * @throws IOException    io异常
     */
    public void exportExcel(HttpServletResponse response,String villageSn) throws IOException{
        List<String> titleList = new ArrayList<> (20);
        for (int i=0;i<=16;i++){
            titleList.add(this.getSpansMap().get(i));
        }
        List<List<String>> dataList = new ArrayList<> ();

        new exportExcelHandler(dataList).addData(villageSn);

        MyExcelUtils.exportExcel(response,"",titleList,dataList);
    }

    /**
     * 导入数据
     * Excel 文件上传解析
     * @param file Excel文件
     * @return 导入结果
     */
    @Override
    public ExcelResultDTO importExcel(MultipartFile file){
        if (file.isEmpty()) {
            throw new ApiException("请上传文件", HttpStatus.BAD_REQUEST);
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if(!".xlsx".equals(suffixName)){
            throw new ApiException("格式不正确，请上传后缀是.xlsx的文件", HttpStatus.BAD_REQUEST);
        }

        // 文件上传路径
        String filePath = System.getProperty("user.dir");
        //文件名称
        String fileRename = UUID.randomUUID().toString().replace("-","") + suffixName;
        //全路径
        String fullFileName = filePath + "/excelfiles/"+fileRename;

        File dest = new File(fullFileName);
        //导入结果
        ExcelResultDTO rs = new ExcelResultDTO();
        try {
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            //读取excel文件
            Excel07SaxReader reader = new Excel07SaxReader(new InitRowHandler(rs));
            reader.read(fullFileName, -1);
        }catch (Exception e) {
            throw new ApiException("导入失败，请联系管理员", HttpStatus.BAD_REQUEST);
        }finally {
            //删除文件
            dest.delete();
        }
        return rs;
    }

    /**
     * excel表列的对应关系(如果调整，涉及导入导出，最好在后面添加原来的顺序不要调整)
     * @return 列的下标代表的意义
     */
    private Map<Integer,String> getSpansMap(){
        Map<Integer,String> dataMap = new HashMap<>(20);
        dataMap.put(0,"村庄编号");
        dataMap.put(1,"村名");
        dataMap.put(2,"村庄地址");
        dataMap.put(3,"用户名");
        dataMap.put(4,"用户账号");
        dataMap.put(5,"身份证号");
        dataMap.put(6,"手机号");
        dataMap.put(7,"表井编号");
        dataMap.put(8,"表井名称");
        dataMap.put(9,"表井设备地址");
        dataMap.put(10,"水表编号");
        dataMap.put(11,"水表通道");
        dataMap.put(12,"初始码盘值");
        dataMap.put(13,"水表脉冲值");
        dataMap.put(14,"水表读数");
        dataMap.put(15,"缴费类型");
        dataMap.put(16,"余额");
        return dataMap;
    }

    @Override
    protected RowHandler createRowHandler(ExcelResultDTO rs) {
        return null;
    }

    /**
     * excel导入
     */
    private class InitRowHandler implements RowHandler{
        /**
         * 处理结果
         */
        private ExcelResultDTO importRs;
        /**
         * 默认密码
         */
        private String defaultPassword;
        /**
         * 收费标准的容器
         */
        private Map<String, WsFeeStandard> standardMap = new HashMap<> ();
        /**
         * 村庄的容器
         */
        private Map<String, WsVillage> villagesMap = new HashMap<> ();

        /**
         * 构造函数，完成初始化
         * @param rs 处理结果
         */
        private InitRowHandler(ExcelResultDTO rs){
            this.importRs = rs;
            SysParam defaultParams = paramService.findByKey(Constants.RESET_PASSWORD);
            if(defaultParams!=null){
                defaultPassword = defaultParams.getConfigValue();
            }
        }

        @Override
        public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
            if(CollUtil.isEmpty(rowList)){
                return;
            }
            //值使用第一个sheet1，其他的不是用
            if (sheetIndex == 0) {
                //跳过第一行
                if (rowIndex > 0) {
                    //处理的总记录数量
                    int sumNumTemp = importRs.getSum()+1;
                    importRs.setSum(sumNumTemp);
                    try {
                        int f = this.dealExcel(rowList,rowIndex);
                        if (f <= 0) {
                            int errSumNumTemp = importRs.getError()+1;
                            importRs.setError(errSumNumTemp);
                        }
                    } catch (Exception e) {
                        //错误的记录数量
                        int errSumNumTemp = importRs.getError()+1;
                        importRs.setError(errSumNumTemp);
                        logger.error(e.getMessage());
                        this.addErrorLog(e.getMessage());
                    }
                }else {
                    logger.info("标题行，不处理");
                }
            }
        }

        /**
         * 处理正常的导入数据
         * @param rowList  excel中读取的导入的数据
         * @param rowIndex 第几行
         * @return 1成功0失败
         * @throws ApiException 异常
         */
        private int dealExcel(List<Object> rowList,long rowIndex) throws ApiException{
            /*处理村庄信息*/

            String villageSnRealTemp = rowList.get(0).toString();
            if(StringUtils.isBlank(villageSnRealTemp)){
                throw new ApiException(rowIndex+" 村庄信息缺失", HttpStatus.BAD_REQUEST);
            }
            WsVillage v = this.villagesMap.get(villageSnRealTemp);
            if(v == null){
                v = villageService.findBySn(villageSnRealTemp);
                if(v == null){
                    //村庄名称
                    String villageRealNameString = rowList.get(1).toString();
                    //村庄地址
                    String villageAddressRealString = rowList.get(2).toString();
                    WsVillage newVillage = new WsVillage();
                    newVillage.setVillageName(villageRealNameString);
                    newVillage.setAddress(villageAddressRealString);
                    newVillage.setSn(villageSnRealTemp);
                    villageService.add(newVillage);
                    v = newVillage;
                    villagesMap.put(villageSnRealTemp,v);
                }else{
                    villagesMap.put(villageSnRealTemp,v);
                }
            }


            /*处理用户信息*/

            //登录名称
            String loginName = rowList.get(4).toString();
            //用户名
            String userName = rowList.get(3).toString();
            //身份证号
            String idCard = rowList.get(5).toString();
            //手机号码
            String phone = rowList.get(6).toString();
            SysUser sysUser = this.userHandle(rowIndex,loginName,userName,idCard,phone,v);


            /*处理收费标准*/

            //收费标准的名称
            String feeStandardName = rowList.get(15).toString();
            if(StrUtil.isBlank(feeStandardName)){
                throw new ApiException(rowIndex+" 用水类型不正确，不能保存水表数据", HttpStatus.BAD_REQUEST);
            }
            //先从缓存中获取
            WsFeeStandard standard = this.standardMap.get(feeStandardName);
            if(standard==null){
                QueryWrapper<WsFeeStandard> standardQueryWrapper = new QueryWrapper<> ();
                standardQueryWrapper.lambda().eq(WsFeeStandard::getDisplayName,feeStandardName).last("limit 1");
                standard = standardService.getOne(standardQueryWrapper);
                if(standard==null){
                    throw new ApiException(rowIndex+" 用水类型不正确，不能保存水表数据", HttpStatus.BAD_REQUEST);
                }
                this.standardMap.put(standard.getDisplayName(),standard);
            }


            /*处理水表信息*/

            //表井名称
            String yardName = rowList.get(8).toString();
            //表井地址
            String yardAddress = rowList.get(9).toString();
            //通道号
            String number = rowList.get(11).toString();
            //表井编号
            String yardSn = rowList.get(7).toString();
            //初始码盘值
            String meterNum = rowList.get(12).toString();
            //水表读数（总用水量）
            String waterSum = rowList.get(14).toString();
            //脉冲
            String pulse = rowList.get(13).toString();
            //余额
            String balance = rowList.get(16).toString();
            //水表编号
            String meterSn = rowList.get(10).toString();
            //保存水表记录
            this.meterHandle(rowIndex,sysUser,standard,yardName,yardAddress,number,meterNum,waterSum,pulse,v,yardSn,balance,meterSn);
            return 1;
        }

        /**
         * 处理用户
         * @param rowIndex   excel的第几行
         * @param loginName  登录名称（唯一）
         * @param userName   用户名称
         * @param idCard    省份证号
         * @param phone     手机号
         * @return 成功返回记录，失败返回null
         */
        private SysUser userHandle(long rowIndex,String loginName,String userName,String idCard,String phone,WsVillage village){
            if(StrUtil.isBlank(loginName)){
                return null;
            }
            if(StrUtil.isBlank(userName)||StrUtil.isBlank(idCard)){
                throw new ApiException(rowIndex+" 确保用户名、手机号、身份证号全部不能为空", HttpStatus.BAD_REQUEST);
            }
            SysUser dbUser = userService.findByAccount(loginName);
            if(dbUser!=null){
                //更新数据
                UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<> ();
                updateWrapper.lambda().eq(SysUser::getId,dbUser.getId());
                SysUser uuu = new SysUser();
                uuu.setName(userName);
                uuu.setPhone(phone);
                uuu.setIdCard(idCard);
                uuu.setVillageName(village.getVillageName());
                uuu.setVillageId(village.getId().intValue());
                uuu.setVillageSn(village.getSn());
                userService.update(uuu,updateWrapper);
                return dbUser;
            }
            SysUser sysUser = new SysUser();
            sysUser.setName(userName);
            sysUser.setIdCard(idCard);
            sysUser.setPhone(phone);
            sysUser.setLoginAccount(loginName);
            sysUser.setLoginPass(defaultPassword);

            sysUser.setVillageId(village.getId().intValue());
            sysUser.setVillageName(village.getVillageName());
            userService.add(sysUser);
            return sysUser;
        }
        /**
         * 添加水表
         * @param rowIndex       行号
         * @param sysUser        用户信息
         * @param standard       用水类型（收费标准）
         * @param yardName       表井名称
         * @param yardAddress    表井地址
         * @param number        通道号
         * @param meterNum      码盘值
         * @param waterSum      总的用水量
         * @param pulse        脉冲值
         */
        private void meterHandle(long rowIndex,SysUser sysUser,
                              WsFeeStandard standard,String yardName,
                              String yardAddress,String number,String meterNum,
                              String waterSum,String pulse,WsVillage village,
                                 String yardSn,String balance,String meterSn){

            if(StrUtil.isBlank(yardSn)){
                throw new ApiException(rowIndex+" 表井标识不能为空", HttpStatus.BAD_REQUEST);
            }
            if(StrUtil.isBlank(yardName)){
                throw new ApiException(rowIndex+" 表井名称不能为空", HttpStatus.BAD_REQUEST);
            }
            if(StrUtil.isBlank(yardAddress)){
                throw new ApiException(rowIndex+" 表井地址不能为空", HttpStatus.BAD_REQUEST);
            }
            if(StrUtil.isBlank(number)){
                throw new ApiException(rowIndex+" 通道号不能为空", HttpStatus.BAD_REQUEST);
            }
            if(StrUtil.isBlank(meterNum)){
                throw new ApiException(rowIndex+" 码盘值不能为空", HttpStatus.BAD_REQUEST);
            }
            if(StrUtil.isBlank(waterSum)){
                throw new ApiException(rowIndex+" 总的用水量不能为空", HttpStatus.BAD_REQUEST);
            }
            if(StrUtil.isBlank(pulse)){
                throw new ApiException(rowIndex+" 脉冲值不能为空", HttpStatus.BAD_REQUEST);
            }
            if(StrUtil.isBlank(balance)){
                balance = "0";
            }

            //查询数据库中的表井信息
            WsGroup yard = groupService.findBySn(yardSn);
            //组装数据
            WsGroup group = new WsGroup();
            group.setGroupName(yardName);
            group.setVillageId(village.getId());
            group.setVillageName(village.getVillageName());
            group.setSn(yardSn);
            group.setAddress(yardAddress);
            group.setVillageSn(village.getSn());
            if(yard==null){
                groupService.add(group);
                yard = group;
            }else {
                group.setId(yard.getId());
                groupService.refresh(group);
            }

            //查询水表信息
            WsMeter dbMeter = meterService.findBySn(meterSn);
            //组装数据
            WsMeter meter = new WsMeter();
            meter.setVillageSn(village.getSn());
            meter.setGroupSn(yard.getSn());
            //码盘值
            meter.setWaterSum(new BigDecimal(meterNum));
            //累计用水量
            meter.setMeterNum(new BigDecimal(waterSum));
            //初始码盘值
            meter.setPulseInit(new BigDecimal(meterNum).intValue());

            meter.setMacSn(number);
            meter.setMeterTypeSn(standard.getSn());
            meter.setPulse(pulse);
            meter.setSn(meterSn);
            if(sysUser!=null){
                meter.setUserSn(sysUser.getSn());
            }
            meter.setBalance(new BigDecimal(balance));

            if(dbMeter==null){
                meter.setActivationTime(DateUtil.format(LocalDateTime.now(),"yyyy-MM-dd"));
                meterService.add(meter);
            }else{
                meter.setId(dbMeter.getId());
                meterService.refresh(meter);
            }
        }

        /**
         * 添加错误日志
         * @param msg 错误信息
         */
        private void addErrorLog(String msg){
            if(StrUtil.isBlank(msg)){
                return;
            }
            msg = DateUtil.now()+" "+msg+"\r\n";
            String usrHome = System.getProperty("user.home")+"/wmms/init/logs/";
            String fileName = usrHome+DateUtil.format(LocalDateTime.now(),"yyyyMMdd")+".log";
            FileWriter writer = new FileWriter(fileName);
            writer.write(msg,true);
        }
    }
    /**
     * excel导出
     */
    private class exportExcelHandler{
        /**
         * 数据容器
         */
        private List<List<String>> dataList = null;
        /**
         * 村信息缓存
         */
        private Map<String,WsVillage> villageCache = new HashMap<> ();
        /**
         * 收费标准缓存
         */
        private Map<String,WsFeeStandard> standardCache = new HashMap<> ();


        private exportExcelHandler(List<List<String>> dataList){
            this.dataList = dataList;
        }
        /**
         * 向dataList中添加数据
         */
        private void addData(String villageSn){
            this.addData(0,0,villageSn);
        }

        /**
         * 向dataList中添加数据
         */
        private void addData(int start,int length,String villageSn){

            List<WsMeter> meters;
            QueryWrapper<WsMeter> queryWrapper = new QueryWrapper<> ();
            if(StringUtils.isNotBlank(villageSn)){
                queryWrapper.lambda().eq(WsMeter::getVillageSn,villageSn);
            }
            if(length<=0){
                meters = meterService.list(queryWrapper);
            }else{
                if(start<0){
                    start = 0;
                }
                queryWrapper.lambda().last("limit "+start+","+length);
                meters = meterService.list(queryWrapper);
            }
            if(CollUtil.isEmpty(meters)){
                return;
            }
            for (WsMeter meter : meters) {
                /*关联的用户信息*/
                SysUser u = userService.findByUserSn(meter.getUserSn());


                /*关联的村信息*/
                WsVillage v = this.villageCache.get(meter.getVillageSn());
                if(v == null){
                    v = villageService.findBySn(meter.getVillageSn());
                    if(v != null){
                        //把村信息缓存起来
                        this.villageCache.put(meter.getVillageSn(),v);
                    }
                }


                /*关联的收费标准信息*/
                WsFeeStandard standard = standardCache.get(meter.getMeterTypeSn());
                if(standard == null){
                    standard = standardService.findBySn(meter.getMeterTypeSn());
                    if (standard != null) {
                        //把收费类型缓存起来
                        this.standardCache.put(meter.getMeterTypeSn(),standard);
                    }
                }


                /*关联的表井信息*/
                WsGroup yard = groupService.findBySn(meter.getGroupSn());

                /*需要的信息*/

                //村名字
                String villageNameForData = "";
                //村地址
                String villageAddressForData = "";
                if(v!=null){
                    villageNameForData = v.getVillageName();
                    villageAddressForData = v.getAddress();
                }
                //用户名
                String userNameForData = "";
                //登录名
                String loginNameForData = "";
                //身份证号
                String idCardForData = "";
                //手机号
                String phoneForData = "";
                if(u!=null){
                    userNameForData = u.getName();
                    loginNameForData = u.getLoginAccount();
                    idCardForData = u.getIdCard();
                    phoneForData = u.getPhone();
                }
                //缴费类型
                String standardNameForData = "";
                if(standard!=null){
                    standardNameForData = standard.getDisplayName();
                }
                //表井名称
                String yardNameForData = "";
                //表井设备地址
                String yardAddressForData = "";
                if(yard!=null){
                    yardNameForData = yard.getGroupName();
                    yardAddressForData = yard.getAddress();
                }

                List<String> temp = new ArrayList<> ();
                temp.add(meter.getVillageSn());
                temp.add(villageNameForData);
                temp.add(villageAddressForData);
                temp.add(userNameForData);
                temp.add(loginNameForData);
                temp.add(idCardForData);
                temp.add(phoneForData);
                temp.add(meter.getGroupSn());
                temp.add(yardNameForData);
                temp.add(yardAddressForData);
                temp.add(meter.getSn());
                temp.add(meter.getMacSn());
                temp.add(meter.getWaterSum().toString());
                temp.add(meter.getPulse());
                temp.add(meter.getMeterNum().toString());
                temp.add(standardNameForData);
                temp.add(meter.getBalance().toString());

                this.dataList.add(temp);
            }
        }
    }
}
