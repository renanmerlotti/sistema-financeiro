import { useState, useEffect } from "react";
import { X } from "lucide-react";
import api from "../../api/axios";

const ACCOUNT_TYPES = ["CHECKING", "SAVINGS", "WALLET"];

export default function AccountSlideOver({ isOpen, onClose, onSuccess, account }) {
  const isEditing = !!account;

  const [name, setName] = useState("");
  const [type, setType] = useState("CHECKING");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (account) {
      setName(account.name);
      setType(account.type.toUpperCase());
    } else {
      setName("");
      setType("CHECKING");
    }
    setError(null);
  }, [account, isOpen]);

  function handleClose() {
    setError(null);
    onClose();
  }

  function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError(null);

    const request = isEditing
      ? api.put(`/api/v1/accounts/${account.id}`, { name, type })
      : api.post("/api/v1/accounts", { name, type });

    request
      .then(() => {
        handleClose();
        onSuccess();
      })
      .catch(() => setError(isEditing ? "Failed to update account." : "Failed to create account."))
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
            {isEditing ? "Edit account" : "New account"}
          </h2>
          <button
            onClick={handleClose}
            className="text-neutral-500 hover:text-neutral-300 transition-colors"
          >
            <X size={16} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col flex-1 px-5 py-6 gap-5">
          <div className="flex flex-col gap-1.5">
            <label className="mono text-neutral-500 text-xs">Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g. Main Checking"
              required
              className="bg-transparent border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-2 mono text-white text-sm placeholder:text-neutral-700 transition-colors"
            />
          </div>

          <div className="flex flex-col gap-1.5">
            <label className="mono text-neutral-500 text-xs">Type</label>
            <select
              value={type}
              onChange={(e) => setType(e.target.value)}
              className="bg-neutral-900 border border-neutral-800 focus:border-neutral-600 outline-none px-3 py-2 mono text-white text-sm transition-colors"
            >
              {ACCOUNT_TYPES.map((t) => (
                <option key={t} value={t}>
                  {t.charAt(0) + t.slice(1).toLowerCase()}
                </option>
              ))}
            </select>
          </div>

          {error && <p className="mono text-neutral-500 text-xs">{error}</p>}

          <div className="mt-auto">
            <button
              type="submit"
              disabled={loading}
              className="w-full mono bg-neutral-100 hover:bg-neutral-300 text-neutral-950 text-xs py-2.5 transition-colors disabled:opacity-50"
            >
              {loading ? (isEditing ? "Saving..." : "Creating...") : (isEditing ? "Save changes" : "Create account")}
            </button>
          </div>
        </form>
      </div>
    </>
  );
}
