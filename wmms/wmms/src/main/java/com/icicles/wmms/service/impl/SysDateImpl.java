package com.icicles.wmms.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.po.BasePo;
import com.icicles.wmms.service.AbstractSynData;
import com.icicles.wmms.service.WsSynService;
import com.icicles.wmms.entity.dto.SynDataDTO;
import com.icicles.wmms.utils.ApiSign;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author lisy
 */
public class SysDateImpl<T extends BasePo> extends AbstractSynData<T> {
    /**
     * 设置url
     */
    private String centerUrl;

    private ApiSign apiSign;

    public SysDateImpl(IService<T> service, WsSynService wsSynService,ApiSign apiSign,String centerUrl) {
        super(service, wsSynService);
        this.centerUrl = centerUrl;
        this.apiSign = apiSign;
    }

    @Override
    protected void synFun(String dataType,List<T> data) {
        if(data==null||data.size()<=0){
            return;
        }
        //数据模板
        SynDataDTO<T> synDataDTO = new SynDataDTO<>();
        synDataDTO.setDataType(dataType);
        synDataDTO.setData(data);
        Long timetamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        synDataDTO.setTimestamp(timetamp);
        synDataDTO.setSign(apiSign.createSign(timetamp));

        //序列化为json
        String dataString = JSON.toJSONString(synDataDTO);
        this.sendData(dataString);
    }

    private void sendData(String data){
        HttpRequest.post(this.centerUrl)
                .body(data)
                .timeout(10000)
                .execute().body();
    }
}

