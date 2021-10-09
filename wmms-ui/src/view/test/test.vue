<template>
  <div style="display: flex;flex-wrap: wrap;">
    <Card style="width: 350px; margin: 25px;">
      <p slot="title">
        配置终端设备地址码
      </p>
      <div>
        <span>目的地址(十六进制，例如：0101)</span>
        <Input type="text" v-model="mac.dAddr"/>
      </div>
      <div>
        <span>新地址(十六进制，例如：0101)</span>
        <Input type="text" v-model="mac.nAddr"/>
      </div>
      <Button long type="success" @click="testConfigAddr">配置</Button>
      <div v-if="value.mac">
        <p>返回值</p>
        <div style="word-wrap:break-word"> {{value.mac}}</div>
      </div>
    </Card>

    <Card style="width: 350px; margin: 25px;">
      <p slot="title">
        配置当前码盘的码值
      </p>
      <div>
        <span>目的地址(十六进制，例如：0101)</span>
        <Input type="text" v-model="code.dAddr"/>
      </div>
      <div>
        <span>通道(十六进制，例如：F)</span>
        <Input type="text" v-model="code.index"/>
      </div>
      <div>
        <span>码盘值(十进制，例如：1000)</span>
        <Input type="text" v-model="code.codeVal"/>
      </div>
      <Button long type="success" @click="testConfigCodeVal">配置</Button>
      <div v-if="value.code">
        <p>返回值</p>
        <div  style="word-wrap:break-word"> {{value.code}}</div>
      </div>
    </Card>

    <Card style="width: 350px; margin: 25px;">
      <p slot="title">
        配置脉冲
      </p>
      <div>
        <span>目的地址(十六进制，例如：0101)</span>
        <Input type="text" v-model="pulse.dAddr "/>
      </div>
      <div>
        <span>通道(十六进制，例如：F)</span>
        <Input type="text" v-model="pulse.index "/>
      </div>
      <div>
        <span>脉冲(十进制，例如：1000)</span>
        <Input type="text" v-model="pulse.pulse"/>
      </div>
      <Button long type="success" @click="testConfigPulse">配置</Button>
      <div v-if="value.pulse">
        <p>返回值</p>
        <div style="word-wrap:break-word"> {{value.pulse}}</div>
      </div>
    </Card>

    <Card style="width: 350px; margin: 25px;">
      <p slot="title">
        配置累计用水量
      </p>
      <div>
        <span>目的地址(十六进制，例如：0101)</span>
        <Input type="text" v-model="watch.dAddr"/>
      </div>
      <div>
        <span>通道(十六进制，例如：F)</span>
        <Input type="text" v-model="watch.index "/>
      </div>
      <div>
        <span>累计用水量(十进制，例如：100)</span>
        <Input type="text" v-model="watch.val"/>
      </div>
      <Button long type="success" @click="testConfigVal">配置</Button>
      <div v-if="value.watch">
        <p>返回值</p>
        <div style="word-wrap:break-word"> {{value.watch}}</div>
      </div>
    </Card>

    <Card style="width: 350px; margin: 25px;">
      <p slot="title">
        读取终端设备数据
      </p>
      <div>
        <span>目的地址(十六进制，例如：0101)</span>
        <Input type="text" v-model="read.dAddr"/>
      </div>
      <div>
        <span>通道(十六进制，例如：F)</span>
        <Input type="text" v-model="read.index "/>
      </div>
      <Button long type="success" @click="testReadSingle">读取</Button>
      <div v-if="value.read">
        <p>返回值</p>
        <div style="word-wrap:break-word" v-html="value.read"></div>
      </div>
    </Card>

  </div>
</template>

<script>
export default {
  name: "test",
  data () {
    return {
      api: this.$api.test,
      mac: {
        dAddr: '', // 目的地址(十六进制，例如：0101)
        nAddr: '', // 新地址(十六进制，例如：0101)
      },
      code: {
        codeVal: '', // 码盘值(十进制，例如：1000)
        dAddr: '', // 目的地址(十六进制，例如：0101)
        index: '', // 通道(十六进制，例如：F)
      },
      pulse: {
        dAddr: '', // 目的地址(十六进制，例如：0101)
        index: '', // 通道(十六进制，例如：F)
        pulse: '', // 脉冲(十进制，例如：1000)
      },
      watch: {
        dAddr: '', // 目的地址(十六进制，例如：0101)
        index: '', // 通道(十六进制，例如：F)
        val: '', // 累计用水量(十进制，例如：100)
      },
      read: {
        dAddr: '', // 目的地址(十六进制，例如：0101)
        index: '', // 通道(十六进制，例如：F)
      },
      value: {
        mac: '',
        code: '',
        pulse: '',
        watch: '',
        read: ''
      }
    }
  },
  methods: {
    testConfigAddr () {
      this.api.testConfigAddr(this.mac).then(res => {
        this.value.mac = res.data.toString()
      }).catch(err => {
        this.value.mac = JSON.stringify(err.response.data)
      })
    },
    testConfigCodeVal () {
      this.api.testConfigCodeVal(this.code).then(res => {
        this.value.code = res.data.toString()
      }).catch(err => {
        this.value.code = JSON.stringify(err.response.data)
      })
    },
    testConfigPulse () {
      this.api.testConfigPulse(this.pulse).then(res => {
        this.value.pulse = res.data.toString()
      }).catch(err => {
        this.value.pulse = JSON.stringify(err.response.data)
      })
    },
    testConfigVal () {
      this.api.testConfigVal(this.watch).then(res => {
        this.value.watch = res.data.toString()
      }).catch(err => {
        this.value.watch = JSON.stringify(err.response.data)
      })
    },
    testReadSingle () {
      this.api.testReadSingle(this.read).then(res => {
        this.value.read = `<table>
            <tr style="border: 1px solid #ccc;"><td style="border: 1px solid #ccc;">通道</td><td style="border: 1px solid #ccc;">${res.data.index}</td></tr>
            <tr style="border: 1px solid #ccc;"><td style="border: 1px solid #ccc;">累计用水量</td><td style="border: 1px solid #ccc;">${res.data.val}</td></tr>
            <tr style="border: 1px solid #ccc;"><td style="border: 1px solid #ccc;">码盘值</td><td style="border: 1px solid #ccc;">${res.data.codeVal}</td></tr>
            <tr style="border: 1px solid #ccc;"><td style="border: 1px solid #ccc;">脉冲</td><td style="border: 1px solid #ccc;">${res.data.pulse}</td></tr>
          </table>`
      }).catch(err => {
        this.value.read = JSON.stringify(err.response.data)
      })
    },
  }
}
</script>

<style scoped>

</style>
