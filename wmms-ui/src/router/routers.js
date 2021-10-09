import Main from '@/components/main'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 *  beforeCloseName: (-) 设置该字段，则在关闭当前tab页时会去'@/router/before-close.js'里寻找该字段名对应的方法，作为关闭前的钩子函数
 * }
 */
let beforeRouter = [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: 'Login - 登录',
      hideInMenu: true
    },
    component: () => import('@/view/login/login.vue')
  },
  {
    path: '/',
    name: '_home',
    redirect: '/home',
    component: Main,
    meta: {
      access: ['admin', 'superAdmin', 'centerAdmin'],
      hideInMenu: true,
      notCache: true
    },
    children: [
      {
        path: '/home',
        name: 'home',
        meta: {
          hideInMenu: true,
          title: '首页',
          access: ['admin', 'superAdmin', 'centerAdmin'],
          notCache: true,
          icon: 'md-home'
        },
        component: () => import('@/view/single-page/home')
      }
    ]
  },
  {
    path: '/message',
    name: 'message',
    component: Main,
    meta: {
      hideInBread: true,
      access: ['admin', 'superAdmin', 'centerAdmin'],
      hideInMenu: true
    },
    children: [
      {
        path: 'message_page',
        name: 'message_page',
        meta: {
          icon: 'md-notifications',
          access: ['admin', 'superAdmin', 'centerAdmin'],
          title: '消息中心'
        },
        component: () => import('@/view/single-page/message/index.vue')
      }
    ]
  }
  // {
  //   path: '/system',
  //   name: 'system',
  //   meta: {
  //     icon: 'md-build',
  //     access: ['admin', 'superAdmin'],
  //     title: '系统管理'
  //   },
  //   component: Main,
  //   children: [
  //     {
  //       path: 'systemParam',
  //       name: 'systemParam',
  //       meta: {
  //         icon: 'md-code',
  //         access: ["admin"], // 'superAdmin'
  //         title: '参数管理'
  //       },
  //       component: () => import('@/view/System/SystemParamPage')
  //     },
  //     {
  //       path: 'DictPage',
  //       name: 'DictPage',
  //       meta: {
  //         icon: 'md-text',
  //         access: ['superAdmin'],
  //         title: '字典管理'
  //       },
  //       component: () => import('@/view/System/DictPage')
  //     },
  //     {
  //       path: 'Role',
  //       name: 'Role',
  //       meta: {
  //         icon: 'ios-happy',
  //         access: ['superAdmin'],
  //         title: '角色管理'
  //       },
  //       component: () => import('@/view/System/Role')
  //     },
  //     {
  //       path: 'rates',
  //       name: 'rates',
  //       meta: {
  //         icon: 'ios-water',
  //         access: ['superAdmin', 'admin'],
  //         title: '收费标准'
  //       },
  //       component: () => import('@/view/System/Rates')
  //     },
  //     {
  //       path: 'menu',
  //       name: 'menu',
  //       meta: {
  //         icon: 'ios-menu',
  //         access: ['superAdmin'],
  //         title: '菜单管理'
  //       },
  //       component: () => import('@/view/System/Menu')
  //     },
  //   ]
  // },
  // {
  //   path: '/org',
  //   name: 'org',
  //   meta: {
  //     icon: 'ios-people',
  //     access: ['superAdmin', 'admin'],
  //     title: '组织架构管理'
  //   },
  //   component: Main,
  //   children: [
  //     {
  //       path: 'Village',
  //       name: 'Village',
  //       meta: {
  //         icon: 'md-pricetags',
  //         access: ['superAdmin', 'admin'],
  //         title: '村庄管理'
  //       },
  //       component: () => import('@/view/Org/Village')
  //     },
  //     {
  //       path: 'TSysUser',
  //       name: 'TSysUser',
  //       meta: {
  //         icon: 'ios-person',
  //         access: ['superAdmin', 'admin'],
  //         title: '用户管理'
  //       },
  //       component: () => import('@/view/Org/TSysUser')
  //     }
  //   ]
  // }, {
  //   path: '/WaterManage',
  //   name: 'WaterManage',
  //   meta: {
  //     icon: 'md-phone-landscape',
  //     access: ['superAdmin', 'admin'],
  //     title: '设备管理'
  //   },
  //   component: Main,
  //   children: [
  //     {
  //       path: 'WsGroup',
  //       name: 'WsGroupr',
  //       meta: {
  //         icon: 'md-pint',
  //         access: ['superAdmin', 'admin'],
  //         title: '表井管理'
  //       },
  //       component: () => import('@/view/WaterManage/WsGroup')
  //     },
  //     {
  //       path: 'WsMeter',
  //       name: 'WsMeter',
  //       meta: {
  //         icon: 'md-pie',
  //         access: ['superAdmin', 'admin'],
  //         title: '水表设备管理'
  //       },
  //       component: () => import('@/view/WaterManage/WsMeter')
  //     },
  //   ]
  // },
  // {
  //   path: '/Pays',
  //   name: 'Pays',
  //   meta: {
  //     icon: 'md-water',
  //     access: ['superAdmin', 'admin'],
  //     title: '用水缴费'
  //   },
  //   component: Main,
  //   children: [
  //     {
  //       path: 'Pay',
  //       name: 'Pay',
  //       meta: {
  //         icon: 'md-planet',
  //         access: ['superAdmin', 'admin'],
  //         title: '用水缴费'
  //       },
  //       component: () => import('@/view/Pays/Pay')
  //     }
  //   ]
  // },
  // {
  //   path: '/Logs',
  //   name: 'Logs',
  //   meta: {
  //     icon: 'ios-unlock',
  //     access: ['superAdmin', 'admin'],
  //     title: '日志管理'
  //   },
  //   component: Main,
  //   children: [
  //     {
  //       path: 'Log',
  //       name: 'Log',
  //       meta: {
  //         icon: 'ios-book',
  //         access: ['superAdmin', 'admin'],
  //         title: '读表记录'
  //       },
  //       component: () => import('@/view/Logs/Log')
  //     },
  //     {
  //       path: 'PayLog',
  //       name: 'PayLog',
  //       meta: {
  //         icon: 'md-bookmarks',
  //         access: ['superAdmin', 'admin'],
  //         title: '缴费记录'
  //       },
  //       component: () => import('@/view/Logs/PayLog')
  //     }
  //   ]
  // },
  // {
  //   path: '/test',
  //   name: 'test',
  //   component: Main,
  //   meta: {
  //     icon: 'md-build',
  //     access: ['superAdmin'],
  //     title: '测试页',
  //   },
  //   children: [
  //     {
  //       path: '/testPage',
  //       name: 'testPage',
  //       meta: {
  //         title: '测试页',
  //         access: ['superAdmin'],
  //         icon: 'md-home'
  //       },
  //       component: () => import('@/view/test/test.vue')
  //     }
  //   ]
  // },
]

