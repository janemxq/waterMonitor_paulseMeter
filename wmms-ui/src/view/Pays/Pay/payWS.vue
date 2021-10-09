<template>
  <div class="slip">
    <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <FormItem label="用户:" class="slip-item">
        {{data.userName || '未知用户'}}
      </FormItem>
      <FormItem class="slip-item" label="起码:">
        {{data.preNumber}}
      </FormItem>
      <FormItem class="slip-item" label="止码:">
        {{data.lastNumber}}
      </FormItem>
      <FormItem class="slip-item" label="用水量:">
        {{data.toPayWaterVolume}}
      </FormItem>
      <FormItem class="slip-item" label="累计用水:">
        {{data.waterSum}}
      </FormItem>
      <FormItem class="slip-item" label="缴费时间:">
        {{data.newTime}}
      </FormItem>
      <FormItem class="slip-item" label="欠费金额:">
        {{data.toPayAccount}}
      </FormItem>
      <FormItem class="slip-item" label="余额:">
        {{data.balance}}
      </FormItem>
      <FormItem label="是否打印凭条:" prop="isSlip">
        <i-switch v-model="formValidate.isSlip" >
          <span slot="open">是</span>
          <span slot="close">否</span>
        </i-switch>
      </FormItem>
      <FormItem label="支付方式:" prop="payment">
        <RadioGroup v-model="formValidate.payment" type="button">
          <Radio label="cash">现金</Radio>
          <Radio label="alipay">支付宝</Radio>
          <Radio label="wechatpay">微信</Radio>
        </RadioGroup>
      </FormItem>
      <FormItem label="收款金额:" prop="realPay">
        <Input v-model.number="formValidate.realPay" placeholder="请输入收款金额" />
      </FormItem>
    </Form>
  </div>
</template>

<script>
export default {
  name: 'payWS',
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
        Object.assign(this.formValidate, {
          isSlip: true,
          meterId: this.data.id,
          realPay: ''
        })
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
