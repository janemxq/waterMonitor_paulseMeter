package com.icicles.wmms.service.impl;

import com.icicles.wmms.entity.po.WsFeeStandard;
import com.icicles.wmms.entity.po.WsFeeStandardItem;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.WaterFeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 计算水费
 * @author lisy
 */
@Service
public class WaterFeeServiceImpl implements WaterFeeService {
    /**
     * 每月天数
     */
    private final int monthDay = 30;

    @Override
    public BigDecimal getWaterFee(BigDecimal volume, WsFeeStandard feeStandard, String startTime, String endTime) {
        if(volume.compareTo(BigDecimal.ZERO)<=0){
            return BigDecimal.ZERO;
        }
        if(feeStandard==null){
            return BigDecimal.ZERO;
        }
        //阶梯计价，需要计算每个月的平均用水，进而获取各个阶梯价格，水费=（各个阶梯用水量*各个阶段的价格）的和
        if(feeStandard.getIsStep()==1){
            //缴费周期内的天数
            int days = this.getDayDiff(startTime,endTime);
            //不足一个月单独计算
            if(days<monthDay){
                //不足一个月，缴费金额单独，计算保留2位小数（2位小数后面如果有值进1）
                return this.getWaterFeeFun(volume,volume,feeStandard,BigDecimal.ONE).setScale(2,BigDecimal.ROUND_UP);
            }
            //缴费周期有几个月（肯定大于等于1个月，小数部分四舍五入，不足半个月按照一个月计算，如果是超过半个月，按照2个月计算）
            double monthNum = (double)days/monthDay;
            BigDecimal realMonthNum = new BigDecimal(monthNum).setScale(0, BigDecimal.ROUND_HALF_UP);
            //计算每个月的平均值
            BigDecimal avg = volume.divide(realMonthNum,2,BigDecimal.ROUND_HALF_UP);
            //返回计算值，保留2位小数（2位小数后面如果有值进1）
            return this.getWaterFeeFun(volume,avg,feeStandard,realMonthNum).setScale(2,BigDecimal.ROUND_UP);
        }else{
            //非阶梯计价，直接获取每立方米价格，水费=价格*用水量，保留2位小数（2位小数后面如果有值进1）
            return this.getWaterFeeFun(volume,BigDecimal.ZERO,feeStandard,null).setScale(2,BigDecimal.ROUND_UP);
        }
    }



    /**
     * 将收费区间格式化
     * @param feeStandard，收费标准
     * @return 收费集合
     */
    @Override
    public List<WsFeeStandardItem> getStandards(WsFeeStandard feeStandard){
        if(StringUtils.isBlank(feeStandard.getFeeStandard())){
            throw new ApiException("请上传收费标准", HttpStatus.BAD_REQUEST);
        }
        List<WsFeeStandardItem> rs = new ArrayList<>();
        //feeStandard.getFeeStandard():形式如0-50,2&51-100,3(区间开始和区间结束用"-"分隔，区间和价格用","分隔，区间直接用"&"分隔)
        String[] step1 = feeStandard.getFeeStandard().split("&");
        BigDecimal startFlag = BigDecimal.ZERO;

        //计数
        int stepNum = 0;
        for (String temp:
                step1) {

            stepNum ++;

            String[] step2 = temp.split(",");
            if(step2.length!=2){
                throw new ApiException("请上传正确的收费标准", HttpStatus.BAD_REQUEST);
            }
            String price = step2[0];
            String[] step3 = price.split("-");
            if(step3.length!=2){
                throw new ApiException("请上传正确的收费标准!", HttpStatus.BAD_REQUEST);
            }
            BigDecimal startNum = new BigDecimal(step3[0]);
            BigDecimal endNum = new BigDecimal(step3[1]);
            BigDecimal priceNum = new BigDecimal(step2[1]);

            /*格式验证*/
            //保证结束大于开始
            if(endNum.compareTo(startNum)<0){
                throw new ApiException("请上传正确的收费标准!!（结束区间大于开始区间）", HttpStatus.BAD_REQUEST);
            }
            //保证区间递增
            if(startNum.compareTo(startFlag)<0){
                throw new ApiException("请上传正确的收费标准!!!（保证区间递增）", HttpStatus.BAD_REQUEST);
            }
            //保证区间开闭关系（形式如0-180，180-200；而不是0-180,181-200）
            if(startNum.subtract(startFlag).compareTo(BigDecimal.ONE)!=0){
                throw new ApiException("区间格式为:1-180,181-200,201-300(从1开始，新区间和上个区间结束相差1)", HttpStatus.BAD_REQUEST);
            }

            startFlag = endNum;

            if(stepNum==step1.length){
                endNum = BigDecimal.valueOf(Long.MAX_VALUE);
            }

            WsFeeStandardItem standard = new WsFeeStandardItem();
            standard.setEndNum(endNum);
            standard.setStartNum(startNum);
            standard.setIsStep(feeStandard.getIsStep());
            standard.setPrice(priceNum);
            standard.setFeeStandardId(feeStandard.getId());
            rs.add(standard);
        }
        return rs;
    }

