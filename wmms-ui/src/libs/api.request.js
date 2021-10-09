import HttpRequest from '@/libs/axios'
import config from '@/config'
import api from '@/api/login'
import { getExpires, getReToken, setTokenData, getToken } from '@/libs/util'
import Router from '@/router'
export const baseUrl = process.env.NODE_ENV === 'development' ? config.baseUrl.dev : config.baseUrl.pro

/**
 * @return {string}
 */
async function SetToken (url) {
  // 添加token并判断
  if (config.filteredRequest.findIndex(m => m === url) === -1) {
    // 如果当前token过期则重新请求token
    if (Number(getExpires()) && new Date().getTime() > Number(getExpires())) {
      const refreshToken = getReToken()
      await api.refreshToken({
        grant_type: 'refresh_token',
        client_id: 'client_1',
        client_secret: 'user',
        refresh_token: refreshToken, // grant_type=refresh_token&client_id=client_1&client_secret=user&refresh_token=
      }).then((res) => {
        if (res.status === 200) {
          const data = res.data
          setTokenData(data)
        }
        return Promise.resolve(res)
      }).catch(err => {
        console.log(err)
        Router.push({
          name: 'login'
        })
      })
      return getToken()
    } else {
      // 未过期使用当前token
      return getToken()
    }
  } else {
    return ''
  }
}

export const axios = new HttpRequest(baseUrl)
export async function post (url, data, params = {}) {
  const token = await SetToken(url)
  return axios.request(Object.assign({
    url,
    data,
    method: 'post'
  }, params, token && {
    headers: {
      authorization: token
    }
  }))
}

export const get = async (url, data, params = {}) => {
  const token = await SetToken(url)
  return axios.request(Object.assign({
    url,
    params: data,
    method: 'get'
  }, params, token && {
    headers: {
      authorization: token
    }
  }))
}

export const del = async (url, data, params = {}) => {
  const token = await SetToken(url)
  return axios.request(Object.assign({
    url,
    params: data,
    method: 'delete'
  }, params, token && {
    headers: {
      authorization: token
    }
  }))
}

export const put = async (url, data, params = {}) => {
  const token = await SetToken(url)
  return axios.request(Object.assign({
    url,
    data,
    method: 'put'
  }, params, token && {
    headers: {
      authorization: token
    }
  }))
}
export const prefix = ''
export const bgmt = '/bgmt'
