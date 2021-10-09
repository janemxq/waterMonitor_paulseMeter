<template>
  <div>
    是否确认删除{{infoParams.userName}}的水表
  </div>
</template>

<script>
export default {
  name: "delUser",
  props: {
    infoParams: Object
  },
  methods: {
    handleSubmit () {
      return Promise.resolve(this.infoParams)
    },
  }
}
</script>

<style scoped>

</style>
