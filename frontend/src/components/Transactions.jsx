import Sidebar from "./layout/Sidebar";
import Header from "./layout/Header";

const MOCK_TRANSACTIONS = [
  {
    id: 1,
    description: "Monthly salary",
    amount: 5000.0,
    type: "income",
    category: "Salary",
    account: "Checking",
    date: "2026-05-01",
  },
  {
    id: 2,
    description: "Supermarket",
    amount: 320.5,
    type: "expense",
    category: "Food",
    account: "Checking",
    date: "2026-05-02",
  },
  {
    id: 3,
    description: "Electricity bill",
    amount: 180.0,
    type: "expense",
    category: "Utilities",
    account: "Checking",
    date: "2026-05-03",
  },
  {
    id: 4,
    description: "Freelance project",
    amount: 1500.0,
    type: "income",
    category: "Freelance",
    account: "Savings",
    date: "2026-05-04",
  },
  {
    id: 5,
    description: "Restaurant",
    amount: 85.9,
    type: "expense",
    category: "Food",
    account: "Wallet",
    date: "2026-05-05",
  },
  {
    id: 6,
    description: "Internet plan",
    amount: 99.9,
    type: "expense",
    category: "Utilities",
    account: "Checking",
    date: "2026-05-05",
  },
  {
    id: 7,
    description: "Fuel",
    amount: 250.0,
    type: "expense",
    category: "Transport",
    account: "Checking",
    date: "2026-05-04",
  },
  {
    id: 8,
    description: "Rent",
    amount: 1200.0,
    type: "expense",
    category: "Housing",
    account: "Checking",
    date: "2026-05-01",
  },
];

function formatAmount(amount, type) {
  const formatted = amount.toLocaleString("en-US", {
    minimumFractionDigits: 2,
  });
  return type === "income" ? `+ $${formatted}` : `- $${formatted}`;
}

function formatDate(dateStr) {
  const [year, month, day] = dateStr.split("-");
  return `${month}/${day}/${year}`;
}

function TypeBadge({ type }) {
  const isIncome = type === "income";
  return (
    <span
      className={`mono text-xs px-2 py-0.5 border ${
        isIncome
          ? "border-neutral-600 text-neutral-200"
          : "border-neutral-800 text-neutral-500"
      }`}
    >
      {type}
    </span>
  );
}

export default function Transactions() {
  return (
    <div className="flex h-screen bg-neutral-950 overflow-hidden">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-auto p-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h1 className="font-display text-white text-xl">Transactions</h1>
              <p className="mono text-neutral-500 text-xs mt-1">
                {MOCK_TRANSACTIONS.length} transactions this month
              </p>
            </div>
          </div>

          <div className="border border-neutral-800">
            <table className="w-full">
              <thead>
                <tr className="border-b border-neutral-800">
                  <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">
                    Description
                  </th>
                  <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">
                    Amount
                  </th>
                  <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">
                    Type
                  </th>
                  <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">
                    Category
                  </th>
                  <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">
                    Account
                  </th>
                  <th className="mono text-neutral-500 text-xs font-normal text-left px-4 py-3">
                    Date
                  </th>
                </tr>
              </thead>
              <tbody>
                {MOCK_TRANSACTIONS.map((tx, i) => (
                  <tr
                    key={tx.id}
                    className={`border-b border-neutral-800 last:border-0 hover:bg-neutral-900 transition-colors ${
                      i % 2 === 0 ? "bg-neutral-950" : "bg-neutral-900/30"
                    }`}
                  >
                    <td className="mono text-white text-sm px-4 py-3">
                      {tx.description}
                    </td>
                    <td
                      className={`mono text-sm px-4 py-3 ${tx.type === "income" ? "text-white" : "text-neutral-400"}`}
                    >
                      {formatAmount(tx.amount, tx.type)}
                    </td>
                    <td className="px-4 py-3">
                      <TypeBadge type={tx.type} />
                    </td>
                    <td className="mono text-neutral-400 text-sm px-4 py-3">
                      {tx.category}
                    </td>
                    <td className="mono text-neutral-400 text-sm px-4 py-3">
                      {tx.account}
                    </td>
                    <td className="mono text-neutral-500 text-sm px-4 py-3">
                      {formatDate(tx.date)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </main>
      </div>
    </div>
  );
}
