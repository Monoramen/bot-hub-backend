// components/Commands.tsx
import React, { useEffect, useState } from "react";
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@nextui-org/react";

interface Command {
  command: string;
  response: string;
  inline_keyboards: { inline_keyboard_name: string; buttons: { text: string; url: string }[] }[];
}

const Commands: React.FC = () => {
  const [commands, setCommands] = useState<Command[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCommands = async () => {
      try {
        const response = await fetch("http://localhost:9090/commands/all");
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await response.json();
        setCommands(data);
      } catch (error: any) {
        console.error("Error fetching commands:", error);
        setError(error.message);
      }
    };

    fetchCommands();
  }, []);

  const columns = [
    { key: "command", label: "Command" },
    { key: "response", label: "Response" },
    { key: "inline_keyboards", label: "Inline Keyboards" },
  ];

  return (
    <div className="bg-gray-800 text-gray-100 rounded-md shadow-lg">
      <h1 className="text-2xl font-bold mb-4">Commands</h1>
      {error && <p className="text-red-500">{error}</p>}
      {commands.length > 0 ? (
        <Table aria-label="Commands table">
          <TableHeader columns={columns}>
            {(column) => (
              <TableColumn key={column.key} className="bg-gray-700 text-gray-200">
                {column.label}
              </TableColumn>
            )}
          </TableHeader>
          <TableBody items={commands}>
            {(item) => (
              <TableRow key={item.command} className="hover:bg-gray-700">
                {(columnKey) => (
                  <TableCell className="border-b border-gray-700 p-2">
                    {columnKey === "inline_keyboards" ? (
                      item.inline_keyboards.length > 0 ? (
                        item.inline_keyboards.map((keyboard) => (
                          <div key={keyboard.inline_keyboard_name} className="flex gap-2 flex-wrap">
                            {keyboard.buttons.map((button) => (
                              <a
                                key={button.text}
                                href={button.url}
                                className="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                                target="_blank"
                                rel="noopener noreferrer"
                              >
                                {button.text}
                              </a>
                            ))}
                          </div>
                        ))
                      ) : (
                        <span>No inline keyboards</span>
                      )
                    ) : (
                      item[columnKey as keyof Command]
                    )}
                  </TableCell>
                )}
              </TableRow>
            )}
          </TableBody>
        </Table>
      ) : (
        <p>No commands available.</p>
      )}
    </div>
  );
};

export default Commands;
