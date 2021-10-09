export function seek () {
  return []
}

export function childColumns (that) {
  return [
    {
      type: 'expand',
      width: 50,
      render: (h, params) => {
        return h('Table', {
          props: {
            columns: [
              {
                type: 'selection',
                width: 60,
                align: 'center'
              },
              {
                key: 'meterMac',
                title: '水表通道号',
                align: 'center'
              },
              {
                key: 'pulseSum',
                title: '总脉冲值',
                align: 'center'
              },
              {
                key: 'waterSum',
                title: '总用水量',
                align: 'center'
              },
              {
                key: 'meterNum',
                title: '码盘值',
                align: 'center'
              },
              {
                key: 'updateTime',
                title: '数据更新时间',
                align: 'center'
              },
              {
                key: '',
                width: 200,
                renderHeader (h, params) {
                  return h('div', [
                    h('Button', {
                      props: {
                        type: 'success',
                        size: 'small',
                        shape: 'circle'
                      },
                      on: {
                        click: () => {
                          console.log(that.onSelectionChange)
                          if (that.onSelectionChange.length) {
                            let data = that.onSelectionChange.map(item => item.sn)
                            that.api.postPadMeterInfoGetMetersNum(data).then(res => {
                              console.log(res)
                              that.$Message.success(`获取${res.data.sum}个水表, 其中${res.data.error}未获取到!!!`)
                              that.refresh(param)
                            })
                          } else {
                            that.$Message.error('请先勾选!')
                          }
                        }
                      }
                    }, '获取勾选数据')
                  ])
                },
                render: (h, param) => {
                  return h('div', [
                    h('Button', {
                      props: {
                        type: 'success',
                        size: 'small'
                      },
                      on: {
                        click: () => {
                          that.api.postPadMeterInfoGetMetersNum([params.row.sn]).then(res => {
                            console.log(res)
                            that.$Message.success(`获取${res.data.sum}个水表, 其中${res.data.error}未获取到!!!`)
                            that.refresh(params)
                          })
                        }
                      }
                    }, '获取数据'),
                    h('Button', {
                      props: {
                        type: 'primary',
                        size: 'small'
                      },
                      style: {
                        marginLeft: '3px',
                      },
                      on: {
                        click: () => {
                          console.log(that.editData)
                          Object.assign(that.editData, param.row)
                          Object.assign(that.modalParams, {
                            type: 'init',
                            title: '初始化水表',
                            show: true
                          })
                        }
                      }
                    }, '初始化'),
                  ])
                }
              }
            ],
            data: that.expandData,
            loading: that.tableLoading,
            size: 'small',
            border: false
          },
          on: {
            'on-selection-change': item => {
              that.onSelectionChange = item
            }
          }
        })
      }
    }
  ]
}
export function columns () {
  return [
    ...childColumns(this),
    {
      key: 'address',
      title: '表井地址',
      align: 'center'
    },
    {
      key: 'name',
      title: '表井名称',
      align: 'center'
    },
    {
      key: 'remark',
      title: '表井备注',
      align: 'center'
    },
    {
      key: '',
      title: '操作',
      align: 'center',
      width: 450,
      renderHeader (h, params) {
        return h('div', [
          h('span', '操作'),
          h('Button', {
            props: {
              type: 'primary',
              size: 'small',
              shape: 'circle'
            },
            style: {
              marginLeft: '10px'
            },
            on: {
              click: () => {
                this.showLoading()
                this.api.postPadMeterYardUpload().then(res => {
                  this.$Message.success('上传成功!')
                  this.hideLoading()
                }).catch(() => {
                  this.hideLoading()
                })
              }
            }
          }, '上传数据')
        ])
      },
      render: (h, param) => {
        return h('div', [
          h('Button', {
            props: {
              type: 'warning',
              size: 'small'
            },
            style: {
              marginLeft: '3px',
            },
            on: {
              click: () => {
                Object.assign(this.editData, param.row)
                Object.assign(this.modalParams, {
                  type: 'edit',
                  title: '修改',
                  show: true
                })
              }
            }
          }, '修改'),
          h('Poptip', {
            style: {
              marginLeft: '3px',
              textAlign: 'left',
            },
            props: {
              confirm: true,
              title: '是否确认删除'
            },
            on: {
              'on-ok': () => {
                this.api.delPadMeterYardId(param.row, param.row.id).then(res => {
                  console.log(res)
                  this.$Message.success(`删除成功！`)
                  this.setCount()
                })
              }
            }
          }, [
            h('Button', {
              props: {
                type: 'error',
                size: 'small'
              }
            }, '删除')
          ]),
          h('Button', {
            props: {
              type: 'success',
              size: 'small'
            },
            style: {
              marginLeft: '3px',
            },
            on: {
              click: () => {
                console.log(this.tableData[param.index]._expanded)
                this.api.getPadMeterYardGetMeterNumId(param.row.sn).then(res => {
                  console.log(res)
                  this.$Message.success(`获取${res.data.sum}个水表, 其中${res.data.error}未获取到!!!`)
                  this.refresh(param)
                })
              }
            }
          }, '获取全部数据'),
          // h('Button', {
          //   props: {
          //     type: 'primary',
          //     size: 'small'
          //   },
          //   style: {
          //     marginLeft: '3px',
          //   },
          //   on: {
          //     click: () => {
          //       this.showLoading()
          //       this.api.postPadMeterYardUpload().then(res => {
          //         this.$Message.success('上传成功!')
          //         this.hideLoading()
          //       }).catch(err => {
          //         this.hideLoading()
          //       })
          //     }
          //   }
          // }, '上传数据'),
        ])
      }
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
export const title = '手持终端'
