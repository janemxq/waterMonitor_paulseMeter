package com.icicles.wmms.service.impl.excel;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.dto.ExcelResultDTO;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 弃用
 * 通过excel来初始化用户和水表信息
 * 只能一个村一个村的导入！！！！
 * excel模板查看resources/excel/userandmeter.xlsx,修改时要注意对应关系
 * @author lisy
 */
@Service
public class DataInit extends AbstractExcelDealService {
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
    public DataInit(List data, ArrayList<AbstractMap.SimpleEntry<String, String>> dataTemp) {
        super(data, dataTemp);
    }
    public DataInit(){
        super(null,null);
    }
    /**
     * Excel 文件上传解析
     * @param file Excel文件
     * @return
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
            Excel07SaxReader reader = new Excel07SaxReader(new InitRowHandler(rs,null));
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
     * excel表列的对应关系
     * @return 列的下标代表的意义
     */
    private Map<Integer,String> getSpansMap(){
        Map<Integer,String> dataMap = new HashMap<>();
        dataMap.put(0,"表井名称");
        dataMap.put(1,"表井地址");
        dataMap.put(2,"通道号");
        dataMap.put(3,"码盘值");
        dataMap.put(4,"累计用水量");
        dataMap.put(5,"脉冲/水方");
        dataMap.put(6,"用水类型");
        dataMap.put(7,"用户名");
        dataMap.put(8,"登录帐号");
        dataMap.put(9,"身份证号");
        dataMap.put(10,"手机号码");
        return dataMap;
    }

    @Override
    protected RowHandler createRowHandler(ExcelResultDTO rs) {
        return null;
    }
    private class InitRowHandler implements RowHandler{
        private ExcelResultDTO importRs;
        private WsVillage village;
        private String defaultPassword;
        private Map<String, WsFeeStandard> standardMap = new HashMap<> ();

        private InitRowHandler(ExcelResultDTO rs, WsVillage village){
            this.importRs = rs;
            this.village = village;
            SysParam defaultParams = paramService.findByKey(Constants.RESET_PASSWORD);
            if(defaultParams!=null){
                defaultPassword = defaultParams.getConfigValue();
            }
        }

        @Override
        public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
            if(rowList.isEmpty()){
                return;
            }
            //使用第一个sheet1
            if (sheetIndex == 0) {
                if (rowIndex > 1) {
                    if(village==null){
                        this.addErrorLog(rowIndex+" 村庄信息不正确");
                        throw new ApiException(rowIndex+" 信息不正确", HttpStatus.BAD_REQUEST);
                    }
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
                }else if (rowIndex == 1){
                    /*标题不处理*/
                }else {
                    /*处理第一行的村的信息*/
                    try {
                        this.dealVillage(rowList,rowIndex);
                    }catch (Exception e) {
                        logger.error(e.getMessage());
                        this.addErrorLog(e.getMessage());
                    }
                }
            }
        }

        /**
         * 处理村的信息
         * @param rowList excel中读取的村的新
         * @param rowIndex 第几行
         * @return 处理结果
         * @throws ApiException
         */
        private void dealVillage(List<Object> rowList,long rowIndex) throws ApiException{
            if(village!=null){
                return;
            }
            //村名
            String villageName = rowList.get(1).toString();
            logger.info("{}:村名{}",rowIndex,villageName);
            if(StrUtil.isBlank(villageName)){
                throw new ApiException(rowIndex+" 无法获取村的名字", HttpStatus.BAD_REQUEST);
            }
            QueryWrapper<WsVillage> villageQueryWrap = new QueryWrapper<>();
            villageQueryWrap.lambda().eq(WsVillage::getVillageName,villageName).last("limit 1");
            WsVillage villageNew = villageService.getOne(villageQueryWrap);
            if(villageNew==null){
                WsVillage addVillage = new WsVillage();
                //excel中填写的地址
                String villageAddressFf = rowList.get(3).toString();
                //保存村的信息
                addVillage.setVillageName(villageName);
                addVillage.setAddress(villageAddressFf);
                addVillage.setSn(IdUtil.simpleUUID());
                villageService.add(addVillage);
                this.village = addVillage;
            }else{
                this.village = villageNew;
            }
        }

