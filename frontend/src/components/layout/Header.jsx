import { useLocation, useNavigate } from 'react-router-dom'

const FILTERS = [
  { label: 'This month', value: 'THIS_MONTH' },
  { label: 'Last month', value: 'LAST_MONTH' },
  { label: 'All time',   value: 'ALL_TIME'   },
]

const PAGE_LABELS = {
  '/dashboard':    'Dashboard',
  '/transactions': 'Transactions',
  '/accounts':     'Accounts',
  '/categories':   'Categories',
}

const FILTER_PAGES = ['/dashboard']

export default function Header({ period, onPeriodChange }) {
  const { pathname } = useLocation()
  const navigate = useNavigate()
  const pageLabel = PAGE_LABELS[pathname] ?? 'Dashboard'
  const showFilter = FILTER_PAGES.includes(pathname)

  return (
    <header className="h-14 border-b border-neutral-800 flex items-center justify-between px-6 shrink-0">

      <div className="flex items-center gap-2">
        <span className="mono text-neutral-500 text-sm">Workspace</span>
        <span className="mono text-neutral-700 text-sm">/</span>
        <span className="mono text-white text-sm">{pageLabel}</span>
      </div>

      <div className="flex items-center gap-3">

        {showFilter && (
          <div className="flex border border-neutral-800">
            {FILTERS.map(({ label, value }, i) => (
              <button
                key={value}
                onClick={() => onPeriodChange?.(value)}
                className={`mono text-xs px-3 py-1.5 transition-colors ${
                  i > 0 ? 'border-l border-neutral-800' : ''
                } ${
                  period === value
                    ? 'bg-neutral-800 text-white'
                    : 'text-neutral-500 hover:text-neutral-300'
                }`}
              >
                {label}
              </button>
            ))}
          </div>
        )}

        <button
          onClick={() => navigate('/transactions', { state: { openNew: true } })}
          className="mono bg-neutral-100 hover:bg-neutral-300 text-neutral-950 text-xs px-4 py-2 transition-colors"
        >
          + New transaction
        </button>

      </div>
    </header>
  )
}
