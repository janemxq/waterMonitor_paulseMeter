export function columns () {
  return [
    {
      type: 'selection',
      width: 60,
      align: 'center'
    },
    {
      key: 'name',
      align: 'center',
      title: '名称'
    },
    {
      key: 'villageName',
      align: 'center',
      title: '所在村落'
    },
    // {
    //   key: 'type',
    //   align: 'center',
    //   title: '类型',
    //   render: (h, param) => {
    //     return h('span', USERTYPE[param.row.type])
    //   }
    // },
    {
      key: 'typeName',
      align: 'center',
      title: '类型'
    },
    {
      key: 'idCard',
      align: 'center',
      title: '身份证'
    },
    {
      key: 'phone',
      align: 'center',
      title: '手机号'
    },
    {
      key: 'loginAccount',
      align: 'center',
      title: '账号'
    },
    // {
    //   key: 'createTime',
    //   align: 'center',
    //   title: '创建时间'
    // },
    {
      key: 'updateTime',
      align: 'center',
      title: '最后修改时间'
    },
    {
      key: 'remark',
      align: 'center',
      title: '备注'
    },
    {
      key: '',
      title: '操作',
      align: 'center',
      width: 220,
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
          h('Button', {
            props: {
              type: 'success',
              size: 'small'
            },
            style: {
              marginLeft: '8px',
              textAlign: 'left',
              display: param.row.deleted === '1' ? 'none' : ''
            },
            on: {
              click: () => {
                this.editPassword(param.row)
              }
            }
          }, '重置密码'),
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
      key: 'villageSn',
      title: '村名',
      type: 'select',
      data: []
    },
    {
      key: 'name',
      title: '姓名',
      type: 'input'
    },
    {
      key: 'phone',
      title: '手机号',
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

export const title = '用户列表'
