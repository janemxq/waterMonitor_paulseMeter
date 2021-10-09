import { del, put, post, prefix, get } from '@/libs/api.request'

const urls = {
  getVillage: prefix + '/wsVillage/page', // 分页查询村庄列表
  Village: prefix + '/wsVillage', // 新增 修改 删除村庄

  getTSysUser: prefix + '/sysUser/page', // 搜索用水监测管理平台用户信息表(分页)
  TSysUser: prefix + '/sysUser', // 新增 删除 修改水监测管理平台用户信息
  getWsVillageAll: prefix + '/wsVillage/all', // 查询所有村庄
  postSysUserMultiDel: prefix + '/sysUser/multiDel' // 删除多个用户
}

export default {
  postSysUserMultiDel (params) {
    return post(urls.postSysUserMultiDel, params)
  },
  getWsVillageAll (params) {
    return get(urls.getWsVillageAll, params)
  },
  getVillage (params) {
    return post(urls.getVillage, params)
  },
  addVillage (params) {
    return post(urls.Village, params)
  },
  updateVillage (params) {
    return put(`${urls.Village}/${params.id}`, params)
  },
  delVillage (params) {
    return del(`${urls.Village}/${params.id}`)
  },

  getTSysUser (params) {
    return post(urls.getTSysUser, params)
  },
  addTSysUser (params) {
    return post(urls.TSysUser, params)
  },
  updateTSysUser (params) {
    return put(`${urls.TSysUser}/${params.id}`, params)
  },
  delTSysUser (params) {
    return del(`${urls.TSysUser}/${params.id}`)
  },
}
