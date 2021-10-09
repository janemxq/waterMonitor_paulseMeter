<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90">
    <FormItem label="收费名称" prop="displayName">
      <Input v-model="formValidate.displayName" placeholder="请输入收费名称" clearable></Input>
    </FormItem>
    <FormItem label="收费标准" prop="feeStandard">
      <Row>
        <Col span="7">
          用水区间(开始)
        </Col>
        <Col span="7">
          用水区间(结束)
        </Col>
        <Col span="7">
          水费标准
        </Col>
        <Col span="3">
          操作
        </Col>
      </Row>
      <Row v-for="(item, index) in formValidate.feeStandard" :key="index" style="margin-bottom: 25px">
        <Col span="6">
          <FormItem
            :prop="`feeStandard.${index}.start`"
            :rules="ruleValidate.start"
          >
            <Input v-model="item.start" placeholder="请输入开始区间"/>
          </FormItem>
        </Col>
        <Col span="6" offset="1">
          <FormItem
            :prop="`feeStandard.${index}.end`"
            :rules="ruleValidate.end"
          >
            <Input v-model="item.end" placeholder="请输入结束区间"/>
          </FormItem>
        </Col>
        <Col span="6" offset="1">
          <FormItem
            :prop="`feeStandard.${index}.money`"
            :rules="ruleValidate.money"
          >
            <Input v-model="item.money" placeholder="请输入水费标准"/>
          </FormItem>
        </Col>
        <Col span="3" offset="1">
          <Button type="error" @click="deletes(index)">删除</Button>
        </Col>
      </Row>
      <Row>
        <Col span="24" v-if="isStep">
          <Button @click="add" type="success" long>新增</Button>
        </Col>
      </Row>
    </FormItem>
    <FormItem label="阶梯计价" prop="isStep">
      <i-switch v-model="formValidate.isStep" @on-change="isStepChange">
        <span slot="open">是</span>
        <span slot="close">否</span>
      </i-switch>
    </FormItem>
    <FormItem label="排序" prop="sortId">
      <Input v-model.number="formValidate.sortId" placeholder="请输入排序" clearable></Input>
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
      isStep: false,
      formValidate: {
        displayName: '',
        sortId: '',
        oldFeeStandard: [],
        feeStandard: [
          {
            start: '',
            end: '',
            money: ''
          }
        ]
      },
      ruleValidate: {
        serial: [
          { required: true, message: '请输入顺序', trigger: 'blur', type: 'number' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNumber(rule, value, callback, 5)
            },
            trigger: 'blur',
            type: 'number'
          }
        ],
        displayName: [
          { required: true, message: '请输入收费名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLengthMin(rule, value, callback, [2, 100])
            },
            trigger: 'blur'
          }
        ],
        start: [
          { required: true, message: '请输入开始区间', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNumbers(rule, value, callback, 10)
            },
            trigger: 'blur'
          }
        ],
        end: [
          { required: true, message: '请输入结束区间', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNumbers(rule, value, callback, 10)
            },
            trigger: 'blur'
          }
        ],
        money: [
          { required: true, message: '请输入水费标准', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNumbers(rule, value, callback, 10)
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
          let oldFeeStandard = this.formValidate.feeStandard.split('&').map(m => {
            let start = m.split('-')[0]
            let end = m.split('-')[1].split(',')[0]
            let money = m.split('-')[1].split(',')[1]
            return {
              start,
              end,
              money
            }
          })
          if (this.formValidate.isStep) this.oldFeeStandard = oldFeeStandard
          else {
            this.oldFeeStandard = [{
              start: '',
              end: '',
              money: ''
            }]
          }
          this.isStep = !!this.formValidate.isStep
          Object.assign(this.formValidate, {
            isStep: !!this.formValidate.isStep,
            feeStandard: oldFeeStandard
          })
        }
      } else {
        this.handleReset()
        this.oldFeeStandard = [{
          start: '',
          end: '',
          money: ''
        }]
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
                feeStandard: this.formValidate.feeStandard.map(m => `${m.start}-${m.end},${m.money}`).join('&'),
                isStep: Number(this.formValidate.isStep)
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
    },
    isStepChange (value) {
      this.isStep = value
      if (value) {
        this.formValidate.feeStandard = this.oldFeeStandard
      } else {
        this.oldFeeStandard = this.formValidate.feeStandard
        this.formValidate.feeStandard = [
          {
            start: '1',
            end: '999999999',
            money: ''
          }
        ]
      }
    }
  }
}
</script>

<style scoped>

</style>
