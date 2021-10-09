const signature = {
  computed: {
    $SIGNATURE () {
      return this.$children[0].$refs.SIGNATURE
    },
    $signatureImg () {
      return this.$children[0].$refs.SIGNATURE.signatureImg
    }
  },
  methods: {
    $open () {
      return this.$children[0].$refs.SIGNATURE.open()
    },
    $sndImg (base64) {
      return this.$children[0].$refs.SIGNATURE.sndImg(base64)
    },
    $hideWin () {
      return this.$children[0].$refs.SIGNATURE.hideWin()
    },
    $ready () {
      return this.$children[0].$refs.SIGNATURE.ready
    }
  }
}

export default signature
