import { useState, useEffect } from "react";
import axios from "axios";
import { Select, SelectItem, Input, Button } from "@heroui/react";

// Интерфейс для кнопки
interface Button {
  text: string;
  url: string;
  callback_data: string;
  switch_inline_query: string;
}

// Интерфейс для клавиатуры
interface Keyboard {
  id: number;
  inline_keyboard_name: string;
  buttons: Button[];
}

// Пропсы компонента
interface EditInlineKeyboardsFormProps {
  keyboards: Keyboard[]; // Список клавиатур
  selectedKeyboard: number | null; // ID выбранной клавиатуры
  onKeyboardUpdated: () => void; // Функция для обновления клавиатур
  onSelect: (keyboardId: number | null) => void; // Функция для выбора клавиатуры
}

export default function EditInlineKeyboardsForm({
  keyboards,
  selectedKeyboard,
  onKeyboardUpdated,
  onSelect,
}: EditInlineKeyboardsFormProps) {
  const [currentKeyboard, setCurrentKeyboard] = useState<Keyboard | null>(null);
  const [isAdding, setIsAdding] = useState<boolean>(false);

  // Загрузка данных о выбранной клавиатуре
  useEffect(() => {
    if (selectedKeyboard) {
      axios.get(`http://localhost:9090/inline-keyboards/${selectedKeyboard}`)
        .then(response => {
          setCurrentKeyboard(response.data);
          setIsAdding(false); // Сбросить режим добавления
        })
        .catch(error => {
          console.error("Error fetching keyboard details:", error);
        });
    } else {
      setCurrentKeyboard(null);
    }
  }, [selectedKeyboard]);

  // Обработчик изменения названия клавиатуры
  const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (currentKeyboard) {
      setCurrentKeyboard({
        ...currentKeyboard,
        inline_keyboard_name: e.target.value,
      });
    }
  };

  // Обработчик изменения кнопки
  const handleButtonChange = (index: number, field: keyof Button, value: string) => {
    if (currentKeyboard) {
      const updatedButtons = [...currentKeyboard.buttons];
      updatedButtons[index] = {
        ...updatedButtons[index],
        [field]: value,
      };
      setCurrentKeyboard({
        ...currentKeyboard,
        buttons: updatedButtons,
      });
    }
  };

  // Добавление новой пустой кнопки
  const handleAddButton = () => {
    if (currentKeyboard) {
      const newButton: Button = {
        text: "",
        url: "",
        callback_data: "",
        switch_inline_query: "",
      };
      setCurrentKeyboard({
        ...currentKeyboard,
        buttons: [...currentKeyboard.buttons, newButton],
      });
    }
  };

  // Удаление кнопки
  const handleRemoveButton = (index: number) => {
    if (currentKeyboard) {
      const updatedButtons = currentKeyboard.buttons.filter((_, i) => i !== index);
      setCurrentKeyboard({
        ...currentKeyboard,
        buttons: updatedButtons,
      });
    }
  };

  // Обработчик сохранения изменений
  const handleSave = () => {
    if (currentKeyboard) {
      if (isAdding) {
        // Создание новой клавиатуры
        axios.post(`http://localhost:9090/inline-keyboards`, currentKeyboard)
          .then(() => {
            onKeyboardUpdated();
            setIsAdding(false); // Сбросить режим добавления
            setCurrentKeyboard(null); // Сбросить текущую клавиатуру
          })
          .catch(error => {
            console.error("Error creating keyboard:", error);
          });
      } else {
        // Обновление существующей клавиатуры
        axios.put(`http://localhost:9090/inline-keyboards/${currentKeyboard.id}`, currentKeyboard)
          .then(() => {
            onKeyboardUpdated();
          })
          .catch(error => {
            console.error("Error updating keyboard:", error);
          });
      }
    }
  };

  // Обработчик выбора "Add"
  useEffect(() => {
    if (selectedKeyboard) {
      // Загрузка существующей клавиатуры
      axios.get(`http://localhost:9090/inline-keyboards/${selectedKeyboard}`)
        .then(response => {
          setCurrentKeyboard(response.data);
          setIsAdding(false); // Сбросить режим добавления, если выбрана существующая клавиатура
        })
        .catch(error => {
          console.error("Error fetching keyboard details:", error);
        });
    } else if (isAdding) {
      // Когда добавляется новая клавиатура, сбрасываем данные
      setCurrentKeyboard({
        id: 0,
        inline_keyboard_name: "",
        buttons: [],
      });
    } else {
      setCurrentKeyboard(null);
    }
  }, [selectedKeyboard, isAdding]);
  
  const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const value = e.target.value;
    if (value === "add") {
      setIsAdding(true);
      setCurrentKeyboard({
        id: null,
        inline_keyboard_name: "",
        buttons: [],
      });
      onSelect(null);
    }
     else {
      setIsAdding(false);
      onSelect(Number(value));
    }
  };
  
  

  return (
    <div className="w-full pt-2 mt-2">
      <Select
        label="Select Keyboard"
        selectedKeys={selectedKeyboard ? [selectedKeyboard.toString()] : isAdding ? ["add"] : []}
        onChange={handleSelectChange}
        placeholder="Select a keyboard"
      >
        <SelectItem value="add">Add</SelectItem>
        {keyboards.map((keyboard) => (
          <SelectItem key={keyboard.id} value={keyboard.id}>
            {keyboard.inline_keyboard_name}
          </SelectItem>
        ))}
      </Select>

      {(currentKeyboard || isAdding) && (
  <div className="mt-4">
    <Input
      label="Keyboard Name"
      value={currentKeyboard ? currentKeyboard.inline_keyboard_name : "add"}
      onChange={handleNameChange}
    />

    {/* Кнопка для сохранения изменений */}
    <Button onClick={handleSave} className="mt-4">
      Save Changes
    </Button>
  </div>
)}

    </div>
  );
}