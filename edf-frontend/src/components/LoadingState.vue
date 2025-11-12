<template>
  <div class="loading">
    <div class="spinner"></div>
    <p>Loading EDF files...</p>
  </div>
</template>

<style scoped>
.loading {
  text-align: center;
  padding: 3rem;
  color: white;
  font-size: 1.25rem;
}

.spinner {
  width: 40px;
  height: 40px;
  margin: 0 auto 1rem;
  border: 4px solid rgba(255,255,255,0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
