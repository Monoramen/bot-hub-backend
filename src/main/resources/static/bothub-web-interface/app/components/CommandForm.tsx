import React, { useState } from "react";
import { CommandRequestDTO } from "../types/command";

interface CommandFormProps {
  onSubmit: (commandData: CommandRequestDTO) => void;
  initialData?: CommandRequestDTO;
}

const CommandForm: React.FC<CommandFormProps> = ({ onSubmit, initialData = {} as CommandRequestDTO }) => {
  const [name, setName] = useState<string>(initialData.name || "");
  const [description, setDescription] = useState<string>(initialData.description || "");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({ name, description });
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Command Name:</label>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
      </div>
      <div>
        <label>Description:</label>
        <textarea
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          required
        />
      </div>
      <button type="submit">Submit</button>
    </form>
  );
};

export default CommandForm;
