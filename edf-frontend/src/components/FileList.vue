<template>
  <TransitionGroup
      name="list"
      tag="div"
      class="list"
      enter-active-class="animate__animated animate__fadeInUp"
      leave-active-class="animate__animated animate__fadeOutDown"
  >
    <FileCard
        v-for="file in files"
        :key="file.fileName"
        :file="file"
    />
  </TransitionGroup>
</template>

<script setup>
import FileCard from './FileCard.vue'

defineProps({
  files: Array
})
</script>

<style scoped>
.list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.list-move {
  transition: transform 0.5s ease;
}
</style>
