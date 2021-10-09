import axios from 'axios'
import store from '@/store'
// import Intercept from './intercept'
import Router from '@/router'
import { Message } from 'view-design'
import { setToken } from '@/libs/util'
// import { Notice } from 'view-design'
const addErrorLog = errorInfo => {
  const { statusText, status, request: { responseURL } } = errorInfo
  let info = {
    type: 'ajax',
    code: status,
    mes: statusText,
    url: responseURL
  }
  if (!responseURL.includes('save_error_logger')) store.dispatch('addErrorLog', info)
}
// let flag = true
class HttpRequest {
  constructor (baseUrl) {
    this.baseUrl = baseUrl
    this.queue = {}
  }
  getInsideConfig () {
    const config = {
      baseURL: this.baseUrl,
      headers: {
        //
      }
    }
    return config
  }
  destroy (url) {
    delete this.queue[url]
    if (!Object.keys(this.queue).length) {
      // Spin.hide()
    }
  }
  interceptors (instance, url) {
    // 请求拦截
    instance.interceptors.request.use(async (config) => {
      // 每次请求添加时间戳 防止缓存
      if (/\?/.test(config.url)) {
        config.url = config.url + '&_t=' + Date.parse(new Date()) / 1000
      } else {
        config.url = config.url + '?_t=' + Date.parse(new Date()) / 1000
      }
      // 添加全局的loading...
      if (!Object.keys(this.queue).length) {
        // Spin.show() // 不建议开启，因为界面不友好
      }
      this.queue[url] = true
      return config
    }, error => {
      return Promise.reject(error)
    })
    // 响应拦截
    instance.interceptors.response.use(res => {
      this.destroy(url)
      const { data, status } = res
      return { data, status }
    }, error => {
      this.destroy(url)
      if (error.response.status === 401) {
        setToken('')
        Router.push({
          name: 'login'
        })
        Message.destroy()
        Message.error('登录过期，请重新登录')
        return
      }
      if (error.response.data && error.response.data.message) {
        Message.destroy()
        Message.error(error.response.data.message)
      }
      let errorInfo = error.response
      if (!errorInfo) {
        const { request: { statusText, status }, config } = JSON.parse(JSON.stringify(error))
        errorInfo = {
          statusText,
          status,
          request: { responseURL: config.url }
        }
      }
      addErrorLog(errorInfo)
      return Promise.reject(error)
    })
    instance.interceptors.response.use(res => {
      if (res.status === 401) {
        store.commit('setToken', '')
        store.commit('setToken', '')
        store.commit('setAccess', [])
        Message.error('登录超时！请重新登录！')
        Router.push({
          name: 'login'
        })
      }
      if (res.status === 500 && res.data.code === 1) {
        Message.destroy()
      }
      if (res.status > 300) {
        return Promise.reject(res.data.message || '未知错误')
      }
      this.destroy(url)
      const { data, status } = res
      return { data, status }
    }, error => {
      this.destroy(url)
      return Promise.reject(error)
    })
  }
  request (options) {
    const instance = axios.create()
    options = Object.assign(this.getInsideConfig(), options)
    this.interceptors(instance, options.url)
    return instance(options)
  }
}
export default HttpRequest
