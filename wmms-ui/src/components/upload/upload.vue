<template>
  <div>
    <div>
      {{progressData}}
    </div>
    <Upload
      multiple
      type="drag"
      :action="action"
      :on-progress="progress"
      :on-success="onSuccess"
    >
      <slot></slot>
    </Upload>
  </div>
</template>

<script>
export default {
  name: 'upload',
  props: ['action', 'onSuccess'],
  data () {
    return {
      progressData: ''
    }
  },
  methods: {
    progress (event) {
      console.log(event)
      this.progressData = event
    }
  }
}
</script>

<style scoped>

</style>
