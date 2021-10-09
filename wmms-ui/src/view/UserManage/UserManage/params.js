export function columns () {
  return [
    {
      key: 'name',
      title: '姓名'
    },
    {
      key: 'phone',
      title: '手机号'
    },
    {
      key: 'deptName',
      title: '部门'
    },
    {
      key: 'roleName',
      title: '职位'
    },
    {
      key: 'loginAccount',
      title: '登录账号'
    },
    {
      key: 'updateTime',
      title: '更新时间'
    },
    {
      key: 'remark',
      title: '备注'
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
            style: {
              marginRight: '8px',
              display: param.row.type === 0 ? 'none' : ''
            },
            on: {
              click: () => {
                consoe.log(param.row)
                this.bindDep(param.row)
              }
            }
          }, '绑定部门'),
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
              display: (param.row.deleted === '1' ? 'none' : param.row.type === 0 ? 'none' : '')
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
  page: 1,
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

export const title = '人员列表'
