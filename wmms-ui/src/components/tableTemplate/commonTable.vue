<!--
参数:
  modalParams: { // 弹出框数据
    show: false, // 弹窗开关
    okText: '', // 确认按钮文字
    width: 600, // 弹框宽度
    modalLoading: true, // 点击确定Loading状态
    type: 'add', // 弹出框类型。目前支持三种 add edit info
    title: '新增', // 弹出框头部
    name: '', // 新增按钮文字
    maskClosable: false // 点击弹框遮罩是否关闭
  }
  columns: [Array] 表格列的配置描述 参见 https://www.iviewui.com/components/table
  seek: [Array] 搜索项配置描述
    {
      span: [Number], // 元素宽度  参见24栅格
      title: [String], // 搜索项名字
      key: [String], // 搜索项对应数据请求字段
      type: [String], // 可选项 input(文本框) select(下拉框) seek(搜索框), 用来确定搜索类型
      data: [Array],
        { // 仅限 type === (select || seek) 所有，选项数据
          value: [String], // 发送请求对应项
          name: [String] // 展示给用户的项
        }
      remoteMethod: [Function], // 仅限type === seek所有，用来请求数据
      loading: [Boolean] // 仅限type === seek所有，等待数据请求返回结果时等待使用
    }
  selectFlag: Boolean 是否显示全选/取消权限按钮
  params: [Object] 请求列表数据时，初始化参数
  title: [String] 表格最外侧框题目  默认为 列表
  addModule: [Boolean] 是否需要展示新增
  dataCallBack: [Function] 数据请求，需要注意，需要在then中将 数据格式设置为 content 将总页数设置为 total
  handleSubmit: [Function Promise] 弹框点击确认事件的回调，必须使用Promise
  header: [string] 自定义头部对应 slot名
  footer: [string] 自定义底部对应 slot名
  selectChange: () => void 选中项发生变化触发
  onselect: () => void 选中某一项触发
  onselectCancel: () => void 取消选中某一项触发
  onSelectAllCancel: () => void 取消全选触发
  onSelectAll: () => void 全选触发

  方法:
  // 更新数据
  调用vuex中mutations的方法 setCount 便可以更新数据

  插槽:
  add
  edit
  info

-->
<template>
  <div>
    <Modal
      v-model="modalParams.show"
      :title="modalParams.title"
      :ok-text="modalParams.okText"
      :loading="modalParams.modalLoading"
      :width="modalParams.width"
      :mask-closable="modalParams.maskClosable"
      :styles="modalParams.styles"
      @on-ok="asyncOK">
      <div slot="header" v-if="header"><slot :name="header"></slot></div>
      <slot ref="content" :name="modalParams.type" :flag="modalParams.show" :type="'black'"></slot>
      <div slot="footer" v-if="footer"><slot :name="footer"></slot></div>
    </Modal>
    <tableTemplate ref="tableTemplate" :newParams="newParams" :selectFlag="selectFlag" :title="title"
                   :pageSize="pageSize" :seekCallBack="seekCallBack" :onExpand="onExpand"
                   :onselectCancel="onselectCancel" :onSelectAllCancel="onSelectAllCancel"
                   :onSelectAll="onSelectAll" :selectChange="selectChange" :titleFlag="titleFlag"
                   :columns="columns" :seek="seek" :params="params" :dataCallBack="dataCallBack"
                   :seekCount="seekCount">
      <slot v-if="titleSlot" slot="fun" name="fun"></slot>
      <a href="javascript:void(0)" @click="add()" slot="fun" v-if="addModule && !titleSlot">
        <Icon type="edit"></Icon>
        {{modalParams.name || "新增"}}
      </a>
    </tableTemplate>
  </div>
</template>

<script>
export default {
  name: 'commonTable',
  components: {
    tableTemplate: require('./index').default
  },
  props: {
    selectFlag: {
      type: Boolean,
      default: false
    },
    pageSize: {
      type: Number,
      default: 10
    },
    seekCount: {
      type: Number,
      default: 0
    },
    header: {
      type: String,
      default: null
    },
    footer: {
      type: String,
      default: null
    },
    params: { // 数据请求初始化参数
      type: Object
    },
    newParams: { // 数据变更参数
      type: Object
    },
    addModule: { // 是否需要新增模块
      type: Boolean,
      default: true
    },
    columns: { // 表格参数
      type: Array,
      default: () => []
    },
    seek: { // 搜索数据
      type: Array,
      default: () => []
    },
    dataCallBack: { // 列表数据回调
      type: Function,
      required: true
    },
    selectChange: { // 列表多选事件
      type: Function,
      default: () => {}
    },
    onselect: {
      type: Function,
      default: () => {
      }
    },
    onselectCancel: {
      type: Function,
      default: () => {}
    },
    onExpand: {
      type: Function,
      default: () => {}
    },
    onSelectAll: {
      type: Function,
      default: () => {}
    },
    onSelectAllCancel: {
      type: Function,
      default: () => {}
    },
    modalParams: { // 弹框信息
      type: Object,
      default: () => {
        return {
          width: 600,
          show: false,
          modalLoading: true,
          type: 'add',
          title: '新增',
          maskClosable: false
        }
      }
    },
    infoData: Object,
    handleSubmit: {
      type: Function,
      required: false
    },
    titleFlag: {
      type: Boolean,
      default: true
    },
    title: {
      type: String
    },
    titleSlot: {
      type: Boolean,
      default: false
    },
    seekCallBack: {
      type: Function,
      default: null
    }
  },
  methods: {
    add () { // 新增
      Object.assign(this.modalParams, {
        show: true,
        type: 'add',
        title: this.modalParams.name || '新增'
      })
      this.$emit('add')
    },
    asyncOK () { // 确定事件
      if (this.modalParams.type !== 'info') {
        if (this.handleSubmit) {
          this.handleSubmit().then(res => {
            this.modalParams.modalLoading = false
            this.modalParams.show = false
            setTimeout(m => {
              this.modalParams.modalLoading = true
            }, 100)
          }).catch(err => {
            console.log(err)
            this.modalParams.modalLoading = false
            setTimeout(m => {
              this.modalParams.modalLoading = true
            }, 100)
          })
        }
      } else {
        this.modalParams.modalLoading = false
        setTimeout(m => {
          this.modalParams.show = false
          this.modalParams.modalLoading = true
        }, 100)
      }
    }
  }
}
</script>

<style scoped>

</style>
<style>
.custom  .ivu-table-wrapper {
  overflow: initial;
}
</style>
