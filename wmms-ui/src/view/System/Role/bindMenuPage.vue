<template>
<div class="bindMenu">
  <Tree :data="menuList" show-checkbox multiple v-if="menuList.length" ref="tree"></Tree>
</div>
</template>

<script>
export default {
  name: "bindMenuPage",
  data () {
    return {
      menuList: [],
      api: this.$api.System
    }
  },
  props: {
    show: Boolean,
    modalParams: Object,
    data: Object
  },
  watch: {
    show () {
      if (this.show) this.getMenuList()
      else this.menuList = []
    }
  },
  mounted () {
    this.getMenuList()
  },
  methods: {
    getMenuList () {
      Promise.all([this.api.userMenu({
        current: 1,
        size: 10
      }), this.api.bindMenu({
        roleId: this.data.id
      })]).then(res => {
        const data = res[0].data
        res[1].data.map((n) => {
          const index = data.findIndex((m) => n.id === m.id)
          data[index].checkedFlag = n.checkedFlag
          if (!data[index].childMenu) {
            data[index].checked = !!n.checkedFlag
          }
          if (index !== -1) {
            if (n.childMenu) {
              n.childMenu.map(j => {
                const _index = data[index].childMenu.findIndex(s => s.id === j.id)
                data[index].childMenu[_index].checkedFlag = j.checkedFlag
                data[index].childMenu[_index].checked = !!j.checkedFlag
              })
            }
          }
        })
        console.log(res[0])
        this.menuList = data.map(m => {
          m.title = m.name
          m.expand = true
          m.children = (m.childMenu || []).map(n => {
            n.title = n.name
            return n
          })
          return m
        })
      })
    },
    handleSubmit () {
      console.log(this.$refs.tree.getCheckedAndIndeterminateNodes(), this.data)
      return Promise.resolve({
        menusIds: this.$refs.tree.getCheckedAndIndeterminateNodes().map(m => m.id),
        roleId: this.data.id
      })
    }
  }
}
</script>

<style scoped lang="less">
.bindMenu {
  min-height: 200px;
  max-height: 450px;
}
</style>
