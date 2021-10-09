import { showColor as render } from '@/components/commonRender'
export function columns () {
  return [
    {
      type: 'selection',
      width: 60,
      align: 'center'
    },
    {
      key: 'sn',
      align: 'center',
      title: '编号'
    },
    {
      key: 'macSn',
      align: 'center',
      title: '通道号'
    },
    {
      key: 'userName',
      align: 'center',
      title: '用户名'
    },
    {
      key: 'groupName',
      align: 'center',
      title: '所属表井'
    },
    {
      key: 'villageName',
      title: '村名',
      align: 'center'
    },
    {
      key: 'meterTypeName',
      align: 'center',
      title: '用水类型'
    },
    // {
    //   key: 'meterInit',
    //   align: 'center',
    //   title: '水表初始值'
    // },
    {
      key: 'pulse',
      align: 'center',
      title: '方水/脉冲'
    },
    {
      key: 'online',
      align: 'center',
      title: '是否在线',
      render
    },
    {
      key: '',
      title: '操作',
      align: 'center',
      width: 210,
      render: (h, param) => {
        return h('div', [
          h('Button', {
            props: {
              type: 'success',
              size: 'small'
            },
            on: {
              click: () => {
                this.updateMeterDeal(param.row)
              }
            }
          }, '更新'),
          h('Button', {
            props: {
              type: 'warning',
              size: 'small'
            },
            style: {
              marginLeft: '8px',
            },
            on: {
              click: () => {
                this.edit(param.row)
              }
            }
          }, '修改'),
          h('Poptip', {
            style: {
              marginLeft: '8px',
              textAlign: 'left',
              marginRight: '8px',
              display: param.row.deleted === '1' ? 'none' : ''
            },
            props: {
              confirm: true,
              title: '是否确认删除'
            },
            on: {
              'on-ok': () => {
                this.handleDelete(param.row)
              }
            }
          },
          [h(
            'Button',
            {
              props: {
                type: 'error',
                size: 'small'
              }
            },
            '删除')]
          )
        ])
      }
    }
  ]
}

export function seek () {
  return [
    {
      type: 'seek',
      key: 'userSn',
      title: '用户名',
      data: [],
      remoteMethod: (name) => {
        this.seek[0].loading = true
        this.api.findUser({
          name,
          current: 1,
          size: 99
        }).then(res => {
          this.seek[0].data = res.data.records.map(m => {
            m.value = m.sn
            m.name = `${m.name}(${m.phone || m.villageName})`
            return m
          })
          console.log(this.seek)
        }).finally(() => {
          this.seek[0].loading = false
        })
      },
      loading: false
    }
  ]
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

export const title = '水表设备列表'