    /**
     * 计算缴费金额
     * @param volume          用水量
     * @param average         平均用水量（阶梯计价时使用）
     * @param feeStandard     收费标准
     * @param monthNum        收费周期中间经历了几个月（阶梯计价时使用）
     * @return
     */
    private BigDecimal getWaterFeeFun(BigDecimal volume, BigDecimal average,WsFeeStandard feeStandard,BigDecimal monthNum) {
        if(volume.compareTo(BigDecimal.ZERO)<=0){
            return BigDecimal.ZERO;
        }
        if(StringUtils.isBlank(feeStandard.getFeeStandard())){
            return BigDecimal.ZERO;
        }
        List<WsFeeStandardItem> standards = this.getStandards(feeStandard);
        if(standards==null){
            return BigDecimal.ZERO;
        }

        BigDecimal account = BigDecimal.ZERO;

        //区间索引
        int index=-1;
        for (WsFeeStandardItem standard:
                standards) {
            if(standard.getIsStep()==1){
                /*阶梯计价，先找到对应的区间，然后看看前面有几个阶梯*/
                //索引加1
                index++;
                //每个月的缴费
                BigDecimal averageSum = BigDecimal.ZERO;
                //阶梯计价，看每个月的平均值是否在设置的区间内
                if(average.compareTo(standard.getStartNum()) >= 0 &&average.compareTo(standard.getEndNum())<=0){
                    //认为前面都是满的，每个区间的金额=区间的值（区间的结束-区间开始）*区间价格
                    for (int i=0;i<=index-1;i++){
                        WsFeeStandardItem ttt = standards.get(i);
                        BigDecimal tp = (ttt.getEndNum().subtract(ttt.getStartNum()).add(BigDecimal.ONE)).multiply(ttt.getPrice());
                        averageSum = averageSum.add(tp);
                    }
                    //最后这个区间的用水量=每个月的均值-最后这个区间的最小值
                    BigDecimal finalNum = average.subtract(standard.getStartNum()).add(BigDecimal.ONE);
                    //最后这个区间的金额=最后这个区间的用水量*最后这个区间的区间价格
                    BigDecimal finalAccount = finalNum.multiply(standard.getPrice());
                    //每个月的总和
                    averageSum = averageSum.add(finalAccount);
                    //总金额=每个月的总和*月份数量
                    account = averageSum.multiply(monthNum);
                    break;
                }
            }else{
                //非阶梯计价，找到区间直接计算
                if(volume.compareTo(standard.getStartNum()) >= 0 &&volume.compareTo(standard.getEndNum())<=0){
                    account = volume.multiply(standard.getPrice());
                    break;
                }
            }
        }
        return account;
    }

    private BigDecimal noEnoughMonth(BigDecimal volume,WsFeeStandard feeStandard){
        BigDecimal account = BigDecimal.ZERO;
        List<WsFeeStandardItem> standards = this.getStandards(feeStandard);
        //区间索引
        int index=-1;
        for (WsFeeStandardItem standard:
                standards) {
            index++;
            if(volume.compareTo(standard.getStartNum()) >= 0 &&volume.compareTo(standard.getEndNum())<=0){
                //第一个区间都不够
                if(index==0){
                    //第一区间的用水量
                    account = standard.getPrice().multiply(volume);
                }else{
                    //认为前面都是满的，每个区间的金额=区间的值（区间的结束-区间开始）*区间价格
                    for (int i=0;i<=index-1;i++){
                        WsFeeStandardItem ttt = standards.get(i);
                        BigDecimal tp = (ttt.getEndNum().subtract(ttt.getStartNum()).add(BigDecimal.ONE)).multiply(ttt.getPrice());
                        account = account.add(tp);
                    }
                    //最后这个区间的用水量=每个月的均值-最后这个区间的最小值
                    BigDecimal finalNum = volume.subtract(standard.getStartNum()).add(BigDecimal.ONE);
                    //最后这个区间的金额=最后这个区间的用水量*最后这个区间的区间价格
                    BigDecimal finalAccount = finalNum.multiply(standard.getPrice());
                    //每个月的总和
                    account = account.add(finalAccount);
                }
                break;
            }
        }
        return account;
    }

    /**
     * 获取两个日期之间的天数
     * @param start 开始日期（格式：yyyy-MM-dd）
     * @param end   结束日期（格式：yyyy-MM-dd）
     * @return      两个日期之间的天数
     */
    private Integer getDayDiff(String start,String end) {
        long betweenDays = 0L;
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(start));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(end));
            long time2 = cal.getTimeInMillis();
            betweenDays=(time2-time1)/(1000*3600*24);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(betweenDays));
    }
}
