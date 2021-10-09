package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.WsVillageQueryParam;
import lombok.*;

/**
 * <p>
 * 村庄对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsVillageQueryForm", description="村庄对外查询条件")
public class WsVillageQueryForm extends BaseQueryForm<WsVillageQueryParam> {


    @ApiModelProperty(name = "village_name", value = "名称||varchar(200)")
    private String villageName;
    @ApiModelProperty(name = "villageSn", value = "村标识||varchar(200)")
    private String villageSn;

    @ApiModelProperty(name = "address", value = "地址||varchar(200)")
    private String address;

    @ApiModelProperty(name = "sort_id", value = "排序字段||int(11)")
    private String sortId;
    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
