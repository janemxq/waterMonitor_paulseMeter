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
import { columns, seek, params, modalParams, title } from './PayLog/params'
import { mapMutations } from 'vuex'
export default {
  name: 'PayLog',
  data () {
    return {
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      modalParams,
      roleData: {},
      infoParams: {},
      title,
      api: this.$api.WaterManage,
      sysApi: this.$api.System
    }
  },
  components: {
    // addOrEdit: require('./PayLog/addOrEdit').default
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.getWsFeeLog(params).then(res => {
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
      this.api.delWsMeter({
        id: item.id
      }).then(() => {
        this.$Message.success(`删除水表设备${item.groupName}成功！`)
        this.setCount()
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        if (type === 'add') {
          return this.api.addWsMeter(res).then(data => {
            this.$Message.success(`新增水表设备${res.groupName}成功！`)
            this.setCount()
            return Promise.resolve(data)
          }).catch(err => {
            return Promise.reject(err)
          })
        } else {
          return this.api.updateWsMeter(res).then(data => {
            this.$Message.success(`修改水表设备${res.groupName}成功！`)
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
    },
    changeTree (data) {
      Object.assign(this.params, data.type === 'village' && {
        villageId: data.id
      }, data.type === 'group' && {
        groupId: data.id
      })
      Object.assign(this.infoParams, data.type === 'village' && {
        villageId: data.id,
        groupId: ''
      }, data.type === 'group' && {
        villageId: data.villageId,
        groupId: data.id
      })
      this.setCount()
    }
  }
}
</script>

<style scoped>

</style>
