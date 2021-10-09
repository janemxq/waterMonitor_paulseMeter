<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90">
    <FormItem label="角色名称" prop="roleName">
      <Input v-model="formValidate.roleName" placeholder="请输入角色名称" clearable></Input>
    </FormItem>
    <FormItem label="角色编码" prop="roleCode">
      <Input v-model="formValidate.roleCode" placeholder="请输入角色编码" clearable></Input>
    </FormItem>
    <FormItem label="是否有效" prop="isValid">
      <i-switch v-model="formValidate.isValid" size="large">
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
        roleName: '',
        roleCode: '',
        isValid: true,
        remark: ''
      },
      ruleValidate: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLength(rule, value, callback, 200)
            },
            trigger: 'blur'
          }
        ],
        roleCode: [
          { required: true, message: '请输入角色编码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNoChinese(rule, value, callback, 32)
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
                isValid: Number(this.formValidate.isValid)
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
