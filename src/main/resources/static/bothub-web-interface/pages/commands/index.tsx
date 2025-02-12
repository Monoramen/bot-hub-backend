import React, { useEffect, useState } from "react";
import { getAllCommands, deleteCommand } from "../../app/services/commandService";
import { CommandResponseDTO } from "../../app/types/command";
import CommandList from "../../app/components/CommandList";

const CommandsPage: React.FC = () => {
  const [commands, setCommands] = useState<CommandResponseDTO[]>([]);

  useEffect(() => {
    fetchCommands();
  }, []);

  const fetchCommands = async () => {
    const data = await getAllCommands();
    setCommands(data);
  };

  const handleDelete = async (id: number) => {
    await deleteCommand(id);
    fetchCommands();
  };

  return (
    <div>
      <h1>Commands</h1>
      <CommandList commands={commands} onDelete={handleDelete} />
    </div>
  );
};

export default CommandsPage;
