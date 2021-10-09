package com.icicles.wmms.utils;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icicles.wmms.exception.CustomException;
import gnu.io.SerialPort;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CommixUtil {

    // 配置地址码
    static final int ADDRESS_CONFIG = 0x01;

    // 配置水表缓冲水量
    static final int ACCUMULATED_VAL_CONFIG = 0x02;

    // 配置多少脉冲对应一方水
    static final int PULSE_CONFIG = 0x03;

    // 配置水表码值
    static final int CODE_VAL_CONFIG = 0x04;


    // 读取多个水表
    static final int MANY_READ = 0x11;

    // 读取指定水表码值
    static final int SINGLE_READ = 0x12;

    // 读取指定水表码值
    static final int FAIL = 0x00;

    // 读取指定水表码值
    static final int SUCCESS = 0x01;

    /**
     * 设置表井地址
     *
     * @param hearder 帧头（十六进制）
     * @param toAddress 目的地址（十六进制）
     * @param fromAddress 源地址（十六进制）
     * @param newAddress 新地址（十六进制）
     * @return 成功失败
     */
    public boolean configAddress(String commName,int baudrate,int hearder,int toAddress,int fromAddress,
                                        int newAddress,String isTest) throws CustomException {
            String data =  String.format("%04x",newAddress);
            String command_ret = encodeCommandStr(ADDRESS_CONFIG,hearder,toAddress,fromAddress,data);
            System.out.println("执行发送串口命令： "+ command_ret);
            String ret;
            if(isTest.equals("1")){
                ret = sendCommand1(command_ret);

            }else{
                ret = comSend(command_ret,commName,baudrate);
            }
            System.out.println("串口返回数据： "+ ret);
            if(StringUtils.isNotBlank(ret)){
                String retData = decodeCommandStr(ret);
                return retDeal(retData);
            }else{
                return false;
            }
    }

    /**
     * 设置预存水
     *
     * @param hearder 帧头（十六进制）
     * @param toAddress 目的地址（十六进制）
     * @param fromAddress 源地址（十六进制）
     * @param dataMap 通道-index（十六进制） 预留水量-val（十进制）
     * @return 成功失败
     */
    public boolean configVal(String commName, int baudrate, int hearder, int toAddress, int fromAddress,
                             Map<Integer,Integer> dataMap, String isTest) throws CustomException{
        String data = "";
        for (Map.Entry<Integer, Integer> entry : dataMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            data = data + String.format("%02x",entry.getKey())+String.format("%06x",entry.getValue());
        }
        System.out.println("执行发送串口数据："+data);
        String command_ret = encodeCommandStr(ACCUMULATED_VAL_CONFIG,hearder,toAddress,fromAddress,data);
        System.out.println("执行发送串口命令： "+ command_ret);
        String ret;
        if(isTest.equals("1")){
            ret = sendCommand1(command_ret);

        }else{
            ret = comSend(command_ret,commName,baudrate);
        }
        System.out.println("串口返回数据： "+ ret);
        if(StringUtils.isNotBlank(ret)){
            String retData = decodeCommandStr(ret);
            return retDeal(retData);
        }else{
            return false;
        }
    }

    /**
     *  发送指令至串口
     *  没有获得就继续发送，连续发送5次，监听到数据为止
     * @param command 指令
     * @param commName  端口
     * @param baudrate  频率
     */
    private String comSend(String command,String commName,int baudrate) throws CustomException{
        SerialPort mSerialport;
        StringBuffer flg = new StringBuffer();
        StringBuffer ret = new StringBuffer();
        try{
            mSerialport = SerialPortManager.openPort(commName, baudrate);
        }catch (CustomException e){
            throw new CustomException("串口打开失败");
        }
        System.out.println("发送第"+1+"次命令："+command);
        SerialPortManager.sendToPort(mSerialport, ByteUtils.hexStr2Byte(command));
        SerialPortManager.DataAvailableListener dataAvailableListener = new SerialPortManager.DataAvailableListener() {
            @Override
            public void dataAvailable() {
                // 读取串口数据
                byte[] data = SerialPortManager.readFromPort(mSerialport);
                if(StringUtils.isNotBlank(ByteUtils.byteArrayToHexString(data))){
                    flg.append("1");
                    ret.delete(0,ret.length());
                    ret.append(ByteUtils.byteArrayToHexString(data));
                }
                System.out.println(ByteUtils.byteArrayToHexString(data));
            }
        };
        try{
            // 添加监听
            SerialPortManager.addListener(mSerialport, dataAvailableListener);
            for (int i = 0; i <= 5 && flg.length()<=0; i++) {
                // 发送命令
                Thread.sleep(3000);
                if(flg.length()<=0){
                    System.out.println("发送第"+(i+2)+"次命令："+command);
                    SerialPortManager.sendToPort(mSerialport, ByteUtils.hexStr2Byte(command));
                }
            }
            if(flg.length()>0){
                return ret.toString();
            }else{
                System.out.println("命令发送失败未回复");
                return "";
            }
        }catch (Exception e){

        }finally {
            try {
                SerialPortManager.removeListener(mSerialport,new SerialPortManager.SerialPortListener(dataAvailableListener));
                SerialPortManager.closePort(mSerialport);
            }catch (Exception e){

            }
        }
        return "";
    }

    /**
     * 设置脉冲
     *
     * @param hearder 帧头（十六进制）
     * @param toAddress 目的地址（十六进制）
     * @param fromAddress 源地址（十六进制）
     * @param dataMap 通道-index（十六进制） 一方水脉冲-pulse（十进制）
     * @return 成功失败
     */
    public boolean configPulse(String commName, int baudrate, int hearder,int toAddress,int fromAddress,
                               Map<Integer,Integer> dataMap, String isTest) throws CustomException{
        String data = "";
        for (Map.Entry<Integer, Integer> entry : dataMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            data = data + String.format("%02x",entry.getKey())+String.format("%04x",entry.getValue());
        }
        System.out.println("执行发送串口数据："+data);
        String command_ret = encodeCommandStr(PULSE_CONFIG,hearder,toAddress,fromAddress,data);
        System.out.println("执行发送串口命令： "+ command_ret);
        String ret;
        if(isTest.equals("1")){
            ret = sendCommand1(command_ret);

        }else{
            ret = comSend(command_ret,commName,baudrate);
        }
        System.out.println("串口返回数据： "+ ret);
        if(StringUtils.isNotBlank(ret)){
            String retData = decodeCommandStr(ret);
            return retDeal(retData);
        }else{
            return false;
        }
    }

    /**
     * 设置码值
     *
     * @param hearder 帧头（十六进制）
     * @param toAddress 目的地址（十六进制）
     * @param fromAddress 源地址（十六进制）
     * @param dataMap 通道-index（十六进制） 码盘值-codeVal（十进制）
     * @return 成功失败
     */
    public boolean configCodeVal(String commName, int baudrate, int hearder,int toAddress,int fromAddress,
                                 Map<Integer,Integer> dataMap, String isTest) throws CustomException {
        String data = "";
        for (Map.Entry<Integer, Integer> entry : dataMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            data = data + String.format("%02x",entry.getKey())+String.format("%06x",entry.getValue());
        }
        System.out.println("执行发送串口数据："+data);
        String command_ret = encodeCommandStr(CODE_VAL_CONFIG,hearder,toAddress,fromAddress,data);
        System.out.println("执行发送串口命令： "+ command_ret);
        String ret;
        if(isTest.equals("1")){
            ret = sendCommand1(command_ret);

        }else{
            ret = comSend(command_ret,commName,baudrate);
        }
        System.out.println("串口返回数据： "+ ret);
        if(StringUtils.isNotBlank(ret)){
            String retData = decodeCommandStr(ret);
            return retDeal(retData);
        }else{
            return false;
        }
    }

    /**
     * 读指定个数表
     *
     * @param hearder 帧头（十六进制）
     * @param toAddress 目的地址（十六进制）
     * @param fromAddress 源地址（十六进制）
     * @param num 个数
     * @return 数据
     */
    public JSONArray readList(String commName, int baudrate,  int hearder, int toAddress,
                                             int fromAddress, int num,String isTest) throws CustomException{
        String numStr =  String.format("%02x",num);
        String command_ret = encodeCommandStr(MANY_READ,hearder,toAddress,fromAddress,numStr);
        System.out.println("执行发送串口命令： "+ command_ret);
        String ret;
        if(isTest.equals("1")){
            ret = sendCommand1(command_ret);
        }else{
            ret = comSend(command_ret,commName,baudrate);
        }
        System.out.println("串口返回数据： "+ ret);
        if(StringUtils.isNotBlank(ret)){
            String retData = decodeCommandStr(ret);
            return readDealAll(retData);
        }else{
            return null;
        }
    }

    /**
     * 读指定表
     *
     * @param hearder 帧头（十六进制）
     * @param toAddress 目的地址（十六进制）
     * @param fromAddress 源地址（十六进制）
     * @param index 通道索引（十六进制）
     * @return 数据
     */
    public JSONObject read(String commName, int baudrate, int hearder, int toAddress, int fromAddress,
                                         int index,String isTest) throws CustomException{
        String indexStr =  String.format("%02x",index);
        String command_ret = encodeCommandStr(SINGLE_READ,hearder,toAddress,fromAddress,indexStr);
        System.out.println("执行发送串口命令： "+ command_ret);
        String ret;
        if(isTest.equals("1")){
            ret = sendCommand1(command_ret);
        }else{
            ret = comSend(command_ret,commName,baudrate);
        }
        System.out.println("串口返回数据： "+ ret);
        if(StringUtils.isNotBlank(ret)){
            String retData = decodeCommandStr(ret);
            return readDeal(retData);
        }else{
            return null;
        }
    }

    /**
     * 获取命令行
     *
     * @param command 命令字（十六进制）
     * @param hearder 帧头（十六进制）
     * @param toAddress 目的地址（十六进制）
     * @param fromAddress 源地址（十六进制）
     * @param data 数据（十六进制）
     * @return 成功失败
     */
    public String encodeCommandStr(int command,int hearder,int toAddress,
                                          int fromAddress,String data)  throws CustomException {
        String command_ret;
        String hearder_str = String.format("%04x",hearder);
        String toAddress_str = String.format("%04x",toAddress);
        String fromAddress_str = String.format("%04x",fromAddress);
        String command_str = String.format("%02x",command);
        int len = (toAddress_str.length()+fromAddress_str.length()+command_str.length()+data.length()+2)/2;
        String len_str = String.format("%02x",len);
        command_ret = hearder_str + len_str + toAddress_str
                + fromAddress_str + command_str + data ;
        command_ret = command_ret + getCheckStr(command_ret);
        return command_ret;
    }
    /**
     * 获取命令行
     *
     * @param ret 结果（十六进制）
     * @return 返回数据
     */
    private String decodeCommandStr(String ret) throws CustomException{
        ret = ret.replaceAll(" ","").trim();
        // 获取前四个字节是否为
        if(!StringUtils.upperCase(ret).substring(0,4).equals("FDDF")){
            return String.format("%02x",FAIL);
        }
        // 判断数据完整性
        if(!checkStr(ret)){
            return String.format("%02x",FAIL);
        }
        // 获取长度
        Integer len = Integer.parseInt(StringUtils.upperCase(ret).substring(4,6), 16);
        return StringUtils.upperCase(ret).substring(16,len*2+4);
    }

    /**
     * 获取校验位
     *
     * @param command 命令（十六进制）
     * @return 返回数据
     */
    public String getCheckStr(String command) throws CustomException {
        command = command.replaceAll(" ","").trim();
        if(command.length()<=4){
            throw new CustomException("命令长度不正确");
        }else{
            // 判断是否带头
            int count = 0;
            String str;
            str = command;
            if(command.substring(0,4).toUpperCase().equals("FDDF")){
                str = command.substring(4,command.length());
            }
            if(str.length()%2!=0){
                throw new CustomException("命令长度不正确");
            }else{
                for (int i = 0; i < str.length()/2; i++) {
                    count = count + Integer.parseInt(str.substring(i*2,(i+1)*2), 16);
                }
                String countStr = String.format("%02x",count);
                if(countStr.length()>2){
                    System.out.println(countStr.substring(countStr.length()-2));
                    return countStr.substring(countStr.length()-2);
                }else{
                    return countStr;
                }
            }
        }
    }

    /**
     * 校验
     *
     * @param command 命令（十六进制）
     * @return 返回数据
     */
    public Boolean checkStr(String command) throws CustomException {
        if(command.length()<=4){
            return false;
        }else {
            if (command.length() % 2 != 0) {
                return false;
            } else {
                String checkStr = getCheckStr(command.substring(0,command.length() - 2));
                if (StringUtils.upperCase(command.substring(command.length() - 2)).equals(StringUtils.upperCase(checkStr))) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private Boolean retDeal(String ret) {
        if(ret.equals(String.format("%02x",SUCCESS))){
            return true;
        }else{
            return false;
        }
    }

    private static JSONObject readDeal(String ret) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index",Integer.parseInt(StringUtils.upperCase(ret).substring(0,1), 16));
        jsonObject.put("codeVal",Integer.parseInt(StringUtils.upperCase(ret).substring(1,6), 16));
        jsonObject.put("val",Integer.parseInt(StringUtils.upperCase(ret).substring(6,14), 16));
        jsonObject.put("pulse",Integer.parseInt(StringUtils.upperCase(ret).substring(14,ret.length()), 16));
        return jsonObject;
    }

    private static JSONArray readDealAll(String ret) {

        JSONArray jsonArray = new JSONArray();
        int num = Integer.parseInt(StringUtils.upperCase(ret).substring(0,2), 16);
        System.out.println(ret);
        System.out.println(ret.length());
        for(int i = 0;i<num;i++){
            System.out.println(ret.substring(2+(i*14),2+((i+1)*14)));
            String retStr = ret.substring(2+(i*14),2+((i+1)*14));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("index",Integer.parseInt(StringUtils.upperCase(retStr).substring(0,1), 16));
            jsonObject.put("codeVal",Integer.parseInt(StringUtils.upperCase(retStr).substring(1,6), 16));
            jsonObject.put("val",Integer.parseInt(StringUtils.upperCase(retStr).substring(6,14), 16));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private static List<Map<String,Object>> retReadDeal(String ret) {
        List<Map<String,Object>> retMapList = new ArrayList<>();
        ret = ret.replaceAll(" ","").trim();
        if(ret.length()<=14){
            Map<String,Object> map= new HashMap<>();
            map.put("index",Integer.parseInt(StringUtils.upperCase(ret).substring(0,1), 16));
            map.put("code",Integer.parseInt(StringUtils.upperCase(ret).substring(1,6), 16));
            map.put("val",Integer.parseInt(StringUtils.upperCase(ret).substring(6,14), 16));
            retMapList.add(map);
        }else{
            List<String> retList = new ArrayList<>();
            for (int i = 0; i < (ret.length()/14); i++) {
                ret = ret.substring(i,i+14);
                Map<String,Object> map= new HashMap<>();
                map.put("index",Integer.parseInt(StringUtils.upperCase(ret).substring(0,1), 16));
                map.put("code",Integer.parseInt(StringUtils.upperCase(ret).substring(1,6), 16));
                map.put("val",Integer.parseInt(StringUtils.upperCase(ret).substring(6,14), 16));
                retMapList.add(map);
            }
        }
        return retMapList;
    }



    public static void main(String[] args) throws Exception {
        //System.out.println(String.format("%02x",15));
        //configVal(0xFDDF,0x0101,0x0000,0x0001);
        /*String hearder_str = String.format("%04x",Integer.valueOf("FDDF",16));
        System.out.println(Long.parseLong("FFFFFD2F", 16));

        System.out.printf("0x%X", Integer.parseInt("FDDF", 16));*/
        //System.out.println(Integer.parseInt("0xFDDF", 16));
        //System.out.println(Integer.parseInt("8", 16));
        //System.out.println(decodeCommandStr("FDDF07000001010101"));
        //System.out.println(decodeCommandStr("FDDF 0D 0000 0101 12 xaaaaa bbbbbbbb"));
        /*CommixUtil commixUtil= new CommixUtil();
        commixUtil.sendCommand("FDDF070102000012011D");*/

        CommixUtil commixUtil = new CommixUtil();
        /*String aa = commixUtil.getCheckStr("FDDF07010200001201FFFFFFFFFFFF0001000101");
        boolean bb = commixUtil.checkStr("FDDF07010200001201FFFFFFFFFFFF00010001011a");
        System.out.println(aa);
        System.out.println(bb);*/
        //commixUtil.read(0xFDDF, 0x0103, 0x0000, 1);


        // 测试设置新地址
        /*Boolean ret = commixUtil.configAddress("COM5",9600,0xFDDF,
                0x0103,0x0000,0x0102,"0");*/
        // 测试设置预存水
        Map<Integer,Integer> map = new HashMap<>();
        //map.put(0x0F,16);
        /*map.put(0x0F,200);
        Boolean ret = commixUtil.configVal("COM4",9600,0xFDDF,
                0x0102,0x0000,map,"0");*/
        /*Boolean ret = commixUtil.configPulse("COM5",9600,0xFDDF,
                0x0103,0x0000,map,"1");*/
        /*Boolean ret = commixUtil.configCodeVal("COM5",9600,0xFDDF,
                0x0103,0x0000,map,"1");*/
        JSONObject ret = commixUtil.read("COM4",9600,0xFDDF,
                0x0102,0x0000,0x01,"0");
        /*JSONArray ret = commixUtil.readList("COM4",9600,0xFDDF,
                0x0102,0x0000,0x10,"0");*/


        System.out.println(ret.toJSONString());
    }


    /**
     * 模拟串口返回数据
     * @param commandStr
     */
    private static String sendCommand1(String commandStr) throws CustomException {
        // 获取帧头（十六进制）
        commandStr = commandStr.replaceAll(" ","").trim();
        String hearder_str = StringUtils.upperCase(commandStr).substring(0,4);
        // 长度
        Integer len = Integer.parseInt(StringUtils.upperCase(commandStr).substring(4,6), 16);
        // 目的地址（十六进制）
        String toAddress_str = StringUtils.upperCase(commandStr).substring(6,10);
        // 源地址（十六进制）
        String fromAddress_str= StringUtils.upperCase(commandStr).substring(10,14);
        // 命令（十六进制）
        String command_str= StringUtils.upperCase(commandStr).substring(14,16);
        // 数据（十六进制）
        String data = StringUtils.upperCase(commandStr).substring(16,len*2+4);
        CommixUtil commixUtil = new CommixUtil();
        if(command_str.equals(String.format("%02x",ADDRESS_CONFIG))){
            String ret_str = hearder_str+"07" + fromAddress_str + data + command_str + "01";
            return ret_str + commixUtil.getCheckStr(ret_str);
        }
        if(command_str.equals(String.format("%02x",ACCUMULATED_VAL_CONFIG))){
            String ret_str = hearder_str+"07" + fromAddress_str + toAddress_str + command_str + "01";
            return ret_str + commixUtil.getCheckStr(ret_str);
        }
        if(command_str.equals(String.format("%02x",PULSE_CONFIG))){
            String ret_str = hearder_str+"07" + fromAddress_str + toAddress_str + command_str + "01";
            return ret_str + commixUtil.getCheckStr(ret_str);
        }
        if(command_str.equals(String.format("%02x",CODE_VAL_CONFIG))){
            String ret_str = hearder_str+"07" + fromAddress_str + toAddress_str + command_str + "01";
            return ret_str + commixUtil.getCheckStr(ret_str);
        }
        if(command_str.equals(String.format("%02x",MANY_READ))){
            // 造数据
            // 获取月日
            Date date = new Date();
            int monthDate = DateUtil.month(date)*31*24+ DateUtil.dayOfMonth(date)*24+DateUtil.hour(date,true);

            monthDate = monthDate-(5*31*24+11*24+23);
            String retData  = String.format("%01x",Integer.valueOf(data,16)) + String.format("%05x",monthDate) + String.format("%08x",monthDate-20)+ String.format("%06x",monthDate*100);
            String ret_str = hearder_str+ "10" + fromAddress_str + toAddress_str + command_str + retData;
            return ret_str + commixUtil.getCheckStr(ret_str);
        }
        if(command_str.equals(String.format("%02x",SINGLE_READ))){
            // 造数据
            // 获取月日
            Date date = new Date();
            int monthDate = DateUtil.month(date)*31*24+ DateUtil.dayOfMonth(date)*24+DateUtil.hour(date,true);

            monthDate = monthDate-(5*31*24+11*24+23);
            String retData  = String.format("%01x",Integer.valueOf(data,16)) + String.format("%05x",monthDate) + String.format("%08x",monthDate-20)+ String.format("%06x",monthDate*100);
            String ret_str = hearder_str+ "10" + fromAddress_str + toAddress_str + command_str + retData;
            return ret_str + commixUtil.getCheckStr(ret_str);
        }
        return "";
    }
}
