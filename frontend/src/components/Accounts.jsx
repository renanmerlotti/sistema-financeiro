import Sidebar from "./layout/Sidebar";
import Header from "./layout/Header";
import AccountCard from "./accounts/AccountCard";

const MOCK_ACCOUNTS = [
  { id: 1, name: "Main Checking",   type: "checking", balance: 3200.00 },
  { id: 2, name: "Emergency Fund",  type: "savings",  balance: 8500.00 },
  { id: 3, name: "Daily Wallet",    type: "wallet",   balance:  120.50 },
];

export default function Accounts() {
  return (
    <div className="flex h-screen bg-neutral-950 overflow-hidden">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-auto p-6">

          <div className="flex items-center justify-between mb-6">
            <div>
              <h1 className="font-display text-white text-xl">Accounts</h1>
              <p className="mono text-neutral-500 text-xs mt-1">
                {MOCK_ACCOUNTS.length} accounts
              </p>
            </div>
            <button className="mono bg-neutral-100 hover:bg-neutral-300 text-neutral-950 text-xs px-4 py-2 transition-colors">
              + New account
            </button>
          </div>

          <div className="grid grid-cols-3 gap-4">
            {MOCK_ACCOUNTS.map((account) => (
              <AccountCard key={account.id} {...account} />
            ))}
          </div>

        </main>
      </div>
    </div>
  );
}
