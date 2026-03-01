<script setup>
import { ref, onMounted } from "vue";
import productionService from "../api/productionService";
import productService from "../api/productService";

const suggestions = ref([]);
const products = ref([]);
const selectedProduct = ref(null);
const quantity = ref(1);
const result = ref(null);
const loading = ref(false);
const error = ref(null);

const loadSuggestions = async () => {
  const response = await productionService.suggest();
  suggestions.value = response.data;
};

const loadProducts = async () => {
  const response = await productService.getAll();
  products.value = response.data;
};

const produce = async (productIdParam = null) => {
  try {
    loading.value = true;
    error.value = null;

    const id = productIdParam || selectedProduct.value;

    if (!id) {
      alert("Select a product");
      return;
    }

    const response = await productionService.produce(id, quantity.value);
    result.value = response.data;

    await loadSuggestions();
  } catch (err) {
    error.value = "Error in executing production";
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  await loadSuggestions();
  await loadProducts();
});
</script>

<template>
  <div class="container">
    <h2>🏭 Production Center</h2>

    <!-- Sugestões -->
    <div class="card">
      <h3>📊 Production Suggestions</h3>

      <table v-if="suggestions.length">
        <thead>
          <tr>
            <th>Product</th>
            <th>Total Profit</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in suggestions" :key="s.productCode">
            <td>{{ s.productName }}</td>
            <td>R$ {{ Number(s.totalValue).toFixed(2) }}</td>
            <td>
              <button @click="produce(s.productCode)">
                Produce
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <p v-else>No suggestions available.</p>
    </div>

    <!-- Produção Manual -->
    <div class="card">
      <h3>⚙️ Manual Production</h3>

      <div class="form-group">
        <select v-model="selectedProduct">
          <option disabled value="">Select a product</option>
          <option v-for="p in products" :key="p.code" :value="p.code">
            {{ p.name }}
          </option>
        </select>

        <input v-model="quantity" type="number" min="1" />

        <button @click="produce()">Produce</button>
      </div>

      <p v-if="loading">Processing production...</p>
      <p v-if="error" class="error">{{ error }}</p>

      <div v-if="result" class="result-box">
        <h4>✅ Production Executed</h4>
        <p>Product: {{ result.productName }}</p>
        <p>Total Generated: R$ {{ Number(result.totalValue).toFixed(2) }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  max-width: 950px;
  margin: auto;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
}

.card {
  background: #1f2937;
  padding: 25px;
  border-radius: 10px;
  margin-bottom: 30px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.4);
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

th, td {
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

.form-group {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin-top: 15px;
}

select, input {
  flex: 1;
  padding: 10px;
  border-radius: 6px;
  border: none;
  background: #374151;
  color: white;
}

button {
  padding: 10px 20px;
  border-radius: 6px;
  border: none;
  background: #f59e0b;
  color: #0f172a;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}

button:hover {
  background: #d97706;
}

.result-box {
  margin-top: 20px;
  padding: 15px;
  background: #065f46;
  border-radius: 8px;
}

.error {
  color: #f87171;
}
</style>