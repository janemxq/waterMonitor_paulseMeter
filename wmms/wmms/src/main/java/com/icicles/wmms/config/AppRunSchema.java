package com.icicles.wmms.config;

import com.icicles.wmms.entity.po.SysParam;
import com.icicles.wmms.service.SysParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 系统运行的模式配置
 * @author lisy
 */
@Component
public class AppRunSchema {
    private SysParamService sysParamService;
    /**
     * 系统运行的模式：schema_single 为村委端，schema_center 为集管端
     */
    private String schema;
    /**
     * 模式为schema_single（村委端）时，代表村id
     */
    @Autowired
    public AppRunSchema(SysParamService sysParamService){
        this.sysParamService = sysParamService;
    }

    /**
     * 从数据库获取系统运行的模式（集管端还是村委端）
     * @return
     */
    public String getSchema() {
        SysParam sysParam = sysParamService.findByKey(Constants.APP_SCHEMA);
        if(sysParam!=null){
            this.schema = sysParam.getConfigValue();
        }else{
            this.schema = "";
        }
        return this.schema;
    }
}
