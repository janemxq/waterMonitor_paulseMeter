package com.icicles.wmms.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 水表读数
 */
@Data
public class ReadMeterNumDTO {
    /**
     * 累计用水量
     */
    private String codeVal;
    /**
     * 码盘值
     */
    private String val;
    public ReadMeterNumDTO(String codeVal,String val){
        this.codeVal = codeVal;
        this.val = val;
    }

    /**
     * 将读到的json格式的数据转换对象
     * @param readData 读到的数据
     */
    public ReadMeterNumDTO(JSONObject readData){
        if(readData!=null){
            this.codeVal = readData.get("codeVal").toString();
            this.val = readData.get("val").toString();
        }
    }
}
