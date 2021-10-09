let session = {
  set (a, b) {
    sessionStorage.setItem(a, JSON.stringify(b))
  },
  get (a) {
    return JSON.parse(sessionStorage.getItem(a))
  },
  delete (a) {
    sessionStorage.removeItem(a)
  },
  clear () {
    sessionStorage.clear()
  }
}

export default session
