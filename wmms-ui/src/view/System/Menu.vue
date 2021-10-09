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
    <addOrEdit ref="childEdit" slot="childEdit" :show="modalParams.show" :data="roleData" :modalParams="modalParams"></addOrEdit>
    <addOrEdit ref="childAdd" slot="childAdd" :show="modalParams.show" :data="roleData" :modalParams="modalParams"></addOrEdit>
  </tableTemplate>
</template>

<script>
import { columns, seek, params, modalParams, title } from './Menu/params'
import { mapMutations } from 'vuex'
export default {
  name: 'Menu',
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
    addOrEdit: require('./Menu/addOrEdit').default
  },
  created () {
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.userMenu(params).then(res => {
        return {
          content: res.data,
          total: res.data.length
        }
      })
    },
    /**
     * 删除用户
     * @param item
     */
    handleDelete (item) {
      this.api.delSysMenu({
        id: item.id
      }).then(() => {
        this.$Message.success(`删除菜单${item.name}成功！`)
        this.setCount()
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        switch (type) {
          case "add":
          case "childAdd":
            return this.api.addSysMenu(res).then(data => {
              this.$Message.success(`新增菜单${res.name}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          case "edit":
          case "childEdit":
            return this.api.updateSysMenu(res).then(data => {
              this.$Message.success(`修改菜单${res.name}成功！`)
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
    addChild (params) {
      this.roleData = {}
      Object.assign(this.roleData, {
        parentId: params.id,
        isValid: 1
      })
      Object.assign(this.modalParams, {
        show: true, // 弹窗开关
        width: 600, // 弹框宽度
        modalLoading: true, // 点击确定Loading状态
        type: 'childAdd', // 弹出框类型。目 前支持三种 add edit info
        title: '新增子菜单'
      })
    }
  }
}
</script>

<style scoped>

</style>
