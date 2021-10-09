import { editUser as render } from '@/components/commonRender'
export function columns () {
  return [
    {
      key: 'groupName',
      align: 'center',
      title: '表井名称'
    },
    {
      key: 'villageName',
      title: '村名',
      align: 'center'
    },
    {
      key: 'address',
      title: '设备地址',
      align: 'center'
    },
    {
      key: 'createTime',
      title: '创建时间',
      align: 'center'
    },
    {
      key: 'updateTime',
      title: '更新时间',
      align: 'center'
    },
    {
      title: '最后修改人',
      align: 'center',
      render
    },
    {
      key: '',
      title: '操作',
      align: 'center',
      width: 180,
      render: (h, param) => {
        return h('div', [
          h('Button', {
            props: {
              type: 'warning',
              size: 'small'
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

export const title = '表井列表'
