export function columns () {
  return [
    {
      key: 'userName',
      align: 'center',
      title: '用户'
    },
    {
      key: 'villageName',
      align: 'center',
      title: '所在村落'
    },
    {
      key: 'groupName',
      align: 'center',
      title: '表井名称'
    },
    {
      key: 'meterSn',
      align: 'center',
      title: '编号'
    },
    {
      key: 'num',
      align: 'center',
      title: '水表读数'
    },
    {
      key: 'volume',
      align: 'center',
      title: '码盘值'
    },
    // {
    //   align: 'center',
    //   title: '最后更新人',
    //   render
    // },
    {
      key: 'createTime',
      align: 'center',
      title: '创建时间'
    },
    // {
    //   key: 'updateTime',
    //   align: 'center',
    //   title: '最后更新时间',
    // },
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

export const title = '读表记录'
