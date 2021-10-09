<template>
  <ComLayout>
    <ComGroupTree @changeTree="changeTree" slot="left"></ComGroupTree>
    <tableTemplate slot="right" ref="tableTemp"
      :handleSubmit="handleSubmit"
      :modalParams="modalParams"
      :columns="columns"
      :seek="seek"
      :params="params"
      :dataCallBack="dataCallBack"
      :title="title"
      :titleSlot="true"
      :selectChange="selectChange"
      :newParams="newParams"
    >
      <label slot="fun" class="titleLabel">
        <a href="javascript:void(0)" >
          <Upload
            :action="api.importExcel()"
            :headers="headers"
            :name="'metersFiles'"
            :format="['xls','xlsx', 'csv']"
            :accept="'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel'"
            :show-upload-list="false"
            :on-success="onSuccess"
          >
            导入
          </Upload>
        </a>
        <a href="javascript:void(0)" @click="exportExcel">导出</a>
        <a href="javascript:void(0)" @click="add">新增</a>
      </label>
      <addOrEdit ref="add" slot="add" :show="modalParams.show" :modalParams="modalParams" :infoParams="infoParams"></addOrEdit>
      <addOrEdit ref="edit" slot="edit" :show="modalParams.show" :data="roleData" :modalParams="modalParams" :infoParams="infoParams"></addOrEdit>
    </tableTemplate>
  </ComLayout>
</template>

<script>
import { columns, seek, params, modalParams, title } from './WsMeter/params'
import { mapMutations } from 'vuex'
import { getToken } from '@/libs/util'
import dayjs from 'dayjs'
export default {
  name: 'WsMeter',
  data () {
    return {
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      modalParams,
      roleData: {},
      infoParams: {},
      downloadList: [],
      title,
      api: this.$api.System,
      headers: {
        authorization: getToken()
      },
      newParams: {
        groupSn: null,
        villageSn: null,
        userSn: null
      }
    }
  },
  components: {
    addOrEdit: require('./WsMeter/addOrEdit').default
  },
  created () {
  },
  methods: {
    ...mapMutations(['setCount']),
    dataCallBack (params) {
      return this.api.getWsMeter(params).then(res => {
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
        let userName = item.userName ? item.userName + '的' : ''
        this.$Message.success(`删除${userName}水表设备成功！`)
        this.setCount()
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then(res => {
        let type = this.modalParams.type
        if (type === 'add') {
          return this.api.addWsMeter(res).then(data => {
            let userName = res.userName ? res.userName + '的' : ''
            this.$Message.success(`新增${userName}水表设备成功！`)
            this.setCount()
            return Promise.resolve(data)
          }).catch(err => {
            return Promise.reject(err)
          })
        } else {
          return this.api.updateWsMeter(res).then(data => {
            let userName = res.userName ? res.userName + '的' : ''
            this.$Message.success(`修改${userName}水表设备成功！`)
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
      console.log(data)
      this.newParams = {}
      Object.assign(this.newParams, data.type === 'village' && {
        villageSn: data.sn,
        groupSn: null,
        userSn: null
      }, data.type === 'group' && {
        villageSn: null,
        groupSn: data.sn,
        userSn: null
      }, data.type === 'user' && (data.userSn ? {
        villageSn: null,
        groupSn: null,
        userSn: data.userSn
      } : {
        villageSn: null,
        groupSn: null,
        sn: data.sn
      }))
      Object.assign(this.infoParams, data.type === 'village' && {
        villageSn: data.sn,
        groupSn: '',
        userSn: ''
      }, data.type === 'group' && {
        villageSn: data.villageSn,
        groupSn: data.sn,
        userSn: ''
      }, data.type === 'user' && {
        villageSn: data.villageSn,
        groupSn: data.groupSn,
        userSn: data.userSn
      })
      this.setCount()
    },
    onSuccess (response) {
      this.$Message.success(`上传${response.sum}个设备数据,失败${response.error}个!`)
      this.setCount()
    },
    updateMeterDeal (params) {
      this.api.updateMeterDeal(params).then(res => {
        let userName = params.userName ? params.userName + '的' : ''
        this.$Message.success(`更新${userName}水表读数成功!`)
        this.setCount()
      })
    },
    add () {
      Object.assign(this.modalParams, {
        show: true,
        type: 'add',
        title: '新增'
      })
    },
    exportExcel () {
      this.api.exportExcel(this.downloadList).then(res => {
        console.log(res, '<<<<<<<')
        this.$saveFile(res.data, `${dayjs(new Date()).format('YYYY_MM_DD_hh_mm_ss_')}水表设备列表.xlsx`)
      })
    },
    selectChange (select) {
      this.downloadList = select.map(m => m.id)
    },
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
