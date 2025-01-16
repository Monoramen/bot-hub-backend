import Navbar from "./components/Navbar";
import Main from "./components/Main";
import Sidebar from "./components/Sidebar";
import Foobar from "./components/Foobar";
import Dashboard from "./components/Dashboard";

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-900 text-gray-100 flex flex-col">
      <Navbar />
      <div className="flex flex-grow justify-center w-full">
        <div className="grid grid-cols-[250px_1fr] w-full">
          <Sidebar />
          <div className="p-4">
            <Dashboard />
          </div>
        </div>
      </div>
      <Foobar />
    </div>
  );
}
