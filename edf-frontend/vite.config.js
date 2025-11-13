import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'


// Environment-based backend URL
const backendUrl = process.env.VITE_BACKEND_URL || 'http://localhost:8080'

export default defineConfig({
    plugins: [vue()],
    base: './',
    server: {
        port: 5173,
        strictPort: true,
        proxy: {
            '/api': {
                target: backendUrl,
                changeOrigin: true
            }
        }
    }
})
