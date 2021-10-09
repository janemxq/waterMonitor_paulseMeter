export function routerParams (data) {
  return data.map(m => {
    return Object.assign(m, {
      component: routerMap[m.filePath],
      children: m.children.map(n => {
        return Object.assign(n, {
          component: routerMap[n.filePath],
        })
      })
    })
  })
}

export const routerMap = {
  main: () => import("@/components/main"),
  systemParam: () => import('@/view/System/SystemParamPage'),
  DictPage: () => import('@/view/System/DictPage'),
  Role: () => import('@/view/System/Role'),
  rates: () => import('@/view/System/Rates'),
  menu: () => import('@/view/System/Menu'),
  Village: () => import('@/view/Org/Village'),
  TSysUser: () => import('@/view/Org/TSysUser'),
  WsGroup: () => import('@/view/WaterManage/WsGroup'),
  WsMeter: () => import('@/view/WaterManage/WsMeter'),
  Pay: () => import('@/view/Pays/Pay'),
  Log: () => import('@/view/Logs/Log'),
  PayLog: () => import('@/view/Logs/PayLog'),
  testPage: () => import('@/view/test/test.vue'),
  hht: () => import('@/view/hht/hht.vue'),
  topUpLog: () => import('@/view/Logs/topUpLog.vue'),
  autoLog: () => import('@/view/Logs/autoLog.vue')
}
