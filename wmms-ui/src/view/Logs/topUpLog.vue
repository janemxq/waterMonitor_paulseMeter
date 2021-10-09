<template>
  <tableTemplate
    :addModule="false"
    :columns="columns"
    :seek="seek"
    :params="params"
    :dataCallBack="dataCallBack"
    :title="title"
  >
  </tableTemplate>
</template>

<script>
import { columns, seek, params, title } from './topUpLog/params'
export default {
  name: "topUpLog",
  data () {
    return {
      api: this.$api.Pays,
      sysApi: this.$api.System,
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      title
    }
  },
  methods: {
    dataCallBack (param) {
      return this.api.postWsChargePage(param).then(res => {
        return {
          content: res.data.records,
          total: res.data.total
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
