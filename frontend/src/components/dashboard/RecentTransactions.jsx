import { useNavigate } from 'react-router-dom'
import { ArrowRight } from 'lucide-react'

const fmt = (val) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(val ?? 0)

const fmtDate = (dateStr) => {
  const [year, month, day] = dateStr.split('-')
  return new Date(year, month - 1, day).toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
  })
}

export default function RecentTransactions({ recentTransactions }) {
  const navigate = useNavigate()

  return (
    <div className="bg-neutral-900 border border-neutral-800 p-5">
      <div className="flex items-center justify-between mb-1">
        <p className="mono text-white text-sm">Recent transactions</p>
        <button
          onClick={() => navigate('/transactions')}
          className="flex items-center gap-1 mono text-xs text-neutral-500 hover:text-neutral-300 transition-colors"
        >
          View all <ArrowRight size={11} />
        </button>
      </div>
      <p className="mono text-neutral-500 text-xs mb-5">Last 6 entries</p>

      {recentTransactions.length === 0 ? (
        <p className="mono text-neutral-600 text-xs py-4 text-center">No transactions yet.</p>
      ) : (
        <div className="divide-y divide-neutral-800">
          {recentTransactions.map((t) => {
            const isIncome = t.transactionType === 'INCOME'
            return (
              <div key={t.id} className="flex items-center justify-between py-3">
                <div>
                  <p className="mono text-neutral-200 text-xs">{t.description}</p>
                  <p className="mono text-neutral-600 text-xs mt-0.5">
                    {t.categoryName} · {t.accountName}
                  </p>
                </div>
                <div className="text-right ml-4 shrink-0">
                  <p className={isIncome ? 'mono text-xs text-emerald-400' : 'mono text-xs text-red-400'}>
                    {isIncome ? '+' : '-'}{fmt(t.amount)}
                  </p>
                  <p className="mono text-neutral-600 text-xs mt-0.5">{fmtDate(t.date)}</p>
                </div>
              </div>
            )
          })}
        </div>
      )}
    </div>
  )
}
