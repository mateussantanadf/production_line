<script setup>
import { ref, onMounted } from "vue";
import productService from "../api/productService";

const products = ref([]);
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

const createProduct = async () => {
  try {
    if (!form.value.name || !form.value.price) {
      alert("Fill in all the fields.");
      return;
    }

    await productService.create({
      name: form.value.name,
      price: Number(form.value.price),
      compositions: []
    });

    form.value.name = "";
    form.value.price = "";

    await loadProducts();
  } catch (err) {
    alert("Error adding product");
  }
};

onMounted(loadProducts);
</script>

<template>
  <div class="container">
    <h2>📦 Products</h2>

    <!-- Formulário -->
    <div class="form-card">
      <h3>New Product</h3>
      <div class="form-group">
        <input v-model="form.name" placeholder="Name Products" />
        <input
          v-model="form.price"
          type="number"
          placeholder="Price"
          step="0.01"
        />
        <button @click="createProduct">Add</button>
      </div>
    </div>

    <!-- Tabela -->
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
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in products" :key="p.code">
            <td>{{ p.code }}</td>
            <td>{{ p.name }}</td>
            <td>R$ {{ Number(p.price).toFixed(2) }}</td>
          </tr>
        </tbody>
      </table>

      <p v-else>No products found.</p>
    </div>
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
</style>