import Mock from 'mockjs'
const files = require.context('.', false, /\.js$/)
files.keys().forEach(key => {
  if (key === './index.js') return
  // modules[key.replace(/(\.\/|\.js)/g, '')] = files(key).default
  Mock.mock(new RegExp(key.replace(/(\.\/|\.js)/g, '')), files(key).default)
})
export default files
