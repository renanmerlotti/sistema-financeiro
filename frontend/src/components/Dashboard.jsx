import Sidebar from "./layout/Sidebar";
import Header from "./layout/Header";

export default function Dashboard() {
  return (
    <div className="flex h-screen bg-neutral-950 overflow-hidden">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-auto p-6"></main>
      </div>
    </div>
  );
}
