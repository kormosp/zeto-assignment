<template>
  <div class="app">
    <AppHeader
        :total="files.length"
        :valid-count="validCount"
        :invalid-count="invalidCount"
    />

    <Controls
        :loading="loading"
        :sorted="sortedByDate"
        @rescan="handleRescan"
        @sortedFetchByRecordingDate="sortedToogle"
    />

    <LoadingState v-if="loading && files.length === 0" />
    <ErrorMessage v-else-if="error" :message="error" />
    <EmptyState v-else-if="files.length === 0 && fetched" />
    <FileList v-else :files="files" />

  </div>
</template>


<script setup>
import {ref, computed, onMounted, watch, watchEffect} from 'vue'

import AppHeader from './components/AppHeader.vue'
import Controls from './components/Controls.vue'
import LoadingState from './components/LoadingState.vue'
import EmptyState from './components/EmptyState.vue'
import ErrorMessage from './components/ErrorMessage.vue'
import FileList from './components/FileList.vue'
import {useFetchEdf} from './use/useFetchEdf.js'

const API_BASE_URL = import.meta.env.VITE_BACKEND_URL + '/api/edfs'
const sortedByDate = ref(false)

/***************************************************************
 * Fetch request to backend
 */
const {files, loading, fetched, error, fetchFiles, rescanFiles} = useFetchEdf(API_BASE_URL)

const validCount = computed(() => files.value.filter(f => f.validEdf).length)
const invalidCount = computed(() => files.value.filter(f => !f.validEdf).length)

/***************************************************************
 * Fetch data from backend at Mounted hook
 */
onMounted(() => {
  fetchFiles({sorted: sortedByDate.value})
})

/***************************************************************
 * Watch sorted value, if changes, refetch
 */
watch(sortedByDate, () => {
  console.log("watch will fire , sortedByDate.value:", sortedByDate.value)
  fetchFiles({sorted: sortedByDate.value})
  console.log("watch fired, sortedByDate.value:", sortedByDate.value)
})


/***************************************************************
 * Delay for test purposes
 */
const delay = async () => {
  await new Promise(resolve => setTimeout(resolve, 1200))
}

function sortedToogle(isChecked) {
  sortedByDate.value = isChecked
}

function handleRescan() {
  rescanFiles({sorted: sortedByDate.value})
}

</script>


