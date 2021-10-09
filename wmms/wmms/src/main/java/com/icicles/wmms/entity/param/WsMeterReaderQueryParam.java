package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsMeterReader;
import lombok.*;

/**
 * <p>
 * 水表读数对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsMeterReaderQueryParam extends BaseParam<WsMeterReader> {


    private String userId;
    private String userSn;

    private String userName;

    private String meterId;

    private String meterSn;

    private String villageId;

    private String villageName;

    private String num;

    private String createId;

    private String updateId;


}
