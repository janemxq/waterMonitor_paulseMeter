import Cookies from 'js-cookie'
import config from '@/config'
const { cookieExpires } = config
export default {
  get: (key, params = {}) => {
    return Cookies.get(key, params)
  },
  set: (key, value, params = {}) => {
    return Cookies.set(key, value, Object.assign({
      expires: cookieExpires || 1
    }, params))
  },
  remove: (key, params = {}) => {
    return Cookies.remove(remove, params)
  }
}
