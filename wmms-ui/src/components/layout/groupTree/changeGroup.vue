<template>
  <Form ref="form" :model="formValidate" :rules="ruleValidate" :label-width="90" style="min-height: 300px">
    <FormItem label="表井名称" prop="groupName">
      <Input v-model="formValidate.groupName" placeholder="请输入表井名称" clearable></Input>
    </FormItem>
    <FormItem label="表井编号" prop="sn">
      <Input v-model="formValidate.sn" placeholder="请输入表井编号(采集器出厂编号，例如BJ00001)" clearable :disabled="modalParams.type === 'editWsGroup'"></Input>
    </FormItem>
    <FormItem label="设备地址" prop="address">
      <Input v-model="formValidate.address" placeholder="请输入设备地址(4位16进制，例如ff01)" clearable></Input>
    </FormItem>
  </Form>
</template>

<script>
// <!--    <FormItem label="村庄" prop="villageSn">-->
//   <!--      <Select v-model="formValidate.villageSn" @on-change="onChange">-->
//   <!--        <Option v-for="item in VillageAll" :value="item.sn" :key="item.sn">{{ item.villageName }}</Option>-->
// <!--      </Select>-->
// <!--    </FormItem>-->
export default {
  name: 'add',
  props: {
    show: Boolean,
    data: Object,
    modalParams: Object,
    infoParams: Object
  },
  data () {
    return {
      formValidate: {
        sn: '',
        groupName: '',
        villageSn: '',
        villageName: '',
        villageId: '',
        id: '',
        address: ''
      },
      VillageAll: [],
      api: this.$api.System,
      ruleValidate: {
        groupName: [
          { required: true, message: '请输入表井名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleLengthMin(rule, value, callback, [3, 20])
            },
            trigger: 'blur'
          }
        ],
        sn: [
          { required: true, message: '请输入表井编号', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              this.$rule.ruleNumAndLetMin(rule, value, callback, [2, 80])
            },
            trigger: 'blur'
          }
        ],
        address: [
          { required: true, message: '请输入设备地址', trigger: 'change' },
          {
            validator: (rule, value, callback) => {
              if (/^([0-9]|[a-f]|[A-F]){4}$/.test(value) || value === '') {
                callback()
              } else {
                callback('设备地址格式为4位十六进制数值')
              }
            },
            trigger: 'blur'
          }
        ],
      }
    }
  },
  watch: {
    show () {
      this.init()
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    init () {
      if (this.show) {
        // this.getVillageAll()
        if (this.data) {
          Object.keys(this.formValidate).map(m => {
            this.formValidate[m] = this.data[m]
          })
        }
        if (this.infoParams) {
          Object.assign(this.formValidate, {
            villageName: this.infoParams.villageName,
            villageId: this.infoParams.villageId ? this.infoParams.villageId : this.infoParams.id,
            villageSn: this.infoParams.villageSn ? this.infoParams.villageSn : this.infoParams.sn,
          })
        }
      } else {
        this.handleReset()
      }
    },
    getVillageAll () {
      this.api.getVillageAll().then(res => {
        this.VillageAll = res.data
      })
    },
    handleSubmit () {
      return new Promise((resolve, reject) => {
        this.$refs['form'].validate((valid) => {
          console.log(valid)
          if (valid) {
            console.log(Object.assign({}, this.modalParams.type === 'edit' && this.data, this.modalParams.type === 'addGroup' && {
              createUserId: localStorage.getItem('userId')
            }, this.formValidate, this.modalParams.type === 'edit' && {
              updateUserId: localStorage.getItem('userId')
            }))
            resolve(Object.assign({}, this.modalParams.type === 'edit' && this.data, this.modalParams.type === 'addGroup' && {
              createUserId: localStorage.getItem('userId')
            }, this.formValidate, this.modalParams.type === 'edit' && {
              updateUserId: localStorage.getItem('userId')
            }))
          } else {
            reject('填写内容有误，请查验')
          }
        })
      })
    },
    handleReset () { // 刷新事件
      if (this.$refs['form']) this.$refs['form'].resetFields()
    },
    onChange (id) { // 选择村庄
      const index = this.VillageAll.findIndex(m => m.sn === id)
      if (index !== -1) {
        this.formValidate.villageName = this.VillageAll[index].villageName
        this.formValidate.villageId = this.VillageAll[index].id
      }
    }
  }
}
</script>

<style scoped>

</style>
