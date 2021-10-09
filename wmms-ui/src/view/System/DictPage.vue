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
    :onExpand="onExpand"
  >
    <addOrEdit ref="add" slot="add" :show="modalParams.show" :modalParams="modalParams"></addOrEdit>
    <addOrEdit ref="edit" slot="edit" :show="modalParams.show" :data="roleData" :modalParams="modalParams"></addOrEdit>
    <childAddOrEdit ref="childAdd" slot="childAdd" :show="modalParams.show" :modalParams="modalParams" :data="roleData"></childAddOrEdit>
    <childAddOrEdit ref="childEdit" slot="childEdit" :show="modalParams.show" :data="roleData" :modalParams="modalParams"></childAddOrEdit>
  </tableTemplate>
</template>

<script>
import { columns, seek, params, modalParams, title } from './DictPage/params'
import { mapMutations } from 'vuex'
export default {
  name: 'DictPage',
  data () {
    return {
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      modalParams,
      roleData: {},
      api: this.$api.System,
      title,
      child: {
        data: [],
        loading: false
      }
    }
  },
  components: {
    addOrEdit: require('./DictPage/addOrEdit').default,
    childAddOrEdit: require('./DictPage/childAddOrEdit').default
  },
  created () {
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.getSysDict(params).then(res => {
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
      this.api.delSysDict({
        id: item.id
      }).then(() => {
        this.$Message.success(`删除字典项${item.name}成功！`)
        this.setCount()
      }).catch(err => {
        console.log(err)
        this.$Message.error(`删除字典项${item.name}失败！`)
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        switch (type) {
          case 'add':
            return this.api.addSysDict(res).then(data => {
              this.$Message.success(`新增字典项${res.name}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          case 'edit':
            return this.api.updateSysDict(res).then(data => {
              this.$Message.success(`修改字典项${res.name}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          case 'childAdd':
            return this.api.addSysDictItem(res).then(data => {
              this.$Message.success(`新增子字典项${res.label}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          case 'childEdit':
            return this.api.updateSysDictItem(res).then(data => {
              this.$Message.success(`修改子字典项${res.label}成功！`)
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
    changeChild (params, type) {
      switch (type) {
        case 'add':
          this.roleData = { dictId: params.id, dictCode: params.code }
          Object.assign(this.modalParams, {
            show: true, // 弹窗开关
            width: 600, // 弹框宽度
            modalLoading: true, // 点击确定Loading状态
            type: 'childAdd', // 弹出框类型。目 前支持三种 add edit info
            title: '新增子字典'
          })
          break
        case 'edit':
          Object.assign(this.roleData, params)
          Object.assign(this.modalParams, {
            show: true, // 弹窗开关
            width: 600, // 弹框宽度
            modalLoading: true, // 点击确定Loading状态
            type: 'childEdit', // 弹出框类型。目 前支持三种 add edit info
            title: '修改子字典'
          })
          break
        case 'del':
          this.api.delSysDictItem({
            id: params.id
          }).then(() => {
            this.$Message.success(`删除子字典项${params.label}成功！`)
            this.setCount()
          }).catch((err) => {
            console.log(err)
            this.$Message.error(`删除子字典项${params.label}失败！`)
          })
          break
      }
    },
    onExpand (row, state) {
      if (state) {
        this.$set(this, 'child', {
          loading: true
        })
        this.api.findSysDictItem({
          dictCode: row.code
        }).then(res => {
          this.$set(this, 'child', {
            data: res.data,
            loading: false
          })
        }).catch(err => {
          console.log(err)
          this.$set(this, 'child', {
            data: [],
            loading: false
          })
        })
      } else {
        this.$set(this, 'child', {
          data: [],
          loading: false
        })
        this.childListData = []
      }
    }
  }
}
</script>

<style scoped>

</style>
