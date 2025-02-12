import React from "react";
import { CommandResponseDTO } from "../types/command";

interface CommandListProps {
  commands: CommandResponseDTO[];
  onDelete: (id: number) => void;
}

const CommandList: React.FC<CommandListProps> = ({ commands, onDelete }) => {
  return (
    <div>
      <h2>Commands</h2>
      <ul>
        {commands.map((command) => (
          <li key={command.id}>
            <strong>{command.name}</strong>: {command.description}
            <button onClick={() => onDelete(command.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CommandList;
