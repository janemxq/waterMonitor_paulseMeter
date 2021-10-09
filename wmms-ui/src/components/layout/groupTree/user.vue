<template>
  <div class="rightMenuWrapper" :style="{ left: data.x + 'px', top: data.y + 'px' }">
    <div ref="aaa">
      <p class="rightMenuItem" @click="editUser">修改水表</p>
      <p class="rightMenuItem" @click="delUser">删除水表</p>
    </div>
  </div>
</template>

<script>
import { component as VueContextMenu } from '@xunlei/vue-context-menu'
export default {
  name: "user",
  props: {
    data: Object,
    modalParams: Object,
    infoParams: Object,
    editData: Object,
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
    editUser () {
      Object.assign(this.editData, JSON.parse(JSON.stringify(this.data)))
      Object.assign(this.infoParams, JSON.parse(JSON.stringify(this.data)))
      Object.assign(this.modalParams, {
        show: true,
        type: 'editUser',
        title: '修改水表',
      })
    },
    delUser () {
      Object.assign(this.infoParams, JSON.parse(JSON.stringify(this.data)))
      Object.assign(this.modalParams, {
        show: true,
        type: 'delUser',
        title: '删除水表',
      })
    }
  }
}
</script>

<style scoped lang="less">

</style>
