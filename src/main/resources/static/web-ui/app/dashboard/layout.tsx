import Sidebar from "@/components/sidebar/sidebar";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="flex w-full min-h-screen">
      {/* Сайдбар */}
      <Sidebar />

      {/* Основной контент */}
      <main className="w-full ml-64 p-0 bg-gray-900 overflow-y-auto">
  <div className="w-full h-full p-4">
    {children}
  </div>
</main>
    </div>
   
  );
}