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
    <FormItem label="备注" prop="remark">
      <Input v-model="formValidate.remark" placeholder="请输入备注"></Input>
    </FormItem>
    <FormItem label="其他" prop="add">
      <Input v-model="formValidate.value" placeholder="请输入其他内容"></Input>
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
      formValidate: {
        name: '',
        phone: '',
        loginAccount: '',
        loginPass: '',
        remark: ''
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
      if (this.modalParams.type === 'add') {
        this.formValidate.loginAccount = ''
        this.formValidate.loginPass = ''
      }
      if (this.show) {
        if (this.data) {
          Object.assign(this.formValidate, this.data)
        }
      } else {
        this.handleReset()
      }
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
