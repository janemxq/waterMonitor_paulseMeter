<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import { createNamespacedHelpers } from 'vuex'
// import { routerParams } from '@/router/addRouter'
import { Event } from '@/plugin/global'
const { mapMutations } = createNamespacedHelpers('user')

export default {
  name: 'App',
  mounted () {
    console.log('菜单异常优先使用刷新,后续修改 --- 1.4.2')
    this.initSetAccess()
  },
  created () {
    Event('console')
    Event('clear')
    Event('reset')
    // if (sessionStorage.getItem("router")) {
    //   this.$router.addRoutes(routerParams(JSON.parse(sessionStorage.getItem('router'))))
    // }
  },
  methods: {
    ...mapMutations(['setAccess']),
    initSetAccess () {
      if (localStorage.getItem('loginAccount') && this.setAccess) {
        this.setAccess([localStorage.getItem('loginAccount')])
      }
    }
  }
}
</script>

<style lang="less">
.size{
  width: 100%;
  height: 100%;
}
html,body{
  .size;
  overflow: hidden;
  margin: 0;
  padding: 0;
}
#app {
  .size;
}

.content-wrapper {
  td.ivu-table-expanded-cell {
    padding: 5px 10px;
    .ivu-table-wrapper {
      border: none;
      .ivu-table-row:last-child >td {
        border: none;
      }
    }
  }
}
</style>
