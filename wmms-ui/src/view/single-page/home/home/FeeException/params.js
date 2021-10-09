export function columns () {
  return [
    {
      key: 'meterSn',
      align: 'center',
      title: '设备地址'
    },
    {
      key: 'userName',
      align: 'center',
      title: '用户名'
    },
    {
      key: 'phone',
      align: 'center',
      title: '手机号',
      width: 125
    },
    {
      key: 'account',
      align: 'center',
      title: '欠费金额'
    },
    {
      key: 'balance',
      title: '余额',
      align: 'center'
    },
    {
      key: 'startNum',
      align: 'center',
      title: '水表起码'
    },
    {
      key: 'endNum',
      align: 'center',
      title: '水表止码'
    },
    {
      key: 'volume',
      align: 'center',
      title: '用水量'
    }
  ]
}

export function seek () {
  return []
}

export const params = {
  current: 1,
  size: 10
}

export const modalParams = {
  show: false, // 弹窗开关
  width: 600, // 弹框宽度
  modalLoading: true, // 点击确定Loading状态
  type: 'add', // 弹出框类型。目 前支持三种 add edit info
  title: '新增', // 弹出框头部
  maskClosable: false // 点击弹框遮罩是否关闭
}

export const title = '异常缴费'
