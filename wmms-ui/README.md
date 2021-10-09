#iview-admin兼容ie9版

## 删除功能如下

> 1、 删除国际化

> 2、 删除组织结构图

> 3、 删除树状表格

> 4、 删除Markdown编辑器

> 5、 删除富文本编辑器（wangedit bug还是很多的）

## 新增功能

> 1、 新增 `this.$isIe` 用来判断是否为ie

```
// ie9 xxx.vue
console.log(this.$isIe) // ie 9
```

> 2、 新增 `this.$cookies` 用来操作cookie, 查看[js-cookie](https://github.com/js-cookie/js-cookie)

```
// xxx.vue
this.$cookies.get('xxx')
```

> 3、 新增 `this.$env` 用来判断是否为生产环境

```
// xxx.vue development
console.log(this.$env) // true
```

> 4、 新增 `this.$indexOf` 用来输出data属性

```
// xxx.vue
console.log(this.$indexOf([])) // array
```

> 5、 新增 dom循环事件

```
document.getElementsByTagName('a').forEach(m => {
  console.log(m)
})
```

> 6、 新增 session 事件 用来存储sessionStorage

```
// 新增
this.$session.set(key, value)
// 查询
this.$session.get(key)
// 删除
this.$session.delete(key)
// 全部删除
this.$session.clear()
```

> 7、 增加config配置项

```
// mian.js
Vue.use(globalTool, {
  cookies: true, // 是否启用cookies功能
  indexOf: true, // 是否启用indexOf功能
  each: 'forEach', // dom元素循环名 及`document.getElementsByTagName('a').forEach`中的`forEach`
  prefix: '$' // 调用时的前缀名及`this.$cookies`中的`$`
})
```

> 8、 新增跳转时是不需要登录 tab导航栏是否显示

  > 用法

  ```
  // src/config/iview.js
  tab: false, // tab导航栏是否显示
  jumpList: ['routerName'] // 写入不需要跳转的router的name值
  ```

> 9、 新增在win7 ie9下css页面加载不全的问题，在打包后，会将css切分为不大于300kb的文件，用于解决ie9页面错乱的问题，开发环境下，请使用ie11模拟ie9来测试

> 10、 新增全局less文件引入,目录如下：

`/src/assets/less/index.less`
[less学习](http://lesscss.cn/)

> 11、 增加rule方法引入，规则添加 /src/plugin/tool/js/rule.js 使用方法如下:

```
xxx.vue
ruleValidate: {
  [NAME]: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  [IDCARD]: [
    {
      validator: this.$rule.validateIdCard, trigger: 'blur'
    }
  ],
  [PHONE]: [
    {
      validator: this.$rule.validatePhone, trigger: 'blur'
    }
  ]
}
```

## 新增模板
> 1、 table表格组合

例子:
```
<template>
  <tableTemplate :columns="columns" :seek="seek" :params="params" :dataCallBack="dataCallBack">
    <a href="javascript:void(0)" @click="add()" slot="fun">
      <Icon type="edit"></Icon>
      新增
    </a>
  </tableTemplate>
</template>
<script>
export default {
  components: {
    tableTemplate: require('_c/tableTemplate').default
  },
  data () {
    return {
      params:{
        size: 10,
        page: 0
      },
      seek: [{
         type: 'input',
         key: 'id',
         title: 'id'
       }, {
         type: 'input',
         key: 'label',
         title: '项目组'
       }],
      columns: [{
        title: 'Name',
        key: 'name'
      },
      {
        title: 'Age',
        key: 'age'
      },
      {
        title: 'Address',
        key: 'address'
      }],
      api: this.$api.template
    }
  }
  methods: {
    add () { // 新增
      console.log('点击新增事件')
    },
    dataCallBack (params) {
      this.api.get(params).then(res => {
        return {
          content: res.data.content,
          total: res.data.totalElements
        }
      })
    }
  }
}
</script>
/**
参数:
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
 params: [Object] 请求列表数据时，初始化参数
 title: [String] 表格最外侧框题目  默认为 列表
 dataCallBack: [Function] 数据请求，需要注意，需要在then中将 数据格式设置为 content 将总页数设置为 total

 方法:
 // 更新数据
 调用vuex中mutations的方法 setCount 便可以更新数据

 插槽:
 开放一个插槽 fun  位于表格上方 及表格title 所在位置，用来添加新增等方法
*/
```
