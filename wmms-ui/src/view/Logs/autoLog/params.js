export function columns () {
  return [
    {
      key: 'id',
      align: 'center',
      title: '编号'
    },
    {
      key: 'remark',
      align: 'center',
      title: '备注'
    },
    {
      key: 'runDate',
      align: 'center',
      title: '执行时间'
    },
    {
      key: 'createTime',
      align: 'center',
      title: '创建时间'
    },
  ]
}

export const params = {
  current: 1,
  size: 10
}

export const title = '自动任务执行日志'
