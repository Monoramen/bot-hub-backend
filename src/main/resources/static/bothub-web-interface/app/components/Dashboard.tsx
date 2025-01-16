export default function Dashboard() {
    return (
      <main className="flex flex-col gap-4">
        <h1 className="text-2xl font-bold">Home Page</h1>
        <p>
          Welcome to the home page! This is the primary content area where you can
          display your main components or data.
        </p>
        <div className="flex flex-wrap gap-4">
          {/* Пример секции данных */}
          <div className="bg-gray-800 p-4 rounded shadow-md w-full md:w-1/2">
            <h2 className="text-lg font-semibold">Section 1</h2>
            <p>Details about section 1.</p>
          </div>
          <div className="bg-gray-800 p-4 rounded shadow-md w-full md:w-1/2">
            <h2 className="text-lg font-semibold">Section 2</h2>
            <p>Details about section 2.</p>
          </div>
        </div>
      </main>
    );
  }
  