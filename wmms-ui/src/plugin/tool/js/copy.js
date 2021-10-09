import copy from 'copy-to-clipboard'
import { Message } from 'view-design'

export default function (text, params = {}) {
  copy(text, params)
  Message.success('复制到粘贴板成功！')
}
