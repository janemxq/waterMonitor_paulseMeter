<template>
  <Form ref="form" :model="formData" :rules="formRule" :label-width="90">
    <FormItem label="表井地址" prop="address">
      <Input v-model="formData.address" placeholder="请输入表井地址" clearable></Input>
    </FormItem>
    <FormItem label="表井名称" prop="name">
      <Input v-model="formData.name" placeholder="请输入表井名称" clearable></Input>
    </FormItem>
    <FormItem label="备注" prop="remark">
      <Input  v-model="formData.remark" placeholder="请输入备注" clearable type="textarea"></Input>
    </FormItem>
  </Form>
</template>

<script>
export default {
  name: "add",
  props: {
    show: Boolean,
    data: Object
  },
  data () {
    return {
      formData: {
        address: '',
        name: '',
        remark: ''
      },
      formRule: {
        name: [
          { required: true, message: '请输入表井名称', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入表井地址', trigger: 'blur' }
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
      if (this.data) Object.assign(this.formData, this.data)
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
