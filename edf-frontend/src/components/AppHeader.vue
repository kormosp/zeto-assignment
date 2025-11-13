<template>
  <div class="header">
    <img width="384" height="88" src="/Zeto-Logo-dark.png" alt="Zeto Logo Dark">
    <h1>EDF File Viewer</h1>
    <div class="info">
      <div>Total: <strong>{{ total }}</strong></div>
      <div>
        <div class="valid">Valid: {{ validCount }} </div>
        <div class="invalid">Invalid: {{ invalidCount }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  total: Number,
  validCount: Number,
  invalidCount: Number
})
</script>

<style scoped>
.header {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  margin-bottom: 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

h1 {
  color: #0679d5;
  font-size: 2rem;
  font-weight: 700;
  margin: 0;
}

.info {
  text-align: right;
  color: #718096;
  font-size: 1.5rem;
}

.valid {
  color: #48bb78;
  font-weight: 600;
}

.invalid {
  color: #f56565;
  font-weight: 600;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  .info {
    text-align: center;
  }
}
</style>
