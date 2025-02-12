export default function DashboardPage() {
  return (
    <div className="w-full">
      <h1 className="text-2xl font-bold mb-4">Добро пожаловать в панель управления</h1>
      <p className="text-gray-600">
        Здесь вы можете управлять своим Telegram-ботом, создавать команды, настраивать клавиатуры и просматривать статистику.
      </p>

      {/* Пример карточек */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-8">
        <div className="bg-blue-700 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold">Команды</h2>
          <p className="text-gray-400 mt-2">Управляйте командами вашего бота.</p>
        </div>
        <div className="bg-blue-700 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold">Клавиатуры</h2>
          <p className="text-gray-400 mt-2">Настройте клавиатуры и кнопки.</p>
        </div>
        <div className="bg-blue-700 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold">Статистика</h2>
          <p className="text-gray-400 mt-2">Просматривайте статистику использования бота.</p>
        </div>
      </div>
    </div>
  );
}