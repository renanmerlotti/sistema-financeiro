import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { Pencil, Trash2 } from "lucide-react";
import Sidebar from "./layout/Sidebar";
import Header from "./layout/Header";
import TransactionSlideOver from "./transactions/TransactionSlideOver";
import api from "../api/axios";

function formatAmount(amount, type) {
  const formatted = Number(amount).toLocaleString("en-US", { minimumFractionDigits: 2 });
  return type === "INCOME" ? `+ $${formatted}` : `- $${formatted}`;
}

function formatDate(dateStr) {
  const [year, month, day] = dateStr.split("-");
  return `${month}/${day}/${year}`;
}

function TypeBadge({ type }) {
  const isIncome = type === "INCOME";
  return (
    <span
      className={`mono text-xs px-2 py-0.5 border ${
        isIncome
          ? "border-neutral-600 text-neutral-200"
          : "border-neutral-800 text-neutral-500"
      }`}
    >
      {isIncome ? "income" : "expense"}
    </span>
  );
}

export default function Transactions() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [slideOverOpen, setSlideOverOpen] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState(null);

  const { state } = useLocation();

  useEffect(() => {
    if (state?.openNew) setSlideOverOpen(true);
  }, [state]);

  function fetchTransactions() {
    setLoading(true);
    const params = { page, size: 20 };
    if (startDate && endDate) {
      params.startDate = startDate;
      params.endDate = endDate;
    }
    api
      .get("/api/v1/transactions", { params })
      .then((res) => {
        setTransactions(res.data.content);
        setTotalPages(res.data.totalPages);
      })
      .catch(() => setError("Failed to load transactions."))
      .finally(() => setLoading(false));
  }

  function handleDelete(id) {
    api
      .delete(`/api/v1/transactions/${id}`)
      .then(fetchTransactions)
      .catch(() => setError("Failed to delete transaction."));
  }

  useEffect(() => {
    fetchTransactions();
  }, [page, startDate, endDate]);

  return (
    <>
    <div className="flex h-screen bg-neutral-950 overflow-hidden">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-auto p-6">

          <div className="flex items-center justify-between mb-6">
            <div>
              <h1 className="font-display text-white text-xl">Transactions</h1>
              <p className="mono text-neutral-500 text-xs mt-1">
                {loading ? "Loading..." : `${transactions.length} transactions`}
              </p>
            </div>
            <div className="flex items-center gap-2">
              <div className="flex flex-col gap-1">
                <label className="mono text-neutral-500 text-xs">From</label>
                <input
                  type="date"
                  value={startDate}
                  onChange={(e) => { setStartDate(e.target.value); setPage(0); }}
                  className="bg-transparent border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-1.5 mono text-white text-xs transition-colors"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="mono text-neutral-500 text-xs">To</label>
                <input
                  type="date"
                  value={endDate}
                  onChange={(e) => { setEndDate(e.target.value); setPage(0); }}
                  className="bg-transparent border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-1.5 mono text-white text-xs transition-colors"
                />
              </div>
            </div>
          </div>

          {error && <p className="mono text-neutral-500 text-sm">{error}</p>}

          {!loading && !error && transactions.length === 0 && (
            <p className="mono text-neutral-500 text-sm">No transactions found.</p>
          )}

          {!loading && !error && transactions.length > 0 && (
            <div className="border border-neutral-800 mb-4">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-neutral-800">
                    <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">Description</th>
                    <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">Amount</th>
                    <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">Type</th>
                    <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">Category</th>
                    <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">Account</th>
                    <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">Date</th>
                    <th className="px-4 py-3" />
                  </tr>
                </thead>
                <tbody>
                  {transactions.map((tx, i) => (
                    <tr
                      key={tx.id}
                      className={`border-b border-neutral-800 last:border-0 hover:bg-neutral-900 transition-colors ${
                        i % 2 === 0 ? "bg-neutral-950" : "bg-neutral-900/30"
                      }`}
                    >
                      <td className="mono text-white text-sm px-4 py-3">
                        {tx.description ?? "—"}
                      </td>
                      <td className={tx.transactionType === "INCOME" ? "mono text-sm px-4 py-3 text-emerald-400" : "mono text-sm px-4 py-3 text-red-400"}>
                        {formatAmount(tx.amount, tx.transactionType)}
                      </td>
                      <td className="px-4 py-3">
                        <TypeBadge type={tx.transactionType} />
                      </td>
                      <td className="mono text-neutral-400 text-sm px-4 py-3">
                        {tx.categoryName ?? "—"}
                      </td>
                      <td className="mono text-neutral-400 text-sm px-4 py-3">
                        {tx.accountName}
                      </td>
                      <td className="mono text-neutral-500 text-sm px-4 py-3">
                        {formatDate(tx.date)}
                      </td>
                      <td className="px-4 py-3">
                        <div className="flex items-center gap-3 justify-end">
                          <button
                            onClick={() => { setSelectedTransaction(tx); setSlideOverOpen(true); }}
                            className="text-neutral-600 hover:text-neutral-300 transition-colors"
                          >
                            <Pencil size={14} />
                          </button>
                          <button
                            onClick={() => handleDelete(tx.id)}
                            className="text-neutral-600 hover:text-neutral-300 transition-colors"
                          >
                            <Trash2 size={14} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {!loading && !error && totalPages > 1 && (
            <div className="flex items-center justify-between">
              <button
                onClick={() => setPage((p) => p - 1)}
                disabled={page === 0}
                className="mono text-xs text-neutral-500 hover:text-neutral-300 transition-colors disabled:opacity-30 disabled:pointer-events-none"
              >
                ← Previous
              </button>
              <span className="mono text-neutral-500 text-xs">
                Page {page + 1} of {totalPages}
              </span>
              <button
                onClick={() => setPage((p) => p + 1)}
                disabled={page === totalPages - 1}
                className="mono text-xs text-neutral-500 hover:text-neutral-300 transition-colors disabled:opacity-30 disabled:pointer-events-none"
              >
                Next →
              </button>
            </div>
          )}

        </main>
      </div>
    </div>

      <TransactionSlideOver
        isOpen={slideOverOpen}
        onClose={() => { setSlideOverOpen(false); setSelectedTransaction(null); }}
        onSuccess={fetchTransactions}
        transaction={selectedTransaction}
      />
    </>
  );
}
