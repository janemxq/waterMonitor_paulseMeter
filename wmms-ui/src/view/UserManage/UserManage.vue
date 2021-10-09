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
    <bindDep ref="bindDep" slot="bindDep" :show="modalParams.show" :data="roleData" :modalParams="modalParams"></bindDep>
  </tableTemplate>
</template>

<script>
import { title, columns, seek, params, modalParams } from './UserManage/params'
import { mapMutations } from 'vuex'
export default {
  name: 'UserManage',
  data () {
    return {
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      modalParams,
      roleData: {},
      api: this.$api.UserManage,
      title
    }
  },
  components: {
    addOrEdit: require('./UserManage/addOrEdit').default,
    bindDep: require('./UserManage/bindDep').default
  },
  created () {
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.getUserList(params).then(res => {
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
      this.api.delUser({
        id: item.id
      }).then(() => {
        this.$Message.success(`删除用户${item.name}成功！`)
        this.setCount()
      }).catch(() => {
        this.$Message.error(`删除用户${item.name}失败！`)
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        switch (type) {
          case 'add':
            return this.api.addUser(res).then(data => {
              this.$Message.success(`新增用户${res.name}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          case 'edit':
            return this.api.updateUser(res).then(data => {
              this.$Message.success(`修改用户${res.name}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          case 'bindDep':
            return this.api.addList(res).then(data => {
              this.$Message.success(`绑定部门成功！`)
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
    bindDep (params) {
      Object.assign(this.roleData, params)
      Object.assign(this.modalParams, {
        show: true, // 弹窗开关
        width: 1100, // 弹框宽度
        modalLoading: true, // 点击确定Loading状态
        type: 'bindDep',
        title: '绑定部门'
      })
    }
  }
}
</script>

<style scoped>

</style>
