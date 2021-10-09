<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90">
    <FormItem label="登录账号" prop="loginAccount">
      <Input v-model="formValidate.loginAccount" placeholder="请输入登录账号" clearable></Input>
    </FormItem>
    <FormItem label="登录密码" prop="loginPass" v-if="modalParams.type !== 'edit'">
      <Input v-model="formValidate.loginPass" placeholder="请输入登录密码(默认111111)" type="password" password></Input>
    </FormItem>
    <FormItem label="姓名" prop="name">
      <Input v-model="formValidate.name" placeholder="请输入姓名" clearable></Input>
    </FormItem>
    <FormItem label="村庄" prop="villageId">
      <Select v-model.number="formValidate.villageId" @on-change="onChange">
        <Option v-for="item in VillageAll" :value="item.id" :key="item.id">{{ item.villageName }}</Option>
      </Select>
    </FormItem>
    <FormItem label="手机号" prop="phone">
      <Input v-model="formValidate.phone" placeholder="请输入手机号" clearable></Input>
    </FormItem>
    <FormItem label="身份证号" prop="idCard">
      <Input v-model="formValidate.idCard" placeholder="请输入身份证号" clearable></Input>
    </FormItem>
    <FormItem label="是否有效" prop="isValid">
      <i-switch v-model="formValidate.isValid"  size="large">
        <span slot="open">有效</span>
        <span slot="close">无效</span>
      </i-switch>
    </FormItem>
    <FormItem label="用户角色" prop="type">
      <RadioGroup v-model.number="formValidate.type">
        <Radio v-for="item in typeList" :key="item.id" :label="item.id">
          <span>{{item.roleName}}</span>
        </Radio>
      </RadioGroup>
    </FormItem>
    <FormItem label="备注" prop="remark">
      <Input clearable type="textarea" v-model="formValidate.remark" placeholder="请输入备注" :rows="3" :autosize="{ minRows: 2, maxRows: 6 }"></Input>
    </FormItem>
  </Form>
</template>

<script>

export default {
  name: 'add',
  props: {
    show: Boolean,
    data: Object,
    modalParams: Object
  },
  data () {
    return {
      VillageAll: [],
      api: this.$api.System,
      typeList: [
      ],
      formValidate: {
        loginAccount: '',
        loginPass: '',
        name: '',
        phone: '',
        remark: '',
        isValid: true,
        type: 0, // 0普通用户1村级管理员2集管管理员
        villageId: '',
        idCard: '',
        villageName: ''
      },
      ruleValidate: {
        loginAccount: [
          { required: true, message: '请输入登录账号', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNoChinese(rule, value, callback, 200)
            },
            type: 'number',
            trigger: 'blur'
          }
        ],
        loginPass: [
          {
            validator: (rule, value, callback) => {
              this.$rule.nonePassword(rule, value, callback, [6, 18])
            },
            trigger: 'blur'
          }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLengthMin(rule, value, callback, [3, 20])
            },
            trigger: 'blur'
          }
        ],
        phone: [
          {
            validator: this.$rule.validateContact,
            trigger: 'blur'
          }
        ],
        idCard: [
          { required: true, message: '请输入身份证号', trigger: 'blur' },
          {
            validator: this.$rule.validateIdCard,
            trigger: 'blur'
          }
        ],
        remark: [
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLength(rule, value, callback, 500)
            },
            trigger: 'blur'
          }
        ],
      }
    }
  },
  watch: {
    show () {
      this.init()
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    init () {
      if (this.show) {
        this.getVillageAll()
        if (this.data) {
          Object.keys(this.formValidate).map(m => {
            this.formValidate[m] = this.data[m]
          })
          Object.assign(this.formValidate, {
            isValid: typeof this.data.isValid === 'number' ? !!this.data.isValid : true,
            serial: typeof this.data.serial === 'number' ? this.data.serial.toString() : (this.data.isValid || '')
          })
        }
      } else {
        this.handleReset()
      }
    },
    getVillageAll () {
      this.api.getVillageAll().then(res => {
        this.VillageAll = res.data
      })
      this.api.getRoleAll().then(res => {
        this.typeList = res.data
      })
    },
    onChange (id) { // 选择村庄
      const index = this.VillageAll.findIndex(m => m.id === id)
      this.formValidate.villageName = this.VillageAll[index].villageName
    },
    handleSubmit () {
      return new Promise((resolve, reject) => {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            resolve(Object.assign({}, this.modalParams.type === 'edit' && this.data, this.modalParams.type === 'add' && {
              createUserId: localStorage.getItem('userId')
            }, this.formValidate, this.modalParams.type === 'edit' && {
              updateUserId: localStorage.getItem('userId'),
              loginPass: ''
            }, {
              isValid: this.formValidate.isValid ? 1 : 0
            }))
          } else {
            reject('填写内容有误，请查验')
          }
        })
      })
    },
    handleReset () { // 刷新事件
      if (this.$refs['form']) this.$refs['form'].resetFields()
    }
  }
}
</script>

<style scoped>

</style>
