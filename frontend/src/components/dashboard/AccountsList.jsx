import { useNavigate } from 'react-router-dom'
import { ArrowRight, Landmark, PiggyBank, Wallet } from 'lucide-react'

const fmt = (val) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(val ?? 0)

const ACCOUNT_TYPE = {
  CHECKING: { label: 'Checking', Icon: Landmark },
  SAVINGS:  { label: 'Savings',  Icon: PiggyBank },
  WALLET:   { label: 'Wallet',   Icon: Wallet },
}

export default function AccountsList({ accounts }) {
  const navigate = useNavigate()
  const total = accounts.reduce((acc, a) => acc + parseFloat(a.balance), 0)

  return (
    <div className="bg-neutral-900 border border-neutral-800 p-5">
      <div className="flex items-center justify-between mb-1">
        <p className="mono text-white text-sm">Accounts</p>
        <button
          onClick={() => navigate('/accounts')}
          className="flex items-center gap-1 mono text-xs text-neutral-500 hover:text-neutral-300 transition-colors"
        >
          Manage <ArrowRight size={11} />
        </button>
      </div>
      <p className="mono text-neutral-500 text-xs mb-5">
        {accounts.length} account{accounts.length !== 1 ? 's' : ''} · {fmt(total)} total
      </p>

      {accounts.length === 0 ? (
        <p className="mono text-neutral-600 text-xs py-4 text-center">No accounts yet.</p>
      ) : (
        <div className="divide-y divide-neutral-800">
          {accounts.map((a) => {
            const { label, Icon } = ACCOUNT_TYPE[a.type] ?? { label: a.type, Icon: Wallet }
            const isNegative = parseFloat(a.balance) < 0
            return (
              <div key={a.id} className="flex items-center justify-between py-3">
                <div className="flex items-center gap-3">
                  <div className="w-7 h-7 bg-neutral-800 flex items-center justify-center shrink-0">
                    <Icon size={13} className="text-neutral-400" />
                  </div>
                  <div>
                    <p className="mono text-neutral-200 text-xs">{a.name}</p>
                    <p className="mono text-neutral-600 text-xs mt-0.5">{label}</p>
                  </div>
                </div>
                <p className={`mono text-xs ${isNegative ? 'text-red-400' : 'text-neutral-200'}`}>
                  {fmt(a.balance)}
                </p>
              </div>
            )
          })}
        </div>
      )}
    </div>
  )
}
