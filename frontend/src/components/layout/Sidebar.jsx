import { NavLink } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import {
  LayoutGrid,
  ArrowLeftRight,
  Wallet,
  Tag,
  ChevronsUpDown,
} from "lucide-react";

const WORKSPACE_NAV = [
  { label: "Dashboard", path: "/dashboard", Icon: LayoutGrid },
  { label: "Transactions", path: "/transactions", Icon: ArrowLeftRight },
  { label: "Accounts", path: "/accounts", Icon: Wallet },
  { label: "Categories", path: "/categories", Icon: Tag },
];

function NavItem({ label, path, Icon }) {
  return (
    <NavLink
      to={path}
      className={({ isActive }) =>
        `flex items-center px-3 py-2 mono text-sm transition-colors ${
          isActive
            ? "bg-neutral-800 text-white"
            : "text-neutral-400 hover:text-neutral-200 hover:bg-neutral-800/50"
        }`
      }
    >
      <span className="flex items-center gap-3">
        <Icon size={14} />
        {label}
      </span>
    </NavLink>
  );
}

export default function Sidebar() {
  const { user } = useAuth();

  const initials = user?.username
    ? user.username
        .split(" ")
        .map((w) => w[0])
        .join("")
        .slice(0, 2)
        .toUpperCase()
    : "?";

  return (
    <aside className="w-[215px] h-screen bg-neutral-900 border-r border-neutral-800 flex flex-col shrink-0">
      <div className="px-4 py-5 border-b border-neutral-800">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 bg-neutral-100 text-neutral-950 flex items-center justify-center text-sm font-semibold mono shrink-0">
            S
          </div>
          <div>
            <p className="mono text-white text-sm leading-tight">Sistema</p>
            <p className="mono text-neutral-500 text-xs leading-tight mt-0.5">
              Personal finance
            </p>
          </div>
        </div>
      </div>

      <nav className="flex-1 px-2 pt-5 space-y-0.5">
        <p className="mono text-neutral-600 text-xs tracking-widest uppercase px-3 mb-2">
          Workspace
        </p>
        {WORKSPACE_NAV.map((item) => (
          <NavItem key={item.path} {...item} />
        ))}
      </nav>

      <div className="border-t border-neutral-800 px-3 py-3">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 rounded-full bg-neutral-800 border border-neutral-700 text-neutral-300 flex items-center justify-center text-xs mono shrink-0">
            {initials}
          </div>
          <div className="flex-1 min-w-0">
            <p className="mono text-white text-xs leading-tight truncate">
              {user?.username ?? "—"}
            </p>
            <p className="mono text-neutral-500 text-xs leading-tight truncate mt-0.5">
              {user?.email ?? "—"}
            </p>
          </div>
          <button className="text-neutral-600 hover:text-neutral-400 transition-colors shrink-0">
            <ChevronsUpDown size={14} />
          </button>
        </div>
      </div>
    </aside>
  );
}
