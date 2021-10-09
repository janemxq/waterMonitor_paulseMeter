let prototype = (config) => {
  if (config.each) {
    HTMLCollection.prototype[config.each] = function (callBack) { // 循环html元素
      for (var i = 0; i < this.length; i++) {
        callBack.call(this[i], this[i], i)
      }
    }
  }
}

export default prototype
