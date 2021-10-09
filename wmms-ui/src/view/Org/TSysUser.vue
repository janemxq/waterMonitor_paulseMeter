<template>
  <tableTemplate
    slot="right"
    :addModule="true"
    :handleSubmit="handleSubmit"
    :modalParams="modalParams"
    :columns="columns"
    :seek="seek"
    :params="params"
    :dataCallBack="dataCallBack"
    :title="title"
    :titleSlot="true"
    :selectChange="onSelectFn"
  >
    <label slot="fun" class="titleLabel">
      <a href="javascript:void(0)" @click="deleteFn">多项删除</a>
      <a href="javascript:void(0)" @click="add">新增</a>
    </label>
    <addOrEdit ref="add" slot="add" :show="modalParams.show" :modalParams="modalParams"></addOrEdit>
    <addOrEdit ref="edit" slot="edit" :show="modalParams.show" :data="roleData" :modalParams="modalParams"></addOrEdit>
    <editPassword ref="editPassword" slot="editPassword" :show="modalParams.show" :data="roleData" :modalParams="modalParams"></editPassword>
  </tableTemplate>
</template>
<script>
import { columns, seek, params, modalParams, title } from './TSysUser/params'
import { mapMutations } from 'vuex'
import { groupTree, layout } from '@/components/layout'
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
      deleteData: []
    }
  },
  components: {
    addOrEdit: require('./TSysUser/addOrEdit').default,
    editPassword: require('./TSysUser/editPassword').default,
    groupTree,
    layout
  },
  created () {
    this.api.getWsVillageAll().then(res => {
      this.seek[0].data = res.data.map(m => {
        return {
          value: m.sn,
          name: m.villageName
        }
      })
    })
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.getTSysUser(params).then(res => {
        return {
          content: res.data.records,
          total: res.data.total
        }
      })
    },
    onSelectFn (e) {
      console.log(e)
      this.deleteData = e
    },
    add () {
      Object.assign(this.modalParams, {
        show: true,
        type: 'add',
        title: '新增'
      })
    },
    deleteFn () {
      if (this.deleteData.length) {
        let formData = []
        this.deleteData.findIndex(m => {
          formData.push(m.id)
        })
        this.api.postSysUserMultiDel(formData).then(res => {
          console.log(res)
          this.setCount()
          this.$Message.success('删除删除成功!')
          this.deleteData = []
        })
      } else {
        this.$Message.error('请先选择要删除的用户!')
      }
    },
    /**
       * 删除用户
       * @param item
       */
    handleDelete (item) {
      this.api.delTSysUser({
        id: item.id
      }).then(() => {
        this.$Message.success(`删除用户${item.name}成功！`)
        this.setCount()
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        if (type === 'add') {
          return this.api.addTSysUser(res).then(data => {
            this.$Message.success(`新增用户${res.name}成功！`)
            this.setCount()
            return Promise.resolve(data)
          }).catch(err => {
            return Promise.reject(err)
          })
        } else {
          return this.api.updateTSysUser(res).then(data => {
            if (type === 'edit') this.$Message.success(`修改用户${res.name}成功！`)
            if (type === 'editPassword') this.$Message.success(`重置用户${res.name}的密码成功！`)
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
    editPassword (params) {
      Object.assign(this.roleData, params)
      Object.assign(this.modalParams, {
        show: true, // 弹窗开关
        width: 600, // 弹框宽度
        modalLoading: true, // 点击确定Loading状态
        type: 'editPassword', // 弹出框类型。目 前支持三种 add edit info
        title: '重置密码'
      })
    }
  }
}
</script>

<style scoped lang="less">
  .titleLabel {
    >a {
      margin-left: 5px;
      display: inline-block;
    }
  }
</style>
