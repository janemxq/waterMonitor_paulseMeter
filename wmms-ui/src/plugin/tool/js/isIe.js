function isie () {
  var explorer = window.navigator.userAgent
  var compare = function (s) { return (explorer.indexOf(s) >= 0) }
  var ie11 = (function () { return ('ActiveXObject' in window) })()
  if (compare('MSIE') || ie11) {
    if (/MSIE/.test(navigator.appVersion)) {
      return `ie ${navigator.appVersion.match(/MSIE (\d{1,2})/)[1]}`
    } else if (/rv:/.test(navigator.appVersion)) {
      return `ie ${navigator.appVersion.match(/rv:(\d{2})/)[1]}`
    }
  } else if (compare('Firefox') && !ie11) { return 'Firefox' } else if (compare('Chrome') && !ie11) { return 'Chrome' } else if (compare('Opera') && !ie11) { return 'Opera' } else if (compare('Safari') && !ie11) { return 'Safari' }
}

export default isie()
