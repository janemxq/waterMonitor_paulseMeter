<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90">
    <FormItem label="字典代码" prop="dictCode">
      <Input v-model="formValidate.dictCode" placeholder="请输入字典代码" disabled="disabled"></Input>
    </FormItem>
    <FormItem label="标签项" prop="label">
      <Input v-model="formValidate.label" placeholder="请输入标签项" clearable></Input>
    </FormItem>
    <FormItem label="值" prop="value">
      <Input v-model="formValidate.value" placeholder="请输入值" clearable></Input>
    </FormItem>
    <FormItem label="顺序" prop="serial">
      <Input v-model="formValidate.serial" placeholder="请输入顺序" clearable></Input>
    </FormItem>
    <FormItem label="备注" prop="remark">
      <Input type="textarea" v-model="formValidate.remark" placeholder="请输入备注" :rows="3" :autosize="{ minRows: 2, maxRows: 6 }"></Input>
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
        dictCode: '',
        dictId: '',
        label: '',
        value: '',
        serial: '',
        remark: ''
      },
      ruleValidate: {
        serial: [
          { required: true, message: '请输入顺序', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNumber(rule, value, callback, 5)
            },
            trigger: 'blur'
          }
        ],
        label: [
          { required: true, message: '请输入标签项', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLength(rule, value, callback, 50)
            },
            trigger: 'blur'
          }
        ],
        value: [
          { required: true, message: '请输入值', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLength(rule, value, callback, 50)
            },
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
      if (this.show) {
        if (this.data) {
          Object.keys(this.formValidate).map(m => {
            this.formValidate[m] = this.data[m]
          })
          Object.assign(this.formValidate, {
            serial: typeof this.data.serial === 'number' ? this.data.serial.toString() : (this.data.serial || '')
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
            console.log(this.data)
            resolve(Object.assign({},
              this.data,
              this.formValidate,
              {
                dictId: this.data.dictId
              },
              this.modalParams.type === 'childEdit' && {
                id: this.data.id
              })
            )
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

<style lang="less">
</style>
