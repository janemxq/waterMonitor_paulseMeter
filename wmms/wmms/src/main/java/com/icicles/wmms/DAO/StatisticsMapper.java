package com.icicles.wmms.DAO;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icicles.wmms.entity.dto.NopayInfoExceptionDTO;
import com.icicles.wmms.entity.dto.PayDataExceptionDTO;
import com.icicles.wmms.entity.dto.PaymentStatisticsDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 统计相关
 * @author lisy
 */
@Component
public interface StatisticsMapper {
    /**
     * 统计未交费的设备和用户信息
     * @param userSn    用户标识
     * @param volume    统计的用水量大于多少
     * @param howLong   统计时间大于多少
     * @return
     */
    List<PayDataExceptionDTO> getNopayUsers(Page<PayDataExceptionDTO> page, @Param("villageSn") String villageSn,@Param("userSn") String userSn, @Param("volume") Integer volume, @Param("howLong") Integer howLong);

    /**
     * 统计未交费的设备和用户信息
     * @param villageSn 村的标识
     * @param userSn    用户标识
     * @return
     */
    List<NopayInfoExceptionDTO> getNopayInfos(@Param("villageSn") String villageSn, @Param("userSn") String userSn);

    /**
     * 通过用户id查询他所有的缴费记录和用水统计
     * @param userSn 用户的标识
     * @return 按时间（年）分组的用户的所有缴费和用户记录
     */
    List<PaymentStatisticsDTO> paymentStatisticsByUser(@Param("userSn") String userSn,@Param("year") String year);
}
