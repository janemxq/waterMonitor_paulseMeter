<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90">
    <FormItem label="登录密码" prop="loginPass" v-if="modalParams.type !== 'edit'">
      <Input v-model="formValidate.loginPass" placeholder="请输入登录密码" type="password" password ></Input>
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
        loginPass: '',
      },
      ruleValidate: {
        loginPass: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.nonePassword(rule, value, callback, [6, 18])
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
        if (this.data) {
          Object.keys(this.formValidate).map(m => {
            this.formValidate[m] = this.data[m]
          })
          Object.assign(this.formValidate, {
            loginPass: ''
          })
        }
      } else {
        this.handleReset()
      }
    },
    handleSubmit () {
      return new Promise((resolve, reject) => {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            resolve(Object.assign({}, this.data, this.formValidate))
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
