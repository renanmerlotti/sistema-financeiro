import { useState, useEffect } from "react";
import { X } from "lucide-react";
import api from "../../api/axios";

const TRANSACTION_TYPES = ["INCOME", "EXPENSE"];

const today = new Date().toISOString().split("T")[0];

export default function TransactionSlideOver({ isOpen, onClose, onSuccess, transaction }) {
  const isEditing = !!transaction;

  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState("");
  const [date, setDate] = useState(today);
  const [transactionType, setTransactionType] = useState("EXPENSE");
  const [accountId, setAccountId] = useState("");
  const [categoryId, setCategoryId] = useState("");

  const [accounts, setAccounts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loadingSelects, setLoadingSelects] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!isOpen) return;

    setLoadingSelects(true);
    Promise.all([
      api.get("/api/v1/accounts"),
      api.get("/api/v1/categories"),
    ])
      .then(([accountsRes, categoriesRes]) => {
        setAccounts(accountsRes.data);
        setCategories(categoriesRes.data);
      })
      .finally(() => setLoadingSelects(false));

    if (transaction) {
      setDescription(transaction.description ?? "");
      setAmount(transaction.amount);
      setDate(transaction.date);
      setTransactionType(transaction.transactionType);
      setAccountId(transaction.accountId);
      setCategoryId(transaction.categoryId ?? "");
    } else {
      setDescription("");
      setAmount("");
      setDate(today);
      setTransactionType("EXPENSE");
      setAccountId("");
      setCategoryId("");
    }

    setError(null);
  }, [transaction, isOpen]);

  function handleClose() {
    setError(null);
    onClose();
  }

  function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError(null);

    const body = {
      description: description || null,
      amount,
      date,
      transactionType,
      accountId: Number(accountId),
      categoryId: categoryId ? Number(categoryId) : null,
    };

    const request = isEditing
      ? api.put(`/api/v1/transactions/${transaction.id}`, body)
      : api.post("/api/v1/transactions", body);

    request
      .then(() => {
        handleClose();
        onSuccess();
      })
      .catch(() => setError(isEditing ? "Failed to update transaction." : "Failed to create transaction."))
      .finally(() => setLoading(false));
  }

  return (
    <>
      <div
        className={`fixed inset-0 bg-black/50 z-40 transition-opacity duration-300 ${
          isOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"
        }`}
        onClick={handleClose}
      />

      <div
        className={`fixed top-0 right-0 h-full w-80 bg-neutral-900 border-l border-neutral-800 z-50 flex flex-col transition-transform duration-300 ${
          isOpen ? "translate-x-0" : "translate-x-full"
        }`}
      >
        <div className="flex items-center justify-between px-5 py-4 border-b border-neutral-800">
          <h2 className="font-display text-white text-base">
            {isEditing ? "Edit transaction" : "New transaction"}
          </h2>
          <button
            onClick={handleClose}
            className="text-neutral-500 hover:text-neutral-300 transition-colors"
          >
            <X size={16} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col flex-1 px-5 py-6 gap-5 overflow-y-auto">
          <div className="flex flex-col gap-1.5">
            <label className="mono text-neutral-500 text-xs">Description</label>
            <input
              type="text"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="e.g. Supermarket"
              className="bg-transparent border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-2 mono text-white text-sm placeholder:text-neutral-700 transition-colors"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="mono text-neutral-500 text-xs">Amount</label>
            <input
              type="number"
              step="0.01"
              min="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="0.00"
              required
              className="bg-transparent border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-2 mono text-white text-sm placeholder:text-neutral-700 transition-colors"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="mono text-neutral-500 text-xs">Date</label>
            <input
              type="date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              required
              className="bg-transparent border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-2 mono text-white text-sm transition-colors"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="mono text-neutral-500 text-xs">Type</label>
            <select
              value={transactionType}
              onChange={(e) => setTransactionType(e.target.value)}
              className="bg-neutral-900 border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-2 mono text-white text-sm transition-colors"
            >
              {TRANSACTION_TYPES.map((t) => (
                <option key={t} value={t}>
                  {t.charAt(0) + t.slice(1).toLowerCase()}
                </option>
              ))}
            </select>
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="mono text-neutral-500 text-xs">Account</label>
            <select
              value={accountId}
              onChange={(e) => setAccountId(e.target.value)}
              required
              disabled={loadingSelects}
              className="bg-neutral-900 border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-2 mono text-white text-sm transition-colors disabled:opacity-50"
            >
              <option value="">{loadingSelects ? "Loading..." : "Select account"}</option>
              {accounts.map((a) => (
                <option key={a.id} value={a.id}>{a.name}</option>
              ))}
            </select>
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="mono text-neutral-500 text-xs">Category</label>
            <select
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
              disabled={loadingSelects}
              className="bg-neutral-900 border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-2 mono text-white text-sm transition-colors disabled:opacity-50"
            >
              <option value="">{loadingSelects ? "Loading..." : "None"}</option>
              {categories.map((c) => (
                <option key={c.id} value={c.id}>{c.name}</option>
              ))}
            </select>
          </div>

          {error && <p className="mono text-neutral-500 text-xs">{error}</p>}

          <div className="mt-auto">
            <button
              type="submit"
              disabled={loading || loadingSelects}
              className="w-full mono bg-neutral-100 hover:bg-neutral-300 text-neutral-950 text-xs py-2.5 transition-colors disabled:opacity-50"
            >
              {loading
                ? isEditing ? "Saving..." : "Creating..."
                : isEditing ? "Save changes" : "Create transaction"}
            </button>
          </div>
        </form>
      </div>
    </>
  );
}
