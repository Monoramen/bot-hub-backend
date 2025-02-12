// app/commands/layout.tsx
"use client";

import Navbar from "../../app/components/Navbar";
import Sidebar from "../../app/components/Sidebar";

export default function CommandsLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="min-h-screen bg-gray-900 text-gray-100 flex flex-col">
      <Navbar />
      <div className="flex flex-grow justify-center w-full">
        <div className="grid grid-cols-[250px_1fr] w-full">
          <Sidebar />
          <div className="p-4">{children}</div>
        </div>
      </div>
    </div>
  );
}
