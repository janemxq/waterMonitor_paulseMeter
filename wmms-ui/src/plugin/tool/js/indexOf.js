/* eslint-disable */
function indexOf(data) {
  let type;
  switch (Object.prototype.toString.call(data)) {
    case '[object Boolean]':
      type = 'boolean';
      break;
    case '[object Object]':
      type = 'object';
      break;
    case '[object Array]':
      type = 'array';
      break;
    case '[object Function]':
      type = 'function';
      break;
    case '[object Number]':
      type = 'number';
      break;
    case '[object String]':
      type = 'string';
      break;
    case '[object Undefined]':
      type = 'undefined';
      break;
    case '[object Null]':
      type = 'null';
      break;
  }
  if (/^\[object HTML.*?ment\]$/.test(Object.prototype.toString.call(data))) type = 'HTMLDom';
  return type
}

export default indexOf;
