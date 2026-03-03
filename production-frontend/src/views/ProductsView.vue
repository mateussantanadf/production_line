<script setup>
import { ref, onMounted } from "vue";
import productService from "../api/productService";
import resourceService from "../api/resourceService";

const isEditing = ref(false);
const editingCode = ref(null);

const products = ref([]);
const resources = ref([]);
const selectedResources = ref([]);

const loading = ref(false);
const error = ref(null);

const form = ref({
  name: "",
  price: ""
});

const loadProducts = async () => {
  try {
    loading.value = true;
    const response = await productService.getAll();
    products.value = response.data;
  } catch (err) {
    error.value = "Error loading products";
  } finally {
    loading.value = false;
  }
};

const loadResources = async () => {
  const response = await resourceService.getAll();
  resources.value = response.data.map(r => ({
    ...r,
    selected: false,
    qtdResource: 1
  }));
};

const createProduct = async () => {
  try {
    if (!form.value.name || !form.value.price) {
      alert("Fill in all fields.");
      return;
    }

    const selectedResources = resources.value.filter(r => r.selected);

    if (selectedResources.length === 0) {
      alert("Select at least one resource.");
      return;
    }

    const compositions = selectedResources.map(r => {

      const quantity = Number(r.qtdResource);

      if (!quantity || quantity <= 0) {
        throw new Error(`Invalid quantity for ${r.name}`);
      }

      if (quantity > r.stock) {
        throw new Error(
          `Quantity for ${r.name} exceeds available stock (${r.stock})`
        );
      }

      return {
        resourceCode: r.code,
        qtdResource: quantity
      };
    });

    await productService.create({
      name: form.value.name,
      price: Number(form.value.price),
      compositions
    });

    form.value.name = "";
    form.value.price = "";

    resources.value.forEach(r => {
      r.selected = false;
      r.qtdResource = 1;
    });

    await loadProducts();

  } catch (err) {
    alert(err.message || "Error adding product");
  }
};

const editProduct = (product) => {

  isEditing.value = true;
  editingCode.value = product.code;

  form.value.name = product.name;
  form.value.price = product.price;

  resources.value.forEach(r => {
    r.selected = false;
    r.qtdResource = 1;
  });

  product.compositions.forEach(comp => {
    const resource = resources.value.find(r => r.code === comp.resourceCode);
    if (resource) {
      resource.selected = true;
      resource.qtdResource = comp.qtdResource;
    }
  });

  window.scrollTo({ top: 0, behavior: "smooth" });
};

const updateProduct = async () => {
  try {
    if (!form.value.name || !form.value.price) {
      alert("Fill in all fields.");
      return;
    }

    const selected = resources.value.filter(r => r.selected);

    if (selected.length === 0) {
      alert("Select at least one resource.");
      return;
    }

    const compositions = selected.map(r => {
      if (!r.qtdResource || r.qtdResource <= 0) {
        throw new Error(`Invalid quantity for ${r.name}`);
      }

      if (r.qtdResource > r.stock) {
        throw new Error(
          `Quantity for ${r.name} exceeds stock (${r.stock})`
        );
      }

      return {
        resourceCode: r.code,
        qtdResource: r.qtdResource
      };
    });

    await productService.update(editingCode.value, {
      name: form.value.name,
      price: Number(form.value.price),
      compositions
    });

    cancelEdit();
    await loadProducts();

  } catch (err) {
    alert(err.message || "Error updating product");
  }
};

const cancelEdit = () => {
  isEditing.value = false;
  editingCode.value = null;

  form.value.name = "";
  form.value.price = "";

  resources.value.forEach(r => {
    r.selected = false;
    r.qtdResource = 1;
  });
};

onMounted(() => {
  loadProducts();
  loadResources();
});
</script>

<template>
    <div class="container">
        <h2>📦 Products</h2>

        <div class="form-card">
            <h3 class="section-title">
                {{ isEditing ? "Edit Product" : "New Product" }}
            </h3>

            <div class="form-group">
            <input
                v-model="form.name"
                placeholder="Product Name"
            />

            <input
                v-model.number="form.price"
                type="number"
                placeholder="Price"
                step="0.01"
                min="0"
            />
            </div>

            <h4 class="section-subtitle">Select Resources</h4>

            <div class="resource-list">
                <div
                    v-for="r in resources"
                    :key="r.code"
                    class="resource-item"
                >
                    <div class="resource-info">
                    <label>
                        <input type="checkbox" v-model="r.selected" />
                        {{ r.name }}
                    </label>

                    <span class="stock">
                        Stock: {{ r.stock }}
                    </span>
                    </div>

                    <input
                    v-if="r.selected"
                    type="number"
                    min="1"
                    :max="r.stock"
                    v-model.number="r.qtdResource"
                    placeholder="Quantity Resource"
                    class="quantity-input"
                    />
                </div>
            </div>
        </div>
    </div>

    <div class="table-card">
        <h3>Products List</h3>

        <p v-if="loading">Loading...</p>
        <p v-if="error" class="error">{{ error }}</p>

        <table v-if="products.length">
            <thead>
            <tr>
                <th>Code</th>
                <th>Name</th>
                <th>Price</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="p in products" :key="p.code">
                <td>{{ p.code }}</td>
                <td>{{ p.name }}</td>
                <td>R$ {{ Number(p.price).toFixed(2) }}</td>
                <td>
                <button @click="editProduct(p)">Edit</button>
                </td>
            </tr>
            </tbody>
        </table>

        <p v-else>No products found.</p>
    </div>
    <div class="button-wrapper">
        <button :disabled="loading" @click="isEditing ? updateProduct() : createProduct()">
            {{ isEditing ? "Update Product" : "Add Product" }}
        </button>

        <button
            v-if="isEditing"
            style="background:#f87171; margin-left:10px;"
            @click="cancelEdit"
        >
            Cancel
        </button>
    </div>
</template>

<style scoped>
.container {
  width: 100%;
  max-width: 800px;
  margin: auto;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
}

.form-card,
.table-card {
  background: #1f2937;
  padding: 20px;
  border-radius: 10px;
  margin-bottom: 30px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.4);
}

.form-group {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

input {
  flex: 1;
  padding: 10px;
  border-radius: 6px;
  border: none;
  background: #374151;
  color: white;
}

input::placeholder {
  color: #9ca3af;
}

button {
  padding: 10px 20px;
  border-radius: 6px;
  border: none;
  background: #38bdf8;
  color: #0f172a;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}

button:hover {
  background: #0ea5e9;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

th,
td {
  padding: 12px;
  text-align: left;
}

thead {
  background: #111827;
}

tbody tr:nth-child(even) {
  background: #2a3441;
}

tbody tr:hover {
  background: #334155;
}

.error {
  color: #f87171;
}

.resource-list {
  margin-top: 15px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.resource-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #374151;
  padding: 8px;
  border-radius: 6px;
}

.section-title {
  margin-bottom: 15px;
}

.section-subtitle {
  margin-top: 25px;
  margin-bottom: 10px;
  color: #9ca3af;
  font-size: 14px;
}

.button-wrapper {
  margin-top: 20px;
  text-align: right;
}

.resource-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stock {
  font-size: 12px;
  color: #9ca3af;
}

.resource-item {
  transition: 0.2s;
}

.resource-item:hover {
  background: #4b5563;
}

.quantity-input {
  max-width: 50px;
  text-align: center;
}
</style>