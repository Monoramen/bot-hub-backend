import { useState, useEffect } from "react";
import axios from "axios";
import { Select, SelectItem } from "@heroui/react";

// Интерфейс для клавиатуры
interface Keyboard {
  id: number;
  label: string;
}

// Пропсы компонента
interface SelectInlineKeysProps {
  keyboards: Keyboard[]; // Список клавиатур
  selectedKeyboard: number | null; // ID выбранной клавиатуры
  onSelect: (keyboardId: number | null) => void; // Функция для выбора клавиатуры
}

export default function SelectInlineKeys({
  keyboards,
  selectedKeyboard,
  onSelect,
}: SelectInlineKeysProps) {
  // Рендер компонента
  return (
    <div className="w-full pt-2 mt-2">
      {/* Компонент Select для выбора клавиатуры */}
      <Select
        label="Select Keyboard" // Заголовок селектора
        selectedKeys={selectedKeyboard ? [selectedKeyboard.toString()] : []} // Выбранный элемент
        onChange={(e) => onSelect(Number(e.target.value))} // Обработчик изменения выбора
        placeholder="Select a keyboard" // Плейсхолдер
      >
        {/* Отображение списка клавиатур */}
        {keyboards.map((keyboard) => (
          <SelectItem key={keyboard.id} value={keyboard.id}>
            {keyboard.label}
          </SelectItem>
        ))}
      </Select>
    </div>
  );
}