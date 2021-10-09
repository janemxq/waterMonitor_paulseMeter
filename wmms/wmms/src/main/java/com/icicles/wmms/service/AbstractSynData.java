package com.icicles.wmms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.po.BasePo;
import com.icicles.wmms.entity.po.WsMeter;
import com.icicles.wmms.entity.po.WsSyn;

import java.util.List;

/**
 * 同步数据
 * @author lisy
 */
public abstract class AbstractSynData<T extends BasePo> {
    /**
     * 服务类
     */
    private IService<T> service;
    /**
     * 同步记录服务类
     */
    private WsSynService wsSynService;

    /**
     * 是否同步完成
     */
    private boolean synFinishFlag = false;

    /**
     * 每次同步的最大记录数量
     */
    private int synDataMaxNum = 200;

    /**
     * 构造函数，对必要的参数赋值
     * @param service       数据服务类
     * @param wsSynService  同步日志服务类
     */
    public AbstractSynData(IService<T> service,WsSynService wsSynService){
        this.service = service;
        this.wsSynService = wsSynService;
    }

    /**
     * 数据同步方法，直接调用这个方法（一定要加事务）
     */
    public void syn(String dataType){
        while (!this.synFinishFlag){
            List<T> data = this.getSynData(dataType);
            if(data!=null&&data.size()>0){
                //同步数据
                this.synFun(dataType,data);
                //保存记录
                this.saveLog(dataType,data.get(data.size() - 1));
            }else{
                break;
            }
        }
    }

    /**
     * 具体的同步方法
     * @param data     需要同步的数据
     */
    public void saveData(List<T> data){
        service.saveBatch(data);
    }

    /**
     * 具体的同步方法
     * @param data     需要同步的数据
     * @param dataType 数据类型:Class.getName
     */
    protected abstract void synFun(String dataType,List<T> data);

    /**
     * 获取每次发送数据的数量
     * @return 数量
     */
    protected int getSynDataMaxNum() {
        return synDataMaxNum;
    }

    /**
     * 设置每次发送数据的数量
     * @param synDataMaxNum 每次发送数据的数量
     */
    protected void setSynDataMaxNum(int synDataMaxNum) {
        this.synDataMaxNum = synDataMaxNum;
    }

    /**
     * 获取是否同步完成的标识
     * @return true已经同步完成，false未同步完成
     */
    protected boolean isSynFinishFlag() {
        return synFinishFlag;
    }

    protected IService<T> getService(){
        return this.service;
    }

    /**
     * 获取同步数据
     * @param dataType 数据类型
     * @return 数据类型
     */
    private List<T> getSynData(String dataType){
        try {
            //获取最近同步的记录的日期（如果没有从头开始同步）
            WsSyn wsSynLog = wsSynService.findByType(dataType);
            QueryWrapper<T> queryWrapper = new QueryWrapper<>();
            if(wsSynLog!=null){
                //查询id大于记录的id(如果是水表数据，全部同步上去，不用判断id)，并且时间大于等于创建时间的
                if(!dataType.equals(WsMeter.class.getName())){
                    queryWrapper.gt("id",wsSynLog.getBranchId());
                }
            }
            //符合条件的记录数量
            int dataNum = service.count(queryWrapper);
            if(dataNum==0){
                this.synFinishFlag = true;
                return null;
            }else{
                //当符合条件的记录数量小于等于每次同步的最大数量时，代表即将同步完成
                if(dataNum<=this.synDataMaxNum){
                    this.synFinishFlag = true;
                }
            }
            //查询一定数量的数据用于同步
            queryWrapper.last("limit "+this.synDataMaxNum);
            //从最近的同步的记录之后开始查询数据并返回数据
            return service.list(queryWrapper);
        }catch (Exception e){
            this.synFinishFlag = true;
            return null;
        }
    }

    /**
     * 保存同步记录
     * @param dataType 数据类型
     * @param lastData 最后一条数据
     */
    private void saveLog(String dataType,T lastData){
        Long id = lastData.getId();
        WsSyn wsSynLog = new WsSyn();
        wsSynLog.setDataType(dataType);
        wsSynLog.setBranchId(id);
        wsSynService.save(wsSynLog);
    }
}
