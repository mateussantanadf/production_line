import api from "./axios";

export default {
  getAll() {
    return api.get("/resources");
  },

  getById(code) {
    return api.get(`/resources/${code}`);
  },

  create(data) {
    return api.post("/resources", data);
  },

  update(code, data) {
    return api.put(`/resources/${code}`, data);
  }
};