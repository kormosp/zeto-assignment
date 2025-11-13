# 🧠 EDF Frontend

This is the **frontend** for the EDF Processing Assignment — a Vue 3 + Vite application that communicates with the Spring Boot backend to display, sort, and rescan EDF files located in a predefined directory.

---
## 🚀 Features

- **Automatic Fetching** — Loads all parsed EDF files from backend on startup
- **Rescan Directory** — Manually triggers backend to rescan the EDF directory
- **Sorting** — Toggle sorting by recording date
- **Loading & Error States** — Visual feedback for fetching progress and errors
- **Responsive Design** — Works smoothly on both desktop and mobile screens
- **Lightweight & Fast** — Powered by Vue 3 and Vite, no unnecessary dependencies

---

## 🏗️ Project Structure
```text
edf-frontend/
├── public/
│ └── vite.svg
├── src/
│ ├── components/
│ │ ├── AppHeader.vue # Top header section
│ │ ├── ChannelList.vue # Displays list of EEG/EDF channels
│ │ ├── Controls.vue # Contains "Rescan" button & sorting checkbox
│ │ ├── EmptyState.vue # Shown when no files are found
│ │ ├── ErrorMessage.vue # Displays backend or fetch errors
│ │ ├── FileCard.vue # Displays details of one EDF file
│ │ ├── FileList.vue # Lists all EDF files from backend
│ │ └── LoadingState.vue # Spinner or “loading” placeholder
│ ├── App.vue # Root component, main layout
│ ├── main.js # Application entry point
│ └── style.css # Global styles
├── index.html # Main HTML entry
├── package.json
├── package-lock.json
├── vite.config.js
└── README.md # This file
```

---

## ⚙️ Installation & Setup

### 1️⃣ Prerequisites
- **Node.js** ≥ 18  
- **npm** ≥ 9  
- Backend (Spring Boot) running locally at `http://localhost:8080`

### 2️⃣ Install Dependencies
```bash
npm install
``` 

### 3️⃣ Run the Development Server
```bash
npm run dev
```

The app will be available at http://localhost:5173

### 🌐 Backend API Integration

By default, the frontend communicates with:

http://localhost:8080/api/edfs

| Endpoint           | Method | Description                                             |
|--------------------|--------|---------------------------------------------------------|
| `/api/edfs`        | `GET`  | Fetch list of parsed EDF files                          |
| `/api/edfs/rescan` | `POST` | Trigger backend directory rescan                        |
| `/api/edfs/sorted` | `GET`  | Fetch list of parsed EDF files ordered by RecordingDate |
|                    |        |                                                         |

CORS is configured on the backend to allow connections from http://localhost:5173.

| Component            | Description                                          |
| -------------------- |------------------------------------------------------|
| **AppHeader.vue**    | Displays the main page header                        |
| **Controls.vue**     | “Rescan EDF File Source” button and sorting checkbox |
| **FileList.vue**     | Fetches and displays list of EDF files               |
| **FileCard.vue**     | Shows details for one EDF file                       |
| **ChannelList.vue**  | Displays the list of EDF channels                    |
| **EmptyState.vue**   | Shown when no EDF files are available                |
| **LoadingState.vue** | Spinner while fetching data                          |
| **ErrorMessage.vue** | Displays API or network errors                       |

###  🧠 Development Notes

- Built with Vue 3 Composition API
- Uses fetch() for REST API calls
- Includes artificial delay for realistic UX simulation
- No state management library — all state is local to components

### 🧪 Build for Production
```bash
npm run build
```
Build output will be located in the /dist folder and can be served via any static web server (e.g., Nginx, Spring Boot static resources, etc.).

### 🧾 License
This project is part of the EDF Processing Assignment for the Senior Full-Stack Developer position for Zeto Inc.
It is for demonstration and evaluation purposes only.


#### 👨‍💻 Author: Peter Kormos
#### 📅 Date: November 2025
