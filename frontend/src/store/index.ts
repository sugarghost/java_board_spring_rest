import { createStore } from "vuex";

export default createStore({
  state: {
    searchParams: {
      categoryId: null,
      startDate: null,
      endDate: null,
      searchWord: null,
      articlePerPage: 5,
      pageNum: 1,
    },
  },
  mutations: {
    updateSearchParams(state, payload) {
      state.searchParams = payload;
    },
  },
  actions: {
    setSearchParams({ commit }, payload) {
      commit("updateSearchParams", payload);
    },
  },
  getters: {
    searchParams: (state) => state.searchParams,
  },
  modules: {},
});
