export default {
  /**
   * @description 配置显示在浏览器标签的title
   */
  title: '山东朝启农村用水监测管理平台',
  /**
   * @description token在Cookie中存储的天数，默认1天
   */
  cookieExpires: 1,
  /**
   * @description 是否使用国际化，默认为false
   *              如果不使用，则需要在路由中给需要在菜单中展示的路由设置meta: {title: 'xxx'}
   *              用来在菜单中显示文字
   */
  useI18n: false,
  /**
   * @description api请求基础路径
   */
  baseUrl: {
    // dev: 'http://49.232.157.125:7771',
    // pro: 'http://192.168.3.32:7751'
    pro: 'http://127.0.0.1:7771'
    // pro: 'http://39.96.62.172:8070'
  },
  /**
   * @description 默认打开的首页的路由name值，默认为home
   */
  homeName: 'home',
  /**
   * @description 需要加载的插件
   */
  plugin: {
    'error-store': {
      showInHeader: true, // 设为false后不会在顶部显示错误日志徽标
      developmentOff: true // 设为true后在开发环境不会收集错误信息，方便开发中排查错误
    }
  },
  /**
   * @description 是否显示顶部的tab框
   * */
  tab: true,
  /**
   * @description 跳转时是不需要登录
   * */
  jumpList: [],
  /**
   * 不进行token校验的请求清单
   */
  filteredRequest: ['/oauth/token'],
  /**
   * 是否打印日志
   */
  console: true
}
