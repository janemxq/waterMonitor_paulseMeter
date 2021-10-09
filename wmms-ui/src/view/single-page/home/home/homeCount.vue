<template>
  <Card>
    <ul class="homeCount">
      <li class="homeCountItem">
        <div class="CountTitle">
          用户总量
        </div>
        <div class="CountContent">
          <pie :value="pidData.userCount"></pie>
        </div>
      </li>
      <li class="homeCountItem">
        <div class="CountTitle">
          智能表总量
        </div>
        <div class="CountContent">
          <pie :value="pidData.meterCount"></pie>
        </div>
      </li>
      <li class="homeCountItem">
        <div class="CountTitle">
          抄表总量
        </div>
        <div class="CountContent">
          <pie :value="pidData.meterReaderCount"></pie>
        </div>
      </li>
      <li class="homeCountItem">
        <div class="CountTitle">
          缴费总次数
        </div>
        <div class="CountContent">
          <pie :value="pidData.paymentCount"></pie>
        </div>
      </li>
      <li class="homeCountItem">
        <div class="CountTitle">
          用水总量
        </div>
        <div class="CountContent">
          <pie :value="pidData.waterSum"></pie>
        </div>
      </li>
    </ul>
  </Card>
</template>

<script>
import pie from './component/pie'
export default {
  name: "homeCount",
  components: {
    pie
  },
  data () {
    return {
      api: this.$api.Home,
      pidData: {
        meterCount: 0, // 设备数量
        meterReaderCount: 0, // 抄表次数
        paymentCount: 0, // 支付次数
        userCount: 0, // 系统人数
        waterSum: 0, // 用水总数
      }
    }
  },
  mounted () {
    this.statisticsHome()
  },
  methods: {
    // 获取平台内用户数量
    statisticsHome () {
      this.api.statisticsHome().then(res => {
        Object.assign(this.pidData, res.data)
      })
    }
  }
}
</script>

<style scoped lang="less">
.homeCount{
  .w;
    height: 20vw;
    display: flex;
  .homeCountItem {
    flex: 1;
    text-align: center;
    .CountTitle{
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: justify;
      .w(100%);
      .h(20%);
      color: #333;
    }
    .CountContent {
      .w(80%);
      .h(80%);
      margin: 0 auto;
    }
  }
}
</style>
