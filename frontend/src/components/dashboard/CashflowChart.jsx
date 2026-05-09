import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  ReferenceLine,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid,
} from 'recharts'

const fmtDay = (dateStr) => parseInt(dateStr.split('-')[2])

const fmtY = (val) => {
  const abs = Math.abs(val)
  if (abs === 0) return '$0'
  if (abs >= 1000) return `$${(abs / 1000).toFixed(0)}k`
  return `$${abs}`
}

function CustomTooltip({ active, payload, label }) {
  if (!active || !payload?.length) return null
  const [year, month, day] = label.split('-')
  const dateLabel = new Date(year, month - 1, day).toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
  })
  return (
    <div className="bg-neutral-800 border border-neutral-700 p-3 mono text-xs">
      <p className="text-neutral-400 mb-2">{dateLabel}</p>
      {payload.map((p) => (
        <p key={p.dataKey} className="text-neutral-300">
          {p.dataKey === 'income' ? 'Income' : 'Expense'}:{' '}
          {new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(
            Math.abs(p.value)
          )}
        </p>
      ))}
    </div>
  )
}

export default function CashflowChart({ dailyCashflow }) {
  const chartData = dailyCashflow.map((d) => ({
    date: d.date,
    income: parseFloat(d.income),
    expense: -parseFloat(d.expenses),
  }))

  return (
    <div className="bg-neutral-900 border border-neutral-800 p-5">
      <div className="flex items-center justify-between mb-1">
        <p className="mono text-white text-sm">Daily cashflow</p>
        <div className="flex items-center gap-4">
          <span className="flex items-center gap-1.5 mono text-xs text-neutral-500">
            <span className="w-2 h-2 inline-block" style={{ backgroundColor: '#4ade80' }} />
            Income
          </span>
          <span className="flex items-center gap-1.5 mono text-xs text-neutral-500">
            <span className="w-2 h-2 inline-block" style={{ backgroundColor: '#f87171' }} />
            Expense
          </span>
        </div>
      </div>
      <p className="mono text-neutral-600 text-xs mb-5">
        Income above the line · expenses below
      </p>

      {chartData.length === 0 ? (
        <p className="mono text-neutral-600 text-xs py-10 text-center">
          No transactions in this period.
        </p>
      ) : (
        <ResponsiveContainer width="100%" height={200}>
          <BarChart data={chartData} barCategoryGap="25%">
            <CartesianGrid vertical={false} stroke="#262626" />
            <XAxis
              dataKey="date"
              tickFormatter={fmtDay}
              tick={{ fill: '#737373', fontSize: 11, fontFamily: 'DM Mono' }}
              axisLine={false}
              tickLine={false}
              interval="preserveStartEnd"
            />
            <YAxis
              tickFormatter={fmtY}
              tick={{ fill: '#737373', fontSize: 11, fontFamily: 'DM Mono' }}
              axisLine={false}
              tickLine={false}
              width={44}
            />
            <ReferenceLine y={0} stroke="#404040" />
            <Tooltip content={<CustomTooltip />} cursor={{ fill: '#1a1a1a' }} />
            <Bar dataKey="income" fill="#4ade80" radius={0} />
            <Bar dataKey="expense" fill="#f87171" radius={0} />
          </BarChart>
        </ResponsiveContainer>
      )}
    </div>
  )
}