const afterRouter = [
  {
    path: '/error_logger',
    name: 'error_logger',
    meta: {
      hideInBread: true,
      hideInMenu: true
    },
    component: Main,
    children: [
      {
        path: 'error_logger_page',
        name: 'error_logger_page',
        meta: {
          icon: 'ios-bug',
          access: ['superAdmin', 'admin'],
          title: '错误收集'
        },
        component: () => import('@/view/single-page/error-logger.vue')
      }
    ]
  },
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/500.vue')
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/404.vue')
  },
  // {
  //   path: '/hhtParent',
  //   name: 'hhtParent',
  //   filePath: "Main",
  //   component: Main,
  //   meta: {
  //     icon: 'md-build',
  //     title: '手持终端',
  //     access: ['superAdmin', 'admin'],
  //   },
  //   children: [
  //     {
  //       path: '/params',
  //       name: 'params',
  //       filePath: "params",
  //       meta: {
  //         title: '手持终端',
  //         icon: 'md-home'
  //       },
  //       component: () => import('@/view/params/params.vue')
  //     }
  //   ]
  // },
]

/*
{
    path: '/system',
    name: 'system',
    meta: {
      icon: 'md-build',
      access: ['admin', 'superAdmin'],
      title: '系统管理'
    },
    component: Main,
    children: [
      {
        path: 'systemParam',
        name: 'systemParam',
        meta: {
          icon: 'md-code',
          access: ["admin"], // 'superAdmin'
          title: '参数管理'
        },
        component: () => import('@/view/System/SystemParamPage')
      },
      {
        path: 'DictPage',
        name: 'DictPage',
        meta: {
          icon: 'md-text',
          access: ['superAdmin'],
          title: '字典管理'
        },
        component: () => import('@/view/System/DictPage')
      },
      {
        path: 'Role',
        name: 'Role',
        meta: {
          icon: 'ios-happy',
          access: ['superAdmin'],
          title: '角色管理'
        },
        component: () => import('@/view/System/Role')
      },
      {
        path: 'rates',
        name: 'rates',
        meta: {
          icon: 'ios-water',
          access: ['superAdmin', 'admin'],
          title: '收费标准'
        },
        component: () => import('@/view/System/Rates')
      },
      {
        path: 'menu',
        name: 'menu',
        meta: {
          icon: 'ios-menu',
          access: ['superAdmin'],
          title: '菜单管理'
        },
        component: () => import('@/view/System/Menu')
      },
    ]
  },
  {
    path: '/org',
    name: 'org',
    meta: {
      icon: 'ios-people',
      access: ['superAdmin', 'admin'],
      title: '组织架构管理'
    },
    component: Main,
    children: [
      {
        path: 'Village',
        name: 'Village',
        meta: {
          icon: 'md-pricetags',
          access: ['superAdmin', 'admin'],
          title: '村庄管理'
        },
        component: () => import('@/view/Org/Village')
      },
      {
        path: 'TSysUser',
        name: 'TSysUser',
        meta: {
          icon: 'ios-person',
          access: ['superAdmin', 'admin'],
          title: '用户管理'
        },
        component: () => import('@/view/Org/TSysUser')
      }
    ]
  }, {
    path: '/WaterManage',
    name: 'WaterManage',
    meta: {
      icon: 'md-phone-landscape',
      access: ['superAdmin', 'admin'],
      title: '设备管理'
    },
    component: Main,
    children: [
      {
        path: 'WsGroup',
        name: 'WsGroupr',
        meta: {
          icon: 'md-pint',
          access: ['superAdmin', 'admin'],
          title: '表井管理'
        },
        component: () => import('@/view/WaterManage/WsGroup')
      },
      {
        path: 'WsMeter',
        name: 'WsMeter',
        meta: {
          icon: 'md-pie',
          access: ['superAdmin', 'admin'],
          title: '水表设备管理'
        },
        component: () => import('@/view/WaterManage/WsMeter')
      },
    ]
  },
  {
    path: '/Pays',
    name: 'Pays',
    meta: {
      icon: 'md-water',
      access: ['superAdmin', 'admin'],
      title: '用水缴费'
    },
    component: Main,
    children: [
      {
        path: 'Pay',
        name: 'Pay',
        meta: {
          icon: 'md-planet',
          access: ['superAdmin', 'admin'],
          title: '用水缴费'
        },
        component: () => import('@/view/Pays/Pay')
      }
    ]
  },
  {
    path: '/Logs',
    name: 'Logs',
    meta: {
      icon: 'ios-unlock',
      access: ['superAdmin', 'admin'],
      title: '日志管理'
    },
    component: Main,
    children: [
      {
        path: 'Log',
        name: 'Log',
        meta: {
          icon: 'ios-book',
          access: ['superAdmin', 'admin'],
          title: '读表记录'
        },
        component: () => import('@/view/Logs/Log')
      },
      {
        path: 'PayLog',
        name: 'PayLog',
        meta: {
          icon: 'md-bookmarks',
          access: ['superAdmin', 'admin'],
            title: '缴费记录'
        },
        component: () => import('@/view/Logs/PayLog')
      }
    ]
  },
  {
    path: '/test',
    name: 'test',
    component: Main,
    meta: {
      icon: 'md-build',
      access: ['superAdmin'],
      title: '测试页',
    },
    children: [
      {
        path: '/testPage',
        name: 'testPage',
        meta: {
          title: '测试页',
          access: ['superAdmin'],
          icon: 'md-home'
        },
        component: () => import('@/view/test/test.vue')
      }
    ]
  },
 */

export {
  beforeRouter,
  afterRouter
}
