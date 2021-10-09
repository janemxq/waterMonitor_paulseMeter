<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90">
    <FormItem label="姓名" prop="name">
      <Input v-model="formValidate.name" placeholder="请输入姓名"></Input>
    </FormItem>
    <FormItem label="手机号" prop="phone">
      <Input v-model="formValidate.phone" placeholder="请输入手机号"></Input>
    </FormItem>
    <FormItem label="登录名" prop="loginAccount" v-if="modalParams.type === 'add'">
      <Input v-model="formValidate.loginAccount" placeholder="请输入登录名"></Input>
    </FormItem>
    <FormItem label="登录密码" prop="loginPass" v-if="modalParams.type === 'add'">
      <Input v-model="formValidate.loginPass" placeholder="请输入登录密码"></Input>
    </FormItem>
    <FormItem label="角色类型">
      <RadioGroup  v-model="formValidate.type">
        <Radio :label="item.value" v-for="item in typeList" :key="item.value">
          <span>{{item.label}}</span>
        </Radio>
      </RadioGroup>
    </FormItem>
    <FormItem label="备注" prop="remark">
      <Input v-model="formValidate.remark" placeholder="请输入备注"></Input>
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
      api: this.$api.UserManage,
      formValidate: {
        name: '',
        phone: '',
        loginAccount: '',
        loginPass: '',
        remark: '',
        type: ''
      },
      ruleValidate: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ],
        loginAccount: [
          { required: true, message: '请输入登录名', trigger: 'blur' }
        ],
        loginPass: [
          { required: true, message: '请输入登录密码', trigger: 'blur' }
        ]
      },
      typeList: []
    }
  },
  watch: {
    show () {
      this.init()
    }
  },
  mounted () {
    this.init()
    this.findByDictCode()
  },
  methods: {
    init () {
      if (this.modalParams.type === 'add') {
        this.formValidate.loginAccount = ''
        this.formValidate.loginPass = ''
      }
      if (this.show) {
        if (this.data) {
          Object.assign(this.formValidate, this.data, {
            type: this.data.type ? this.data.type.toString() : ''
          })
        }
      } else {
        this.handleReset()
      }
    },
    findByDictCode () {
      this.api.findByDictCode({
        dictCode: 'user_type'
      }).then(res => {
        this.typeList = res.data || []
        this.formValidate.type = res.data ? res.data[0].value : ''
      })
    },
    handleSubmit () {
      return new Promise((resolve, reject) => {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            resolve(Object.assign(this.modalParams.type === 'edit' && this.data, {
              createUserId: localStorage.getItem('userId')
            }, this.formValidate, {
              updateUserId: localStorage.getItem('userId')
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
