import api from "./axios";

export default {
  suggest() {
    return api.get("/production/suggest");
  },

  produce(productId, quantity) {
    return api.post(`/production/${productId}?quantity=${quantity}`);
  }
};