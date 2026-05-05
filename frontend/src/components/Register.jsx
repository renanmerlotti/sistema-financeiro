export default function Register() {
  return (
    <>
      <div className="min-h-screen bg-neutral-950 flex items-center justify-center px-6">
        <div className="w-full max-w-[360px]">
          <div className="h-px bg-neutral-800 mb-10" />

          <h1 className="serif text-white text-4xl mb-1">Create account</h1>
          <p className="mono text-neutral-500 text-xs tracking-widest uppercase mb-10">
            Personal Finance
          </p>

          <form className="space-y-5">
            <div>
              <label className="mono block text-xs text-neutral-500 tracking-widest uppercase mb-2">
                Name
              </label>
              <input
                type="text"
                placeholder="Your name"
                className="mono w-full bg-transparent border border-neutral-800 text-white px-3 py-3 text-sm placeholder:text-neutral-700 focus:outline-none focus:border-neutral-600 transition-colors"
              />
            </div>

            <div>
              <label className="mono block text-xs text-neutral-500 tracking-widest uppercase mb-2">
                Email
              </label>
              <input
                type="email"
                placeholder="you@example.com"
                className="mono w-full bg-transparent border border-neutral-800 text-white px-3 py-3 text-sm placeholder:text-neutral-700 focus:outline-none focus:border-neutral-600 transition-colors"
              />
            </div>

            <div>
              <label className="mono block text-xs text-neutral-500 tracking-widest uppercase mb-2">
                Password
              </label>
              <input
                type="password"
                placeholder="••••••••"
                className="mono w-full bg-transparent border border-neutral-800 text-white px-3 py-3 text-sm placeholder:text-neutral-700 focus:outline-none focus:border-neutral-600 transition-colors"
              />
            </div>

            <button
              type="submit"
              className="mono w-full bg-neutral-100 hover:bg-neutral-300 text-neutral-950 py-3 text-sm font-medium tracking-wide transition-colors"
            >
              Create account →
            </button>
          </form>

          <div className="h-px bg-neutral-800 mt-10 mb-6" />

          <p className="mono text-xs text-neutral-600 text-center">
            Already have an account?{" "}
            <a href="/login" className="text-neutral-400 hover:text-neutral-300 transition-colors">
              Sign in
            </a>
          </p>
        </div>
      </div>
    </>
  )
}
