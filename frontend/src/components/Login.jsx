import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'

export default function Login() {
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const { login } = useAuth()
  const navigate = useNavigate()

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const { data } = await api.post('/api/v1/auth/login', form)
      login(data.token)
      navigate('/dashboard')
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <div className="min-h-screen bg-neutral-950 flex items-center justify-center px-6">
        <div className="w-full max-w-[360px]">
          <div className="h-px bg-neutral-800 mb-10" />

          <h1 className="serif text-white text-3xl mb-1">Welcome back</h1>
          <p className="mono text-neutral-500 text-xs tracking-widest uppercase mb-10">
            Personal Finance
          </p>

          <form className="space-y-5" onSubmit={handleSubmit}>
            <div>
              <label className="mono block text-xs text-neutral-500 tracking-widest uppercase mb-2">
                Email
              </label>
              <input
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                placeholder="you@example.com"
                required
                className="mono w-full bg-transparent border border-neutral-800 text-white px-3 py-3 text-sm placeholder:text-neutral-700 focus:outline-none focus:border-neutral-600 transition-colors"
              />
            </div>

            <div>
              <label className="mono block text-xs text-neutral-500 tracking-widest uppercase mb-2">
                Password
              </label>
              <input
                type="password"
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="••••••••"
                required
                className="mono w-full bg-transparent border border-neutral-800 text-white px-3 py-3 text-sm placeholder:text-neutral-700 focus:outline-none focus:border-neutral-600 transition-colors"
              />
            </div>

            {error && (
              <p className="mono text-xs text-red-400">{error}</p>
            )}

            <button
              type="submit"
              disabled={loading}
              className="mono w-full bg-neutral-100 hover:bg-neutral-300 text-neutral-950 py-3 text-sm font-medium tracking-wide transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Signing in...' : 'Sign in →'}
            </button>
          </form>

          <div className="h-px bg-neutral-800 mt-10 mb-6" />

          <p className="mono text-xs text-neutral-600 text-center">
            No account?{" "}
            <a href="/register" className="text-neutral-400 hover:text-neutral-300 transition-colors">
              Sign up
            </a>
          </p>
        </div>
      </div>
    </>
  )
}
