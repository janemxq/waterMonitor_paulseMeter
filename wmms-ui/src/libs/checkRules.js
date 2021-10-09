let check = {}
check.event = {}
check.blurFun = function (event) {
  check.checkOnEvent(event)
  check.event = event
}
check.checkOnEvent = function (event) {
  let ele = event.target
  let checkType = ele.getAttribute('checkType')
  let maxLen = ele.getAttribute('maxLength')
  let minLen = ele.getAttribute('minLength')
  let len = ele.getAttribute('length')
  let rangeLen = ele.getAttribute('rangeLength')
  if (checkType !== null) {
    if (!check.checkBase('checkType', checkType, ele)) return false
  }
  if (maxLen !== null) {
    if (check.checkBase('maxLength', maxLen, ele)) return false
  }
  if (minLen !== null) {
    check.checkBase('minLength', minLen, ele)
  }
  if (len !== null) {
    check.checkBase('length', len, ele)
  }
  if (rangeLen !== null) {
    check.checkBase('rangeLength', rangeLen, ele)
  }
}
check.checkBase = function (rule, attrValue, ele) {
  let errMsg = ''
  let flag = true
  if (rule === 'checkType') {
    let arrayType = attrValue.split(' ')
    for (let i = 0, len = arrayType.length; i < len; i++) {
      let type = arrayType[i]
      // 校验checkType
      let rel = check.checkCheckType(type, ele.value)
      flag = rel.flag
      errMsg = rel.errMsg
    }
  } else if (rule === 'maxLength') {
    if (!check.validateMaxLen(ele.value, parseInt(attrValue))) {
      flag = false
      errMsg = '输入长度最多为' + attrValue + '个字符(汉字算两个字符)'
    }
  } else if (rule === 'minLength') {
    if (!check.validateMinLen(ele.value, parseInt(attrValue))) {
      flag = false
      errMsg = '输入长度最少为' + attrValue + '个字符(汉字算两个字符)'
    }
  } else if (rule === 'length') {
    if (!check.validateLen(ele.value, parseInt(attrValue))) {
      flag = false
      errMsg = '输入长度为' + attrValue + '个字符(汉字算两个字符)'
    }
  } else if (rule === 'rangeLength') {
    let range = attrValue.split(',')
    if (!check.validateRangeLen(ele.value, parseInt(range[0]), parseInt(range[1]))) {
      flag = false
      errMsg = '输入长度为介于' + range[0] + '和' + range[1] + '个字符(汉字算两个字符)'
    }
  }
  if (!flag) {
    ele.style.border = '1px solid red'
    showErr(errMsg)
    return false
  } else {
    ele.style.borderColor = '1px solid #ccc'
    return true
  }
}
/**
 * 功能：验证checkType属性
 */
check.checkCheckType = function (type, value) {
  let errMsg = ''
  let flag = true
  if (type === 'required') {
    if (!check.required(value)) {
      errMsg = '输入不能为空'
      flag = false
    }
  } else if (type === 'phone') {
    if (!check.validateMobile(value)) {
      errMsg = '请输入正确的手机号'
      flag = false
    }
  } else if (type === 'telephone') {
    if (!check.validateLandline(value)) {
      errMsg = '请输入正确的固定电话'
      flag = false
    }
  } else if (type === 'contact') {
    if (!check.validateContact(value)) {
      errMsg = '请输入正确的手机号或固话'
      flag = false
    }
  } else if (type === 'email') {
    if (!check.validateEmail(value)) {
      errMsg = '请输入正确的邮箱'
      flag = false
    }
  } else if (type === 'idCard') {
    if (!check.validateIdCard(value)) {
      errMsg = '请输入正确的身份证号码'
      flag = false
    }
  } else if (type === 'chinese') {
    if (!check.validateChinese(value)) {
      errMsg = '请输入中文'
      flag = false
    }
  } else if (type === 'number') {
    if (!check.validateNumber(value)) {
      errMsg = '请输入正数、负数、小数'
      flag = false
    }
  } else if (type === 'url') {
    if (!check.validateUrl(value)) {
      errMsg = '请输入正确的url'
      flag = false
    }
  } else if (type === 'date') {
    if (!check.validateDate(value)) {
      errMsg = '请输入正确的日期'
      flag = false
    }
  } else if (type === 'dateTime') {
    if (!check.validateDateTime(value)) {
      errMsg = '请输入正确的时间'
      flag = false
    }
  } else if (type === 'digits') {
    if (!check.validateDigits(value)) {
      errMsg = '请输入整数'
      flag = false
    }
  } else if (type === 'bankCard') {
    if (!check.validateBankCard(value)) {
      errMsg = '请输入合法的银行卡号'
      flag = false
    }
  } else if (type === 'Legal') {
    if (!check.validateLegal(value)) {
      errMsg = '输入包含非法字符'
      flag = false
    }
  }
  return {
    flag,
    errMsg
  }
}
check.required = function (str) {
  return (str.length !== 0)
}
/**
 * 功能：验证座机
 */
