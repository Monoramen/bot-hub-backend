"use client";
import { useState, useEffect } from "react";
import axios from "axios";
import { Form, Input, Button } from "@heroui/react";
import { Textarea } from "@heroui/react";
import SelectInlineKeys from "@/components/SelectInlineKeys";
import { InlineKeyboardDTO } from "@/types/CommandDTO";


export default function AddCommandForm({ onCommandUpdated }: { onCommandUpdated: () => void }) {
  const [command, setCommand] = useState("");
  const [response, setResponse] = useState("");
  const [inlineKeyboardId, setInlineKeyboardId] = useState<number | null>(null);
  const [keyboards, setKeyboards] = useState([]);

  useEffect(() => {
    async function fetchKeyboards() {
      try {
        const response = await axios.get("http://localhost:9090/inline-keyboards");
        const keyboards = response.data.map((keyboard: InlineKeyboardDTO) => ({
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

  
  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const newCommand = { command, response, inline_keyboard_id: inlineKeyboardId };
  
    try {
      await axios.post("http://localhost:9090/commands/add", newCommand);
      setCommand("");
      setResponse("");
      setInlineKeyboardId(null);
      alert("Command added successfully!");
      onCommandUpdated(); // Обновляем список команд
    } catch (error) {
      console.error("Error adding command", error);
      alert("Failed to add command");
    }
  };
  

  return (
    <div>
 <Form className="w-full pt-2" validationBehavior="native" onSubmit={handleSubmit}>
      <h2 className="center mx-auto px-2">
        <b>Add Command</b>
      </h2>
      <Input
        isRequired
        label="Command"
        labelPlacement="outside"
        name="command"
        placeholder="Enter your command"
        type="text"
        value={command}
        onChange={(e) => setCommand(e.target.value)}
        validate={(value) => (value.length < 3 ? "Command must be at least 3 characters long" : null)}
      />

      <Textarea
        isRequired
        label="Response"
        labelPlacement="outside"
        name="response"
        placeholder="Enter the response"
        type="text"
        value={response}
        onChange={(e) => setResponse(e.target.value)}
      
      />

      <SelectInlineKeys
        keyboards={keyboards}
        selectedKeyboard={inlineKeyboardId}
        onSelect={(keyboardId) => setInlineKeyboardId(keyboardId)}
      />
     

      <Button type="submit" variant="bordered">
        Add Command
      </Button>
    </Form>

    </div>
   
  );
}