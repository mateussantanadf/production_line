import api from "./axios";

export default {
  getAll() {
    return api.get("/products");
  },

  getById(code) {
    return api.get(`/products/${code}`);
  },

  create(data) {
    return api.post("/products", data);
  },

  update(code, data) {
    return api.put(`/products/${code}`, data);
  }
};