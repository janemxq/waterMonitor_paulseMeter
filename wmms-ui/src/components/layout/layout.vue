<template>
  <div class="layout">
    <Split v-model="Split" :min="0.15">
      <div slot="left" class="layout-split-pane">
        <slot name="left"></slot>
      </div>
      <div slot="right" class="layout-split-pane layout-split-right">
        <slot name="right"></slot>
      </div>
    </Split>
  </div>
</template>

<script>
export default {
  name: 'index',
  data () {
    return {
      Split: 0.15
    }
  },
  watch: {
    Split () {
      if (this.Split > 0.4) this.Split = 0.4
    }
  }
}
</script>

<style scoped lang="less">
.layout {
  .w;
  .h;
  .layout-split-pane {
    .w;
    .h;
  }
  .layout-split-left {
    padding-right: 15px;
    overflow-x: auto;
  }
  .layout-split-right{
    padding-left: 5px;
  }
}
</style>
