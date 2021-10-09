<template>
  <Form ref="form" :model="formData" :rules="formRule" :label-width="90">
    <FormItem label="码盘值" prop="meterNum">
      <Input v-model="formData.meterNum" placeholder="请输入码盘值" clearable></Input>
    </FormItem>
    <FormItem label="总脉冲" prop="pulseSum">
      <Input v-model="formData.pulseSum" placeholder="请输入总脉冲" clearable></Input>
    </FormItem>
    <FormItem label="总用水量" prop="waterSum">
      <Input v-model="formData.waterSum" placeholder="请输入总用水量" clearable></Input>
    </FormItem>
  </Form>
</template>

<script>
export default {
  name: "init",
  props: {
    show: Boolean,
    data: Object
  },
  data () {
    return {
      formData: {
        meterNum: '', // 码盘值
        pulseSum: '', // 总脉冲
        sn: '', // 标示
        waterSum: '' // 总用水量
      },
      formRule: {
        meterNum: [
          { required: true, message: '请输入码盘值', trigger: 'blur' }
        ],
        pulseSum: [
          { required: true, message: '请输入总脉冲', trigger: 'blur' }
        ],
        waterSum: [
          { required: true, message: '请输入总用水量', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    show () {
      if (this.show) {
        this.$refs.form.resetFields()
        this.init()
      }
    }
  },
  mounted () {
    if (this.show) {
      this.$refs.form.resetFields()
      this.init()
    }
  },
  methods: {
    init () {
      if (this.data) {
        Object.assign(this.formData, { sn: this.data.sn })
      }
    },
    handleSubmit () {
      return new Promise((resolve, reject) => {
        this.$refs.form.validate((valid) => {
          if (valid) {
            resolve(this.formData)
          } else {
            reject(false)
          }
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
