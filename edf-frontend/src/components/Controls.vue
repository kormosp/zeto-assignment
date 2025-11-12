<template>
<div class="controls">
  <button
      class="btn"
      @click="emit('rescan')"
      :disabled="loading"
  >
    <span v-if="loading" class="spinner"></span>
    {{ loading ? 'Scanning...' : 'Rescan EDS File Source' }}
  </button>


  <label class="checkbox-label">
    <span>Fetch by recording date: </span>
    <input
        type="checkbox"
        :checked="sorted"
        @change="emit('sortedFetchByRecordingDate', $event.target.checked)"
        class="checkbox"
    />
  </label>
</div>

</template>


<script setup>
defineProps({
      loading: {
        type: Boolean,
        default: false
      },
      sorted: Boolean
    }
)
const emit = defineEmits(['rescan', 'sortedFetchByRecordingDate'])

</script>

<style scoped>
.controls {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  margin-bottom: 2rem;
  display: flex;
  align-items: center;
  justify-content: left;
  gap: 1.5rem;
  flex-wrap: wrap;
}

.btn {
  background: #0679d5;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-size: 1.2rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn:hover:not(:disabled) {
  background: #5a67d8;
  transform: translateY(-2px);
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255,255,255,0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  user-select: none;
  color: #2d3748;
  font-size: 1.2rem;
  font-weight: 500;
}

.checkbox {
  width: 18px;
  height: 18px;
  cursor: pointer;
  accent-color: #667eea;
}

.checkbox-label:hover {
  color: #1a202c;
}

@media (max-width: 768px) {
  .controls {
    flex-direction: column;
    align-items: stretch;
  }

  .btn {
    justify-content: center;
  }
}
</style>
