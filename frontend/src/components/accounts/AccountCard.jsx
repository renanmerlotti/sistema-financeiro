function formatBalance(balance) {
  return balance.toLocaleString("en-US", { minimumFractionDigits: 2 });
}

const TYPE_LABELS = {
  checking: "Checking",
  savings:  "Savings",
  wallet:   "Wallet",
};

export default function AccountCard({ name, type, balance }) {
  return (
    <div className="bg-neutral-900 border border-neutral-800 p-5 flex flex-col gap-4 hover:border-neutral-600 transition-colors">
      <div className="flex items-center justify-between">
        <span className="mono text-neutral-500 text-xs">{TYPE_LABELS[type] ?? type}</span>
      </div>
      <div>
        <p className="font-display text-white text-lg leading-tight">{name}</p>
      </div>
      <div className="border-t border-neutral-800 pt-4">
        <p className="mono text-neutral-500 text-xs mb-1">Balance</p>
        <p className="mono text-white text-xl">${formatBalance(balance)}</p>
      </div>
    </div>
  );
}
