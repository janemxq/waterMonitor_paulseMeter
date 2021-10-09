import Cookie from 'js-cookie'
import router from '@/router'
// const { ipcRenderer } = window.require('electron')

export const Event = (type) => {
  switch (type) {
    case "console":
      Console()
      break
    case "clear":
      Clear()
      break
    case "reset":
      Reset()
      break
  }
}

export const Console = () => {
  window.addEventListener('keyup', (event) => {
    if (event.ctrlKey && event.keyCode === 123) {
      // ipcRenderer.send('Event', 'console')
    }
  }, true)
}

export const Clear = () => {
  window.addEventListener('keyup', (event) => {
    if (event.ctrlKey && event.keyCode === 122) {
      Cookie.remove("token")
      Object.keys(localStorage).map(m => {
        localStorage.removeItem(m)
      })
      Object.keys(localStorage).map(m => {
        sessionStorage.removeItem(m)
      })
      router.push({
        name: "login"
      })
    }
  }, true)
}

export const Reset = () => {
  window.addEventListener('keyup', (event) => {
    if (event.ctrlKey && event.keyCode === 121) {
      history.go(0)
    }
  }, true)
}
