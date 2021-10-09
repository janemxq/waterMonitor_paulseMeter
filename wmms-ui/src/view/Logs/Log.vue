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
    <addOrEdit ref="add" slot="add" :show="modalParams.show" :modalParams="modalParams"></addOrEdit>
    <addOrEdit ref="edit" slot="edit" :show="modalParams.show" :data="roleData" :modalParams="modalParams"></addOrEdit>
  </tableTemplate>
</template>

<script>
import { columns, seek, params, modalParams, title } from './Log/params'
import { groupTree, layout } from '@/components/layout'
import { mapMutations } from 'vuex'
export default {
  name: 'Log',
  data () {
    return {
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      modalParams,
      roleData: {},
      title,
      api: this.$api.WaterManage,
      sysApi: this.$api.System
    }
  },
  components: {
    addOrEdit: require('./Log/addOrEdit').default,
    groupTree,
    layout
  },
  created () {
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.getWsMeterReader(params).then(res => {
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
      this.api.delWsMeterReader({
        id: item.id
      }).then(() => {
        this.$Message.success(`删除用户${item.userName}的用水记录成功！`)
        this.setCount()
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        if (type === 'add') {
          return this.api.addWsMeterReader(res).then(data => {
            this.$Message.success(`新增用户${item.userName}的用水记录成功！`)
            this.setCount()
            return Promise.resolve(data)
          }).catch(err => {
            return Promise.reject(err)
          })
        } else {
          return this.api.updateWsMeterReader(res).then(data => {
            this.$Message.success(`修改用户${item.userName}的用水记录成功！`)
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
      if (data.villageId) {
        this.params.villageId = data.villageId
      } else {
        this.params.villageId = undefined
      }
      this.setCount()
      console.log(data.villageId)
    }
  }
}
</script>

<style scoped>

</style>
