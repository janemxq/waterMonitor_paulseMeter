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
import { columns, params, title } from './autoLog/params'
export default {
  name: "autoLog",
  data () {
    return {
      api: this.$api.Pays,
      columns: columns.call(this),
      params,
      title
    }
  },
  methods: {
    dataCallBack (param) {
      return this.api.postWsCrontabLogPage(param).then(res => {
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
