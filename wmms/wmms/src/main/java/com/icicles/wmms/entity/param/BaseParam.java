package com.icicles.wmms.entity.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icicles.wmms.entity.po.BasePo;
import lombok.Data;

/**
 * Created by zhoutaoo on 2018/6/1.
 */
@Data
public class BaseParam<T extends BasePo> {
    private String createdTimeStart;
    private String createdTimeEnd;

    public QueryWrapper<T> build() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(null != this.createdTimeStart, "create_time", this.createdTimeStart)
                .le(null != this.createdTimeEnd, "create_time", this.createdTimeEnd);
        return queryWrapper;
    }
}
