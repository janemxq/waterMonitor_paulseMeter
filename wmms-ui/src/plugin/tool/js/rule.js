import check from '@/libs/checkRules'
/* eslint-disable */
let extValid = {}
/**
 * 验证用户输入是否为空格
 */
extValid.validEmpty = function (rule, value, callback) {
  if (value.replace(/ /g, '').length === 0) {
    callback(new Error('输入不能为空'))
  }
  callback()
}

extValid.validateLen30 = function (rule, value, callback) {
  if (value === '') {
    callback(new Error('输入不能为空'))
  } else {
    if (getRealLen(value) > 30) {
      callback(new Error('长度不能大于30个字符（或15个汉字）'))
    }
    callback()
  }
}
/**
 * 限制输入英文和数组
 */
extValid.ruleNumAndLet = function(rule, value, callback, count = 30, str) {
  if (value === '') callback()
  if (!/^([0-9]|[a-z]|[A-Z])*?$/g.test(value) || value.length > count) {
    callback(new Error(str ? str : `仅限输入英文和数字且不得大于${count}个字符`))
  }
  callback()
}
/**
 * 限制输入英文和数组最小长度
 */
extValid.ruleNumAndLetMin = function(rule, value, callback, count = [3, 20], str) {
  if (value === '') callback()
  if (!/^([0-9]|[a-z]|[A-Z])*?$/g.test(value) || value.length > count[1] || value.length < count[0]) {
    callback(new Error(str ? str : `仅限输入英文和数字且不得大于${count[1]}个字符或小于${count[0]}个字符`))
  }
  callback()
}
/**
 * 限制输入中文
 */
extValid.ruleNoChinese = function(rule, value, callback, count = 30, str) {
  if (value === '') callback()
  if (/[\u4E00-\u9FA5]/g.test(value) || value.length > count) {
    callback(new Error(str ? str : `不可以输入中文且不大于${count}个字符`))
  }
  callback()
}
/**
 * 限制输入中文最小长度
 */
extValid.ruleNoChineseMin = function(rule, value, callback, count = [2, 30], str) {
  if (value === '') callback()
  if (/[\u4E00-\u9FA5]/g.test(value) || value.length > count[1] || value.length < count[0]) {
    callback(new Error(str ? str : `不可以输入中文且不大于${count[1]}个字符或小于${count[0]}个字符`))
  }
  callback()
}
/**
 * 限制长度
 */
extValid.ruleLength = function(rule, value, callback, count = 30, str) {
  if (!value) callback()
  if (getRealLen(value.toString()) > count) {
    callback(new Error(str ? str : `长度不能大于${count}个字符（或${Math.floor(count / 2)}个汉字）`))
  }
  callback()
}
/**
 * 限制长度
 */
extValid.ruleLengthMin = function(rule, value, callback, count = [2, 30], str) {
  if (!value) callback()
  if (getRealLen(value.toString()) > count[1] || getRealLen(value.toString()) < count[0]) {
    callback(new Error(str ? str : `长度不能大于${count[1]}个字符或小于${count[0]}个字符`))
  }
  callback()
}
/**
 * 限制数字长度
 */
extValid.ruleNumberMin = function(rule, value, callback, count = [2, 30], str) {
  if (!value) callback()
  if (/^\d*?\.?\d*?$/.test(value) && (getRealLen(value.toString()) > count[1] || getRealLen(value.toString()) < count[0])) {
    callback(new Error(str ? str : `只可以输入数字,长度不能大于${count[1]}个字符或小于${count[0]}个字符`))
  }
  callback()
}
/**
 * 限制数字 不包含小数点
 */
extValid.ruleNumber = function(rule, value, callback, len = 30) {
  console.log(/\D/g.test(value), value)
  if (value === '') callback()
  if (/\D/g.test(value) || value.toString().length > len) {
    callback(new Error(`只可以输入数字且不得大于${len}位`))
  }
  callback()
}
/**
 * 限制数字 包含小数点
 */
