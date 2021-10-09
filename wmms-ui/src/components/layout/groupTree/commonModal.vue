<template>
  <Modal
    v-model="modalParams.show"
    :title="modalParams.title"
    :ok-text="modalParams.okText"
    :loading="modalParams.modalLoading"
    :width="modalParams.width"
    :mask-closable="modalParams.maskClosable"
    :styles="modalParams.styles"
    @on-ok="asyncOK">
    <changeGroups v-if="modalParams.type === 'addGroup'" :modalParams="modalParams" :infoParams="infoParams" :show="modalParams.show" ref="addGroup"></changeGroups>
    <changeGroups v-if="modalParams.type === 'editWsGroup'" :data="data" :modalParams="modalParams" :infoParams="infoParams" :show="modalParams.show" ref="editWsGroup"></changeGroups>
    <delGroup v-if="modalParams.type === 'delGroup'" :data="data" :modalParams="modalParams" :infoParams="infoParams" :show="modalParams.show" ref="delGroup"></delGroup>
    <changeUser v-if="modalParams.type === 'addUser'" :modalParams="modalParams" :infoParams="infoParams" :show="modalParams.show" ref="addUser"></changeUser>
    <changeUser v-if="modalParams.type === 'editUser'" :data="data" :modalParams="modalParams" :infoParams="infoParams" :show="modalParams.show" ref="editUser"></changeUser>
    <delUser v-if="modalParams.type === 'delUser'" :data="data" :modalParams="modalParams" :infoParams="infoParams" :show="modalParams.show" ref="delUser"></delUser>
  </Modal>
</template>

<script>
import { mapMutations } from 'vuex'
export default {
  name: "commonModal",
  data () {
    return {
      api: this.$api.System,
    }
  },
  props: {
    modalParams: Object,
    infoParams: Object,
    data: Object,
  },
  components: {
    changeGroups: () => import('./changeGroup.vue'),
    delGroup: () => import('./delGroup.vue'),
    changeUser: () => import('./changeUser.vue'),
    delUser: () => import('./delUser.vue'),
  },
  methods: {
    ...mapMutations(['setCount']),
    pushData () {
      console.log(this.modalParams.type)
      switch (this.modalParams.type) {
        case "addGroup":
          return this.$refs.addGroup.handleSubmit().then(res => {
            return this.api.addWsGroup(res).then(data => {
              this.$Message.success(`新增水井${res.groupName}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          })
        case "editWsGroup":
          return this.$refs.editWsGroup.handleSubmit().then(res => {
            return this.api.updateWsGroup(res).then(data => {
              this.$Message.success(`修改水井${res.groupName}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          })
        case "delGroup":
          return this.$refs.delGroup.handleSubmit().then(res => {
            return this.api.delWsGroup(res).then(data => {
              this.$Message.success(`删除水井${res.groupName}成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          })
        case "addUser":
          return this.$refs.addUser.handleSubmit().then(res => {
            console.log(res, '<<<<<<<<<<')
            return this.api.addWsMeter(res).then(data => {
              let userName = res.userName ? res.userName + '的' : ''
              this.$Message.success(`新增${userName}水表设备成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              console.log(err)
              return Promise.reject(err)
            })
          })
        case "editUser":
          return this.$refs.editUser.handleSubmit().then(res => {
            return this.api.updateWsMeter(res).then(data => {
              this.$Message.success(`修改${res.userName}的水表设备成功！`)
              this.setCount()
              return Promise.resolve(data)
            }).catch(err => {
              return Promise.reject(err)
            })
          })
        case "delUser":
          return this.$refs.delUser.handleSubmit().then(res => {
            this.api.delWsMeter({
              id: res.id
            }).then(() => {
              this.$Message.success(`删除${res.userName}的水表设备成功！`)
              this.setCount()
              return Promise.resolve(res)
            }).catch(err => {
              return Promise.reject(err)
            })
          })
        default:
          break
      }
    },
    asyncOK () {
      this.pushData().then(() => {
        this.modalParams.modalLoading = false
        setTimeout(m => {
          this.modalParams.show = false
          this.modalParams.modalLoading = true
        }, 100)
      }).catch(() => {
        this.modalParams.modalLoading = false
        setTimeout(m => {
          this.modalParams.modalLoading = true
        }, 100)
      })
    }
  }
}
</script>

<style scoped>

</style>
