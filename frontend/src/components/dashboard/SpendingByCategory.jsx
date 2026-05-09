const fmt = (val) =>
  new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(val ?? 0)

const PALETTE = ['#f87171', '#fb923c', '#facc15', '#4ade80', '#60a5fa', '#c084fc']

export default function SpendingByCategory({ spendingByCategory }) {
  return (
    <div className="bg-neutral-900 border border-neutral-800 p-5">
      <p className="mono text-white text-sm mb-1">Spending by category</p>
      <p className="mono text-neutral-500 text-xs mb-5">
        {spendingByCategory.length} categor{spendingByCategory.length === 1 ? 'y' : 'ies'} ·{' '}
        {fmt(spendingByCategory.reduce((acc, c) => acc + parseFloat(c.amount), 0))} total
      </p>

      {spendingByCategory.length === 0 ? (
        <p className="mono text-neutral-600 text-xs py-4 text-center">No expenses in this period.</p>
      ) : (
        <div className="space-y-4">
          {spendingByCategory.map((cat, i) => {
            const color = PALETTE[i % PALETTE.length]
            return (
              <div key={cat.categoryName}>
                <div className="flex items-center justify-between mb-1.5">
                  <span className="mono text-neutral-300 text-xs">{cat.categoryName}</span>
                  <span className="mono text-neutral-500 text-xs">
                    {fmt(cat.amount)} · {parseFloat(cat.percentage).toFixed(1)}%
                  </span>
                </div>
                <div className="h-1 bg-neutral-800">
                  <div
                    className="h-1"
                    style={{
                      width: `${parseFloat(cat.percentage).toFixed(1)}%`,
                      backgroundColor: color,
                    }}
                  />
                </div>
              </div>
            )
          })}
        </div>
      )}
    </div>
  )
}
