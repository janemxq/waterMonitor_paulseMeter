package com.icicles.wmms.service.impl.syn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.po.WsVillage;
import com.icicles.wmms.service.AbstractSynData;
import com.icicles.wmms.service.WsSynService;

import java.util.List;

/**
 * 同步村记录
 * @author lisy
 */
public class SynWsVillageDataServiceImpl extends AbstractSynData<WsVillage> {
    /**
     * 构造函数，对必要的参数赋值
     *
     * @param service      数据服务类
     * @param wsSynService 同步日志服务类
     */
    public SynWsVillageDataServiceImpl(IService<WsVillage> service, WsSynService wsSynService) {
        super(service, wsSynService);
    }

    @Override
    protected void synFun(String dataType, List<WsVillage> data) {
    }

    /**
     * 不同的保存记录的方法
     * @param data     需要同步的数据
     */
    @Override
    public void saveData(List<WsVillage> data){
        for (WsVillage temp:
        data) {
            QueryWrapper<WsVillage> queryWrap = new QueryWrapper<> ();
            queryWrap.lambda().eq(WsVillage::getSn,temp.getSn()).last("limit 1");
            WsVillage v = super.getService().getOne(queryWrap);
            if (v == null){
                super.getService().save(temp);
            }
        }
    }
}
