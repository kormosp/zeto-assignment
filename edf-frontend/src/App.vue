<template>
  <div class="app">
    <AppHeader
        :total="files.length"
        :valid-count="validCount"
        :invalid-count="invalidCount"
    />
    <Controls
        :loading="loading"
        :sorted="sorted"
        @rescan="rescanFiles"
        @sortedFetchByRecordingDate="sortedToogle();"
    />

    <LoadingState v-if="loading && files.length === 0" />
    <ErrorMessage v-else-if="error" :message="error" />
    <EmptyState v-else-if="files.length === 0 && fetched" />
    <FileList v-else :files="files" />

  </div>
</template>


<script setup>
import { ref, computed, onMounted, watch  } from 'vue'

import AppHeader from './components/AppHeader.vue'
import Controls from './components/Controls.vue'
import LoadingState from './components/LoadingState.vue'
import EmptyState from './components/EmptyState.vue'
import ErrorMessage from './components/ErrorMessage.vue'
import FileList from './components/FileList.vue'

const API_BASE_URL = import.meta.env.VITE_BACKEND_URL + '/api/edfs'

const files = ref([])
const loading = ref(false)
const fetched = ref(false)
const error = ref(null)
const sorted = ref(false)


const validCount = computed(() => files.value.filter(f => f.validEdf).length)
const invalidCount = computed(() => files.value.filter(f => !f.validEdf).length)

/***************************************************************
 * Fetch request to backend
 */
const fetchFiles = async () => {
  loading.value = true
  fetched.value = false
  error.value = null

  // ONLY for test, wait a bit before starting the fetch, to see spinner
  //TODO delete in production
  await delay();

  try {
    const url = sorted.value ? `${API_BASE_URL}/sorted` : API_BASE_URL
    const response = await fetch(`${url}?t=${Date.now()}`, {
      method: 'GET',
      headers: {
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache'
      }
    })
    console.log('fetch response code', response.ok, response.status)
    // Handle error
    if (!response.ok) {
      const errorData = await response.json()
      console.log('Backend error response:', errorData)
      error.value = errorData.detail || 'An error occurred'
    } else {
      files.value = await response.json()
    }
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
    fetched.value = true
  }
}


/***************************************************************
 * Send rescan request to backend for fetching new data
 */
const rescanFiles = async () => {
  loading.value = true
  error.value = null
  fetched.value = false

  // ONLY for test
  //TODO delete in production
  // delay for a bit  before starting the fetch to see spinner
  await delay();

  try {
    const url = `${API_BASE_URL}/rescan?sorted=${sorted.value}`
    const response = await fetch(url, { method: 'POST' })
    // Handle error response
    if (!response.ok) {
      const errorData = await response.json()
      console.log('Backend error response:', errorData)
      error.value = errorData.detail || 'An error occurred'
    } else {
      files.value = await response.json()
    }
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
    fetched.value = true
  }
}


/***************************************************************
 * Fetch data from backend at Mounted hook
 */
onMounted(() => {
  fetchFiles()
})

/***************************************************************
 * Watch sorted value, if changes, refetch
 */
watch(sorted, () => {
  fetchFiles()
})

/***************************************************************
 * Delay for test purposes
 */
const delay = async () => {
  await new Promise(resolve => setTimeout(resolve, 1200))
}

function sortedToogle() {
  sorted.value = !sorted.value
}</script>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #0679d5);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
