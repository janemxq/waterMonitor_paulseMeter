package com.icicles.wmms.service.impl.syn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.po.WsFeeStandard;
import com.icicles.wmms.service.AbstractSynData;
import com.icicles.wmms.service.WsSynService;

import java.util.List;

/**
 * 同步收费标准
 * @author lisy
 */
public class SynFeeStandardDataServiceImpl extends AbstractSynData<WsFeeStandard> {
    /**
     * 构造函数，对必要的参数赋值
     *
     * @param service      数据服务类
     * @param wsSynService 同步日志服务类
     */
    public SynFeeStandardDataServiceImpl(IService<WsFeeStandard> service, WsSynService wsSynService) {
        super(service, wsSynService);
    }

    @Override
    protected void synFun(String dataType, List<WsFeeStandard> data) {
    }

    /**
     * 不同的保存记录的方法
     * @param data     需要同步的数据
     */
    @Override
    public void saveData(List<WsFeeStandard> data){
        for (WsFeeStandard temp:
        data) {
            QueryWrapper<WsFeeStandard> queryWrap = new QueryWrapper<> ();
            queryWrap.lambda().eq(WsFeeStandard::getSn,temp.getSn()).last("limit 1");
            WsFeeStandard v = super.getService().getOne(queryWrap);
            if (v == null){
                super.getService().save(temp);
            }
        }
    }
}
