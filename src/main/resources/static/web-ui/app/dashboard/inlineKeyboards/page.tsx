"use client";
import { useEffect, useState } from "react";
import axios from "axios";
import { Card, CardBody, CardHeader } from "@heroui/react";
import { InlineKeyboardDTO } from "@/types/CommandDTO";

export default function InlineButtonPage() {
  const [keyboards, setKeyboards] = useState<InlineKeyboardDTO[]>([]);
  const [selectedKeyboard, setSelectedKeyboard] = useState<InlineKeyboardDTO | null>(null);

  const fetchInlineKeyboards = async () => {
    try {
      const response = await axios.get<InlineKeyboardDTO[]>("http://localhost:9090/inline-keyboards");
      setKeyboards(response.data);
    } catch (error) {
      console.error("Error fetching keyboards:", error);
    }
  };

  useEffect(() => {
    fetchInlineKeyboards();
  }, []);

  const handleSelectInlineKeyboard = (keyboard: InlineKeyboardDTO) => {
    setSelectedKeyboard(keyboard);
  };

  return (
    <div className="max-w-screen-xl mx-auto px-4 py-6 pt-2">
      <h1 className="text-2xl font-bold mb-4">Inline Keyboards</h1>

      <div className="grid grid-cols-4 gap-6">
        {/* Левая колонка с клавиатурами */}
        <div className="col-span-3 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {keyboards.map((keyboard, index) => (
            <Card
              key={index}
              className="p-2 cursor-pointer hover:shadow-lg transition-shadow bg-[#6020a0] text-white rounded-lg"
              onClick={() => handleSelectInlineKeyboard(keyboard)}
            >
              <CardHeader className="pb-2 pt-1 px-3 flex-col items-start">
                <p className="font-bold uppercase text-sm text-white">Keyboard Name:</p>
                <small className="text-gray-200 truncate">{keyboard.inline_keyboard_name}</small>
              </CardHeader>
              <CardBody className="py-2 px-3">
                {/* Кнопки внутри клавиатуры */}
                {keyboard.buttons.map((button, btnIndex) => (
                  <div key={btnIndex} className="mb-2">
                    <p className="font-semibold text-sm text-white">Text: {button.text}</p>
                    {button.url && (
                      <p className="text-gray-300 text-xs">URL: {button.url}</p>
                    )}
                    {button.callback_data && (
                      <p className="text-gray-300 text-xs">Callback: {button.callback_data}</p>
                    )}
                    {button.switch_inline_query && (
                      <p className="text-gray-300 text-xs">
                        Switch Inline Query: {button.switch_inline_query}
                      </p>
                    )}
                  </div>
                ))}
              </CardBody>
            </Card>
          ))}
        </div>

        {/* Правая колонка с детальной информацией */}
        {selectedKeyboard && (
          <div className="col-span-1 flex flex-col border p-4 rounded-lg shadow-lg bg-[#6020a0]">
            <h2 className="font-semibold text-lg mb-4 text-white">Selected Keyboard</h2>
            <p className="text-white">Name: {selectedKeyboard.inline_keyboard_name}</p>
            <div className="mt-4">
              <h3 className="font-semibold text-sm text-white mb-2">Buttons:</h3>
              {selectedKeyboard.buttons.map((button, index) => (
                <div key={index} className="mb-2">
                  <p className="font-semibold text-sm text-white">Text: {button.text}</p>
                  {button.url && (
                    <p className="text-gray-300 text-xs">URL: {button.url}</p>
                  )}
                  {button.callback_data && (
                    <p className="text-gray-300 text-xs">Callback: {button.callback_data}</p>
                  )}
                  {button.switch_inline_query && (
                    <p className="text-gray-300 text-xs">
                      Switch Inline Query: {button.switch_inline_query}
                    </p>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
