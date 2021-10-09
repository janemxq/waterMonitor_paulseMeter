<template>
  <div class="slip">
    <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <FormItem label="用户:" class="slip-item">
        {{data.userName || '未知用户'}}
      </FormItem>
      <FormItem class="slip-item" label="累计用水:">
        {{data.waterSum}}
      </FormItem>
      <FormItem class="slip-item" label="缴费时间:">
        {{data.newTime}}
      </FormItem>
      <FormItem class="slip-item" label="原余额:">
        {{data.balance}}
      </FormItem>
      <FormItem class="slip-item" label="欠费金额:">
        {{data.toPayAccount}}
      </FormItem>
      <FormItem class="slip-item"  label="收款金额:">
        <!--        <Input v-model.number="formValidate.realPay" placeholder="请输入收款金额" disabled/>-->
        {{formValidate.realPay}}
      </FormItem>
      <FormItem class="slip-item" label="账户余额:">
        {{balance}}
      </FormItem>
    </Form>
  </div>
</template>

<script>
export default {
  name: 'affirm',
  props: {
    show: Boolean,
    data: Object,
    modalParams: Object,
  },
  data () {
    return {
      formValidate: {
        isSlip: true,
        meterId: '',
        payment: 'cash',
        realPay: ''
      },
      balance: 0,
      ruleValidate: {
        realPay: [
          { required: true, type: 'number', message: '请输入收款金额', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              // this.$rule.ruleNumber(rule, value, callback, 7)
              let RuleTest = /(^[1-9]\d{0,7}$)|(^\.\d{1,2}$)|(^[1-9]\d{0,7}\.\d{1,2}$)/
              if (value < 1) {
                callback(new Error('输入的金额必须大于1!'))
              } else if (!RuleTest.test(value)) {
                callback(new Error('只能输入7位数字或者小数点后只能保留两位!'))
              }
              callback()
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
        console.log(this.modalParams)
        Object.assign(this.formValidate, this.data)
        console.log(this.formValidate.balance, this.formValidate.realPay, this.formValidate.toPayAccount)
        this.balance = ((this.formValidate.balance + this.formValidate.realPay) - this.formValidate.toPayAccount).toFixed(2)
      }
    },
    handleSubmit () {
      return new Promise((resolve, reject) => {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            resolve(Object.assign({
              meterId: this.formValidate.meterId,
              payment: this.formValidate.payment,
              realPay: this.formValidate.realPay,
              isSlip: this.formValidate.isSlip,
              userName: this.data.userName
            }, this.data))
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

<style scoped lang="less">
.slip{
  >p {
    padding-left: 5px;
    margin-bottom: 10px;
    >span {
      margin-right: 10px;
    }
  }
}
.slip {
  .slip-item {
    margin-bottom: 0px;
  }
}
</style>
<style lang="less">
</style>
