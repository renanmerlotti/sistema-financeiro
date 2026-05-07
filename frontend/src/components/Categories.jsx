import Sidebar from "./layout/Sidebar";
import Header from "./layout/Header";

const MOCK_CATEGORIES = [
  { id: 1, name: "Salary",     type: "income",  total: 5000.00, count: 1 },
  { id: 2, name: "Freelance",  type: "income",  total: 1500.00, count: 2 },
  { id: 3, name: "Food",       type: "expense", total:  505.40, count: 8 },
  { id: 4, name: "Housing",    type: "expense", total: 1200.00, count: 1 },
  { id: 5, name: "Transport",  type: "expense", total:  250.00, count: 3 },
  { id: 6, name: "Utilities",  type: "expense", total:  279.90, count: 2 },
];

function formatAmount(amount) {
  return amount.toLocaleString("en-US", { minimumFractionDigits: 2 });
}

function CategoryRow({ name, type, total, count }) {
  const isIncome = type === "income";
  return (
    <div className="flex items-center justify-between px-5 py-4 border-b border-neutral-800 last:border-0 hover:bg-neutral-900/50 transition-colors">
      <div className="flex items-center gap-4">
        <div>
          <p className="mono text-white text-sm">{name}</p>
          <p className="mono text-neutral-500 text-xs mt-0.5">
            {count} {count === 1 ? "transaction" : "transactions"}
          </p>
        </div>
      </div>
      <div className="flex items-center gap-6">
        <span
          className={`mono text-xs px-2 py-0.5 border ${
            isIncome
              ? "border-neutral-500 text-neutral-200"
              : "border-neutral-700 text-neutral-400"
          }`}
        >
          {type}
        </span>
        <p className={`mono text-sm ${isIncome ? "text-white" : "text-neutral-400"}`}>
          ${formatAmount(total)}
        </p>
      </div>
    </div>
  );
}

function Section({ title, categories }) {
  return (
    <div>
      <p className="mono text-neutral-300 text-[13px] tracking-widest uppercase mb-3">
        {title}
      </p>
      <div className="border border-neutral-800">
        {categories.map((cat) => (
          <CategoryRow key={cat.id} {...cat} />
        ))}
      </div>
    </div>
  );
}

export default function Categories() {
  const income  = MOCK_CATEGORIES.filter((c) => c.type === "income");
  const expense = MOCK_CATEGORIES.filter((c) => c.type === "expense");

  return (
    <div className="flex h-screen bg-neutral-950 overflow-hidden">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-auto p-6">

          <div className="flex items-center justify-between mb-6">
            <div>
              <h1 className="font-display text-white text-xl">Categories</h1>
              <p className="mono text-neutral-400 text-xs mt-1">
                {MOCK_CATEGORIES.length} categories
              </p>
            </div>
            <button className="mono bg-neutral-100 hover:bg-neutral-300 text-neutral-950 text-xs px-4 py-2 transition-colors">
              + New category
            </button>
          </div>

          <div className="flex flex-col gap-8">
            <Section title="Income" categories={income} />
            <Section title="Expense" categories={expense} />
          </div>

        </main>
      </div>
    </div>
  );
}
