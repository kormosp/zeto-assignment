import {computed, ref} from "vue";


export function useFetchEdf(apiBaseUrl) {

    const files = ref([])
    const loading = ref(false)
    const fetched = ref(false)
    const error = ref(null)


    /***************************************************************
     * Perform Fetch request to backend
     */
    const performFetch = async (url, httpMethod = "GET") => {
        loading.value = true
        fetched.value = false
        error.value = null

        // ONLY for test, wait a bit before starting the fetch, to see spinner
        //TODO delete in production
        await delay();

        try {
            const response = await fetch(url, { method: httpMethod })
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
     * Fetch GET request to backend
     */
    const fetchFiles = async (options = {}) => {
        const {sorted = false} = options;
        const url = sorted ? `${apiBaseUrl}/sorted` : apiBaseUrl
        await performFetch(`${url}?t=${Date.now()}`, 'GET')
    }

    /***************************************************************
     * Rescan POST request to backend
     */
    const rescanFiles = async (options = {}) => {
        const {sorted = false} = options;
        const url = `${apiBaseUrl}/rescan?sorted=${sorted}`;
        await performFetch(url, 'POST')
    }

    return {files, loading, fetched, error, fetchFiles, rescanFiles }
}

/***************************************************************
 * Delay for test purposes
 */
const delay = async () => {
    await new Promise(resolve => setTimeout(resolve, 1200))
}
