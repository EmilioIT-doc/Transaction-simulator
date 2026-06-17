import { useForm } from 'react-hook-form'
import { register as registerUser } from '../services/api'
import { useNavigate } from 'react-router-dom'

export default function Register() {
  const { register, handleSubmit, formState: { errors } } = useForm()
  const navigate = useNavigate()

  const onSubmit = async (data) => {
    try {
      await registerUser({ ...data, role: 'USER' })
      alert('Usuario registrado exitosamente')
      navigate('/login')
    } catch (error) {
      alert('Error al registrar usuario')
    }
  }

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">Registro</h2>
        
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
            <input
              {...register('username', { required: 'El username es requerido' })}
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Tu username"
            />
            {errors.username && <p className="text-red-500 text-sm mt-1">{errors.username.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input
              type="password"
              {...register('password', { required: 'El password es requerido' })}
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Tu password"
            />
            {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>}
          </div>

          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition"
          >
            Registrarse
          </button>

          <p className="text-center text-sm text-gray-600">
            ¿Ya tienes cuenta?{' '}
            <span onClick={() => navigate('/login')} className="text-blue-600 cursor-pointer hover:underline">
              Inicia sesión
            </span>
          </p>
        </form>
      </div>
    </div>
  )
}