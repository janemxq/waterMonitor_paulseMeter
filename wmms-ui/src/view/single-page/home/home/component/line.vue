<template>
  <div class="line" ref="line"></div>
</template>

<script>
import echarts from "echarts"
export default {
  name: "lines",
  mounted () {
    this.myChart = echarts.init(this.$refs.line)
    this.myChart.setOption(this.option)
  },
  props: {
    lineData: {
      type: Object,
      require: true
    }
  },
  watch: {
    lineData: {
      deep: true,
      handler () {
        this.myChart.setOption(this.option)
      }
    }
  },
  data () {
    return {
      myChart: ''
    }
  },
  computed: {
    option () {
      return {
        title: {
          left: '12%',
          top: '13%',
          textStyle: {
            color: '#333',
            fontSize: 14
          }
        },
        legend: {
          show: true,
          icon: 'circle',
          top: '13%',
          itemWidth: 6,
          itemHeight: 6,
          itemGap: 25,
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: [{
          type: 'category',
          data: this.lineData.y,
          axisLine: {
            lineStyle: {
              color: '#ddd'
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            interval: 0,
            textStyle: {
              color: '#333'
            },
            margin: 15
          },
          boundaryGap: false
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#ddd'
            }
          },
          axisLabel: {
            show: false,
            textStyle: {
              color: '#c56790'
            }
          },
          splitLine: {
            show: false
          }
        }],
        series: [{
          name: '用水量',
          type: 'line',
          data: this.lineData.water,
          symbolSize: 6,
          symbol: 'circle',
          smooth: true,
          lineStyle: {
            color: '#fe9a8b'
          },
          itemStyle: {
            normal: {
              color: '#fe9a8b',
              borderColor: '#fe9a8b'
            }
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
              [
                {
                  offset: 0,
                  color: '#fe9a8bb3'
                },
                {
                  offset: 1,
                  color: '#fe9a8b03'
                }
              ]
            )
          },
          emphasis: {
            itemStyle: {
              color: {
                type: 'radial',
                x: 0.5,
                y: 0.5,
                r: 0.5,
                colorStops: [
                  {
                    offset: 0,
                    color: '#fe9a8b'
                  },
                  {
                    offset: 0.4,
                    color: '#fe9a8b'
                  },
                  {
                    offset: 0.5,
                    color: '#fff'
                  },
                  {
                    offset: 0.7,
                    color: '#fff'
                  },
                  {
                    offset: 0.8,
                    color: '#fff'
                  },
                  {
                    offset: 1,
                    color: '#fff'
                  }
                ]
              },
              borderColor: '#fe9a8b',
              borderWidth: 2
            }
          }
        },
        {
          name: '缴费金额',
          type: 'line',
          data: this.lineData.money,
          symbolSize: 6,
          symbol: 'circle',
          smooth: true,
          lineStyle: {
            color: '#9E87FF'
          },
          itemStyle: {
            normal: {
              color: '#9E87FF',
              borderColor: '#9E87FF'
            }
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
              [
                {
                  offset: 0,
                  color: '#9E87FFb3'
                },
                {
                  offset: 1,
                  color: '#9E87FF03'
                }
              ]
            )
          },
          emphasis: {
            itemStyle: {
              color: {
                type: 'radial',
                x: 0.5,
                y: 0.5,
                r: 0.5,
                colorStops: [
                  {
                    offset: 0,
                    color: '#9E87FF'
                  },
                  {
                    offset: 0.4,
                    color: '#9E87FF'
                  },
                  {
                    offset: 0.5,
                    color: '#fff'
                  },
                  {
                    offset: 0.7,
                    color: '#fff'
                  },
                  {
                    offset: 0.8,
                    color: '#fff'
                  },
                  {
                    offset: 1,
                    color: '#fff'
                  }
                ]
              },
              borderColor: '#9E87FF',
              borderWidth: 2
            }
          }
        }
        ]
      }
    }
  }
}
</script>

<style scoped lang="less">
.line{
  .w;
  .h;
}
</style>
