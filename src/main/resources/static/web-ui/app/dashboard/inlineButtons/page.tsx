"use client";
import { useEffect, useState } from "react";
import axios from "axios";
import { Button, Input,ButtonGroup } from "@heroui/react";
import { InlineButtonDTO } from "@/types/InlineButtonDTO";
import { Card, CardBody, CardHeader } from "@heroui/react";
import SingleSelectionTypeInlineButton from "@/components/SingleSelectionTypeInlineButton";
import { title } from "@/components/primitives";
import InlineButton from "@/components/InlineButton";
import InlineButtonForm from "@/components/InlineButtonForm";

export default function InlineButtonPage() {
  const [buttons, setButtons] = useState<InlineButtonDTO[]>([]);
  const [selectedButton, setSelectedButton] = useState<InlineButtonDTO | null>(null);
  const [newButton, setNewButton] = useState<InlineButtonDTO>({
    text: "",
    url: null,
    callback_data: null,
    switch_inline_query: null,
  });

  const fetchButtons = async () => {
    try {
      const response = await axios.get<InlineButtonDTO[]>("http://localhost:9090/inline-buttons");
      setButtons(response.data);
    } catch (error) {
      console.error("Error fetching buttons:", error);
    }
  };

  useEffect(() => {
    fetchButtons();
  }, []);

  const handleSelectButton = (button: InlineButtonDTO) => {
    setSelectedButton(button);
  };

  const handleSave = async () => {
    if (selectedButton) {
      try {
        await axios.put(`http://localhost:9090/inline-buttons/${selectedButton.id}`, selectedButton);
        fetchButtons();
      } catch (error) {
        console.error("Error updating button:", error);
      }
    }
  };

  const handleCreate = async () => {
    try {
      await axios.post("http://localhost:9090/inline-buttons/add", newButton);
      fetchButtons();
      setNewButton({ text: "", url: null, callback_data: null, switch_inline_query: null });
    } catch (error) {
      console.error("Error creating button:", error);
    }
  };

  return (
    <div className="max-w-screen-xl mx-auto px-4 py-6">
      <h1 className={title()}>Inline Buttons</h1>
      <Button onClick={fetchButtons} className="mb-4">
        Refresh
      </Button>
      <div className="grid grid-cols-4 gap-6">
  {/* Левая колонка с кнопками */}
  <div className="col-span-3 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
    {buttons.map((button, index) => (
      <Card
        key={index}
        className="p-2 cursor-pointer hover:shadow-lg transition-shadow bg-[#6020a0] text-white rounded-lg"
        isPressable
        shadow="sm"
        onClick={() => handleSelectButton(button)}
      >
        <CardHeader className="pb-2 pt-1 px-3 flex-col items-start">
          <p className="font-bold uppercase text-sm text-white">Text:</p>
          <small className="text-gray-200 truncate">{button.text}</small>
        </CardHeader>
        <CardBody className="overflow-visible py-1 px-3 text-sm">
          <div className="flex flex-col gap-1">
            {button.url && (
              <div>
                <h6 className="font-semibold text-xs text-white">URL:</h6>
                <small className="text-gray-300 truncate">{button.url}</small>
              </div>
            )}
            {button.callback_data && (
              <div>
                <h6 className="font-semibold text-xs text-white">Callback:</h6>
                <small className="text-gray-300 truncate">{button.callback_data}</small>
              </div>
            )}
            {button.switch_inline_query && (
              <div>
                <h6 className="font-semibold text-xs text-white">Switch Inline Query:</h6>
                <small className="text-gray-300 truncate">{button.switch_inline_query}</small>
              </div>
            )}
          </div>
        </CardBody>
      </Card>
    ))}
  </div>

  {/* Правая колонка с формой */}
  
{selectedButton && (
   <div className="col-span-1 flex flex-col border p-4 rounded-lg shadow-lg  bg-[#6020a0]">
      <h2 className="font-semibold text-lg mb-4 text-white">Edit Button</h2>
  <InlineButtonForm
    selectedButton={selectedButton}
    onSave={(updatedButton) => {
      setSelectedButton(updatedButton);
      handleSave(updatedButton); // Обновите свою логику сохранения здесь
    }}
    onDelete={() => {
      handleDelete(selectedButton); // Вызовите свою функцию удаления
    }}
  />
</div>
)}

</div>



    </div>
  );
}
