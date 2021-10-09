<template>
  <div>
    是否确认删除表井{{infoParams.groupName}}
  </div>
</template>

<script>
export default {
  name: "delGroup",
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
