import Router from '../router/index'
import axios from 'axios'
import { Message } from 'view-design'
// token拦截器
let flag = true
export default function () {
  function f () {
    Message.destroy()
    Message.error('登录超时！请重新登录！')
    localStorage.removeItem('tokenTime')
    Router.push({
      name: 'login'
    })
  }
  return new Promise((resolve, reject) => {
    if (localStorage.getItem('tokenTime') > new Date().getTime() || !flag) { // 若token有效
      resolve(true)
    } else if (flag && (localStorage.getItem('tokenTime') < new Date().getTime()) && ((localStorage.getItem('token') * 1 + 15 * 6000) > new Date().getTime())) { // 若在刷新范围内
      flag = false
      axios({
        url: `/api/bgmt/refreshToken`,
        method: 'post'
      }).then((res) => {
        if (!res.data.content) {
          f()
        }
        localStorage.setItem('tokenTime', new Date().getTime() + res.data.content)
        resolve(true)
        flag = true
      }).catch((err) => {
        console.log(err)
        f()
        resolve(true)
        flag = true
      })
    } else { // 跳转登录页
      resolve(true)
    }
  })
}
