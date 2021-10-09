<!--
╔═════════════════ fileUpload组件说明 ═══════════════════╗
║## 参数                                                                                     ║
║  action        :[String]                   Post请求上传地址                                ║
║  accept        :[String]                   允许上传的类型(> IE9)                           ║
║  multiple      :[Boolean]                  是否允许多文件上传                              ║
║  extensions    :[Array | String | RegExp]  允许上传的文件后缀                              ║
║  size          :[Number]                   允许上传的最大字节 0 == 不限制                  ║
║  timeout       :[Number]                   上传超时时间毫秒 0 == 不限制(> IE9)             ║
║  maximum       :[Number]                   列表最大文件数                                  ║
║  thread        :[Number]                   同时并发上传的文件数量 线程数(> IE9)            ║
║  drop          :[Boolean]                  拖拽上传                                        ║
║  data          :[Object]                   上传附加参数                                    ║
║  showUploadList:[Boolean]                  是否显示上传的列表                              ║
║  beforeUpload  :[Function]                 上传前事件 return false 则停止上传              ║
║  onProgress    :[Function]                 上传进度事件                                    ║
║  onSuccess     :[Function]                 上传成功事件                                    ║
║  onError       :[Function]                 上传失败事件                                    ║
║## 方法                                                                                     ║
║  afterUpload   上传全部完成后事件                                                          ║
╚══════════════════════════════════════════════╝
-->
<template>
  <file-upload
    v-if="action"
    v-model="files"
    :post-action="action"
    :multiple="multiple"
    :accept="accept"
    :extensions="extensions"
    :size="size"
    :timeout="timeout"
    :maximum="maximum"
    :thread="thread"
    :drop="drop"
    :data="data"
    :headers="{authorization: 'Bearer 94a80df7-dfc3-48b4-acdb-ce590b04cfe2'}"
    ref="upload"
    @input-file="inputFile"
    @input="input"
  >
    <slot></slot>
  </file-upload>
</template>

<script>
import FileUpload from 'vue-upload-component'
export default {
  name: 'upload',
  components: {
    FileUpload
  },
  props: {
    /**
     * POST 请求的上传URL
     * */
    action: {
      type: String,
      defalut: ''
    },
    /**
     *  是否显示上传的列表
     * */
    'show-upload-list': {
      type: Boolean,
      default: false
    },
    /**
     * 允许上传的类型(> IE9)
     * */
    accept: {
      type: String,
      default: undefined
    },
    /**
     * 是否运行多文件上传
     * */
    multiple: {
      type: Boolean,
      default: false
    },
    /**
     * 允许上传的文件后缀
     * */
    extensions: {
      type: Array | String | RegExp,
      default: undefined
    },
    /**
     * 允许上传的最大字节 0 == 不限制
     * */
    size: {
      type: Number,
      default: 0
    },
    /**
     * 上传超时时间毫秒 0 == 不限制(> IE9)
     * */
    timeout: {
      type: Number,
      default: 0
    },
    /**
     * 列表最大文件数
     * */
    maximum: {
      type: Number,
      default: 0
    },
    /**
     * 同时并发上传的文件数量 线程数(> IE9)
     * */
    thread: {
      type: Number,
      default: 1
    },
    /**
     * 拖拽上传
     * */
    drop: {
      type: Boolean,
      default: false
    },
    /**
     * 上传附加参数
     * */
    data: {
      type: Object,
      default: null
    },
    /**
     * 上传前事件 return false 则停止上传
     * */
    beforeUpload: {
      type: Function,
      default: null
    },
    /**
     * 上传进度事件
     * */
    onProgress: {
      type: Function,
      default: null
    },
    /**
     * 上传成功事件
     * */
    onSuccess: {
      type: Function,
      default: null
    },
    /**
     * 上传失败事件
     * */
    onError: {
      type: Function,
      default: null
    }
  },
  data () {
    return {
      files: [],
      file: ''
    }
  },
  watch: {
    '$refs.upload.uploaded' () {
      this.$emit('afterUpload', this.files)
    }
  },
  methods: {
    input (newFile, oldFile) {
    },
    stop () { // 暂停上传
      this.$refs.upload.active = false
    },
    start () { // 开始上传
      this.$refs.upload.active = true
    },
    inputFile (newFile, oldFile) { // 添加，更新，移除后
      if (newFile && !oldFile) {
        // 添加文件
        if (this.beforeUpload && !this.beforeUpload(this.files[0], this.files)) { // 阻止自动上传事件
          this.stop()
          return false
        }
      }
      if (newFile && oldFile) {
        // 上传进度
        if (newFile.progress !== oldFile.progress) {
          this.$Notice.close(newFile.id)
          this.$Notice.open({
            title: '上传进度',
            name: newFile.id,
            duration: 0,
            render: (h) => {
              return h('div', [
                h('span', newFile.name),
                h('Progress', {
                  props: {
                    percent: newFile.progressData * 1
                  }
                })
              ])
            }
          })
          this.$set(newFile, 'progressData', newFile.progress)
          if (this.progress) this.onProgress(newFile.progress, newFile, this.files)
        }
        // 上传错误
        if (newFile.error !== oldFile.error) {
          if (this.onError) this.onError(newFile.error, newFile, this.files)
        }
        // 上传成功
        if (newFile.success !== oldFile.success) {
          if (this.onSuccess) this.onSuccess(newFile.response, newFile, this.files)
          setTimeout(m => {
            this.$Notice.close(newFile.id)
          }, 200)
        }
      }
      // 自动上传
      if (Boolean(newFile) !== Boolean(oldFile) || oldFile.error !== newFile.error) {
        if (!this.$refs.upload.active) {
          this.start()
        }
      }
    }
  }
}
</script>

<style>

</style>