        /**
         * 处理正常的导入数据
         * @param rowList  excel中读取的导入的数据
         * @param rowIndex 第几行
         * @return 1成功0失败
         * @throws ApiException
         */
        private int dealExcel(List<Object> rowList,long rowIndex) throws ApiException{
            if(village==null){
                throw new ApiException(rowIndex+" 无法获取村庄的信息，请先完成村庄信息的完善", HttpStatus.BAD_REQUEST);
            }
            /*处理用户信息*/
            //登录名称
            String loginName = rowList.get(8).toString();
            //用户名
            String userName = rowList.get(7).toString();
            //身份证号
            String idCard = rowList.get(9).toString();
            //手机号码
            String phone = rowList.get(10).toString();
            SysUser sysUser = this.addUser(rowIndex,loginName,userName,idCard,phone);
            /*处理收费标准*/
            //收费标准的名称
            String feeStandardName = rowList.get(6).toString();
            if(StrUtil.isBlank(feeStandardName)){
                throw new ApiException(rowIndex+" 用水类型不正确，不能保存水表数据", HttpStatus.BAD_REQUEST);
            }
            WsFeeStandard standard = this.standardMap.get(feeStandardName);
            if(standard==null){
                QueryWrapper<WsFeeStandard> standardQueryWrapper = new QueryWrapper<WsFeeStandard> ();
                standardQueryWrapper.lambda().eq(WsFeeStandard::getDisplayName,feeStandardName).last("limit 1");
                standard = standardService.getOne(standardQueryWrapper);
                if(standard==null){
                    throw new ApiException(rowIndex+" 用水类型不正确，不能保存水表数据", HttpStatus.BAD_REQUEST);
                }
                this.standardMap.put(standard.getDisplayName(),standard);
            }
            /*处理水表信息*/
            //表井名称
            String yardName = rowList.get(0).toString();
            //表井地址
            String yardAddress = rowList.get(1).toString();
            //通道号
            String number = rowList.get(2).toString();
            //码盘值
            String meterNum = rowList.get(3).toString();
            //累计用水量
            String waterSum = rowList.get(4).toString();
            //脉冲
            String pulse = rowList.get(5).toString();
            //保存水表记录
            this.addMeter(rowIndex,sysUser,standard,yardName,yardAddress,number,meterNum,waterSum,pulse);
            return 1;
        }

        /**
         * 添加用户
         * @param rowIndex   excel的第几行
         * @param loginName  登录名称（唯一）
         * @param userName   用户名称
         * @param idCard    省份证号
         * @param phone     手机号
         * @return 成功返回记录，失败返回null
         */
        private SysUser addUser(long rowIndex,String loginName,String userName,String idCard,String phone){
            if(StrUtil.isBlank(loginName)){
                return null;
            }
            if(StrUtil.isBlank(userName)||StrUtil.isBlank(idCard)){
                throw new ApiException(rowIndex+" 确保用户名、手机号、身份证号全部不能为空", HttpStatus.BAD_REQUEST);
            }
            SysUser dbUser = userService.findByAccount(loginName);
            if(dbUser!=null){
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
        private void addMeter(long rowIndex,SysUser sysUser,WsFeeStandard standard,String yardName,String yardAddress,String number,String meterNum,String waterSum,String pulse){
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
            //查询数据库中的表井信息
            QueryWrapper<WsGroup> groupQueryWrapper = new QueryWrapper<> ();
            groupQueryWrapper.lambda().eq(WsGroup::getGroupName,yardName)
                    .eq(WsGroup::getAddress,yardAddress)
                    .last("limit 1");
            WsGroup yard = groupService.getOne(groupQueryWrapper);
            if(yard==null){
                WsGroup group = new WsGroup();
                group.setGroupName(yardName);
                group.setVillageId(village.getId());
                group.setVillageName(village.getVillageName());
                group.setSn(IdUtil.simpleUUID());
                group.setAddress(yardAddress);
                group.setVillageSn(village.getSn());
                groupService.add(group);
                yard = group;
            }else{
                if(!yard.getVillageSn().equals(village.getSn())){
                    throw new ApiException(rowIndex+" 相同的表井名称和通道地址出现在不同的村", HttpStatus.BAD_REQUEST);
                }
            }
            //保存数据
            WsMeter meter = new WsMeter();
            meter.setActivationTime(DateUtil.format(LocalDateTime.now(),"yyyy-MM-dd"));
            meter.setVillageSn(village.getSn());
            meter.setGroupSn(yard.getSn());
            //码盘值
            meter.setWaterSum(new BigDecimal(meterNum));
            //累计用水量
            meter.setMeterNum(new BigDecimal(waterSum));
            //初始码盘值
            meter.setPulseInit(Integer.parseInt(meterNum));

            meter.setMacSn(number);
            meter.setMeterTypeSn(standard.getSn());
            meter.setPulse(pulse);
            meter.setSn(IdUtil.simpleUUID());
            if(sysUser!=null){
                meter.setUserSn(sysUser.getSn());
            }
            meterService.add(meter);
        }
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
}
