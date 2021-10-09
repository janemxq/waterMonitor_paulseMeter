import 'babel-polyfill'
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import iView from 'view-design'
import config from '@/config'
import importDirective from '@/directive'
import { directive as clickOutside } from 'v-click-outside-x'
import { groupTree, layout } from '@/components/layout'
import installPlugin from '@/plugin'
import globalTool from '@/plugin/tool/main'
import tableTemplate from '@/components/tableTemplate/commonTable'
import './index.less'
import '@/assets/icons/iconfont.css'
import '@/assets/less/init.less'
import 'vue-loaders/dist/vue-loaders.css'
import VueLoaders from 'vue-loaders'
import commitTree from '@/components/tree'
// 实际打包时应该不引入mock
/* eslint-disable */
 if (process.env.NODE_ENV !== 'production')
 require('@/mock')
Vue.use(iView)
Vue.use(globalTool)
Vue.component('tableTemplate', tableTemplate)
Vue.component('MyLoading', VueLoaders.component)
Vue.component('ComGroupTree', groupTree)
Vue.component('ComLayout', layout)
Vue.component('CommitTree', commitTree)
// Vue.component('VideoRoom', VideoRoom)
/**
 * @description 注册admin内置插件
 */
installPlugin(Vue)
/**
 * @description 生产环境关掉提示
 */
Vue.config.productionTip = true
/**
 * @description 全局注册应用配置
 */
Vue.prototype.$config = config
/**
 * 注册指令
 */
importDirective(Vue)
Vue.directive('clickOutside', clickOutside)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