check.validateLandline = function (str) {
  let reg = /^(0\d{2,3}-?)?\d{7,8}$/
  if (reg.test(str)) {
    return true
  } else {
    return false
  }
}
/**
 * 功能：验证手机号
 */
check.validateMobile = function (str) {
  if (str.length !== 11) return false
  let reg = /^1[0-9]{10}$/
  return reg.test(str)
}
/**
 * 功能：验证联系方式
 */
check.validateContact = function (str) {
  if (this.validateMobile(str) || this.validateLandline(str)) {
    return true
  } else {
    return false
  }
}
/**
 * 功能：验证汉字
 */
check.validateChinese = function (str) {
  let reg = /^[\u4e00-\u9fa5]+$/
  return reg.test(str)
}
/**
 * 验证mac地址
 * @param {*} value
 */
check.validateMAC = function (str) {
  var reg = /(([a-f0-9]{2}:)|([a-f0-9]{2}-)){5}[a-f0-9]{2}/gi
  var flag = reg.test(str)
  return flag
}
/**
 * 功能：验证email
 */
check.validateEmail = function (str) {
  let reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
  return reg.test(str)
}
/**
 * 功能：验证url
 */
check.validateUrl = function (str) {
  let reg = /^((https|http|ftp|rtsp|mms)?:\/\/)+[A-Za-z0-9]+\.[A-Za-z0-9]+[=%\-&_~`@[\]:+!]*([^<>])*$/
  return reg.test(str)
}
/**
 * 功能：验证date:2009-07-13 或 2009年07月13日
 */
check.validateDate = function (str) {
  let reg = /(^\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2]\d|3[0-1])$)|(^\d{4}年\d{1,2}月\d{1,2}日$)/
  return reg.test(str)
}
/**
 * 功能：验证dateTime:2014-01-01 12:00:00 2014-1-1 12:00:00
 */
check.validateDateTime = function (str) {
  let reg = /^\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2]\d|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/
  return reg.test(str)
}
/**
 * 功能：验证Number: 正数、负数、小数
 */
check.validateNumber = function (str) {
  let reg = /^-?[0-9]+\.?[0-9]*$/
  return reg.test(str)
}
/**
 * 功能：验证digits: 整数(正、负)
 */
check.validateDigits = function (str) {
  let reg = /(^-?[1-9]\d*$)/
  return reg.test(str)
}
/**
 * 功能：验证身份证号
 */
check.validateIdCard = function (obj) {
  if (this.validateIdCardB(obj) === 0) {
    return true
  } else {
    return false
  }
}
/**
 * 功能：验证最大长度
 */
check.validateMaxLen = function (obj, len) {
  return !(getStrActualLen(obj) > len)
}
/**
 * 功能：验证最小长度
 */
check.validateMinLen = function (obj, len) {
  return (getStrActualLen(obj) >= len)
}
/**
 * 功能：验证长度范围
 */
check.validateRangeLen = function (obj, minLen, maxLen) {
  let strLen = getStrActualLen(obj)
  return !(strLen < minLen || strLen > maxLen)
}
/**
 * 功能：验证长度
 */
check.validateLen = function (obj, len) {
  return (getStrActualLen(obj) === len)
}
/**
 * 功能：验证银行卡号
 */
check.validateBankCard = function (bankno) {
  if (bankno.length < 16 || bankno.length > 19) {
    // msg = '银行卡号长度必须在16到19之间'
    return false
  }
  let reg = /^\d*$/
  if (!reg.test(bankno)) {
    // msg = '银行卡号必须全为数字'
    return false
  }
  return true
  /* return luhnCheck(bankno) */
}

// 显示错误信息
function showErr (str) {
  // alert(str)
}

// 银行卡号Luhn校验算法
// luhn校验规则：16位银行卡号（19位通用）:
// 1.将未带校验位的 15（或18）位卡号从右依次编号 1 到 15（18），位于奇数位号上的数字乘以 2。
// 2.将奇位乘积的个十位全部相加，再加上所有偶数位上的数字。
// 3.将加法和加上校验位能被 10 整除。
// bankno为银行卡号
/* function luhnCheck (bankno) {
  let lastNum = bankno.substr(bankno.length - 1, 1) // 取出最后一位（与luhn进行比较）

  let first15Num = bankno.substr(0, bankno.length - 1) // 前15或18位
  let newArr = new Array([])
  for (let i = first15Num.length - 1; i > -1; i--) {    // 前15或18位倒序存进数组
    newArr.push(first15Num.substr(i, 1))
  }
  let arrJiShu = []  // 奇数位*2的积 <9
  let arrJiShu2 = [] // 奇数位*2的积 >9

  let arrOuShu = []  // 偶数位数组
  for (let j = 0; j < newArr.length; j++) {
    if ((j + 1) % 2 === 1) { // 奇数位
      if (parseInt(newArr[j]) * 2 < 9) arrJiShu.push(parseInt(newArr[j]) * 2)
      else arrJiShu2.push(parseInt(newArr[j]) * 2)
    } else arrOuShu.push(newArr[j]) // 偶数位
  }

  let jishuChild1 = [] // 奇数位*2 >9 的分割之后的数组个位数
  let jishuChild2 = [] // 奇数位*2 >9 的分割之后的数组十位数
  for (let h = 0; h < arrJiShu2.length; h++) {
    jishuChild1.push(parseInt(arrJiShu2[h]) % 10)
    jishuChild2.push(parseInt(arrJiShu2[h]) / 10)
  }

  let sumJiShu = 0 // 奇数位*2 < 9 的数组之和
  let sumOuShu = 0 // 偶数位数组之和
  let sumJiShuChild1 = 0 // 奇数位*2 >9 的分割之后的数组个位数之和
  let sumJiShuChild2 = 0 // 奇数位*2 >9 的分割之后的数组十位数之和
  let sumTotal = 0
  for (let m = 0; m < arrJiShu.length; m++) {
    sumJiShu = sumJiShu + parseInt(arrJiShu[m])
  }

  for (let n = 0; n < arrOuShu.length; n++) {
    sumOuShu = sumOuShu + parseInt(arrOuShu[n])
  }

  for (let p = 0; p < jishuChild1.length; p++) {
    sumJiShuChild1 = sumJiShuChild1 + parseInt(jishuChild1[p])
    sumJiShuChild2 = sumJiShuChild2 + parseInt(jishuChild2[p])
  }
  // 计算总和
  sumTotal = parseInt(sumJiShu) + parseInt(sumOuShu) + parseInt(sumJiShuChild1) + parseInt(sumJiShuChild2)

  // 计算luhn值
  let k = parseInt(sumTotal) % 10 === 0 ? 10 : parseInt(sumTotal) % 10
  let luhn = 10 - k

  if (lastNum === luhn) {
    return true
  } else {
    return false
  }
} */
/**
 * 获得字符串的实际长度，中文为2，英文、数字为1 \x00-\xff 是字符编码的范围，如果查过这个范围，就不是字母和数字了
 * 把全部符合\x00-\xff条件的字符用**替换，然后计算长度，即遇到一个中文就用**替换，计算为两位
 *
 * @param {Object}
 *            sChars
 */
function getStrActualLen (sChars) {
  return sChars.replace(/[^\x00-\xff]/g, '**').length
}

/**
 * 验证用户输入是否包含特殊字符， 特殊字符包括:
 * <  > ' "   input select update insert delete  execute
 * @param str
 * @returns {String}
 */
check.validateLegal = function (str) {
  let validResult = validInputText(str)
  if (validResult !== '') {
    return false
  }
  return true
}

/**
 * 验证用户输入是否包含特殊字符， 特殊字符包括:
 * <  > ' "   input select update insert delete  execute
 * @param str
 * @returns {String}
 */
function validInputText (str) {
  str = str.toLowerCase()
  let flag = ''
  if (str.indexOf('input') >= 0) {
    flag = 'input'
  } else if (str.indexOf('select') >= 0) {
    flag = 'select'
  } else if (str.indexOf('update') >= 0) {
    flag = 'update'
  } else if (str.indexOf('insert') >= 0) {
    flag = 'insert'
  } else if (str.indexOf('delete') >= 0) {
    flag = 'delete'
  } else if (str.indexOf('execute') >= 0) {
    flag = 'execute'
  } else if (str.indexOf('html') >= 0) {
    flag = 'html'
  } else if (str.indexOf('iframe') >= 0) {
    flag = 'iframe'
  } else {
    flag = ''
  }
  return flag
}

/**
 * 功能：验证身份证号码是否有效 提 示信息：未输入或输入身份证号不正确！ 使用：validateIdCard(obj) 返回：0,1,2,3
 */
check.validateIdCardB = function (obj) {
  let aCity = {
    11: '北京',
    12: '天津',
    13: '河北',
    14: '山西',
    15: '内蒙古',
    21: '辽宁',
    22: '吉林',
    23: '黑龙江',
    31: '上海',
    32: '江苏',
    33: '浙江',
    34: '安徽',
    35: '福建',
    36: '江西',
    37: '山东',
    41: '河南',
    42: '湖 北',
    43: '湖南',
    44: '广东',
    45: '广西',
    46: '海南',
    50: '重庆',
    51: '四川',
    52: '贵州',
    53: '云南',
    54: '西 藏',
    61: '陕西',
    62: '甘肃',
    63: '青海',
    64: '宁夏',
    65: '新疆',
    71: '台湾',
    81: '香港',
    82: '澳门',
    91: '国 外'
  }
  let iSum = 0
  let sBirthday
  // var info = "";
  let strIDno = obj
  let idCardLength = strIDno.length
  if (!/^\d{17}(\d|x)$/i.test(strIDno) && !/^\d{15}$/i.test(strIDno)) {
    return 1 // 非法身份证号
  }
  if (aCity[parseInt(strIDno.substr(0, 2))] === null) {
    return 2// 非法地区
  }
  // 15位身份证转换为18位
  if (idCardLength === 15) {
    sBirthday = '19' + strIDno.substr(6, 2) + '-' +
            Number(strIDno.substr(8, 2)) + '-' +
            Number(strIDno.substr(10, 2))
    let d = new Date(sBirthday.replace(/-/g, '/'))
    let dd = d.getFullYear().toString() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
    if (sBirthday !== dd) {
      return 3 // 非法生日
    }
    strIDno = strIDno.substring(0, 6) + '19' + strIDno.substring(6, 15)
    /* strIDno = strIDno + GetVerifyBit(strIDno) */
  }
  // 判断是否大于2078年，小于1900年
  let year = strIDno.substring(6, 10)
  if (year < 1900 || year > 2078) {
    return 3 // 非法生日
  }
  // 18位身份证处理
  // 在后面的运算中x相当于数字10,所以转换成a
  strIDno = strIDno.replace(/x$/i, 'a')
  sBirthday = strIDno.substr(6, 4) + '-' + Number(strIDno.substr(10, 2)) + '-' + Number(strIDno.substr(12, 2))
  let d = new Date(sBirthday.replace(/-/g, '/'))
  if (sBirthday !== (d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate())) {
    return 3 // 非法生日
  }
  // 身份证编码规范验证
  for (let i = 17; i >= 0; i--) {
    iSum += (Math.pow(2, i) % 11) * parseInt(strIDno.charAt(17 - i), 11)
  }
  if (iSum % 11 !== 1) {
    return 1 // 非法身份证号
  }
  // 判断是否屏蔽身份证
  let words = new Array(['11111119111111111', '12121219121212121'])
  for (let k = 0; k < words.length; k++) {
    if (strIDno.indexOf(words[k]) !== -1) {
      return 1
    }
  }
  return 0
}
export default check
