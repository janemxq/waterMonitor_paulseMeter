package com.icicles.wmms.service.impl.syn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.entity.po.WsMeter;
import com.icicles.wmms.service.AbstractSynData;
import com.icicles.wmms.service.WsSynService;

import java.util.List;

/**
 * @author lisy
 */
public class SynWsMeterDataServiceImpl extends AbstractSynData<WsMeter> {

    /**
     * 构造函数，对必要的参数赋值
     *
     * @param service      数据服务类
     * @param wsSynService 同步日志服务类
     */
    public SynWsMeterDataServiceImpl(IService<WsMeter> service, WsSynService wsSynService) {
        super(service, wsSynService);
    }

    @Override
    protected void synFun(String dataType, List<WsMeter> data) {

    }
    /**
     * 不同的保存记录的方法
     * @param data     需要同步的数据
     */
    @Override
    public void saveData(List<WsMeter> data){
        for (WsMeter temp:
                data) {
            QueryWrapper<WsMeter> queryWrap = new QueryWrapper<> ();
            queryWrap.lambda().eq(WsMeter::getSn,temp.getSn()).last("limit 1");
            WsMeter meter = super.getService().getOne(queryWrap);
            if (meter == null){
                super.getService().save(temp);
            }else {
                //如果设备存在，进行更新
                super.getService().update(temp,new UpdateWrapper<WsMeter>().lambda().eq(WsMeter::getSn,temp.getSn()));
            }
        }
    }
}
