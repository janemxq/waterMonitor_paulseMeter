package com.icicles.wmms.utils;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.po.SysParam;
import com.icicles.wmms.service.SysParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * api签名
 * @author lisy
 */
@Component
public class ApiSign {
    /**
     * 数据库配置服务
     */
    private SysParamService sysParamService;


    @Autowired
    public ApiSign(SysParamService sysParamService){
        this.sysParamService = sysParamService;
    }

    /**
     * 创建签名
     * @param timestamp 时间戳
     * @return 加密后的字符串
     */
    public String createSign(Long timestamp){
        //获取密钥
        SysParam param = sysParamService.findByKey(Constants.API_KEY);
        String secret;
        if(param==null){
            //如果服务器没有设置，用配置文件中的内容
            secret = Constants.API_SECRET;
        }else{
            secret = param.getConfigValue();
        }


        //通过HmacSHA256加密，HmacSHA256的密钥即数据库设置的服务密钥
        HMac mac = new HMac(HmacAlgorithm.HmacSHA256, secret.getBytes());
        //待加密的字符串
        String plain = secret+"@@@\n@@@"+timestamp;
        return mac.digestHex(plain);
    }

    /**
     * 签名验证
     * @param timestamp 时间戳
     * @param sign 签名
     * @return true成功，false失败
     */
    public boolean verifySign(Long timestamp,String sign){
        if(StringUtils.isBlank(sign)){
            return false;
        }
        return sign.equals(this.createSign(timestamp));
    }
}
