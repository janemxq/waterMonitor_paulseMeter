<template>
  <Card>
    <div @click="onClickFn" v-if="$route.name === 'WsMeter'" class="ivu-tree-title">
      全部村庄
    </div>
    <CommitTree :data="treeData"
          :load-data="loadData"
          @on-toggle-expand="onToggleExpand"
          @on-select-change="onSelectChange"
          @setContextMenuVisible="setContextMenuVisible"
          :show="!modalParams.show"
          ref="tree"
    >
    </CommitTree>
    <!--弹窗-->
    <commonModal :modalParams="modalParams" :infoParams="infoParams" :data="editData"></commonModal>
    <!--弹窗end-->
    <!-- 村庄右键 -->
    <village v-if="ContextMenuVisible.type === 'village'" :data="ContextMenuVisible" :modalParams="modalParams" :infoParams="infoParams"></village>
    <!-- 村庄右键end -->
    <!-- 表井右键 -->
    <group v-if="ContextMenuVisible.type === 'group'" :data="ContextMenuVisible" :modalParams="modalParams" :infoParams="infoParams" :editData="editData"></group>
    <!-- 表井右键end -->
    <!-- 人员右键 -->
    <user v-if="ContextMenuVisible.type === 'user'" :data="ContextMenuVisible" :modalParams="modalParams" :infoParams="infoParams" :editData="editData"></user>
    <!-- 人员右键end -->
  </Card>
</template>

<script>
import village from './groupTree/village'
import commonModal from './groupTree/commonModal'
import group from './groupTree/group'
import user from './groupTree/user'
export default {
  name: 'groupTree',
  data () {
    return {
      api: this.$api.System,
      apiWater: this.$api.Pays,
      treeData: [],
      ContextMenuVisible: { type: '' },
      infoParams: {},
      editData: {},
      modalParams: {
        width: 600,
        show: false,
        modalLoading: true,
        type: 'add',
        title: '新增',
        maskClosable: false
      }
    }
  },
  watch: {
    'modalParams.show' () {
      if (!this.modalParams.show) {
        this.$nextTick(() => {
          this.infoParams = {}
          this.editData = {}
        })
      }
    }
  },
  mounted () {
  },
  components: {
    village,
    commonModal,
    group,
    user
  },
  created () {
    this.getVillageAll()
    this.init()
  },
  methods: {
    onClickFn () {
      this.$emit('changeTree', { sn: '', type: 'village' })
    },
    getVillageAll () {
      this.api.getVillageAll().then(res => {
        this.treeData = res.data.map(m => {
          m.children = []
          m.loading = false
          m.title = `${m.villageName}(${m.address})`
          m.type = 'village'
          return m
        })
      })
    },
    loadData (item, callback) {
      console.log(item)
      if (item.type === 'village') {
        this.api.getWsGroup({
          current: 1,
          size: 9999,
          villageSn: item.sn
        }).then(res => {
          callback(res.data.records.map(m => {
            m.title = m.groupName
            m.type = 'group'
            m.loading = false
            m.children = []
            return m
          }))
        })
      } else if (item.type === 'group') {
        this.apiWater.getWsMeter({
          current: 1,
          size: 9999,
          groupSn: item.sn
        }).then(res => {
          callback(res.data.records.map(m => {
            m.title = m.userName || '未绑定'
            m.type = 'user'
            return m
          }))
        })
      }
    },
    onToggleExpand (data) {
      this.$emit('onToggleExpand', data)
    },
    onSelectChange (list, data) {
      console.log(data, '<<<<<<')
      this.$emit('changeTree', data, list)
    },
    setContextMenuVisible (event, data) {
      this.ContextMenuVisible.type = ''
      setTimeout(() => {
        Object.assign(this.ContextMenuVisible, data, {
          x: event.clientX,
          y: event.clientY,
        })
      })
    },
    init () {
      window.addEventListener("click", () => {
        this.ContextMenuVisible = { type: '' }
      })
      window.addEventListener("contextmenu", () => {
        this.ContextMenuVisible = { type: '' }
      })
    }
  }
}
</script>

<style lang="less">
.rightMenuWrapper {
  position: fixed;
  background: #fff;
  border: solid 1px #e8eaec;
  border-radius: 3px;
  z-index: 999;
  box-shadow: 0 0.5em 1em 0 rgba(0,0,0,.1);
  .rightMenuItem {
    padding: 8px 15px;
    transition: background .2s;
    cursor: pointer;
    &:hover{
      background: #EBF7FF;
    }
  }
}
</style>
