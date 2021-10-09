import copy from '@/plugin/tool/js/copy'

export function showColor (h, params) {
  return h('span', {
    style: {
      width: '12px',
      height: '12px',
      borderRadius: '50%',
      background: params.row[params.column.key] ? '#1ba784' : '#ff4000',
      display: 'inline-block',
      boxShadow: `0 0 5px 0 ${params.row[params.column.key] ? '#1ba784' : '#ff4000'}`,
      verticalAlign: 'middle',
    }
  })
}

export function copyContent (h, params) {
  return h('span', {
    domProps: {
      title: '点击复制'
    },
    style: {
      cursor: 'pointer'
    },
    on: {
      click: () => {
        copy(params.row[params.column.key])
      }
    }
  }, params.row[params.column.key])
}

export function authorization (h, params) {
  return h('span', {
    // style: {
    //   cursor: 'pointer'
    // },
  }, params.row[params.column.key] ? '✔' : '✖')
}

export function editUser (h, params) {
  return h('span',
    {},
    params.row.updateUserId ? params.row.updateUserId : params.row.createUserId
  )
}
