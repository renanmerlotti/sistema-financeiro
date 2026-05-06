import { useState } from 'react'
import { useLocation } from 'react-router-dom'

const FILTERS = ['This month', 'Last month', 'All time']

const PAGE_LABELS = {
  '/dashboard':    'Dashboard',
  '/transactions': 'Transactions',
  '/accounts':     'Accounts',
  '/categories':   'Categories',
}

export default function Header() {
  const [activeFilter, setActiveFilter] = useState('This month')
  const { pathname } = useLocation()
  const pageLabel = PAGE_LABELS[pathname] ?? 'Dashboard'

  return (
    <header className="h-14 border-b border-neutral-800 flex items-center justify-between px-6 shrink-0">

      <div className="flex items-center gap-2">
        <span className="mono text-neutral-500 text-sm">Workspace</span>
        <span className="mono text-neutral-700 text-sm">/</span>
        <span className="mono text-white text-sm">{pageLabel}</span>
      </div>

      <div className="flex items-center gap-3">

        <div className="flex border border-neutral-800">
          {FILTERS.map((f, i) => (
            <button
              key={f}
              onClick={() => setActiveFilter(f)}
              className={`mono text-xs px-3 py-1.5 transition-colors ${
                i > 0 ? 'border-l border-neutral-800' : ''
              } ${
                activeFilter === f
                  ? 'bg-neutral-800 text-white'
                  : 'text-neutral-500 hover:text-neutral-300'
              }`}
            >
              {f}
            </button>
          ))}
        </div>

        <button className="mono bg-neutral-100 hover:bg-neutral-300 text-neutral-950 text-xs px-4 py-2 transition-colors">
          + New transaction
        </button>

      </div>
    </header>
  )
}
