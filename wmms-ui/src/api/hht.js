import { del, get, put, post, prefix } from '@/libs/api.request'
const urls = {
  postPadMeterYard: prefix + '/padMeterYard', // 新增pad端表井
  getPadMeterYardAll: prefix + '/padMeterYard/all', // 查询所有pad端表井
  getPadMeterYardGetMeterNumId: prefix + '/padMeterYard/getMeterNum/', // 根据id获取数据
  postPadMeterYardPage: prefix + '/padMeterYard/page', // 搜索pad端表井(分页)
  PadMeterYardId: prefix + '/padMeterYard/', // 根据id获取,修改,删除pad端表井

  getPadMeterInfoGetMetersSn: prefix + '/padMeterInfo/getMeters/', // 根据表井标识获取水表列表
  postPadMeterInfoGetMetersNum: prefix + '/padMeterInfo/getMetersNum', // 获取水表数据
  postPadMeterInfoInit: prefix + '/padMeterInfo/init', // 初始化水表
  postPadMeterYardUpload: prefix + '/padMeterYard/upload' // 表井上传
}
export default {
  postPadMeterYardUpload (params) {
    return post(urls.postPadMeterYardUpload, params)
  },
  postPadMeterInfoInit (params) {
    return post(urls.postPadMeterInfoInit, params)
  },
  // 表井
  postPadMeterYard (params) {
    return post(urls.postPadMeterYard, params)
  },
  getPadMeterYardAll (params) {
    return get(urls.getPadMeterYardAll, params)
  },
  getPadMeterYardGetMeterNumId (sn, params) {
    return get(urls.getPadMeterYardGetMeterNumId + sn, params)
  },
  postPadMeterYardPage (params) {
    return post(urls.postPadMeterYardPage, params)
  },
  getPadMeterYardId (params, id) {
    return get(urls.PadMeterYardId + id, params)
  },
  putPadMeterYardId (params, id) {
    return put(urls.PadMeterYardId + id, params)
  },
  delPadMeterYardId (params, id) {
    return del(urls.PadMeterYardId + id, params)
  },
  // 水表
  getPadMeterInfoGetMetersSn (sn) {
    return get(urls.getPadMeterInfoGetMetersSn + sn)
  },
  postPadMeterInfoGetMetersNum (params) {
    return post(urls.postPadMeterInfoGetMetersNum, params)
  }
}
