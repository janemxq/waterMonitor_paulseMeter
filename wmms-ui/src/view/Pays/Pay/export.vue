<template>
    <Form ref="form" :model="formData" :label-width="100">
        <FormItem label="年份">
            <DatePicker :transfer="true" :format="'yyyy'" @on-change="onChange" :options="options"
                        type="year" placeholder="选择要导出的年份" style="width: 200px"></DatePicker>
        </FormItem>
    </Form>
</template>

<script>
export default {
  name: "export",
  props: {
    show: Boolean
  },
  data () {
    return {
      formData: {
        year: ''
      },
      options: {
        disabledDate (date) {
          return date && date.valueOf() > Date.now() - 86400000
        }
      }
    }
  },
  watch: {
    show () {
      if (this.show) this.formData.year = ''
    }
  },
  mounted () {
    this.formData.year = ''
  },
  methods: {
    onChange (e) {
      console.log(e)
      this.formData.year = e
    },
    handleSubmit () {
      return new Promise((resolve, reject) => {
        this.$refs['form'].validate((valid) => {
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
