/* eslint-disable */
import module from './js'
let myPlugin = {};
myPlugin.install = function(Vue, option = {}) {
  let config = module.config(option);
  Object.keys(module).forEach((m, i) => {
    switch(m) {
      case 'prototype':
        Vue.prototype[`${config.prefix}${m}`] = Object.values(module)[i](config);
        break;
      case 'isIe':
        Vue.prototype[`${config.prefix}${m}`] = Object.values(module)[i];
        break;
      default:
        Vue.prototype[`${config.prefix}${m}`] = Object.values(module)[i];
        break;
    }
  })
};
export default myPlugin
