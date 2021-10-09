<template>
  <Menu mode="horizontal" theme="dark" :active-name="activeName" @on-select="router" class="menuWrapper">
    <div class="layout-logo" @click="router('home')" v-if="width > 1050">
      <img :src="logo" height="100%">
      <label v-if="width > 1300">山东朝启农村用水监测管理平台</label>
    </div>
    <div class="layout-menu" v-else @click="flag = true">
      <Icon type="md-menu" size="24"/>
    </div>
    <div class="layout-nav" v-if="width > 1050">
      <template v-for="(item) in menuList" >
        <template v-if="item.children && !item.children.length"></template>
        <template v-else-if="item.children && item.children.length === 1">
          <MenuItem :name="it.name" v-for="it in item.children" v-bind:key="it.name" >
            <Icon :type="it.icon" :key="it.title"></Icon>
            <span>{{it.meta.title}}</span>
          </MenuItem>
        </template>
        <template v-else>
          <Submenu :name="item.name" :key="item.name"  class="mainMenu">
            <template slot="title">
              <Icon :type="item.icon"></Icon>
              <span>{{item.meta.title}}</span>
            </template>
            <MenuItem :name="it.name" v-for="it in item.children" :key="it.name">
              <Icon :type="it.icon"></Icon>
              <span>{{it.meta.title}}</span>
            </MenuItem>
          </Submenu>
        </template>
      </template>
    </div>
    <div v-else-if="flag && width <= 1050" class="miniMenu">
      <div class="layout-close">
        <Icon type="md-close" size="24"  @click="flag = false"/>
      </div>
      <Menu ref="menu" v-show="!collapsed" :active-name="activeName" :open-names="openedNames" :accordion="accordion" :theme="theme" width="auto" @on-select="handleSelect">
        <template v-for="item in menuList">
          <template v-if="item.children && item.children.length === 1">
            <side-menu-item v-if="showChildren(item)" :key="`menu-${item.name}`" :parent-item="item"></side-menu-item>
            <menu-item v-else :name="getNameOrHref(item, true)" :key="`menu-${item.children[0].name}`"><common-icon :type="item.children[0].icon || ''"/><span>{{ showTitle(item.children[0]) }}</span></menu-item>
          </template>
          <template v-else>
            <side-menu-item v-if="showChildren(item)" :key="`menu-${item.name}`" :parent-item="item"></side-menu-item>
            <menu-item v-else :name="getNameOrHref(item)" :key="`menu-${item.name}`"><common-icon :type="item.icon || ''"/><span>{{ showTitle(item) }}</span></menu-item>
          </template>
        </template>
      </Menu>
    </div>
    <div class="menuTool">
      <div class="menuExit" @click="exit">
        退出<Icon type="md-exit" size="20"/>
      </div>
    </div>
  </Menu>
</template>

<script>
import logo from '@/assets/images/logo.png'
import { createNamespacedHelpers } from 'vuex'
import SideMenuItem from './side-menu-item.vue'
import CollapsedMenu from './collapsed-menu.vue'
import mixin from './mixin'
const { mapActions } = createNamespacedHelpers('user')
export default {
  name: 'new-menu',
  mixins: [ mixin ],
  data () {
    return {
      logo,
      width: document.documentElement.clientWidth,
      openedNames: [],
      flag: false
    }
  },
  components: {
    SideMenuItem,
    CollapsedMenu
  },
  props: {
    menuList: {
      type: Array,
      default () {
        return []
      }
    },
    collapsed: {
      type: Boolean
    },
    theme: {
      type: String,
      default: 'dark'
    },
    rootIconSize: {
      type: Number,
      default: 20
    },
    iconSize: {
      type: Number,
      default: 16
    },
    accordion: Boolean,
    activeName: {
      type: String,
      default: ''
    },
    openNames: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    ...mapActions(['handleLogOut']),
    router (it) {
      if (it === this.activeName) return
      this.$router.push({
        name: it
      })
    },
    handleSelect (name) {
      this.flag = false
      this.$emit('on-select', name)
    },
    updateOpenName (name) {
      // if (name === this.$config.homeName) this.openedNames = []
      // else this.openedNames = this.getOpenedNamesByActiveName(name)
    },
    exit () {
      this.$emit("exit")
      this.handleLogOut().then(res => {
        this.$router.push({
          name: 'login'
        })
        // history.go(0)
      })
    }
  }
}
</script>

<style scoped lang="less">
  .menuWrapper {
    padding: 0 30px;
    .miniMenu {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
    }
    .layout-logo{
      .h(60px);
      float: left;
      margin: 10px 15px 10px 0;
      padding: 5px 20px 5px 10px;
      text-align: center;
      line-height: 50px;
      border-radius: 4px;
      background: #0F2940;
      label {
        text-align: center;
        line-height: 50px;
        height: 50px;
        display: inline-block;
        vertical-align: top;
        font-size: 16px;
        color: #ddd;
        margin-left: 5px;
      }
    }
    .layout-menu {
      .h(60px);
      .w(60px);
      margin: 10px 10px 10px -10px;
      text-align: center;
      float: left;
      line-height: 60px;
    }
    .menuTool {
      float: right;
      .menuExit {
        cursor: pointer;
        color: rgba(255, 255, 255, 0.7);
      }
    }
  }
</style>
<style lang="less">
.miniMenu {
  overflow-y: auto;
  overflow-x: hidden;
  background: #001529;
  .ivu-menu-dark {
    background: rgba(0, 21, 41, 1);
  }
  .ivu-menu {
    .ivu-menu-submenu {
      float: none;
      padding: 0;
    }
    .ivu-menu-item {
      float: none;
    }
  }
  .ivu-menu-horizontal {
    line-height: 40px;
  }
  .ivu-menu-vertical .ivu-menu-submenu-title, .ivu-menu-vertical .ivu-menu-item {
    padding: 0 24px;
  }
  .layout-close {
    line-height: 40px;
    text-align: right;
    padding: 10px 24px 0;
    .ivu-icon-md-close {
      padding: 10px
    }
  }
}
</style>
