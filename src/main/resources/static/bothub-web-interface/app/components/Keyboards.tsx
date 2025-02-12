// components/Keyboards.tsx
"use client";

import React, { useEffect, useState } from "react";
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@nextui-org/react";

// Типы для данных из API
interface InlineButtonDto {
  text: string;
  url: string;
}

interface InlineKeyboardDto {
  inline_keyboard_name: string;
  buttons: InlineButtonDto[];
}

const Keyboards: React.FC = () => {
  const [keyboards, setKeyboards] = useState<InlineKeyboardDto[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // Получаем данные с API
    const fetchKeyboards = async () => {
      try {
        const response = await fetch("http://localhost:9090/inline-keyboards");
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await response.json();
        setKeyboards(data);
      } catch (error: any) {
        console.error("Error fetching keyboards:", error);
        setError(error.message);
      }
    };

    fetchKeyboards();
  }, []);

  const columns = [
    { key: "inline_keyboard_name", label: "Keyboard Name" },
    { key: "buttons", label: "Buttons" },
  ];

  return (
    <div className="bg-gray-800 text-gray-100 rounded-md shadow-lg p-6">
      <h1 className="text-2xl font-bold mb-4">Inline Keyboards</h1>
      
      {error && <p className="text-red-500 mb-4">{error}</p>} {/* Отображаем ошибку, если она есть */}
      
      {keyboards.length > 0 ? (
        <Table aria-label="Keyboards table">
          <TableHeader columns={columns}>
            {(column) => (
              <TableColumn key={column.key} className="bg-gray-700 text-gray-200">
                {column.label}
              </TableColumn>
            )}
          </TableHeader>
          <TableBody items={keyboards}>
            {(item) => (
              <TableRow key={item.inline_keyboard_name} className="hover:bg-gray-700">
                {(columnKey) => (
                  <TableCell className="border-b border-gray-700 p-2">
                    {columnKey === "buttons" ? (
                      item.buttons.length > 0 ? (
                        <div className="flex gap-2 flex-wrap">
                          {item.buttons.map((button) => (
                            <a
                              key={button.text}
                              href={button.url}
                              className="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 text-sm"
                              target="_blank"
                              rel="noopener noreferrer"
                            >
                              {button.text}
                            </a>
                          ))}
                        </div>
                      ) : (
                        <span className="text-sm text-gray-400">No buttons available</span>
                      )
                    ) : (
                      item[columnKey as keyof InlineKeyboardDto]
                    )}
                  </TableCell>
                )}
              </TableRow>
            )}
          </TableBody>
        </Table>
      ) : (
        <p className="text-sm text-gray-400">No keyboards available.</p>
      )}
    </div>
  );
};

export default Keyboards;
