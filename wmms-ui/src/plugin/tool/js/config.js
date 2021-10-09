let config = (obj = {}) => {
  return Object.assign({
    cookies: false,
    indexOf: true,
    each: 'forEach',
    prefix: '$'
  }, process.env, obj)
}

export default config
