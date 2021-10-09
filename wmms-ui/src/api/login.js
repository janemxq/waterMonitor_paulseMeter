import { get, post } from '@/libs/api.request'

const prefix = ''
console.log('未知原因，此处无法获取到prefix')
const urls = {
  logouts: prefix + '/loginout',
  login: prefix + '/oauth/token', // 用户登录
  refreshToken: prefix + '/oauth/token', // 刷新token
  getUserData: prefix + '/sysUser/userInfo', // 根据账号查询用户
}

export default {
  logouts (params) {
    return post(urls.logouts, {}, { params })
  },
  login (params) {
    return post(urls.login, {}, { params })
  },
  refreshToken (params) {
    return post(urls.refreshToken, {}, { params })
  },
  getUserData (params) {
    return get(urls.getUserData, params)
  },
}
