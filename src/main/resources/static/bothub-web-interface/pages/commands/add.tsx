import React from "react";
import { useRouter } from "next/router";
import CommandForm from "../../app/components/CommandForm";
import { createCommand } from "../../app/services/commandService";

const AddCommandPage: React.FC = () => {
  const router = useRouter();

  const handleCreate = async (commandData: { name: string; description: string }) => {
    await createCommand(commandData);
    router.push("/commands");
  };

  return (
    <div>
      <h1>Add Command</h1>
      <CommandForm onSubmit={handleCreate} />
    </div>
  );
};

export default AddCommandPage;
