import { useState, useEffect } from "react";
import { CommandRequestDTO, CommandResponseDTO, InlineKeyboard } from "../types/CommandDTO";
import { Form, Input, Button } from "@heroui/react";
import { Textarea } from "@heroui/react";
import axios from "axios";
import SelectInlineKeys from "@/components/SelectInlineKeys";
export default function UpdateCommandForm({
  selectedCommand,
  onCommandUpdated,
}: {
  selectedCommand: CommandResponseDTO;
  onCommandUpdated: () => void;
}) {
  const [keyboards, setKeyboards] = useState<InlineKeyboard[]>([]);
  const [formData, setFormData] = useState<CommandRequestDTO>({
    command: selectedCommand.command,
    response: selectedCommand.response,
    inline_keyboard_id: selectedCommand.inline_keyboard ? selectedCommand.inline_keyboard.id : null,
  });

  useEffect(() => {
    async function fetchKeyboards() {
      try {
        const response = await axios.get("http://localhost:9090/inline-keyboards");
        const keyboards = response.data.map((keyboard: InlineKeyboard) => ({
          id: keyboard.id,
          label: keyboard.inline_keyboard_name,
        }));
        setKeyboards(keyboards);
      } catch (error) {
        console.error("Error fetching keyboards", error);
      }
    }

    fetchKeyboards();
  }, []);

  useEffect(() => {
    setFormData({
      command: selectedCommand.command,
      response: selectedCommand.response,
      inline_keyboard_id: selectedCommand.inline_keyboard ? selectedCommand.inline_keyboard.id : null,
    });
  }, [selectedCommand]);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      await axios.put(`http://localhost:9090/commands/update/${selectedCommand.id}`, formData);
      alert("Command updated successfully!");
      onCommandUpdated(); // Обновляем список команд
    } catch (error) {
      console.error("Error updating command", error);
      alert("Failed to update command");
    }
  };

  const handleKeyboardSelect = (keyboardId: number | null) => {
    setFormData((prevData) => ({
      ...prevData,
      inline_keyboard_id: keyboardId,
    }));
  };

  return (
    <Form className="w-full pt-2" validationBehavior="native" onSubmit={handleSubmit}>
      <h2 className="center mx-auto px-2">
        <b>Update Command</b>
      </h2>
      <Input
        isRequired
        label="Command"
        labelPlacement="outside"
        name="command"
        placeholder="Enter your command"
        type="text"
        value={formData.command}
        onChange={(e) => setFormData({ ...formData, command: e.target.value })}
        validate={(value) => (value.length < 3 ? "Command must be at least 3 characters long" : null)}
      />

      <Textarea
        isRequired
        label="Response"
        labelPlacement="outside"
        name="response"
        placeholder="Enter the response"
        value={formData.response}
        onChange={(e) => setFormData({ ...formData, response: e.target.value })}

      />

      <SelectInlineKeys
        keyboards={keyboards}
        selectedKeyboard={formData.inline_keyboard_id}
        onSelect={handleKeyboardSelect}
        onKeyboardUpdated={() => {}}
      />

      <Button type="submit" variant="bordered">
        Update Command
      </Button>
    </Form>
  );
}
