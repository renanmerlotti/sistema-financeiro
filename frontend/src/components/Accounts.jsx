import { useEffect, useState } from "react";
import Sidebar from "./layout/Sidebar";
import Header from "./layout/Header";
import AccountCard from "./accounts/AccountCard";
import AccountSlideOver from "./accounts/AccountSlideOver";
import api from "../api/axios";

export default function Accounts() {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [slideOverOpen, setSlideOverOpen] = useState(false);
  const [selectedAccount, setSelectedAccount] = useState(null);

  function handleDelete(id) {
    api
      .delete(`/api/v1/accounts/${id}`)
      .then(fetchAccounts)
      .catch(() => setError("Cannot delete account with associated transactions."));
  }

  function fetchAccounts() {
    setLoading(true);
    api
      .get("/api/v1/accounts")
      .then((res) => setAccounts(res.data))
      .catch(() => setError("Failed to load accounts."))
      .finally(() => setLoading(false));
  }

  useEffect(() => {
    fetchAccounts();
  }, []);

  return (
    <>
      <div className="flex h-screen bg-neutral-950 overflow-hidden">
        <Sidebar />
        <div className="flex-1 flex flex-col overflow-hidden">
          <Header />
          <main className="flex-1 overflow-auto p-6">

            <div className="flex items-center justify-between mb-6">
              <div>
                <h1 className="font-display text-white text-xl">Accounts</h1>
                <p className="mono text-neutral-500 text-xs mt-1">
                  {loading ? "Loading..." : `${accounts.length} accounts`}
                </p>
              </div>
              <button
                onClick={() => { setSelectedAccount(null); setSlideOverOpen(true); }}
                className="mono bg-neutral-100 hover:bg-neutral-300 text-neutral-950 text-xs px-4 py-2 transition-colors"
              >
                + New account
              </button>
            </div>

            {error && (
              <p className="mono text-neutral-500 text-sm">{error}</p>
            )}

            {!loading && !error && accounts.length === 0 && (
              <p className="mono text-neutral-500 text-sm">No accounts found.</p>
            )}

            {!loading && !error && accounts.length > 0 && (
              <div className="grid grid-cols-3 gap-4">
                {accounts.map((account) => (
                  <AccountCard
                    key={account.id}
                    name={account.name}
                    type={account.type.toLowerCase()}
                    balance={account.balance}
                    onDelete={() => handleDelete(account.id)}
                    onEdit={() => { setSelectedAccount(account); setSlideOverOpen(true); }}
                  />
                ))}
              </div>
            )}

          </main>
        </div>
      </div>

      <AccountSlideOver
        isOpen={slideOverOpen}
        onClose={() => { setSlideOverOpen(false); setSelectedAccount(null); }}
        onSuccess={fetchAccounts}
        account={selectedAccount}
      />
    </>
  );
}
