import { mount, flushPromises } from "@vue/test-utils";
import { describe, it, expect, vi, beforeEach } from "vitest";
import Resource from "../views/ResourcesView.vue";

// Mock do service
vi.mock("../api/resourceService", () => ({
  default: {
    getAll: vi.fn(),
    create: vi.fn(),
    update: vi.fn()
  }
}));

import resourceService from "../api/resourceService";

describe("Resource.vue", () => {

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("loads resources on mount", async () => {
    resourceService.getAll.mockResolvedValue({
      data: [
        { code: 1, name: "Sugar", stock: 10 }
      ]
    });

    const wrapper = mount(Resource);

    await flushPromises();

    expect(resourceService.getAll).toHaveBeenCalled();
    expect(wrapper.text()).toContain("Sugar");
  });

  it("creates a resource", async () => {
    resourceService.getAll.mockResolvedValue({ data: [] });
    resourceService.create.mockResolvedValue({});

    const wrapper = mount(Resource);
    await flushPromises();

    await wrapper.find("input[placeholder='Name Resource']")
      .setValue("Flour");

    await wrapper.find("input[placeholder='Quantity in Stock']")
      .setValue("20");

    await wrapper.find("button").trigger("click");

    await flushPromises();

    expect(resourceService.create).toHaveBeenCalledWith({
      name: "Flour",
      qtdStock: 20
    });
  });

  it("enters edit mode", async () => {
    resourceService.getAll.mockResolvedValue({
      data: [{ code: 1, name: "Salt", stock: 5 }]
    });

    const wrapper = mount(Resource);
    await flushPromises();

    const editButton = wrapper.findAll("button")
      .find(btn => btn.text() === "Edit");

    await editButton.trigger("click");

    expect(wrapper.text()).toContain("Edit Resource");
  });

  it("updates a resource", async () => {
    resourceService.getAll.mockResolvedValue({
      data: [{ code: 1, name: "Oil", stock: 15 }]
    });

    resourceService.update.mockResolvedValue({});

    const wrapper = mount(Resource);
    await flushPromises();

    const editButton = wrapper.findAll("button")
      .find(btn => btn.text() === "Edit");

    await editButton.trigger("click");

    await wrapper.find("input[placeholder='Name Resource']")
      .setValue("Olive Oil");

    await wrapper.find("button").trigger("click");

    await flushPromises();

    expect(resourceService.update).toHaveBeenCalledWith(1, {
      name: "Olive Oil",
      qtdStock: 15
    });
  });

});