<template>
  <div class="error">
    <h2>Error: </h2>
    <p> {{ message }} </p>
  </div>
</template>

<script setup>
defineProps({
  message: String
})
</script>

<style scoped>
.error {
  font-size: 1.25rem;
  text-align: center;
  padding: 3rem;
  background: white;
  border-radius: 12px;
  color: #718096;
}

h2 {
  color: #2d3748;
  margin-bottom: 0.5rem;
}
</style>
