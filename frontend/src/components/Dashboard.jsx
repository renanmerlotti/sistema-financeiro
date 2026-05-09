import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'
import Sidebar from './layout/Sidebar'
import Header from './layout/Header'
import SummaryCards from './dashboard/SummaryCards'
import CashflowChart from './dashboard/CashflowChart'
import SpendingByCategory from './dashboard/SpendingByCategory'
import RecentTransactions from './dashboard/RecentTransactions'
import AccountsList from './dashboard/AccountsList'

function greeting() {
  const h = new Date().getHours()
  if (h < 12) return 'Good morning'
  if (h < 18) return 'Good afternoon'
  return 'Good evening'
}

export default function Dashboard() {
  const { user } = useAuth()
  const [period, setPeriod] = useState('THIS_MONTH')
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    setLoading(true)
    api.get(`/api/v1/dashboard?period=${period}`)
      .then(res => setData(res.data))
      .finally(() => setLoading(false))
  }, [period])

  const firstName = user?.username?.split(' ')[0] ?? ''

  return (
    <div className="flex h-screen bg-neutral-950 overflow-hidden">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header period={period} onPeriodChange={setPeriod} />
        <main className="flex-1 overflow-auto p-6">

          <div className="mb-6">
            <h1 className="serif text-white text-2xl">{greeting()}, {firstName}.</h1>
            <p className="mono text-neutral-500 text-xs mt-1">Here's how your money moved this month.</p>
          </div>

          {loading && (
            <p className="mono text-neutral-600 text-sm">Loading...</p>
          )}

          {!loading && data && (
            <>
              <SummaryCards data={data} />
              <div className="grid grid-cols-[1fr_360px] gap-4">
                <div className="flex flex-col gap-4">
                  <CashflowChart dailyCashflow={data.dailyCashflow} />
                  <RecentTransactions recentTransactions={data.recentTransactions} />
                </div>
                <div className="flex flex-col gap-4">
                  <SpendingByCategory spendingByCategory={data.spendingByCategory} />
                  <AccountsList accounts={data.accounts} />
                </div>
              </div>
            </>
          )}

        </main>
      </div>
    </div>
  )
}
