import { useForm } from 'react-hook-form'
import { procesarTransaccion } from '../services/api'
import { useNavigate } from 'react-router-dom'
import { useState } from 'react'

export default function Transaccion() {
  const { register, handleSubmit, formState: { errors }, reset } = useForm()
  const navigate = useNavigate()
  const [resultado, setResultado] = useState(null)
  const username = localStorage.getItem('username')

  const onSubmit = async (data) => {
    try {
      const response = await procesarTransaccion(data)
      setResultado(response.data)
      reset()
    } catch (error) {
      if (error.response?.status === 401) {
        alert('Sesión expirada, inicia sesión de nuevo')
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        navigate('/login')
      } else {
        alert('Error al procesar la transacción')
      }
    }
  }

  const handleLogout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-md mx-auto">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Simulador de Transacciones</h2>
          <div className="flex items-center gap-3">
            <span className="text-sm text-gray-600">Hola, {username}</span>
            <button
              onClick={handleLogout}
              className="text-sm text-red-600 hover:underline"
            >
              Cerrar sesión
            </button>
          </div>
        </div>

        <div className="bg-white p-8 rounded-lg shadow-md">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">RRN (12 dígitos)</label>
              <input
                {...register('rrn', {
                  required: 'El RRN es requerido',
                  pattern: { value: /^\d{12}$/, message: 'El RRN debe tener exactamente 12 dígitos' }
                })}
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="123456789012"
                maxLength={12}
              />
              {errors.rrn && <p className="text-red-500 text-sm mt-1">{errors.rrn.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">STAN (6 dígitos)</label>
              <input
                {...register('stan', {
                  required: 'El STAN es requerido',
                  pattern: { value: /^\d{6}$/, message: 'El STAN debe tener exactamente 6 dígitos' }
                })}
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="123456"
                maxLength={6}
              />
              {errors.stan && <p className="text-red-500 text-sm mt-1">{errors.stan.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Importe</label>
              <input
                type="number"
                step="0.01"
                {...register('importe', {
                  required: 'El importe es requerido',
                  min: { value: 0.01, message: 'El importe debe ser mayor a 0' }
                })}
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="1500.00"
              />
              {errors.importe && <p className="text-red-500 text-sm mt-1">{errors.importe.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Fecha</label>
              <input
                type="date"
                {...register('fecha', { required: 'La fecha es requerida' })}
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              {errors.fecha && <p className="text-red-500 text-sm mt-1">{errors.fecha.message}</p>}
            </div>

            <button
              type="submit"
              className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition"
            >
              Procesar Transacción
            </button>
          </form>

          {resultado && (
            <div className={`mt-6 p-4 rounded-md ${resultado.status === '00' ? 'bg-green-100 border border-green-400' : 'bg-red-100 border border-red-400'}`}>
              <h3 className={`font-bold text-lg ${resultado.status === '00' ? 'text-green-700' : 'text-red-700'}`}>
                {resultado.mensaje}
              </h3>
              <p className="text-sm text-gray-700 mt-1">RRN: {resultado.rrn}</p>
              <p className="text-sm text-gray-700">STAN: {resultado.stan}</p>
              <p className="text-sm text-gray-700">Status: {resultado.status}</p>
              {resultado.autorizacion && (
                <p className="text-sm text-gray-700">Autorización: {resultado.autorizacion}</p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}