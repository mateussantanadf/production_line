import { mount, flushPromises } from "@vue/test-utils";
import { describe, it, expect, vi, beforeEach } from "vitest";
import Production from "../views/ProductionView.vue";

import productionService from "../api/productionService";
import productService from "../api/productService";

vi.mock("../api/productionService");
vi.mock("../api/productService");

describe("Production Component", () => {

  beforeEach(() => {
    vi.clearAllMocks();
    
    productionService.suggest.mockResolvedValue({
      data: [
        {
          productCode: 1,
          productName: "Product A",
          totalValue: 100
        }
      ]
    });

    productService.getAll.mockResolvedValue({
      data: [
        { code: 1, name: "Product A" }
      ]
    });

    productionService.produce.mockResolvedValue({
      data: {
        productName: "Product A",
        totalValue: 200
      }
    });

    window.alert = vi.fn();
  });

  // ----------------------------------------------------

  it("loads suggestions and products on mount", async () => {
    const wrapper = mount(Production);
    await flushPromises();

    expect(productionService.suggest).toHaveBeenCalled();
    expect(productService.getAll).toHaveBeenCalled();
    expect(wrapper.text()).toContain("Product A");
  });

  // ----------------------------------------------------

  it("produces from suggestion button", async () => {
    const wrapper = mount(Production);
    await flushPromises();

    const produceButton = wrapper.find("table button");
    await produceButton.trigger("click");

    await flushPromises();

    expect(productionService.produce).toHaveBeenCalledWith(1, 1);
    expect(wrapper.text()).toContain("Production Executed");
  });

  // ----------------------------------------------------

  it("produces manually", async () => {
    const wrapper = mount(Production);
    await flushPromises();

    await wrapper.find("select").setValue("1");
    await wrapper.find("input[type='number']").setValue(3);

    const buttons = wrapper.findAll("button");
    const manualButton = buttons[buttons.length - 1];

    await manualButton.trigger("click");
    await flushPromises();

    expect(productionService.produce).toHaveBeenCalledWith(1, 3);
    expect(wrapper.text()).toContain("Total Generated");
  });

  // ----------------------------------------------------

  it("shows alert if no product selected", async () => {
    const wrapper = mount(Production);
    await flushPromises();

    const buttons = wrapper.findAll("button");
    const manualButton = buttons[buttons.length - 1];

    await manualButton.trigger("click");

    expect(window.alert).toHaveBeenCalledWith("Select a product");
    expect(productionService.produce).not.toHaveBeenCalled("1", 3);
  });

  // ----------------------------------------------------

  it("shows error when production fails", async () => {
    productionService.produce.mockRejectedValue(new Error("fail"));

    const wrapper = mount(Production);
    await flushPromises();

    await wrapper.find("select").setValue("1");

    const buttons = wrapper.findAll("button");
    const manualButton = buttons[buttons.length - 1];

    await manualButton.trigger("click");
    await flushPromises();

    expect(wrapper.text()).toContain("Error in executing production");
  });

});