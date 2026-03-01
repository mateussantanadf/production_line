<template>
  <div class="app-container">
    <header class="navbar">
      <h1 class="logo">Production Line</h1>

      <nav class="nav-links">
        <router-link to="/" class="link">Resources</router-link>
        <router-link to="/products" class="link">Products</router-link>
        <router-link to="/production" class="link">Production</router-link>
      </nav>
    </header>

    <main class="content">
      <div class="card">
        <router-view />
      </div>
    </main>
  </div>
</template>

<style scoped>
.app-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1e293b, #0f172a);
  display: flex;
  flex-direction: column;
  font-family: Arial, Helvetica, sans-serif;
  color: #f1f5f9;
}

/* Navbar */
.navbar {
  background: #111827;
  padding: 20px 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #38bdf8;
}

.nav-links {
  display: flex;
  gap: 20px;
}

.link {
  text-decoration: none;
  color: #e2e8f0;
  font-weight: 500;
  transition: 0.3s;
}

.link:hover {
  color: #38bdf8;
}

.router-link-active {
  color: #38bdf8;
  border-bottom: 2px solid #38bdf8;
  padding-bottom: 4px;
}

/* Main */
.content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
}

/* Card */
.card {
  width: 100%;
  max-width: 900px;
  background: #1f2937;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.4);
}
</style>