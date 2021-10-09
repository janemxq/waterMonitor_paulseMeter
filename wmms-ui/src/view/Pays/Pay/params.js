import { showColor } from '@/components/commonRender'
export function columns () {
  return [
    {
      key: 'groupName',
      align: 'center',
      title: '表井名称',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.groupName)
      }
      // width: '100 #515a6e',
      // fixed: 'left'
    },
    {
      key: 'villageName',
      title: '村名',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.villageName)
      }
      // width: '200',
      // fixed: 'left'
    },
    {
      key: 'userName',
      title: '用户名',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.userName)
      }
      // width: '100',
      // fixed: 'left'
    },
    // {
    //   key: 'meterInit',
    //   title: '水表初始值',
    //   align: 'center',
    //   width: 110
    // },
    {
      key: 'preNumber',
      title: '上期水表数',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.preNumber)
      }
    },
    {
      key: 'lastNumber',
      title: '最新水表数',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.lastNumber)
      }
    },
    {
      key: 'meterTypeName',
      title: '用水类型',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.meterTypeName)
      }
    },
    {
      key: 'pulse',
      title: '方水/脉冲',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.pulse)
      }
    },
    {
      key: 'online',
      title: '是否在线',
      align: 'center',
      render: showColor,
    },
    // {
    //   key: 'createTime',
    //   title: '创建时间',
    //   align: 'center',
    //   width: 150
    // },
    // {
    //   key: 'updateTime',
    //   title: '最后修改时间',
    //   align: 'center',
    //   width: 150
    // },
    // {
    //   title: '最后修改人',
    //   align: 'center',
    //   render,
    //   width: 120
    // },
    {
      key: 'waterSum',
      title: '累计用水',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.waterSum)
      }
    },
    {
      key: 'toPayWaterVolume',
      title: '待付用水量',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.toPayWaterVolume)
      }
      // fixed: 'right',
      // width: 110
    },
    {
      key: 'toPayAccount',
      title: '欠费金额',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.toPayAccount)
      }
      // fixed: 'right',
      // width: 100
    },
    {
      key: 'balance',
      title: '余额',
      align: 'center',
      render: (h, params) => {
        return h('span', {
          style: {
            color: (params.row.balance - params.row.toPayWaterVolume) < 0 ? 'rgb(255, 64, 0)' : '#515a6e'
          }
        }, params.row.balance)
      }
      // fixed: 'right',
      // width: 100
    },
    // {
    //   key: 'meterTypeName',
    //   title: '支付方式',
    //   align: 'center',
    //   // fixed: 'right',
    //   // width: 100
    // },
    {
      key: '',
      title: '操作',
      align: 'center',
      width: 210,
      // fixed: 'right',
      render: (h, param) => {
        return h('ButtonGroup', [
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
          }, '抄表'),
          h('Button', {
            props: {
              type: 'success',
              size: 'small'
            },
            on: {
              click: () => {
                console.log(param.row)
                this.payWSCall(param.row)
              }
            }
          }, '缴费'),
          // h(
          //   'Button',
          //   {
          //     props: {
          //       type: 'success',
          //       size: 'small'
          //     },
          //     on: {
          //       click: () => {
          //         // console.log('待制作')
          //         // this.$Message.info('努力开发中')
          //         console.log(param)
          //         this.api.print({
          //           accountNum: param.row.toPayAccount, // 欠费金额
          //           realPay: param.row.realPay, // 缴费金额
          //           userName: param.row.userName, // 用户
          //           volume: param.row.toPayWaterVolume // 待付用水量
          //         }).then(res => {
          //           console.log(res)
          //         })
          //       }
          //     }
          //   },
          //   '打印凭条')
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
    },
    {
      type: 'input',
      key: 'phone',
      title: '手机号'
    },
    {
      type: 'select',
      key: 'meterTypeSn',
      title: '用水类型',
      data: []
    }
  ]
}

export const params = {
  current: 1,
  size: 10
}

export const modalParams = {
  show: true, // 弹窗开关
  width: 400, // 弹框宽度
  modalLoading: true, // 点击确定Loading状态
  type: 'affirm', // 弹出框类型。目 前支持三种 add edit info
  title: '缴费信息确认', // 弹出框头部
  maskClosable: false // 点击弹框遮罩是否关闭
}

export const title = '用水缴费'
