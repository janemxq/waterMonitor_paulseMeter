import Vue from 'vue'
import Vuex from 'vuex'
import { state, mutations, actions } from './common'

import user from './module/user'
import app from './module/app'

Vue.use(Vuex)

export default new Vuex.Store({
  state: Object.assign({
    //
  }, state),
  mutations: Object.assign({
    //
  }, mutations),
  actions: Object.assign({
    //
  }, actions),
  modules: {
    user,
    app
  }
})
