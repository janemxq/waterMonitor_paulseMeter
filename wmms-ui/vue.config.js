const path = require('path')
const CSSSplitWebpackPlugin = require('css-split-webpack-plugin').default

const resolve = dir => {
  return path.join(__dirname, dir)
}
// const externals = {
//   'T': 'T'
// }

// 项目部署基础
// 默认情况下，我们假设你的应用将被部署在域的根目录下,
// 例如：https://www.my-app.com/
// 默认：'/'
// 如果您的应用程序部署在子路径中，则需要在这指定子路径
// 例如：https://www.foobar.com/my-app/
// 需要将它改为'/my-app/'
// iview-admin线上演示打包路径： https://file.iviewui.com/admin-dist/
const BASE_URL = process.env.NODE_ENV === 'production'
  ? './'
  : '/'

module.exports = {
  devServer: {
    proxy: 'http://127.0.0.1:7771',
    https: false,
    open: false
  },
  // Project deployment base
  // By default we assume your app will be deployed at the root of a domain,
  // e.g. https://www.my-app.com/
  // If your app is deployed at a sub-path, you will need to specify that
  // sub-path here. For example, if your app is deployed at
  // https://www.foobar.com/my-app/
  // then change this to '/my-app/'
  publicPath: BASE_URL,

  // tweak internal webpack configuration.
  // see https://github.com/vuejs/vue-cli/blob/dev/docs/webpack.md
  // 如果你不需要使用eslint，把lintOnSave设为false即可
  lintOnSave: true,

  chainWebpack: config => {
    config.resolve.alias
      .set('@', resolve('src')) // key,value自行定义，比如.set('@@', resolve('src/components'))
      .set('_c', resolve('src/components'))
    // config.externals(externals)
  },

  // less 全局样式引入
  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'less',
      patterns: [
        './src/assets/less/index.less'
      ]
    },
    electronBuilder: {
      builderOptions: {
        copyright: '济南冰凌科技信息有限公司<bingling@binglingkeji.com>',
        productName: '山东朝启农村用水监测管理平台',
        'win': {
          'icon': 'public/icon.ico'
        },
        nsis: {
          'installerIcon': 'public/icon.ico', // 安装图标
          'uninstallerIcon': 'public/icon.ico', // 卸载图标
          'installerHeaderIcon': 'public/icon.ico', // 安装时头部图标
          shortcutName: '山东朝启农村用水监测管理平台',
          createDesktopShortcut: true
        }
      }
    }
  },

  // 设为false打包时不生成.map文件
  productionSourceMap: false,

  // 这里写你调用接口的基础路径，来解决跨域，如果设置了代理，那你本地开发环境的axios的baseUrl要写为 '' ，即空字符串
  // devServer: {
  //   proxy: 'localhost:3000'
  // }
  configureWebpack: config => {
    config.plugins.push(new CSSSplitWebpackPlugin({
      size: 4000,
      filename: 'css/[name]-[part].[ext]'
    }))
  }
}
