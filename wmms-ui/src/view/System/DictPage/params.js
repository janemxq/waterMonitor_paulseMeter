import { showColor as render } from '@/components/commonRender'
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
                key: 'label',
                align: 'center',
                title: '标签项'
              },
              {
                key: 'value',
                align: 'center',
                title: '值'
              },
              {
                key: 'updateTime',
                align: 'center',
                title: '更新时间'
              },
              {
                key: 'serial',
                align: 'center',
                title: '顺序'
              },
              {
                key: 'remark',
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
                          this.changeChild(param.row, 'edit')
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
                          this.changeChild(param.row, 'del')
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
            data: this.child.data,
            loading: this.child.loading,
            size: 'small',
            border: false
          },

        })
      }
    },
    {
      key: 'serial',
      align: 'center',
      title: '顺序'
    },
    {
      key: 'code',
      align: 'center',
      title: '字典代码'
    },
    {
      key: 'name',
      align: 'center',
      title: '字典名称'
    },
    // {
    //   key: 'createTime',
    //   align: 'center',
    //   title: '创建时间'
    // },
    // {
    //   key: 'createUserId',
    //   align: 'center',
    //   title: '创建人'
    // },
    {
      key: 'isValid',
      align: 'center',
      title: '是否有效',
      render
    },
    {
      key: 'updateTime',
      align: 'center',
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
      width: 250,
      render: (h, param) => {
        return h('div', [
          h('Button', {
            props: {
              type: 'success',
              size: 'small'
            },
            style: {
              marginRight: '8px'
            },
            on: {
              click: () => {
                this.changeChild(param.row, 'add')
              }
            }
          }, '新增子字典'),
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
    {
      title: '字典名称',
      key: 'name',
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

export const title = '字典列表'
