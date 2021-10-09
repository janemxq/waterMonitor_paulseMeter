import { del, put, post, prefix, baseUrl } from '@/libs/api.request'
const urls = {
  getWsMeterReader: prefix + '/wsMeterReader/page', // 搜索水表读数(分页)
  wsMeterReader: prefix + '/wsMeterReader', // 新增字典 更新字典 删除水表读数

  getWsFeeLog: prefix + '/wsFeeLog/page', // 搜索缴费记录(分页)

  importExcel: baseUrl + prefix + '/wsMeter/importExcel', // excel导入设备
}

export default {
  getWsMeterReader (params) {
    return post(urls.getWsMeterReader, params)
  },
  delWsMeterReader (params) {
    return del(`${urls.wsMeterReader}/${params.id}`)
  },
  addWsMeterReader (params) {
    return post(urls.wsMeterReader, params)
  },
  updateWsMeterReader (params) {
    return put(`${urls.wsMeterReader}/${params.id}`, params)
  },

  getWsFeeLog (params) {
    return post(urls.getWsFeeLog, params)
  },

  importExcel () {
    return urls.importExcel
  }
}
