<script setup>
import { ref, onMounted } from "vue";
import resourceService from "../api/resourceService";

const resources = ref([]);
const loading = ref(false);
const error = ref(null);

const isEditing = ref(false);
const editingCode = ref(null);

const form = ref({
  name: "",
  qtdStock: ""
});

const loadResources = async () => {
  try {
    loading.value = true;
    const response = await resourceService.getAll();
    resources.value = response.data;
  } catch (err) {
    error.value = "Error loading resources";
  } finally {
    loading.value = false;
  }
};

const createResource = async () => {
  try {
    if (!form.value.name || form.value.qtdStock === "") {
      alert("Fill in all the fields.");
      return;
    }

    await resourceService.create({
      name: form.value.name,
      qtdStock: Number(form.value.qtdStock)
    });

    resetForm();
    await loadResources();
  } catch (err) {
    alert("Error adding resource");
  }
};

const editResource = (resource) => {
  isEditing.value = true;
  editingCode.value = resource.code;

  form.value.name = resource.name;
  form.value.qtdStock = resource.stock;

  window.scrollTo({ top: 0, behavior: "smooth" });
};

const updateResource = async () => {
  try {
    if (!form.value.name || form.value.qtdStock === "") {
      alert("Fill in all the fields.");
      return;
    }

    await resourceService.update(editingCode.value, {
      name: form.value.name,
      qtdStock: Number(form.value.qtdStock)
    });

    resetForm();
    await loadResources();
  } catch (err) {
    alert("Error updating resource");
  }
};

const cancelEdit = () => {
  resetForm();
};

const resetForm = () => {
  form.value.name = "";
  form.value.qtdStock = "";
  isEditing.value = false;
  editingCode.value = null;
};

onMounted(loadResources);
</script>

<template>
  <div class="container">
    <h2>🧱 Resources</h2>

    <div class="form-card">
      <h3>{{ isEditing ? "Edit Resource" : "New Resource" }}</h3>
      <div class="form-group">
        <input v-model="form.name" placeholder="Name Resource" />
        <input
          v-model="form.qtdStock"
          type="number"
          placeholder="Quantity in Stock"
        />
        <button @click="isEditing ? updateResource() : createResource()">
            {{ isEditing ? "Update" : "Add" }}
        </button>

<button
  v-if="isEditing"
  style="background:#f87171;"
  @click="cancelEdit"
>
  Cancel
</button>
      </div>
    </div>

    <div class="table-card">
      <h3>Resources List</h3>

      <p v-if="loading">Loading...</p>
      <p v-if="error" class="error">{{ error }}</p>

      <table v-if="resources.length">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Stock</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in resources" :key="r.code">
            <td>{{ r.code }}</td>
            <td>{{ r.name }}</td>
            <td>{{ r.stock }}</td>
            <td>
                <button
                    style="background:#38bdf8;"
                    @click="editResource(r)"
                >
                    Edit
                </button>
            </td>
          </tr>
        </tbody>
      </table>

      <p v-else>No resources registered.</p>
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
  background: #22c55e;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}

button:hover {
  background: #16a34a;
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