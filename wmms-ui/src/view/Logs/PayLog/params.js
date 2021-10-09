// import { editUser as render } from '@/components/commonRender'
export function columns () {
  return [
    {
      key: 'userName',
      align: 'center',
      title: '用户名',
      // width: 100,
      // fixed: 'left'
    },
    {
      key: 'phone',
      align: 'center',
      title: '手机号',
      // width: 140,
      // fixed: 'left'
    },
    {
      key: 'startNum',
      align: 'center',
      title: '水表起码',
      // width: 100,
      // fixed: 'left'
    },
    {
      key: 'endNum',
      align: 'center',
      title: '水表止码',
      // width: 100,
      // fixed: 'left'
    },
    {
      key: 'volume',
      align: 'center',
      title: '用水量',
      // width: 100,
      // fixed: 'left'
    },
    {
      key: 'feeStartTime',
      align: 'center',
      // width: 160,
      title: '本期账单开始时间'
    },
    {
      key: 'feeEndTime',
      align: 'center',
      // width: 160,
      title: '本期账单结束时间'
    },
    {
      key: 'payment',
      align: 'center',
      // width: 100,
      title: '支付方式'
    },
    {
      key: 'payStatus',
      align: 'center',
      title: '缴费状态',
      // width: 100,
      render: (h, params) => {
        return h('span', {}, Number(params.row.payStatus) === 1 ? '已支付' : Number(params.row.payStatus) === 0 ? '未支付' : '已取消')
      }
    },
    {
      key: 'account',
      align: 'center',
      // width: 100,
      title: '缴费金额'
    },
    {
      key: 'payTime',
      align: 'center',
      // width: 100,
      title: '缴费时间'
    },
    {
      key: 'waterSum',
      align: 'center',
      // width: 100,
      title: '累计用水量'
    },
    // {
    //   align: 'center',
    //   width: 100,
    //   title: '最后修改人',
    //   render
    // },
    // {
    //   key: 'createTime',
    //   align: 'center',
    //   width: 180,
    //   title: '创建时间'
    // },
    // {
    //   key: 'updateTime',
    //   align: 'center',
    //   width: 180,
    //   title: '最后修改时间'
    // },
    // {
    //   key: '',
    //   title: '操作',
    //   align: 'center',
    //   width: 180,
    //   render: (h, param) => {
    //     return h('div', [
    //       h('Button', {
    //         props: {
    //           type: 'success',
    //           size: 'small'
    //         },
    //         on: {
    //           click: () => {
    //             this.$Message.info('功能开发中')
    //             console.log(param.row)
    //             // let formData = new FormData()
    //             // formData.append('accountNum', param.row.account)
    //             // formData.append('realPay', param.row.account)
    //             // formData.append('userName', param.row.userName)
    //             // formData.append('volume', param.row.toPayWaterVolume)
    //             // formData.append('balances', param.row.balance)
    //             // this.api.print(formData).then(res => {
    //             //   console.log(res)
    //             //   this.$Message.success(`打印成功!`)
    //             //   return Promise.resolve(res)
    //             // }).catch(err => {
    //             //   return Promise.reject(err)
    //             // })
    //           }
    //         }
    //       }, '打印凭条')
    //     ])
    //   }
    // }
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
        this.sysApi.findUser({
          name,
          current: 1,
          size: 99
        }).then(res => {
          this.seek[0].data = res.data.records.map(m => {
            m.value = m.sn
            m.name = `${m.name}(${m.phone || m.villageName})`
            return m
          })
        }).finally(() => {
          this.seek[0].loading = false
        })
      },
      loading: false
    },
    {
      type: 'time',
      month: 'datetime',
      key: 'createdTimeStart',
      title: '开始时间',
      format: 'yyyy-MM-dd HH:mm:ss'
    },
    {
      type: 'time',
      key: 'createdTimeEnd',
      month: 'datetime',
      title: '结束时间',
      format: 'yyyy-MM-dd HH:mm:ss'
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

export const title = '缴费记录'
