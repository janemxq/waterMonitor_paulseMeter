export function columns () {
  return [
    {
      key: 'sortId',
      align: 'center',
      title: '排序'
    },
    {
      key: 'villageName',
      align: 'center',
      title: '村庄名称'
    },
    {
      key: 'address',
      align: 'center',
      title: '村庄地址'
    },
    {
      key: 'createTime',
      align: 'center',
      title: '创建时间'
    },
    {
      key: 'updateTime',
      align: 'center',
      title: '最后修改时间'
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
  return [
    // {
    //   key: 'villageSn',
    //   title: '村名',
    //   type: 'select',
    //   data: []
    // }
    {
      key: 'villageName',
      title: '村名',
      type: 'input'
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

export const title = '村庄列表'
