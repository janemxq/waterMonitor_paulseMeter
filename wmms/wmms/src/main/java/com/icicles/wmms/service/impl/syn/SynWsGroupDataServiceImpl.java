package com.icicles.wmms.service.impl.syn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.po.WsGroup;
import com.icicles.wmms.service.AbstractSynData;
import com.icicles.wmms.service.WsSynService;

import java.util.List;

/**
 * @author lisy
 */
public class SynWsGroupDataServiceImpl extends AbstractSynData<WsGroup> {
    /**
     * 构造函数，对必要的参数赋值
     *
     * @param service      数据服务类
     * @param wsSynService 同步日志服务类
     */
    public SynWsGroupDataServiceImpl(IService<WsGroup> service, WsSynService wsSynService) {
        super(service, wsSynService);
    }

    @Override
    protected void synFun(String dataType, List<WsGroup> data) {

    }
    /**
     * 不同的保存记录的方法
     * @param data     需要同步的数据
     */
    @Override
    public void saveData(List<WsGroup> data){
        for (WsGroup temp:
        data) {
            QueryWrapper<WsGroup> queryWrap = new QueryWrapper<> ();
            queryWrap.lambda().eq(WsGroup::getSn,temp.getSn()).last("limit 1");
            WsGroup group = super.getService().getOne(queryWrap);
            if (group == null) {
                super.getService().save(temp);
            }
        }
    }
}
