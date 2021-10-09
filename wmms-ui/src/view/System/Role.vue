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
    <bindMenuPage ref="bindMenu" slot="bindMenu" :show="modalParams.show" :data="roleData" :modalParams="modalParams"></bindMenuPage>
  </tableTemplate>
</template>

<script>
import { columns, seek, params, modalParams, title } from './Role/params'
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
      api: this.$api.System
    }
  },
  components: {
    addOrEdit: require('./Role/addOrEdit').default,
    bindMenuPage: require('./Role/bindMenuPage').default
  },
  created () {
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.getSysRole(params).then(res => {
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
      this.api.delSysRole({
        id: item.id
      }).then(() => {
        this.$Message.success(`删除角色${item.roleName}成功！`)
        this.setCount()
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        switch (type) {
          case "add":
            return this.api.addSysRole(res).then(data => {
              this.$Message.success(`新增角色${res.roleName}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          case "edit":
            return this.api.updateSysRole(res).then(data => {
              this.$Message.success(`修改角色${res.roleName}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          case "bindMenu":
            return this.api.setBindMenu(res).then(data => {
              this.$Message.success(`绑定菜单成功！`)
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
    bindMenu (params) {
      Object.assign(this.roleData, params)
      Object.assign(this.modalParams, {
        show: true, // 弹窗开关
        width: 600, // 弹框宽度
        modalLoading: true, // 点击确定Loading状态
        type: 'bindMenu', // 弹出框类型。目 前支持三种 add edit info
        title: '绑定菜单'
      })
    }
  }
}
</script>

<style scoped>

</style>
