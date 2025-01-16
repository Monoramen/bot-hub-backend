import Sidebar from "./Sidebar";
import Dashboard from "./Dashboard";

export default function Main() {
  return (
    <div className=" flex justify-center grid grid-cols-[250px_1fr] bg-gray-900 text-gray-100">
      <Sidebar />
      <div className="p-4">
        <Dashboard />
      </div>
    </div>
  );
}
