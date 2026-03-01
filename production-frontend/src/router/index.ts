import { createRouter, createWebHistory } from "vue-router";
import ResourcesView from "../views/ResourcesView.vue";
import ProductsView from "../views/ProductsView.vue";
import ProductionView from "../views/ProductionView.vue";

const routes = [
  { path: "/", component: ResourcesView },
  { path: "/products", component: ProductsView },
  { path: "/production", component: ProductionView }
];

export default createRouter({
  history: createWebHistory(),
  routes
});