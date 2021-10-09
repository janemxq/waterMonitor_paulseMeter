<template>
  <div class="home">
    <homeCount></homeCount>
    <statistics></statistics>
    <div class="abnormal">
      <Card>
        <div class="homeStatisticsHeader" slot="title">
          快捷缴费
        </div>
        <Pay :flag="false"></Pay>
      </Card>
    </div>
    <div class="abnormal">
      <Card>
        <div class="homeStatisticsHeader" slot="title">
          异常监控
        </div>
        <div class="abnormalWrapper">
          <FeeException style="width: 49%"></FeeException>
          <WaterCostException style="width: 49%"></WaterCostException>
        </div>
      </Card>
    </div>
    <Modal v-model="modalConfig.show"
           :loading="modalConfig.loadding"
           :title="`修改密码`"
           @on-ok="asyncOK">
      <Form ref="form" :model="formData" :rules="formRule" :label-width="100">
        <FormItem label="新密码" prop="newPassword">
          <Input v-model="formData.newPassword" placeholder="请输入新密码" clearable></Input>
        </FormItem>
        <FormItem label="确认密码"  prop="password">
          <Input v-model="formData.password" placeholder="请确认密码" clearable></Input>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import dayjs from 'dayjs'
import homeCount from "./home/homeCount"
import statistics from "./home/statistics"
import FeeException from "./home/FeeException"
import WaterCostException from "./home/WaterCostException"
import Pay from "@/view/Pays/Pay"
export default {
  name: 'home',
  components: {
    homeCount,
    statistics,
    FeeException,
    WaterCostException,
    Pay,
  },
  data () {
    return {
      userData: {
        name: ''
      },
      api: this.$api.Home,
      timeText: '',
      openOneGoodText: '',
      modalConfig: {
        show: false,
        loadding: true
      },
      formData: {
        newPassword: '',
        password: ''
      },
      formRule: {
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value === '') {
                callback(new Error('请输入新密码'))
              } else {
                if (!/^(\w){6,20}$/.test(value)) {
                  callback(new Error('密码为6~20位字母、数字或下划线组成'))
                }
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        password: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value === '') {
                callback(new Error('请确认密码'))
              } else {
                if (value !== this.formData.newPassword) {
                  callback(new Error('两次输入的密码不一致'))
                }
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      }
    }
  },
  mounted () {
    if (localStorage.getItem('userData')) Object.assign(this.userData, JSON.parse(localStorage.getItem('userData')))
    // this.openOneGood()
    // this.getTimeText()
    this.getSysUserUserInfo()
  },
  methods: {
    openOneGood () {
      this.api.openOneGood().then(res => {
        console.log(res.data)
        this.openOneGoodText = res.data.txt
        console.log(this.openOneGoodText)
      })
    },
    getTimeText () {
      let hours = Number(dayjs().format('HH'))
      if (hours > 22 || hours <= 4) {
        this.timeText = '凌晨了,注意休息'
      } else if (hours >= 5 && hours <= 11) {
        this.timeText = '上午好'
      } else if (hours >= 12 && hours < 18) {
        this.timeText = '下午好'
      } else {
        this.timeText = '晚上好'
      }
    },
    getSysUserUserInfo () {
      this.api.getSysUserUserInfo({
        name: localStorage.getItem('loginAccount')
      }).then(res => {
        console.log(res, '<<<<<<登陆次数')
        if (res.data.loginNumber <= 1) this.modalConfig.show = true
      })
    },
    asyncOK () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.api.postSysUserChangePwd({
            password: this.formData.newPassword
          }).then(res => {
            this.modalConfig.show = false
            this.modalConfig.loadding = false
            setTimeout(() => {
              this.modalConfig.loadding = true
            }, 300)
            this.$Message.success(`修改密码成功！`)
            console.log(res)
          }).catch(() => {
            this.$Message.error(`修改密码失败！`)
            this.modalConfig.loadding = false
            setTimeout(() => {
              this.modalConfig.loadding = true
            }, 300)
          })
        } else {
          this.modalConfig.loadding = false
          setTimeout(() => {
            this.modalConfig.loadding = true
          }, 300)
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .home{
    .w;
    .h;
    padding: 10px;
    position: absolute;
    background: #F9f9f9;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    overflow-x: hidden;
    overflow-y: auto;
    h1{
      margin-top: 50px;
    }
    .desc {
      position: absolute;
      text-align: right;
      padding: 50px;
      bottom: 0;
      right: 0;
      font-size: 18px;
    }
    .abnormal {
      margin-top: 10px;
      width: 100%;
      .abnormalWrapper {
        display: flex;
        justify-content: space-between;
        width: 100%;
      }
    }
  }
</style>
<style lang="less">
  .homeStatisticsHeader{
    padding: 5px 0;
    text-align: center;
    font-weight: 600;
    font-size: 24px;
  }
</style>
