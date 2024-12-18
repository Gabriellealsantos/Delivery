import { createRoot } from 'react-dom/client'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import DeliveryHome from './routes/DeliveryHome/index.tsx'
import Home from './routes/DeliveryHome/Home/index.tsx'
import Orders from './routes/DeliveryHome/Orders/index.tsx'
import './index.css'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<DeliveryHome />}>
        <Route index element={<Home />} />
        <Route path="home" element={<Home />} />
        <Route path="orders" element={<Orders />} />
      </Route>
    </Routes>
  </BrowserRouter>
)
