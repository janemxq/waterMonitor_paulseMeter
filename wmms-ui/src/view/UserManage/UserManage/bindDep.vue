<template>
  <Row>
    <Col span="9">
      <Card>
        <p slot="title">
          部门列表
        </p>
        <div class="boxWrapper">
          <Tree :data="deptList" @on-select-change="treeCheck"></Tree>
        </div>
      </Card>
    </Col>
    <Col span="14" offset="1">
      <Card>
        <p slot="title">
          职位选择
        </p>
        <div class="boxWrapper" >
          <div v-if="selectDept.length > 0">
            <Row class="boxContentTitle">
              <Col span="10">
                部门
              </Col>
              <Col span="10" offset="1">
                职位
              </Col>
              <Col span="2" offset="1">
                操作
              </Col>
            </Row>
            <Row v-for="(item, index) in selectDept" :key="item.deptId" class="boxContent">
              <Col span="10">
                <Input type="text" :value="item.deptName" disabled="disabled" />
              </Col>
              <Col span="10" offset="1">
                <Select v-model="item.roleId">
                  <Option v-for="it in ruleList" :value="it.id" :key="it.id">{{ it.name }} ({{it.remark}})</Option>
                </Select>
              </Col>
              <Col span="2" offset="1">
                <Button @click="selectDept.splice(index, 1)" type="error" title="删除">
                  <Icon type="md-trash" style="font-size: 16px;color: #fff"/>
                </Button>
              </Col>
            </Row>
          </div>
        </div>
      </Card>
    </Col>
  </Row>
</template>

<script>
export default {
  name: 'bindDep',
  data () {
    return {
      api: this.$api.UserManage,
      ruleList: [],
      deptList: [],
      selectDept: [],
      flag: true
    }
  },
  props: {
    show: Boolean,
    data: Object,
    modalParams: Object
  },
  watch: {
    show () {
      if (this.show) this.getData()
    }
  },
  mounted () {
    this.getData()
  },
  methods: {
    getData () {
      this.flag = true
      this.ruleList = []
      this.deptList = []
      this.selectDept = this.data.deptId ? this.data.deptId.split(',').map((m, i) => {
        return {
          deptName: this.data.deptName.split(',')[i],
          deptId: m,
          roleId: this.data.roleId.split(',')[i] * 1
        }
      }) : []
      Promise.all([this.api.treeDep(), this.api.getRole({ page: 1, size: 999 })]).then(res => {
        this.flag = false
        this.ruleList = res[1].data.records
        this.deptList = res[0].data.map(m => {
          return this.treeData(m)
        })
      }).catch(err => {
        console.log(err)
        this.$Message.error(err.message || '系统内部错误，暂无法使用，请联系管理员！')
        this.flag = false
      })
    },
    treeData (data) {
      const index = this.selectDept.findIndex(n => n.deptId === data.id + '')
      if (index !== -1) {
        this.selectDept[index].deptName = `${data.name} (${data.remark})`
      }
      return Object.assign(data, {
        title: `${data.name} (${data.remark})`,
        expand: true,
        disabled: data.isValid === 0,
        disableCheckbox: data.isValid === 0,
        children: data.sysDeptList.map(m => {
          return this.treeData(m)
        })
      })
    },
    treeCheck (data) {
      if (data && data.length > 0 && this.selectDept.findIndex(m => m.deptId === data[0].id) === -1) {
        this.selectDept.push({
          deptId: data[0].id,
          deptName: data[0].title,
          roleId: ''
        })
      }
    },
    handleSubmit () {
      return new Promise((resolve, reject) => {
        const index = this.selectDept.findIndex(m => m.roleId === '')
        if (index !== -1) {
          this.$Message.error(`${this.selectDept[index].deptName}的职位不允许为空`)
          reject(`${this.selectDept[index].deptName}的职位不允许为空`)
        } else {
          resolve({
            deptsRolesList: this.selectDept,
            userId: this.data.id
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.boxWrapper {
  height: 450px;
  overflow-x: hidden;
  overflow-y: auto;
}
.boxContentTitle{
  margin-bottom: 10px;
}
.boxContent {
  margin-bottom: 10px;
}
</style>
