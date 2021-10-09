import { del, get, put, post, prefix, baseUrl } from '@/libs/api.request'

const urls = {
  getSysRole: prefix + '/sysRole/page', // 搜索角色(分页)
  SysRole: prefix + '/sysRole', // 新增删除修改角色

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
  getWsGroupAll: prefix + '/wsGroup/all', // 查询所有组（表井）
  WsGroup: prefix + '/wsGroup', // 增 删 改表井

  getWsFeeStandard: prefix + '/wsFeeStandard/page', // 搜索水表的收费标准(分页)
  WsFeeStandard: prefix + '/wsFeeStandard', // 新增 删除 修改 收费标准
  getWsFeeStandardAll: prefix + '/wsFeeStandard/all', // 查询所有水表的收费标准

  getVillageAll: prefix + '/wsVillage/all', // 查询所有村庄
  findUser: prefix + '/sysUser/page', // 搜索用水监测管理平台用户信息表(分页)
  findUserAll: prefix + '/sysUser/all', // 查询所有用水监测管理平台用户信息表
  getRoleAll: prefix + '/sysRole/findByAccount', // 查询所有角色
  getPay: prefix + '/wsMeter/getMetersFeeInfo', // 用户设备水费管理

  importExcel: baseUrl + prefix + '/wsMeter/importExcel', // excel导入设备
  exportExcel: prefix + '/wsMeter/exportExcel', // 设备信息导出到excel

  getSysMenu: prefix + '/sysMenu/page', // 菜单列表(包含分页)
  SysMenu: prefix + '/sysMenu', // 新增 删除 修改菜单
  userMenu: prefix + '/sysMenu/menus', // 登录用户获取菜单
  bindMenu: prefix + '/sysMenu/menusTreeByRoleId', // 根据角色id获取菜单，用于角色和菜单绑定管理
  setBindMenu: prefix + '/sysRoleMenus/save', // 根据角色id获取菜单，用于角色和菜单绑定管理
}

export default {

  getSysMenu (params) {
    return post(urls.getSysMenu, params)
  },
  setBindMenu (params) {
    return post(urls.setBindMenu, params)
  },
  bindMenu (params) {
    return get(`${urls.bindMenu}/${params.roleId}`, params)
  },
  delSysMenu (params) {
    return del(`${urls.SysMenu}/${params.id}`)
  },
  addSysMenu (params) {
    return post(urls.SysMenu, params)
  },
  updateSysMenu (params) {
    return put(`${urls.SysMenu}/${params.id}`, params)
  },
  userMenu (params) {
    return post(urls.userMenu, params)
  },

  getSysRole (params) {
    return post(urls.getSysRole, params)
  },
  delSysRole (params) {
    return del(`${urls.SysRole}/${params.id}`)
  },
  addSysRole (params) {
    return post(urls.SysRole, params)
  },
  updateSysRole (params) {
    return put(`${urls.SysRole}/${params.id}`, params)
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
  getWsGroupAll () {
    return get(urls.getWsGroupAll)
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
  findUserAll (params) {
    return get(urls.findUserAll, params)
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

  importExcel () {
    return urls.importExcel
  },
  exportExcel (params) {
    return post(urls.exportExcel, params, { responseType: 'blob' })
  }
}
