<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="100">
    <FormItem label="设备启用时间" prop="activationTime">
      <DatePicker type="date" placeholder="请选择设备启用时间" v-model="formValidate.activationTime" clearable style="width: 100%"></DatePicker>
    </FormItem>
    <FormItem label="编号" prop="sn">
      <Input v-model="formValidate.sn" placeholder="请输入编号" clearable></Input>
    </FormItem>
    <FormItem label="用户" prop="userSn" v-if="isShow">
      <Select
        placeholder="请搜索并选择用户"
        v-model="formValidate.userSn"
        filterable
        remote
        ref="user"
        :remote-method="getUser"
        @on-change="userChange"
        :loading="userLoading">
        <Option v-for="(option, index) in userList" :value="option.sn" :key="index">{{option.name}}({{option.phone || option.villageName}})</Option>
      </Select>
    </FormItem>
    <FormItem label="表井通道号" prop="macSn">
      <Input v-model="formValidate.macSn" placeholder="请输入表井通道号" clearable></Input>
    </FormItem>
    <FormItem label="用水类型" prop="meterTypeSn">
      <Select v-model="formValidate.meterTypeSn" clearable placeholder="请选择用水类型" filterable @on-change="meterTypeChange">
        <Option  v-for="item in getWsFeeStandardAll" :value="item.sn" :key="item.sn" >{{item.displayName}}</Option>
      </Select>
    </FormItem>
    <FormItem label="初始码盘值" prop="pulseInit">
      <Input v-model.number="formValidate.pulseInit" placeholder="请输入初始码盘值" clearable></Input>
    </FormItem>
    <FormItem label="方水/脉冲" prop="pulse">
      <Input v-model="formValidate.pulse" placeholder="请输入方水/脉冲(n方水/脉冲)" clearable></Input>
    </FormItem>
    <FormItem label="备注" prop="remark">
      <Input type="textarea" v-model="formValidate.remark" placeholder="请输入备注" :rows="3" :autosize="{ minRows: 2, maxRows: 6 }"></Input>
    </FormItem>
  </Form>
</template>

<script>
import dayjs from 'dayjs'
export default {
  name: 'changeUser',
  props: {
    show: Boolean,
    data: Object,
    modalParams: Object,
    infoParams: Object
  },
  data () {
    return {
      api: this.$api.System,
      formValidate: {
        groupId: '',
        groupSn: '',
        villageId: '',
        villageSn: '',
        userId: '',
        userSn: '',
        sn: '',
        macSn: '',
        meterInit: '',
        meterType: '',
        meterTypeSn: '',
        pulseInit: '',
        pulse: '',
        remark: '',
        name: '',
        activationTime: new Date()
      },
      ruleValidate: {
        groupSn: [
          { required: true, message: '请选择表井', trigger: 'change' }
        ],
        meterTypeSn: [
          { required: true, message: '请选择用水类型', trigger: 'change' }
        ],
        sn: [
          { required: true, message: '请输入编号', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNoChineseMin(rule, value, callback, [3, 80])
            },
            trigger: 'blur'
          }
        ],
        macSn: [
          { required: true, message: '请输入表井通道号', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value === '' || /^([0-9]|[a-f]|[A-F])$/.test(value)) {
                callback()
              } else {
                callback('表井通道号格式为1位十六进制数值')
              }
            },
            trigger: 'blur'
          }
        ],
        pulseInit: [
          {
            validator: (rule, value, callback) => {
              if (value) {
                this.$rule.ruleNumbers(rule, value, callback, 10)
              } else {
                callback()
              }
            },
            type: 'number',
            trigger: 'blur'
          }
        ],
        pulse: [
          { required: true, message: '请输入方水/脉冲', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value) {
                this.$rule.ruleNumberMin(rule, value, callback, [2, 50])
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        remark: [
          {
            validator: (rule, value, callback) => {
              if (value) {
                this.$rule.ruleLength(rule, value, callback, 500)
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      },
      VilList: [],
      GroupList: [],
      userList: [],
      getWsFeeStandardAll: [],
      userLoading: false,
      isShow: false
    }
  },
  watch: {
    show () {
      this.init()
    },
    userList () {
      console.log(this.userList)
      if (this.userList && this.userList.length) {
        this.isShow = true
      }
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    getVilData () { // 获取村庄数据
      this.api.getVillageAll().then(res => {
        console.log(res.data)
        this.VilList = res.data
      })
    },
    getWsGroup (villageSn) { // 获取水井数据
      this.api.getWsGroup({
        villageSn,
        current: 1,
        size: 99
      }).then(res => {
        this.GroupList = res.data.records
      })
    },
    findUser (name) {
      return this.api.findUser({
        name,
        size: 99,
        current: 1,
        villageId: this.infoParams.villageId
      }).then(res => {
        console.log(res.data.records)
        this.userList = res.data.records
        return res
      })
    },
    getUser (name) {
      this.userLoading = true
      this.findUser(name).finally(() => {
        this.userLoading = false
      })
    },
    init () {
      if (this.show) {
        // this.getVilData()
        this.getWsFeeStandardAllCallBack()
        if (this.data) {
          this.getWsGroup(this.data.villageId)
          this.findUser(this.data.userName).finally(() => {
            Object.keys(this.formValidate).map(m => {
              this.formValidate[m] = this.data[m]
            })
            Object.assign(this.formValidate, {
              activationTime: new Date(this.formValidate.activationTime),
            })
          })
          console.log(this.formValidate, '<<<')
        } else {
          if (this.infoParams.villageId) this.getWsGroup(this.infoParams.villageId)
          Object.assign(this.formValidate, {
            groupId: this.infoParams.id,
            groupSn: this.infoParams.sn,
            villageId: this.infoParams.villageId,
            villageSn: this.infoParams.villageSn,
          })
        }
      } else {
        this.handleReset()
        this.VilList = []
        this.GroupList = []
        this.userList = []
        this.getWsFeeStandardAll = []
      }
    },
    handleSubmit () {
      return new Promise((resolve, reject) => {
        this.$refs['form'].validate(valid => {
          if (valid) {
            let activationTime = dayjs(this.formValidate.activationTime).format('YYYY-MM-DD')
            let formData
            if (this.data) {
              formData = Object.assign(
                {},
                this.data,
                this.formValidate,
                {
                  activationTime,
                  id: this.data.id
                }
              )
            } else {
              formData = Object.assign(
                {},
                this.formValidate,
                {
                  activationTime
                }
              )
            }
            resolve(formData)
          } else {
            reject('填写内容有误，请查验')
          }
        })
      })
    },
    handleReset () { // 刷新事件
      if (this.$refs['form']) this.$refs['form'].resetFields()
    },
    VilChange (data) {
      const index = this.VilList.findIndex(m => m.sn === data)
      if (index !== -1) this.formValidate.villageId = this.VilList[index].id
      this.getWsGroup(data)
    },
    groupChange (data) {
      const index = this.GroupList.findIndex(m => m.sn === data)
      if (index !== -1) this.formValidate.groupId = this.GroupList[index].id
    },
    userChange (data) {
      const index = this.userList.findIndex(m => m.sn === data)
      if (index !== -1) this.formValidate.userId = this.userList[index].id
    },
    meterTypeChange (data) {
      const index = this.getWsFeeStandardAll.findIndex(m => m.sn === data)
      if (index !== -1) this.formValidate.meterType = this.getWsFeeStandardAll[index].id
    },
    getWsFeeStandardAllCallBack () {
      this.api.getWsFeeStandardAll().then(res => {
        this.getWsFeeStandardAll = res.data
      })
    }
  }
}
</script>

<style scoped>

</style>
