<template>
  <div class="rightMenuWrapper" :style="{ left: data.x + 'px', top: data.y + 'px' }">
    <div ref="aaa">
      <p class="rightMenuItem" @click="editWsGroup">修改表井</p>
      <p class="rightMenuItem" @click="delWsGroup">删除表井</p>
      <p class="rightMenuItem" @click="addUser">绑定水表</p>
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
    editWsGroup () {
      Object.assign(this.editData, JSON.parse(JSON.stringify(this.data)))
      Object.assign(this.infoParams, JSON.parse(JSON.stringify(this.data)))
      Object.assign(this.modalParams, {
        show: true,
        type: 'editWsGroup',
        title: '修改表井',
      })
    },
    delWsGroup () {
      Object.assign(this.infoParams, JSON.parse(JSON.stringify(this.data)))
      Object.assign(this.modalParams, {
        show: true,
        type: 'delGroup',
        title: '删除表井',
      })
    },
    addUser () {
      console.log(this.data)
      Object.assign(this.infoParams, JSON.parse(JSON.stringify(this.data)))
      Object.assign(this.modalParams, {
        show: true,
        type: 'addUser',
        title: '绑定水表',
      })
    }
  }
}
</script>

<style scoped lang="less">

</style>
