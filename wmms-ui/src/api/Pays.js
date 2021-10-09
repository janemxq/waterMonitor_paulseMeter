import { del, get, put, post, prefix } from '@/libs/api.request'

const urls = {
  getSysDict: prefix + '/sysDict/page', // 分页查询字典表列表
  SysDict: prefix + '/sysDict', // 新增字典 更新字典 删除字典

  findSysDictItem: prefix + '/sysDictItem/findByDictCode', // 根据字典代码查询字典项
  SysDictItem: prefix + '/sysDictItem', // 新增字典项 更新字典项 删除字典项

  getSystemParam: prefix + '/sysParams/page', // 搜索系统参数(分页)
  SysParams: prefix + '/sysParams', // 新增系统参数 更新系统参数 删除系统参数

  getWsMeter: prefix + '/wsMeter/page', // 搜索水表设备(分页)
  WsMeter: prefix + '/wsMeter', // 新增水表设备 更新水表设备 删除水表设备
  updateMeterDeal: prefix + '/meterDeal/', // 根据水表编号更新水表数据

  getWsGroup: prefix + '/wsGroup/page', // 搜索表井(分页)
  WsGroup: prefix + '/wsGroup', // 增 删 改表井

  getWsFeeStandard: prefix + '/wsFeeStandard/page', // 搜索水表的收费标准(分页)
  WsFeeStandard: prefix + '/wsFeeStandard', // 新增 删除 修改 收费标准
  getWsFeeStandardAll: prefix + '/wsFeeStandard/all', // 查询所有水表的收费标准

  getVillageAll: prefix + '/wsVillage/all', // 查询所有村庄
  findUser: prefix + '/sysUser/page', // 搜索用水监测管理平台用户信息表(分页)
  getRoleAll: prefix + '/sysRole/findByAccount', // 查询所有角色
  getPay: prefix + '/wsMeter/getMetersFeeInfo', // 用户设备水费管理
  getTime: '//api.k780.com:88/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json', // 获取北京时间

  wsFeeLog: '/wsFeeLog', // 新增缴费

  print: prefix + '/print', // 打印凭条

  postWsChargePage: prefix + '/wsCharge/page', // 充值记录分页

  postWsCrontabLogPage: prefix + '/wsCrontabLog/page', // 搜索定时任务执行日志(分页)
  postExportNoPay: prefix + '/Statistics/exportNoPay', // 导出欠费信息
  postExportUserCostAndWaterSumToPdf: prefix + '/Statistics/exportUserCostAndWaterSumToPdf' // 导出根据水费和时间生成的报表导出pdf
}

export default {
  postExportUserCostAndWaterSumToPdf (params) {
    return post(urls.postExportUserCostAndWaterSumToPdf, params, { responseType: 'blob' })
  },
  postExportNoPay (params) {
    return post(urls.postExportNoPay, params, { responseType: 'blob' })
  },
  postWsCrontabLogPage (params) {
    return post(urls.postWsCrontabLogPage, params)
  },
  postWsChargePage (params) {
    return post(urls.postWsChargePage, params)
  },
  print (params) {
    return post(urls.print, params)
  },
  getPay (params) {
    return post(urls.getPay, params)
  },
  getRoleAll () {
    return get(urls.getRoleAll)
  },
  getSysDict (params) {
    return post(urls.getSysDict, params)
  },
  delSysDict (params) {
    return del(`${urls.SysDict}/${params.id}`)
  },
  addSysDict (params) {
    return post(urls.SysDict, params)
  },
  updateSysDict (params) {
    return put(`${urls.SysDict}/${params.id}`, params)
  },

  findSysDictItem (params) {
    return get(urls.findSysDictItem, params)
  },
  addSysDictItem (params) {
    return post(urls.SysDictItem, params)
  },
  updateSysDictItem (params) {
    return put(`${urls.SysDictItem}/${params.id}`, params)
  },
  delSysDictItem (params) {
    return del(`${urls.SysDictItem}/${params.id}`)
  },

  getSystemParam (params) {
    return post(urls.getSystemParam, params)
  },
  delSystemParam (params) {
    return del(`${urls.SysParams}/${params.id}`)
  },
  addSystemParam (params) {
    return post(urls.SysParams, params)
  },
  updateSystemParam (params) {
    return put(`${urls.SysParams}/${params.id}`, params)
  },

  getWsMeter (params) {
    return post(urls.getWsMeter, params)
  },
  updateWsMeter (params) {
    return put(`${urls.WsMeter}/${params.id}`, params)
  },
  delWsMeter (params) {
    return del(`${urls.WsMeter}/${params.id}`)
  },
  addWsMeter (params) {
    return post(urls.WsMeter, params)
  },
  updateMeterDeal (params) {
    return post(`${urls.updateMeterDeal}${params.id}`)
  },

  getWsGroup (params) {
    return post(urls.getWsGroup, params)
  },
  addWsGroup (params) {
    return post(urls.WsGroup, params)
  },
  delWsGroup (params) {
    return del(`${urls.WsGroup}/${params.id}`)
  },
  updateWsGroup (params) {
    return put(`${urls.WsGroup}/${params.id}`, params)
  },

  getVillageAll () {
    return get(urls.getVillageAll)
  },
  findUser (params) {
    return post(urls.findUser, params)
  },

  getWsFeeStandard (params) {
    return post(urls.getWsFeeStandard, params)
  },
  addWsFeeStandard (params) {
    return post(urls.WsFeeStandard, params)
  },
  delWsFeeStandard (params) {
    return del(`${urls.WsFeeStandard}/${params.id}`)
  },
  updateWsFeeStandard (params) {
    return put(`${urls.WsFeeStandard}/${params.id}`, params)
  },
  getWsFeeStandardAll (params) {
    return get(urls.getWsFeeStandardAll, params)
  },

  getTime () {
    return get(urls.getTime)
  },

  wsFeeLog (params) {
    return post(urls.wsFeeLog, params)
  },
}
