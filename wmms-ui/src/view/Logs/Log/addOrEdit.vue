<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90">
    <FormItem label="名称" prop="villageName">
      <Input v-model="formValidate.villageName" placeholder="请输入登录账号" clearable></Input>
    </FormItem>
    <FormItem label="排序字段" prop="sortId">
      <Input v-model.number="formValidate.sortId" placeholder="请输入排序字段" clearable></Input>
    </FormItem>
    <FormItem label="地址" prop="address">
      <Input v-model="formValidate.address" placeholder="请输入地址" clearable></Input>
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
        villageName: '',
        sortId: '',
        address: ''
      },
      ruleValidate: {
        sortId: [
          { required: true, type: 'number', message: '请输入顺序', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNumber(rule, value, callback, 5)
            },
            type: 'number',
            trigger: 'blur'
          }
        ],
        villageName: [
          { required: true, message: '请输入村庄名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLength(rule, value, callback, 200)
            },
            trigger: 'blur'
          }
        ],
        address: [
          { required: true, message: '请输入村庄地址', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLength(rule, value, callback, 200)
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
            isValid: typeof this.data.isValid === 'number' ? !!this.data.isValid : true,
            serial: typeof this.data.serial === 'number' ? this.data.serial.toString() : (this.data.isValid || '')
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
            resolve(Object.assign({}, this.modalParams.type === 'edit' && this.data, {
              createUserId: localStorage.getItem('userId')
            }, this.formValidate, {
              updateUserId: localStorage.getItem('userId')
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
