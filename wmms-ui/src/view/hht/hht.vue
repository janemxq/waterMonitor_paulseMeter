<template>
  <tableTemplate
          style="max-height: 600px"
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
    <add ref="add" slot="add" :show="modalParams.show"></add>
    <add ref="edit" slot="edit" :show="modalParams.show" :data="editData"></add>
    <init ref="init" slot="init" :show="modalParams.show" :data="editData"></init>
  </tableTemplate>
</template>

<script>
import { seek, columns, params, modalParams, title } from './params/params'
import { mapMutations } from 'vuex'
export default {
  name: "hht",
  components: {
    add: require('./params/add').default,
    init: require('./params/init').default
  },
  data () {
    return {
      api: this.$api.hht,
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      modalParams,
      editData: {},
      title,
      onSelectionChange: [],
      tableLoading: false,
      expandData: [],
      tableData: []
    }
  },
  methods: {
    ...mapMutations(['setCount', 'showLoading', 'hideLoading']),
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(formData => {
        console.log(formData)
        if (this.modalParams.type === 'add') {
          return this.add(formData)
        } else if (this.modalParams.type === 'edit') {
          return this.edit(formData)
        } else if (this.modalParams.type === 'init') {
          return this.popInit(formData)
        }
      })
    },
    popInit (formData) {
      console.log(formData)
      return this.api.postPadMeterInfoInit(formData).then(popInit => {
        console.log(popInit)
        this.$Message.success(`初始化成功！`)
        this.setCount()
        return Promise.resolve(popInit)
      })
    },
    add (formData) {
      return this.api.postPadMeterYard(formData).then(add => {
        console.log(add)
        this.$Message.success(`新增成功！`)
        this.setCount()
        return Promise.resolve(add)
      })
    },
    edit (formData) {
      return this.api.putPadMeterYardId(formData, formData.id).then(edit => {
        console.log(edit)
        this.$Message.success(`修改成功！`)
        this.setCount()
        return Promise.resolve(edit)
      })
    },
    dataCallBack (params) {
      return this.api.postPadMeterYardPage(params).then(res => {
        console.log(res)
        this.tableData = JSON.parse(JSON.stringify(res.data.records))
        this.tableData.map(m => {
          m._expanded = false
        })
        return {
          content: this.tableData,
          total: res.data.total
        }
      })
    },
    onExpand (row, status) {
      console.log(row, status)
      if (status) {
        this.tableLoading = true
        this.getPadMeterInfoGetMetersSn(row)
      }
    },
    refresh (param) {
      Object.assign(this.tableData[param.index], {
        _expanded: true,
      })
      this.tableData.sort()
      this.tableLoading = true
      this.api.getPadMeterInfoGetMetersSn(param.row.sn).then(res => {
        this.tableLoading = false
        this.expandData = res.data
      }).catch(() => {
        this.tableLoading = false
      })
    },
    getPadMeterInfoGetMetersSn (row) {
      this.api.getPadMeterInfoGetMetersSn(row.sn).then(res => {
        this.tableLoading = false
        this.expandData = res.data
      }).catch(() => {
        this.tableLoading = false
      })
    }
  }
}
</script>

<style >
.RefreshStyle{
  display: none;
}
</style>
