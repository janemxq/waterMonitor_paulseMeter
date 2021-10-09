<template>
  <tableTemplate
    :addModule="true"
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
import { columns, seek, params, modalParams, title } from './Village/params'
import { mapMutations } from 'vuex'
export default {
  name: 'Village',
  data () {
    return {
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      modalParams,
      roleData: {},
      title,
      api: this.$api.Org,
      seekSelectData: []
    }
  },
  components: {
    addOrEdit: require('./Village/addOrEdit').default
  },
  created () {
    // this.api.getWsVillageAll().then(res => {
    //   this.seek[0].data = res.data.map(m => {
    //     return {
    //       value: m.sn,
    //       name: m.villageName
    //     }
    //   })
    // })
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.getVillage(params).then(res => {
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
      this.api.delVillage({
        id: item.id
      }).then(() => {
        this.$Message.success(`删除村庄${item.villageName}成功！`)
        this.setCount()
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        if (type === 'add') {
          return this.api.addVillage(res).then(data => {
            this.$Message.success(`新增村庄${res.villageName}成功！`)
            this.setCount()
            return Promise.resolve(data)
          }).catch(err => {
            return Promise.reject(err)
          })
        } else {
          return this.api.updateVillage(res).then(data => {
            this.$Message.success(`修改村庄${res.villageName}成功！`)
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
