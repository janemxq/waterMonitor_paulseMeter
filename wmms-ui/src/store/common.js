export const state = {
  count: 0,
  loading: false
}

export const mutations = {
  setCount (state) {
    state.count++
  },
  showLoading (state) {
    state.loading = true
  },
  hideLoading (state) {
    state.loading = false
  }
}

export const actions = {

}
