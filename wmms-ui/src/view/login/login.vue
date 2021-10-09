<style lang="less">
  @import './login.less';
</style>

<template>
  <div class="login">
    <div class="login-con">
      <Card icon="log-in" title="欢迎登录" :bordered="false">
        <div class="form-con">
          <login-form @on-success-valid="handleSubmit" :loading="loading"></login-form>
        </div>
      </Card>
    </div>
  </div>
</template>

<script>
import LoginForm from '_c/login-form'
// import { routerParams } from '@/router/addRouter'
// import { resetRouter } from '@/router'
// import { routerParams } from '@/router/addRouter'
import { createNamespacedHelpers } from 'vuex'
import { setTokenData } from '@/libs/util'
const { mapActions, mapMutations } = createNamespacedHelpers('user')
export default {
  components: {
    LoginForm
  },
  data () {
    return {
      api: this.$api.login,
      apiSys: this.$api.System,
      loading: false
    }
  },
  methods: {
    ...mapActions([
      'handleLogin',
      'getUserInfo'
    ]),
    ...mapMutations(['setUserName']),
    handleSubmit ({ userName, password }) {
      this.loading = true
      this.api.login({
        username: userName,
        password,
        // grant_type=password&client_id=client_1&client_secret=user&password=111111&username=yanwei
        grant_type: 'password',
        client_id: 'client_1',
        client_secret: 'user'
      }).then(res => {
        setTokenData(res.data)
        // { account: userName }
        this.getUserInfo({ userName }).then(res => {
          console.log(res)
          localStorage.setItem('userData', JSON.stringify(res.data))
          localStorage.setItem('userId', res.data.id)
          localStorage.setItem('userType', res.data.type)
          localStorage.setItem('loginAccount', res.data.loginAccount)
          this.getRouter(res.data.type)
        }).catch(() => {
          this.loading = false
        })
      }).catch(() => {
        this.loading = false
      })
    },
    getRouter (id) {
      this.apiSys.bindMenu({
        roleId: id
      }).then(res => {
        // let data = []
        // res.data.map(m => {
        //   let obj = {}
        //   if (m.checkedFlag) {
        //     obj = {
        //       path: m.href,
        //       name: m.no,
        //       meta: {
        //         icon: m.icon,
        //         title: m.name
        //       },
        //       filePath: m.filePath,
        //       children: []
        //     }
        //   }
        //   if (m.childMenu) {
        //     if (!obj.children) obj.children = []
        //     m.childMenu.map(n => {
        //       if (n.checkedFlag) {
        //         obj.children.push({
        //           path: n.href,
        //           name: n.no,
        //           meta: {
        //             icon: n.icon,
        //             title: n.name
        //           },
        //           filePath: n.filePath
        //         })
        //       }
        //     })
        //     if (obj.children.length && !obj.path) {
        //       Object.assign(obj, {
        //         path: m.href,
        //         name: m.no,
        //         meta: {
        //           icon: m.icon,
        //           title: m.name
        //         },
        //         filePath: m.filePath
        //       })
        //     }
        //   }
        //   if (obj.path) {
        //     data.push(obj)
        //   }
        // })
        // data = [
        //   {
        //     path: '/system',
        //     name: 'system',
        //     meta: {
        //       icon: 'md-water',
        //       title: '系统管理1'
        //     },
        //     filePath: 'main',
        //     children: [
        //       {
        //         path: 'Role',
        //         name: 'Role',
        //         meta: {
        //           icon: 'md-planet',
        //           title: '角色管理'
        //         },
        //         filePath: 'Role'
        //       },
        //       {
        //         path: 'menu',
        //         name: 'menu',
        //         meta: {
        //           icon: 'md-planet',
        //           title: '菜单管理'
        //         },
        //         filePath: 'menu'
        //       }
        //     ]
        //   },
        // ]
        // sessionStorage.setItem('router', JSON.stringify(data))
        // this.$router.addRoutes(routerParams(data))
        localStorage.setItem('roleMenu', JSON.stringify(res.data))
        setTimeout(() => {
          this.$router.push({
            name: this.$config.homeName
          })
        })
        // this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style>

</style>
