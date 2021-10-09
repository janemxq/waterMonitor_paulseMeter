<template>
  <div style="height: 100%">
    <ComLayout v-if="flag">
      <ComGroupTree @changeTree="changeTree" slot="left"></ComGroupTree>
      <tableTemplate slot="right"
                     :addModule="false"
                     :handleSubmit="handleSubmit"
                     :modalParams="modalParams"
                     :columns="columns"
                     :seek="seek"
                     :params="params"
                     :dataCallBack="dataCallBack"
                     :title="title"
                     :newParams="newParams"
                     :titleSlot="true"
      >
        <label slot="fun" class="titleLabel">
          <a href="javascript:void(0)" @click="exportForms">导出报表</a>
          <a href="javascript:void(0)" @click="exportExcel">导出欠费情况</a>
        </label>
        <export ref="export" slot="export" :show="modalParams.show"></export>
        <payWS ref="payWS" slot="payWS" :show="modalParams.show" :data="roleData" :modalParams="modalParams" ></payWS>
        <affirm ref="affirm" slot="affirm" :show="modalParams.show" :data="affirmData" :modalParams="modalParams"></affirm>
      </tableTemplate>
    </ComLayout>
    <tableTemplate v-else
                   :addModule="false"
                   :handleSubmit="handleSubmit"
                   :modalParams="modalParams"
                   :columns="columns"
                   :seek="seek"
                   :params="params"
                   :dataCallBack="dataCallBack"
                   :title="title"
                   :newParams="newParams"
                   :titleSlot="true"
    >
      <label slot="fun" class="titleLabel">
        <a href="javascript:void(0)" @click="exportForms">导出报表</a>
        <a href="javascript:void(0)" @click="exportExcel">导出欠费情况</a>
      </label>
      <export ref="export" slot="export" :show="modalParams.show"></export>
      <payWS ref="payWS" slot="payWS" :show="modalParams.show" :data="roleData" :modalParams="modalParams" ></payWS>
      <affirm ref="affirm" slot="affirm" :show="modalParams.show" :data="affirmData" :modalParams="modalParams"></affirm>
    </tableTemplate>
  </div>
</template>

<script>
import dayjs from 'dayjs'
import { columns, seek, params, modalParams, title } from './Pay/params'
import { mapMutations } from 'vuex'
export default {
  name: 'Pay',
  props: {
    flag: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      columns: columns.call(this),
      seek: seek.call(this),
      params,
      modalParams,
      roleData: {},
      title,
      api: this.$api.Pays,
      apiSys: this.$api.System,
      newParams: {
        villageSn: null,
        groupSn: null,
        userSn: null
      },
      affirmData: {}
    }
  },
  components: {
    payWS: require('./Pay/payWS').default,
    affirm: require('./Pay/affirm').default,
    export: require('./Pay/export').default
  },
  created () {
    this.api.getWsFeeStandardAll().then(res => {
      this.seek[2].data = res.data.map(m => {
        return {
          value: m.sn,
          name: m.displayName
        }
      })
    })
  },
  mounted () {
    this.modalParams.show = false
    console.log(this.$refs)
  },
  methods: {
    ...mapMutations(['setCount', 'showLoading', 'hideLoading']),
    dataCallBack (params) {
      return this.api.getPay(params).then(res => {
        return {
          content: res.data.records,
          total: res.data.total
        }
      })
    },
    updateMeterDeal (params) {
      this.showLoading()
      this.apiSys.updateMeterDeal(params).then(res => {
        this.$Message.success(`抄表成功!`)
        this.setCount()
        this.hideLoading()
      }).catch(() => {
        this.setCount()
        this.hideLoading()
      })
    },
    handleSubmit () {
      return this.$refs[this.modalParams.type].handleSubmit().then((res) => {
        console.log(res)
        if (this.modalParams.type === 'payWS') {
          setTimeout(() => {
            Object.assign(this.affirmData, res)
            Object.assign(this.modalParams, {
              type: 'affirm',
              show: true,
              title: '缴费信息确认',
              okText: '确认'
            }, 500)
            console.log(this.$refs)
          })
          return Promise.resolve(res)
        } else if (this.modalParams.type === 'export') {
          this.api.postExportUserCostAndWaterSumToPdf(res).then(exportData => {
            console.log(exportData, '<<<<<<<')
            this.$saveFile(exportData.data, `${dayjs(new Date()).format('YYYY_MM_DD_hh_mm_ss_')}报表.pdf`)
          })
        } else {
          return this.api.wsFeeLog(res).then(() => {
            this.setCount()
            if (res.isSlip) {
              console.log(res)
              let formData = new FormData()
              formData.append('accountNum', res.toPayAccount)
              formData.append('realPay', res.realPay)
              formData.append('userName', res.userName)
              formData.append('volume', res.toPayWaterVolume)
              formData.append('balances', res.balance)
              return this.api.print(formData).then(res => {
                console.log(res)
                this.$Message.success(`打印成功!`)
                return Promise.resolve(res)
              }).catch(err => {
                return Promise.reject(err)
              })
            } else {
              this.$Message.success(`${res.userName ? res.userName : ''}缴费成功!`)
            }
          }).catch(err => {
            return Promise.reject(err)
          })
        }
      })
    },
    async payWSCall (param) {
      // await this.api.getTime().then(res => {
      //   console.log(res.data.result.datetime_1)
      //   Object.assign(this.roleData, {
      //     newTime: res.data.result.datetime_1
      //   })
      // }).catch(err => {
      //   console.log(err)
      // })
      this.roleData = {}
      Object.assign(this.roleData, param, {
        newTime: dayjs().format('YYYY-MM-DD hh:mm:ss')
      })
      Object.assign(this.modalParams, {
        show: true, // 弹窗开关
        modalLoading: true, // 点击确定Loading状态
        type: 'payWS', // 弹出框类型。目 前支持三种 add edit info
        title: '缴费',
        okText: '缴费',
      })
    },
    exportForms () {
      Object.assign(this.modalParams, {
        type: 'export',
        show: true,
        title: '选择导出年份'
      })
    },
    exportExcel () {
      this.api.postExportNoPay({
        userSn: '',
        villageSn: ''
      }).then(res => {
        console.log(res, '<<<<<<<')
        this.$saveFile(res.data, `${dayjs(new Date()).format('YYYY_MM_DD_hh_mm_ss_')}欠费情况.xlsx`)
      })
    },
    changeTree (data) {
      Object.assign(this.newParams, data.type === 'village' && {
        villageSn: data.sn,
        groupSn: null,
        userSn: null
      }, data.type === 'group' && {
        villageSn: null,
        groupSn: data.sn,
        userSn: null
      }, data.type === 'user' && {
        villageSn: null,
        groupSn: null,
        userSn: data.userSn
      })
      this.setCount()
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
