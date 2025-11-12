<template>
  <div class="channels" @click="emit('toggle')">
    <div class="header" >
      <span :class="['icon', { expanded: show }]">--></span>
      Channels ({{ channels.length }})
    </div>

    <Transition
        enter-active-class="animate__animated animate__fadeInDown animate__faster"
        leave-active-class="animate__animated animate__fadeOutUp animate__faster"
    >
      <div v-if="show" class="list">
        <div
            v-for="(channel, idx) in channels"
            :key="idx"
            class="channel"
        >
          <div class="name">{{ channel.name }}</div>
          <div class="type">{{ channel.type || 'No type' }}</div>
        </div>
      </div>
    </Transition>
  </div>
</template>


<script setup>

defineProps({
  channels: Array,
  show: Boolean
})

const emit = defineEmits(['toggle'])
</script>

<style scoped>
.channels {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #e2e8f0;
}

.header {
  font-size: 1rem;
  color: black;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  user-select: none;
  transition: color 0.2s ease;
}

.header:hover {
  color: #2d3748;
}

.icon {
  transition: transform 0.3s ease;
}

.icon.expanded {
  transform: rotate(90deg);
}

.list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 0.5rem;
  margin-top: 0.75rem;
}

.channel {
  background: #f7fafc;
  padding: 0.5rem;
  border-radius: 6px;
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.channel:hover {
  background: #edf2f7;
  transform: translateY(-2px);
}

.name {
  font-weight: 600;
  color: #2d3748;
}

.type {
  color: #718096;
  font-size: 0.75rem;
}
</style>
