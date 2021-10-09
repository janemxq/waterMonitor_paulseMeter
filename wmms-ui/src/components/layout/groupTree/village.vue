<template>
  <div class="rightMenuWrapper" :style="{ left: data.x + 'px', top: data.y + 'px' }">
    <div ref="aaa">
      <p class="rightMenuItem" @click="addWsGroup">新增表井</p>
<!--      <p class="rightMenuItem" v-if="role === 'superAdmin'">修改村庄</p>-->
<!--      <p class="rightMenuItem" v-if="role === 'superAdmin'">删除村庄</p>-->
    </div>
  </div>
</template>

<script>
import { component as VueContextMenu } from '@xunlei/vue-context-menu'
export default {
  name: "village",
  props: {
    data: Object,
    modalParams: Object,
    infoParams: Object
  },
  mounted () {
    this.contextMenuTarget = this.$refs.aaa
  },
  components: {
    VueContextMenu
  },
  data () {
    return {
      contextMenuVisible: false,
      contextMenuTarget: ''
    }
  },
  computed: {
    role () {
      return localStorage.getItem("loginAccount")
    }
  },
  methods: {
    addWsGroup () {
      Object.assign(this.infoParams, JSON.parse(JSON.stringify(this.data)))
      Object.assign(this.modalParams, {
        show: true,
        type: 'addGroup',
        title: '新增表井',
      })
    }
  }
}
</script>

<style scoped lang="less">

</style>
