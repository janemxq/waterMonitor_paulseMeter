export function columns () {
  return [
    {
      type: 'expand',
      width: 50,
      render: (h, params) => {
        return h('Table', {
          props: {
            columns: [
              {
                key: 'serial',
                align: 'center',
                title: '排序'
              },
              {
                key: 'name',
                align: 'center',
                title: '菜单名称'
              },
              {
                key: 'href',
                align: 'center',
                title: '链接'
              },
              {
                key: 'icon',
                align: 'center',
                title: '图标'
              },
              {
                key: 'label',
                align: 'center',
                title: '菜单标签'
              },
              {
                key: 'no',
                align: 'center',
                title: '菜单编号',
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
                width: 140,
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
            ],
            data: params.row.childMenu,
            size: 'small',
            border: false
          },
        })
      }
    },
    {
      key: 'serial',
      align: 'center',
      title: '排序'
    },
    {
      key: 'name',
      align: 'center',
      title: '菜单名称'
    },
    {
      key: 'href',
      align: 'center',
      title: '链接'
    },
    {
      key: 'icon',
      align: 'center',
      title: '图标'
    },
    {
      key: 'label',
      align: 'center',
      title: '菜单标签'
    },
    {
      key: 'no',
      align: 'center',
      title: '菜单编号',
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
      width: 240,
      render: (h, param) => {
        return h('div', [
          h('Button', {
            props: {
              type: 'success',
              size: 'small'
            },
            style: {
              marginRight: '8px',
              display: param.row.deleted === '1' ? 'none' : ''
            },
            on: {
              click: () => {
                this.addChild(param.row)
              }
            }
          }, '新增子菜单'),
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

export const title = '菜单列表'
