<template>
  <tableTemplate
    :addModule="false"
    :handleSubmit="handleSubmit"
    :modalParams="modalParams"
    :columns="columns"
    :seek="seek"
    :params="params"
    :dataCallBack="dataCallBack"
    :title="title"
  >
  </tableTemplate>
</template>

<script>
import { columns, seek, params, modalParams, title } from './WaterCostException/params'
import { mapMutations } from 'vuex'
export default {
  name: 'Well',
  data () {
    return {
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      modalParams,
      roleData: {},
      title,
      api: this.$api.Home,
      flag: false
    }
  },
  created () {
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.getWaterCostException(params).then(res => {
        this.flag = !!res.data.total
        console.log(res.data)
        return {
          content: res.data.records,
          total: res.data.total
        }
      })
    },
    /**
     * 删除用户
     * @param item
     */
    handleDelete (item) {
      this.api.delWsGroup({
        id: item.id
      }).then(() => {
        this.$Message.success(`删除水井${item.groupName}成功！`)
        this.setCount()
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        if (type === 'add') {
          return this.api.addWsGroup(res).then(data => {
            this.$Message.success(`新增水井${res.groupName}成功！`)
            this.setCount()
            return Promise.resolve(data)
          }).catch(err => {
            return Promise.reject(err)
          })
        } else {
          return this.api.updateWsGroup(res).then(data => {
            this.$Message.success(`修改水井${res.groupName}成功！`)
            this.setCount()
            return Promise.resolve(data)
          }).catch(err => {
            return Promise.reject(err)
          })
        }
      })
    },
    edit (params) {
      Object.assign(this.roleData, params)
      Object.assign(this.modalParams, {
        show: true, // 弹窗开关
        width: 600, // 弹框宽度
        modalLoading: true, // 点击确定Loading状态
        type: 'edit', // 弹出框类型。目 前支持三种 add edit info
        title: '修改'
      })
    }
  }
}
</script>

<style scoped>

</style>
