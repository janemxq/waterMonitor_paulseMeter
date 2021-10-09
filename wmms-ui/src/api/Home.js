import { get, post, prefix } from '@/libs/api.request'

const urls = {
  openOneGood: 'https://data.zhai78.com/openOneGood.php', // 分页查询字典表列表
  getMeterNum: prefix + '/Statistics/getMeterNum', // 获取平台内设备数量
  getUserNum: prefix + '/Statistics/getUserNum', // 获取平台内用户数量
  meterReaderCount: prefix + '/Statistics/meterReaderCount', // 获取抄表次数
  paymentCount: prefix + '/Statistics/paymentCount', // 获取支付次数。
  paymentSum: prefix + '/Statistics/paymentSum', // 获取支付总额。
  statisticsHome: prefix + '/Statistics/statisticsHome', // 综合统计类
  waterSum: prefix + '/Statistics/waterSum', // 统计用水总数。

  getWaterSumByMonth: prefix + '/wsFeeLog/getWaterSumByMonth', // 缴费按月统计
  getWaterSumBySeason: prefix + '/wsFeeLog/getWaterSumBySeason', // 缴费按季度统计
  getWaterMoneyByMonth: prefix + '/wsMeterReader/getWaterSumByMonth', // 用水量按月统计
  getWaterMoneyBySeason: prefix + '/wsMeterReader/getWaterSumBySeason', // 用水量按季度统计
  getSysUser: prefix + '/sysUser/page', // 搜索用水监测管理平台用户信息表(分页)

  getNopayUsers: prefix + '/DataException/getNopayUsers', // 查询一定时间内没有缴费的用户（查欠费情况）
  getFeeException: prefix + '/DataException/getNopayUsers', // 查询缴费异常（缴费记录中小于或者大于某个阈值的记录）
  getWaterCostException: prefix + '/DataException/getWaterCostException', // 查询用水异常（水表读数记录中小于或者大于某个阈值的记录）
  exportNopayUsers: prefix + '/DataException/exportNopayUsers', // 导出未缴费的用户（导出欠费情况）
  postSysUserChangePwd: prefix + '/sysUser/changePwd', // 修改密码
  getSysUserUserInfo: prefix + '/sysUser/userInfo', // 获取当前登陆人信息
  postWsVillagePage: prefix + '/wsVillage/page', // 搜索村庄(分页)
  getWsVillageAll: prefix + '/wsVillage/all' // 查询所有村庄
}

export default {
  getWsVillageAll (params) {
    return get(urls.getWsVillageAll, params)
  },
  postWsVillagePage (params) {
    return post(urls.postWsVillagePage, params)
  },
  getSysUserUserInfo (params) {
    return get(urls.getSysUserUserInfo, params)
  },
  postSysUserChangePwd (params) {
    return post(urls.postSysUserChangePwd, params)
  },
  getNopayUsers (params) {
    return post(urls.getNopayUsers, params)
  },
  getFeeException (params) {
    return post(urls.getFeeException, params)
  },
  getWaterCostException (params) {
    return post(urls.getWaterCostException, params)
  },
  exportNopayUsers (params) {
    return post(urls.exportNopayUsers, params)
  },

  openOneGood () {
    return get(urls.openOneGood)
  },
  getMeterNum (params) {
    return post(urls.getMeterNum, params)
  },
  getUserNum (params) {
    return post(urls.getUserNum, params)
  },
  meterReaderCount (params) {
    return post(urls.meterReaderCount, params)
  },
  paymentCount (params) {
    return post(urls.paymentCount, params)
  },
  paymentSum (params) {
    return post(urls.paymentSum, params)
  },
  statisticsHome (params) {
    return post(urls.statisticsHome, params)
  },
  waterSum (params) {
    return post(urls.waterSum, params)
  },
  getWaterSumByMonth (params) {
    return post(urls.getWaterSumByMonth, params)
  },
  getWaterSumBySeason (params) {
    return post(urls.getWaterSumBySeason, params)
  },
  getWaterMoneyByMonth (params) {
    return post(urls.getWaterMoneyByMonth, params)
  },
  getWaterMoneyBySeason (params) {
    return post(urls.getWaterMoneyBySeason, params)
  },
  getSysUser (params) {
    return post(urls.getSysUser, params)
  }
}
