export function columns () {
  return [
    {
      key: 'villageName',
      align: 'center',
      title: '村名称'
    },
    {
      key: 'userName',
      align: 'center',
      title: '用户名称'
    },
    {
      key: 'phone',
      align: 'center',
      title: '手机号'
    },
    {
      key: 'cost',
      align: 'center',
      title: '水费'
    },
    {
      key: 'balance',
      align: 'center',
      title: '余额'
    },
    {
      key: 'accountNum',
      align: 'center',
      title: '收款金额'
    },
    {
      key: 'createTime',
      align: 'center',
      title: '充值时间'
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
      key: 'phone',
      type: 'input',
      title: '手机号'
    },
    {
      key: 'accountNum',
      type: 'input',
      title: '收款金额'
    },
    {
      key: 'balance',
      type: 'input',
      title: '余额'
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

export const title = '充值记录'
