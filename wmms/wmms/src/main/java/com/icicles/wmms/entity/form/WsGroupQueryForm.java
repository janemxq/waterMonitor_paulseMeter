package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.WsGroupQueryParam;
import lombok.*;

/**
 * <p>
 * 表井对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsGroupQueryForm", description="组（表井）对外查询条件")
public class WsGroupQueryForm extends BaseQueryForm<WsGroupQueryParam> {


    @ApiModelProperty(name = "group_name", value = "组名||varchar(50)")
    private String groupName;
    @ApiModelProperty(name = "sn", value = "表井标识||varchar(50)")
    private String sn;

    @ApiModelProperty(name = "village_id", value = "村id||bigint(20)")
    private String villageId;

    @ApiModelProperty(name = "village_name", value = "村名||varchar(255)")
    private String villageName;

    @ApiModelProperty(name = "address", value = "表井终端设备地址")
    private String address;


    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;

    @ApiModelProperty(name = "villageSn", value = "村的标识||varchar(255)")
    private String villageSn;
}
