"use client";
import axios from "axios";
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell, Button } from "@heroui/react";
import { FaEdit, FaTrash } from "react-icons/fa";
import { CommandResponseDTO } from "../types/CommandDTO"; 

export default function CommandList({
  commands,
  onSelectCommand,
  onCommandUpdated,
}: {
  commands: CommandResponseDTO[];
  onSelectCommand: (command: CommandResponseDTO) => void;
  onCommandUpdated: () => void;
}) {
  // Функция для удаления команды
  const handleDelete = async (id: number) => {
    try {
      await axios.delete(`http://localhost:9090/commands/delete/${id}`);
      onCommandUpdated();  // Обновляем список команд после удаления
    } catch (error) {
      console.error("Error deleting command", error);
    }
  };

  return (
    <div>
      <Table>
        <TableHeader>
          <TableColumn>ID</TableColumn>
          <TableColumn>Command</TableColumn>
          <TableColumn>Response</TableColumn>
          <TableColumn>Inline Keyboard</TableColumn>
          <TableColumn> </TableColumn>
          <TableColumn> </TableColumn>
        </TableHeader>
        <TableBody>
          {commands.map((command) => (
            <TableRow key={command.id}>
              <TableCell>{command.id}</TableCell>
              <TableCell>{command.command}</TableCell>
              <TableCell>{command.response}</TableCell>
              <TableCell>{command.inline_keyboard ? command.inline_keyboard.inline_keyboard_name : "No keyboard"}</TableCell>

              <TableCell>
                <Button color="warning" size="sm" radius="full" onClick={() => onSelectCommand(command)}>
                  <FaEdit />
                </Button>
              </TableCell>
              <TableCell>
                <Button color="danger" size="sm" radius="full" onClick={() => handleDelete(command.id)}>
                  <FaTrash />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
