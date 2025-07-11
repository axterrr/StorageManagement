import React, { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App'

import 'bootstrap/dist/css/bootstrap.min.css'
import './index.css'
import './App.css'

const container = document.getElementById('root')!
const root = createRoot(container)

root.render(
    <StrictMode>
        <App />
    </StrictMode>
)
