import { Scale, TrendingUp, TrendingDown, PiggyBank } from 'lucide-react'

const fmt = (val) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(val ?? 0)

export default function SummaryCards({ data }) {
  const cards = [
    {
      label: 'Total balance',
      value: fmt(data.totalBalance),
      Icon: Scale,
    },
    {
      label: 'Income',
      value: fmt(data.totalIncome),
      Icon: TrendingUp,
    },
    {
      label: 'Expenses',
      value: fmt(data.totalExpenses),
      Icon: TrendingDown,
    },
    {
      label: 'Savings rate',
      value: `${parseFloat(data.savingsRate ?? 0).toFixed(1)}%`,
      Icon: PiggyBank,
    },
  ]

  return (
    <div className="grid grid-cols-4 gap-3 mb-4">
      {cards.map(({ label, value, Icon }) => (
        <div key={label} className="bg-neutral-900 border border-neutral-800 p-5">
          <div className="flex items-center gap-2 mb-4">
            <Icon size={13} className="text-neutral-500" />
            <span className="mono text-neutral-500 text-xs">{label}</span>
          </div>
          <p className="serif text-white text-2xl leading-none">{value}</p>
        </div>
      ))}
    </div>
  )
}
