import Vue from 'vue'
import Router from 'vue-router'
import { afterRouter, beforeRouter } from './routers'
// import store from '@/store'
import iView from 'view-design'
// setToken canTurnTo
import { getToken, setTitle, updateMenuList, loadMenu } from '@/libs/util'
import config from '@/config'
const { homeName } = config

Vue.use(Router)
const router = new Router({
  routes: [...beforeRouter, ...loadMenu(), ...afterRouter]
  // mode: 'history'
})
const LOGIN_PAGE_NAME = 'login'
router.beforeEach((to, from, next) => {
  iView.LoadingBar.start()
  console.log(`%c ${to.path}`, 'color: green;')
  const token = getToken()
  if (!token && to.name !== LOGIN_PAGE_NAME) {
    // 未登录且要跳转的页面不是登录页
    next({
      name: LOGIN_PAGE_NAME // 跳转到登录页
    })
  } else if (!token && to.name === LOGIN_PAGE_NAME) {
    // 未登陆且要跳转的页面是登录页
    localStorage.removeItem('menu')
    localStorage.removeItem('roleMenu')
    next() // 跳转
  } else if (token && to.name === LOGIN_PAGE_NAME) {
    // 已登录且要跳转的页面是登录页
    next({
      name: homeName // 跳转到homeName页
    })
  } else {
    // router.addRoutes(routerList)
    // {
    // ...to, replace: true
    // }
    updateMenuList()
    next()
    // if (store.state.user.hasGetInfo) {
    //   turnTo(to, store.state.user.access, next)
    // } else {
    //   store.dispatch('getUserInfo').then((user) => {
    //     // 拉取用户信息，通过用户权限和跳转的页面的name来判断是否有权限访问;access必须是一个数组，如：['super_admin'] ['super_admin', 'admin']
    //     turnTo(to, user.access, next)
    //   }).catch(() => {
    //     console.log('此处后续整理')
    //     turnTo(to, ['admin'], next)
    //     // setToken('')
    //     // next({
    //     //   name: 'login'
    //     // })
    //   })
    // }
  }
})

router.afterEach(to => {
  setTitle(to, router.app)
  iView.LoadingBar.finish()
  window.scrollTo(0, 0)
})

export default router
