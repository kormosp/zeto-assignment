import {computed, ref} from "vue";
import axios from "axios";


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
            const response = await axios.request({url,  method: httpMethod, timeout: 10000})
            files.value = response.data
        } catch (err) {
            console.log('Backend error response:', err)
            error.value = err.response?.data?.detail || err.message
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
