import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import CommandForm from "../../../app/components/CommandForm";
import { getCommandById, updateCommand } from "../../../app/services/commandService";
import { CommandRequestDTO } from "../../../app/types/command";

const EditCommandPage: React.FC = () => {
  const router = useRouter();
  const { id } = router.query;
  const [command, setCommand] = useState<CommandRequestDTO | null>(null);

  useEffect(() => {
    if (id) {
      fetchCommand();
    }
  }, [id]);

  const fetchCommand = async () => {
    const data = await getCommandById(Number(id));
    setCommand({ name: data.name, description: data.description });
  };

  const handleUpdate = async (commandData: CommandRequestDTO) => {
    await updateCommand(Number(id), commandData);
    router.push("/commands");
  };

  if (!command) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h1>Edit Command</h1>
      <CommandForm onSubmit={handleUpdate} initialData={command} />
    </div>
  );
};

export default EditCommandPage;
