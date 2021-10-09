import { post, prefix } from '@/libs/api.request'

const urls = {
  testConfigAddr: prefix + '/meterDeal/testConfigAddr', // 配置终端设备地址码
  testConfigCodeVal: prefix + '/meterDeal/testConfigCodeVal', // 配置当前码盘的码值
  testConfigPulse: prefix + '/meterDeal/testConfigPulse', // 配置脉冲
  testConfigVal: prefix + '/meterDeal/testConfigVal', // 配置累计用水量
  testReadSingle: prefix + '/meterDeal/testReadSingle', // 读取终端设备数据
}

export default {
  testConfigAddr (params) {
    return post(urls.testConfigAddr, {}, { params })
  },
  testConfigCodeVal (params) {
    return post(urls.testConfigCodeVal, {}, { params })
  },
  testConfigPulse (params) {
    return post(urls.testConfigPulse, {}, { params })
  },
  testConfigVal (params) {
    return post(urls.testConfigVal, {}, { params })
  },
  testReadSingle (params) {
    return post(urls.testReadSingle, {}, { params })
  },
}
