<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90">
    <FormItem label="字典代码" prop="code">
      <Input v-model="formValidate.code" placeholder="请输入字典代码" clearable></Input>
    </FormItem>
    <FormItem label="字典名称" prop="name">
      <Input v-model="formValidate.name" placeholder="请输入字典名称" clearable></Input>
    </FormItem>
    <FormItem label="是否有效" prop="isValid">
      <i-switch v-model="formValidate.isValid" size="large">
        <span slot="open">有效</span>
        <span slot="close">无效</span>
      </i-switch>
    </FormItem>
    <FormItem label="顺序" prop="serial">
      <Input clearable v-model="formValidate.serial" placeholder="请输入顺序"></Input>
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
      formValidate: {
        code: '',
        name: '',
        isValid: true,
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
        code: [
          { required: true, message: '请输入字典代码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNoChineseMin(rule, value, callback, [3, 20])
            },
            trigger: 'blur'
          }
        ],
        name: [
          { required: true, message: '请输入字典名称', trigger: 'blur' },
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
            resolve(Object.assign({}, this.modalParams.type === 'edit' && this.data, this.modalParams.type === 'add' && {
              createUserId: localStorage.getItem('userId')
            }, this.formValidate, this.modalParams.type === 'edit' && {
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

<style lang="less">
</style>
