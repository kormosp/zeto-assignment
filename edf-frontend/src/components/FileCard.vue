<template>
  <div :class="['card', file.validEdf ? 'valid' : 'invalid']">
    <div class="header">
      <div class="name">{{ file.fileName }}</div>
      <span :class="['badge', file.validEdf ? 'valid' : 'invalid']">
        {{ file.validEdf ? 'Valid' : 'Invalid' }}
      </span>
    </div>

    <div v-if="!file.validEdf" class="error">
      {{ file.errorMessage || 'Unable to parse this file' }}
    </div>

    <div v-else>
      <div class="details">
        <div class="item">
          <div class="label">Recording ID</div>
          <div class="value">{{ file.recordingID || 'N/A' }}</div>
        </div>
        <div class="item">
          <div class="label">Recording Date</div>
          <div class="value">{{ formatDate(file.recordingDate) }}</div>
        </div>
        <div class="item">
          <div class="label">Patient Name</div>
          <div class="value">{{ file.patientName || 'Not Available' }}</div>
        </div>
        <div class="item">
          <div class="label">Channels</div>
          <div class="value">{{ file.numberOfChannels }}</div>
        </div>
        <div class="item">
          <div class="label">Recording Length</div>
          <div class="value">{{ formatDuration(file.recordingLength) }}</div>
        </div>
        <div class="item">
          <div class="label">Annotations</div>
          <div class="value">{{ file.numberOfAnnotations }}</div>
        </div>
      </div>

      <ChannelList
          v-if="file.channels?.length"
          :channels="file.channels"
          :show="showChannels"
          @toggle="showChannels = !showChannels"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ChannelList from './ChannelList.vue'

const props = defineProps({
  file: Object
})

const showChannels = ref(false)

const formatDate = (dateString) => {
  if (!dateString) return 'Not Available'
  return new Date(dateString).toLocaleString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const formatDuration = (seconds) => {
  if (seconds == null) return 'N/A'
  const h = Math.floor(seconds / 3600)
  const m = Math.floor((seconds % 3600) / 60)
  const s = Math.floor(seconds % 60)
  return h > 0 ? `${h}h ${m}m ${s}s` : m > 0 ? `${m}m ${s}s` : `${s}s`
}
</script>

<style scoped>
.card {
  background: #F2F2F2;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
}

.card:hover {
  background: #3ca1f1;
  color: white;
  box-shadow: 0 8px 16px rgba(0,0,0,0.15);
  transform: translateY(-4px);
}

.card.valid {
  border-left-color: #48bb78;
}

.card.invalid {
  border-left-color: #f56565;
  opacity: 0.8;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: start;
  margin-bottom: 1rem;
}

.name {
  font-size: 1.25rem;
  font-weight: 600;
  /*color: #2d3748;*/
}

.badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  text-transform: uppercase;
}

.badge.valid {
  background: #c6f6d5;
  color: #22543d;
}

.badge.invalid {
  background: #fed7d7;
  color: #742a2a;
}

.error {
  background: #fff5f5;
  color: #c53030;
  padding: 1rem;
  border-radius: 8px;
  border-left: 4px solid #f56565;
}

.details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
  margin-top: 1rem;
}


.item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.label {
  font-size: 1.2rem;

  text-transform: uppercase;
  font-weight: 600;
}

.value {
  font-size: 1rem;
  /*color: #2d3748;*/
  font-weight: 500;
}

@media (max-width: 768px) {
  .details {
    grid-template-columns: 1fr;
  }
}
</style>
