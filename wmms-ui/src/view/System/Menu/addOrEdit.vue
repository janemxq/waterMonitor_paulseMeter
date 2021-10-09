<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90">
    <FormItem label="菜单名称" prop="name">
      <Input v-model="formValidate.name" placeholder="请输入菜单名称" clearable></Input>
    </FormItem>
    <FormItem label="菜单编号" prop="no">
      <Input v-model="formValidate.no" placeholder="请输入菜单编号" clearable></Input>
    </FormItem>
    <FormItem label="链接" prop="href">
      <Input v-model="formValidate.href" placeholder="请输入链接" clearable></Input>
    </FormItem>
    <FormItem label="图标" prop="icon">
      <Input v-model="formValidate.icon" placeholder="请输入图标" clearable></Input>
    </FormItem>
    <FormItem label="标签" prop="label">
      <Input v-model="formValidate.label" placeholder="请输入标签" clearable></Input>
    </FormItem>
    <FormItem label="文件地址" prop="filePath">
      <Input v-model="formValidate.filePath" placeholder="请输入文件地址" clearable></Input>
    </FormItem>
    <FormItem label="顺序" prop="serial">
      <Input v-model="formValidate.serial" placeholder="请输入顺序" clearable></Input>
    </FormItem>
    <FormItem label="是否有效" prop="isValid">
      <i-switch v-model="formValidate.isValid"  size="large">
        <span slot="open">有效</span>
        <span slot="close">无效</span>
      </i-switch>
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
        name: '',
        no: '',
        href: '',
        icon: '',
        label: '',
        serial: '',
        isValid: true,
        remark: '',
        filePath: ''
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
        name: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLength(rule, value, callback, 200)
            },
            trigger: 'blur'
          }
        ],
        icon: [
          { required: true, message: '请输入图标', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLength(rule, value, callback, 200)
            },
            trigger: 'blur'
          }
        ],
        label: [
          { required: true, message: '请输入标签', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNumAndLetMin(rule, value, callback, [2, 200])
            },
            trigger: 'blur'
          }
        ],
        filePath: [
          { required: true, message: '请输入文件地址', trigger: 'blur' }
        ],
        href: [
          { required: true, message: '请输入链接', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNoChinese(rule, value, callback, 200)
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
            isValid: !!this.formValidate.isValid,
            serial: this.formValidate.serial && this.formValidate.serial.toString()
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
            resolve(Object.assign({},
              this.modalParams.type === 'edit' && this.data,
              this.formValidate,
              {
                isValid: Number(this.formValidate.isValid),
                parentId: (this.data && this.data.parentId) ? this.data.parentId : '-1'
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
    },
    add () {
      this.formValidate.feeStandard.push({
        start: '',
        end: '',
        money: ''
      })
    },
    deletes (index) {
      if (this.formValidate.feeStandard.length < 2) {
        this.$Message.error('至少保留一条规则!')
        return
      }
      this.formValidate.feeStandard.splice(index, 1)
    }
  }
}
</script>

<style scoped>

</style>
