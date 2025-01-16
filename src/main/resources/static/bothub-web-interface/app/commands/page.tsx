// app/commands/page.tsx
"use client";

import { useState } from "react";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import Commands from "../components/Commands"; // Компонент с таблицей команд
import Keyboards from "../components/Keyboards"; // Компонент с клавишами
import Foobar from "../components/Foobar"; // Если нужно

const CommandsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<"commands" | "keyboards">("commands");

  return (
    <div className="min-h-screen bg-gray-900 text-gray-100 flex flex-col">
      <Navbar />
      <div className="flex flex-grow justify-center w-full">
        <div className="grid grid-cols-[250px_1fr] w-full">
          <Sidebar />
          <div className="p-4">
            <div className="flex gap-4 mb-8">
              {/* Кнопки для переключения вкладок */}
              <button
                onClick={() => setActiveTab("commands")}
                className={`w-32 h-32 rounded-md ${activeTab === "commands" ? "bg-blue-500" : "bg-gray-700"} text-white font-bold flex justify-center items-center`}
              >
                Commands
              </button>
              <button
                onClick={() => setActiveTab("keyboards")}
                className={`w-32 h-32 rounded-md ${activeTab === "keyboards" ? "bg-blue-500" : "bg-gray-700"} text-white font-bold flex justify-center items-center`}
              >
                Keyboards
              </button>
            </div>

            {/* Отображаем контент в зависимости от выбранной вкладки */}
            {activeTab === "commands" ? (
              <Commands />
            ) : (
              <Keyboards />
            )}
          </div>
        </div>
      </div>
      <Foobar /> {/* Если нужно */}
    </div>
  );
};

export default CommandsPage;
