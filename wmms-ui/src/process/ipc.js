import { ipcMain } from 'electron'

export function Event (win) {
  ipcMain.on('Event', (event, args) => {
    if (args === 'console') {
      win.webContents.openDevTools()
    }
  })
}
