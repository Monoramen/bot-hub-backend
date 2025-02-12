// app/commands/page.tsx
"use client";

import { useState } from "react";
import Commands from "../../app/components/Commands";
import Keyboards from "../../app/components/Keyboards";

const CommandsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<"commands" | "keyboards">("commands");

  return (
    <div>
      <div className="flex gap-4 mb-8">
        {/* Кнопки для переключения вкладок */}
        <button
          onClick={() => setActiveTab("commands")}
          className={`w-32 h-12 rounded-md ${
            activeTab === "commands" ? "bg-blue-500" : "bg-gray-700"
          } text-white font-bold flex justify-center items-center`}
        >
          Commands
        </button>
        <button
          onClick={() => setActiveTab("keyboards")}
          className={`w-32 h-12 rounded-md ${
            activeTab === "keyboards" ? "bg-blue-500" : "bg-gray-700"
          } text-white font-bold flex justify-center items-center`}
        >
          Keyboards
        </button>
      </div>

      {/* Отображение контента */}
      {activeTab === "commands" ? <Commands /> : <Keyboards />}
    </div>
  );
};

export default CommandsPage;