extValid.ruleNumbers = function(rule, value, callback, len = 30) {
  if (value === '') callback()
  if (/^\d*?\.?\d*?$/.test(value) && value.toString().length <= len) {
    callback()
  }
  callback(new Error(`不得大于${len}位数字`))
}
// 限制密码
extValid.nonePassword = function (rule, value, callback, arr = [8, 20]) {
  if (arr.length !== 2 && arr.findIndex(m => /\D/.test(m) !== -1)) console.error('nonePassword 数组长度为2且必须为数字')
  if (!value) {
    callback()
  } else if (!passwordCallBack(value, arr)) {
    callback(new Error(`密码为${arr[0]}~${arr[1]}大写字母、小写字母、数字、下划线的组合`))
  }
  callback()
}
function passwordCallBack (value, arr = [8, 20]) {
  return new RegExp(`^\\w{${arr[0]},${arr [1]}}$`).test(value)
}
extValid.validateLen20 = function (rule, value, callback) {
  if (value === '') {
    callback(new Error('输入不能为空'))
  } else {
    if (getRealLen(value) > 20) {
      callback(new Error('长度不能大于20个字符（或10个汉字）'))
    }
    callback()
  }
}
extValid.validateUserName = function (rule, value, callback) {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else {
    if (!checkLoginname(value)) {
      callback(new Error('用户名为6~20位字母、数字或下划线组成'))
    }
    callback()
  }
}
extValid.validateIP = function (rule, value, callback) {
  if (value !== '') {
    if (!validIP(value)) {
      callback(new Error('请输入正确格式的IP'))
    }
  }
  callback()
}
extValid.validatePwd = function (rule, value, callback) {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (!checkLoginname(value)) {
      callback(new Error('密码为6~20位字母、数字或下划线组成'))
    }
    callback()
  }
}
extValid.validatePass = function (rule, value, callback) {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {

  }
}
extValid.validateIdCardB = function (obj) {
  if (this.validateIdCard(obj) === 0) {
    return true
  } else {
    return false
  }
}
extValid.validateUnifiedCode = function (rule, value, callback) {
  if (value === '') {
    callback(new Error('请输入统一社会信用代码'))
  } else {
    if (!checkUnifiedCode(value) || value.length !== 18) {
      callback(new Error('统一社会信用代码为18位英文字母、数字组成'))
    }
    callback()
  }
}
/**
 * 功能：验证银行卡号
 */
extValid.validateBankAccount = function (rule, value, callback) {
  if (value === '') {
    callback(new Error('请输入对公账户账号'))
  } else {
    if (!check.validateBankCard(value)) {
      callback(new Error('请输入正确格式的对公账户账号'))
    }
    callback()
  }
}
extValid.validateIdCard = function (rule, value, callback) {
  if (value !== '') {
    if (!check.validateIdCard(value)) {
      callback(new Error('请输入正确的身份证号码'))
    }
    callback()
  }
  callback()
}
extValid.validateMAC = function (rule, value, callback) {
  if (value !== '') {
    if (!check.validateMAC(value)) {
      callback(new Error('请输入正确的MAC地址'))
    }
    callback()
  }
  callback()
}
extValid.validateContact = function (rule, value, callback) {
  if (value === '') {
    callback()
  } else {
    if (!(check.validateLandline(value) || check.validateMobile(value))) {
      callback(new Error('请输入正确格式的手机号或座机号'))
    }
    callback()
  }
}
extValid.validatePhone = function (rule, value, callback) {
  if (value) {
    if (!(check.validateMobile(value))) {
      callback(new Error('请输入正确的手机号码'))
    }
    callback()
  }
  callback()
}
extValid.valArr = function (ele) {
  let obj = {}
  ele.map(m => {
    if (typeof m === 'string') {
      obj[m] = {
        validator: (rule, value, callback) => {
          if (getRealLen(value) > 30) {
            callback(new Error(`长度不能大于30个字符（或15个汉字）`))
          }
          callback()
        }, trigger: 'blur'
      }
    } else {
      obj[m.name] = {
        validator: (rule, value, callback) => {
          if (getRealLen(value) > m.length) {
            callback(new Error(`长度不能大于${m.length}个字符（或${Math.floor(m.length/2)}个汉字）`))
          }
          callback()
        }, trigger: m.trigger || 'blur'
      }
    }
  })
  return obj
}

/**
 * 获取字符串实际长度（英文、汉字）
 */
function getRealLen (str) {
  return str.replace(/[\u0391-\uFFE5]/g, 'aa').length // 先把中文替换成两个字节的英文，在计算长度
}

/**
 * 验证用户名格式 （6~20位字母、数字、下划线）
 */
function checkLoginname (str) {
    const reg = /^(\w){6,20}$/
  return reg.test(str)
}

/**
 * 验证密码（包括8~20大写字母、小写字母、数字、下划线组合） 特殊字符：
 */
function validPassword (str) {
  if (/^.*?[\d]+.*$/.test(str) && /^.*?[A-Z]/.test(str) &&
    /^.*?[a-z]/.test(str) && /^.*?[_].*$/.test(str) &&
    /^.{8,20}$/.test(str)) {
    return true
  } else {
    return false
  }
}

/**
 * 验证统一社会信用代码格式 （18位字母、数字）
 */
function checkUnifiedCode (str) {
  let reg = /^[A-Za-z0-9]+$/
  let flag = reg.test(str)
  return flag
}

function validIP (ip) {
  let reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
  return reg.test(ip)
}

export default extValid
