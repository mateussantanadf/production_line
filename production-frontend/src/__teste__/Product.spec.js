import { mount, flushPromises } from "@vue/test-utils";
import { describe, it, expect, vi, beforeEach } from "vitest";
import Product from "../views/ProductsView.vue";

vi.mock("../api/productService", () => ({
  default: {
    getAll: vi.fn(),
    create: vi.fn(),
    update: vi.fn()
  }
}));

vi.mock("../api/resourceService", () => ({
  default: {
    getAll: vi.fn()
  }
}));

import productService from "../api/productService";
import resourceService from "../api/resourceService";

describe("Product Component", () => {

  beforeAll(() => {
    window.scrollTo = vi.fn();
  });

  beforeEach(() => {
    productService.getAll.mockResolvedValue({
      data: [
        { code: 1, name: "Product 1", price: 10, compositions: [] }
      ]
    });

    resourceService.getAll.mockResolvedValue({
      data: [
        { code: 100, name: "Resource 1", stock: 10 }
      ]
    });
  });

  it("loads products on mount", async () => {
    const wrapper = mount(Product);

    await new Promise(process.nextTick);

    expect(productService.getAll).toHaveBeenCalled();
    expect(wrapper.text()).toContain("Product 1");
  });
});

it("does not create product without name", async () => {
  window.alert = vi.fn();

  const wrapper = mount(Product);
  await flushPromises();

  await wrapper.find('input[placeholder="Price"]').setValue(10);

  const addButton = wrapper.findAll("button").find(btn =>
    btn.text().includes("Add Product")
  );

  await addButton.trigger("click");

  expect(window.alert).toHaveBeenCalledWith("Fill in all fields.");
});

it("creates product successfully", async () => {
  window.alert = vi.fn();
  productService.create.mockResolvedValue({});

  const wrapper = mount(Product);
  await flushPromises();

  await wrapper.find('input[placeholder="Product Name"]').setValue("Test");
  await wrapper.find('input[placeholder="Price"]').setValue(50);

  const checkbox = wrapper.find('input[type="checkbox"]');
  await checkbox.setValue(true);

  await wrapper.vm.$nextTick();

  const quantityInput = wrapper.find('.quantity-input');
  await quantityInput.setValue(2);

  const addButton = wrapper.findAll("button").find(btn =>
    btn.text().includes("Add Product")
  );

  await addButton.trigger("click");
  await flushPromises();

  expect(productService.create).toHaveBeenCalledTimes(1);
});

it("enters edit mode when clicking edit", async () => {
  const wrapper = mount(Product);
  await new Promise(process.nextTick);

  await wrapper.find("button").trigger("click");

  expect(wrapper.text()).toContain("Edit Product");
});