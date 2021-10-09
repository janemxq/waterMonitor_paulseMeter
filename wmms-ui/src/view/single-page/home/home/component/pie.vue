<template>
  <div class="animatePie" ref="pie" @mouseenter="doing" @mouseout="unDoing"></div>
</template>

<script>
import echarts from 'echarts'
export default {
  name: "pie",
  data () {
    return {
      myChart: '',
      inter: ''
    }
  },
  watch: {
    value () {
      this.myChart.setOption(this.option)
    }
  },
  props: {
    value: {
      type: Number,
      default: 0
    }
  },
  computed: {
    option () {
      return {
        series: [
          {
            type: 'pie',
            zlevel: 1,
            silent: true,
            /*
            radius
            饼图的半径。可以为如下类型：
            number：直接指定外半径值。
            string：例如，'20%'，表示外半径为可视区尺寸（容器高宽中较小一项）的 20% 长度。
            Array.<number|string>：数组的第一项是内半径，第二项是外半径。每一项遵从上述 number string 的描述。
            */
            radius: ['99%', '97%'],
            hoverAnimation: false,
            color: "rgba(88,142,197,0.5)",
            // animation:false,    //charts3 no
            label: {
              normal: {
                show: false
              },
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [1]
          },
          {
            type: 'pie',
            zlevel: 2,
            silent: true,
            radius: ['90%', '93%'],
            startAngle: 50,
            hoverAnimation: false,
            // animation:false,    // charts3 no
            label: {
              normal: {
                show: false
              },
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: this.pie2()
          },
          {
            type: 'pie',
            zlevel: 3,
            silent: true,
            radius: ['89%', '86%'],
            label: {
              normal: {
                show: false
              },
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: this.pie2()
          },
          {
            type: 'pie',
            zlevel: 4,
            silent: true,
            radius: ['84%', '82%'],
            label: {
              normal: {
                show: false
              },
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: this.pie3()
          },
          {
            type: 'pie',
            zlevel: 5,
            silent: true,
            radius: ['80%', '78%'],
            color: ["#fc8d89", "#46d3f3", "rgba(203,203,203,.2)"],
            startAngle: 50,
            hoverAnimation: false,
            // animation:false,    //charts3 no
            label: {
              normal: {
                show: false
              },
            },
            data: [50, 20, 40]
          },
          {
            name: "",
            type: 'gauge',
            splitNumber: 30, // 刻度数量
            min: 0,
            max: 100,
            radius: '73%', // 图表尺寸
            center: ['50%', '50%'],
            startAngle: 90,
            endAngle: -269.9999,
            axisLine: {
              show: false,
              lineStyle: {
                width: 0,
                shadowBlur: 0,
                color: [
                  [1, '#0dc2fe']
                ]
              }
            },
            axisTick: {
              show: false,
              lineStyle: {
                color: 'auto',
                width: 2
              },
              length: 20,
              splitNumber: 5
            },
            splitLine: {
              show: true,
              length: 32,
              lineStyle: {
                color: 'auto',
              }
            },
            axisLabel: {
              show: false
            },
            pointer: { // 仪表盘指针
              show: 0,
            },
            detail: {
              show: 0,
            },
          },
          {
            name: '统计',
            type: 'gauge',
            splitNumber: 30, // 刻度数量
            min: 0,
            max: 100,
            radius: '68%', // 图表尺寸
            center: ['50%', '50%'],
            startAngle: 90,
            endAngle: -269.9999,
            axisLine: {
              show: true,
              lineStyle: {
                width: 0,
                shadowBlur: 0,
                color: [
                  [0, '#0dc2fe'],
                  [1, '#0dc2fe']
                ]
              }
            },
            axisTick: {
              show: true,
              lineStyle: {
                color: '#0dc2fe',
                width: 2
              },
              length: 20,
              splitNumber: 5
            },
            splitLine: {
              show: false,
              length: 20,
              lineStyle: {
                color: '#0dc2fe',
              }
            },
            axisLabel: {
              show: false
            },
            pointer: { // 仪表盘指针
              show: 0,
            },
            detail: {
              borderColor: '#333',
              shadowColor: '#333', // 默认透明
              shadowBlur: 2,
              offsetCenter: [0, '0%'], // x, y，单位px
              textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                color: '#333',
                fontSize: 25,
              },
              formatter: '{value}'
            },
            data: [{
              name: "",
              value: this.value
            }]
          },
          // {
          //   type: 'pie',
          //   zlevel: 20,
          //   silent: true,
          //   radius: ['60%', '59%'],
          //   hoverAnimation: false,
          //   color: '#2dc0c9',
          //   // animation:false,
          //   data: [1],
          //   labelLine: {
          //     normal: {
          //       show: false
          //     }
          //   }
          // },
        ]
      }
    }
  },
  mounted () {
    this.myChart = echarts.init(this.$refs.pie)
    this.myChart.setOption(this.option)
  },
  methods: {
    pie2 () {
      let dataArr = []
      for (let i = 0; i < 8; i++) {
        if (i % 2 === 0) {
          dataArr.push({
            name: (i + 1).toString(),
            value: 25,
            itemStyle: {
              normal: {
                color: "rgba(88,142,197,0.5)",
                borderWidth: 0,
                borderColor: "rgba(0,0,0,0)"
              }
            }
          })
        } else {
          dataArr.push({
            name: (i + 1).toString(),
            value: 20,
            itemStyle: {
              normal: {
                color: "rgba(0,0,0,0)",
                borderWidth: 0,
                borderColor: "rgba(0,0,0,0)"
              }
            }
          })
        }
      }
      return dataArr
    },
    pie3 () {
      let dataArr = []
      for (let i = 0; i < 100; i++) {
        if (i % 2 === 0) {
          dataArr.push({
            name: (i + 1).toString(),
            value: 25,
            itemStyle: {
              normal: {
                color: "rgb(126,190,255)",
                borderWidth: 0,
                borderColor: "rgba(0,0,0,0)"
              }
            }
          })
        } else {
          dataArr.push({
            name: (i + 1).toString(),
            value: 20,
            itemStyle: {
              normal: {
                color: "rgba(0,0,0,0)",
                borderWidth: 0,
                borderColor: "rgba(0,0,0,0)"
              }
            }
          })
        }
      }
      return dataArr
    },
    doing () {
      this.inter = setInterval(() => {
        let option = this.myChart.getOption()
        option.series[1].startAngle = option.series[1].startAngle + 5
        option.series[2].startAngle = option.series[2].startAngle - 5
        option.series[3].startAngle = option.series[3].startAngle + 5
        option.series[4].startAngle = option.series[4].startAngle - 5
        // option.series[5].startAngle = option.series[5].startAngle + 5
        this.myChart.setOption(option)
      }, 200)
    },
    unDoing () {
      clearInterval(this.inter)
    }
  }
}
</script>
<style scoped lang="less">
  .animatePie {
    width: 100%;
    height: 100%;
  }
</style>
