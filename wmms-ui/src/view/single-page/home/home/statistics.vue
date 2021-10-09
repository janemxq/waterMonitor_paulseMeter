<template>
  <Card style="margin-top: 10px;">
    <div class="homeStatisticsHeader" slot="title">
      用水缴费一览图
    </div>
    <div class="homeStatistics">
      <ul class="homeStatisticsTitle">
        <li class="homeStatisticsItem">
        <span>
          查询方式
        </span>
          <RadioGroup v-model="params.timeType" type="button" size="small">
            <Radio label="0">月</Radio>
            <Radio label="1">季度</Radio>
          </RadioGroup>
        </li>
        <li class="homeStatisticsItem">
        <span>
          查询时间
        </span>
          <DatePicker v-model="params.year" type="year" placeholder="Select year" style="width: 150px" size="small"></DatePicker>
        </li>
        <li class="homeStatisticsItem">
          <span>村庄</span>
          <Select
            v-model="params.villageSn"
            style="width: 150px"
            size="small"
          >
            <Option v-for="(option, index) in hamletList" :value="option.sn" :key="index">{{option.villageName}}</Option>
          </Select>
        </li>
        <li class="homeStatisticsItem">
          <span>用户</span>
          <Select
            v-model="params.userSn"
            filterable
            remote
            :remote-method="remoteMethod"
            :loading="userLoading"
            style="width: 150px"
            size="small"
            clearable
          >
            <Option v-for="(option, index) in userList" :value="option.sn" :key="index">{{option.name}}</Option>
          </Select>
        </li>
      </ul>
      <div class="homeStatisticsContent">
        <lines :lineData="lineData"></lines>
      </div>
    </div>
  </Card>
</template>

<script>
import lines from "./component/line"
import dayjs from "dayjs"
export default {
  name: "statistics",
  components: {
    lines
  },
  data () {
    return {
      params: {
        timeType: '0', // 0 按月 1 按季度
        year: dayjs().format('YYYY'), // 查询年
        userSn: '',
        villageSn: ''
      },
      userList: [],
      hamletList: [],
      userLoading: false,
      hamletLoading: false,
      api: this.$api.Home,
      lineData: {
        water: [],
        money: [],
        y: []
      }
    }
  },
  watch: {
    params: {
      deep: true,
      handler () {
        if (this.params.timeType === '0') {
          this.getMonthData()
        } else {
          this.getSeasonData()
        }
      }
    }
  },
  mounted () {
    this.getMonthData()
    this.hamletRemoteMethod()
  },
  methods: {
    hamletRemoteMethod () {
      this.hamletList = []
      this.api.getWsVillageAll().then(res => {
        console.log(res)
        this.hamletList = res.data
      })
    },
    remoteMethod (value) {
      if (!value) {
        this.userList = []
        return
      }
      this.userLoading = true
      this.api.getSysUser({
        current: 1,
        name: value,
        size: 99
      }).then(res => {
        this.userLoading = false
        this.userList = res.data.records
      }).catch(() => {
        this.userLoading = false
      })
    },
    getMonthData () {
      let params = Object.assign(this.params, {
        year: dayjs(this.params.year).format('YYYY')
      })
      return Promise.all([this.api.getWaterSumByMonth(params), this.api.getWaterMoneyByMonth(params)]).then(res => {
        Object.assign(this.lineData, {
          water: [],
          money: [],
          y: []
        })
        res[1].data.map(m => {
          this.lineData.y.push(m.date)
          this.lineData.water.push(m.num)
        })
        res[0].data.map(m => {
          this.lineData.money.push(m.num)
        })
        return Promise.resolve(res)
      })
    },
    getSeasonData () {
      let params = Object.assign(this.params, {
        year: dayjs(this.params.year).format('YYYY')
      })
      return Promise.all([this.api.getWaterSumBySeason(params), this.api.getWaterMoneyBySeason(params)]).then(res => {
        Object.assign(this.lineData, {
          water: [],
          money: [],
          y: []
        })
        res[0].data.map(m => {
          this.lineData.y.push(m.date)
          this.lineData.water.push(m.num)
        })
        res[1].data.map(m => {
          this.lineData.money.push(m.num)
        })
        return Promise.resolve(res)
      })
    }
  }
}
</script>

<style scoped lang="less">
.homeStatistics {
  .w;
  margin-top: 3%;
  .h(40vh);
  display: flex;
  flex-direction: column;
  .homeStatisticsTitle {
    .h (40px);
    display: flex;
    padding-left: 40px;
    .homeStatisticsItem {
      display: block;
      >span {
        margin: 0 10px 0 20px;
        color: #333;
      }
    }
  }
  .homeStatisticsContent{
    flex: 1;
  }
}
</style>
<style lang="less">
  .homeStatisticsTitle {
    .homeStatisticsItem {

    }
  }
</style>
