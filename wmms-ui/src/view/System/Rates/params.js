import { showColor as render } from '@/components/commonRender'
export function columns () {
  return [
    {
      key: 'displayName',
      align: 'center',
      title: '收费名称'
    },
    {
      key: 'feeStandard',
      align: 'center',
      title: '收费标准'
    },
    {
      key: 'sortId',
      align: 'center',
      title: '排序'
    },
    {
      key: 'isStep',
      align: 'center',
      title: '阶梯计价',
      render
    },
    // {
    //   key: 'createUserId',
    //   align: 'center',
    //   title: '创建人'
    // },
    // {
    //   key: 'updateUserId',
    //   align: 'center',
    //   title: '最后修改人'
    // },
    // {
    //   align: 'center',
    //   title: '最后修改时间',
    //   render: (h, params) => {
    //     return h('span', {}, params.row.updateTime ? params.row.updateTime : params.row.createTime)
    //   }
    // },
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

export const title = '收费标准'
