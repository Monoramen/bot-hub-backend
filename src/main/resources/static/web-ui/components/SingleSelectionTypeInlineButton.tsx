import React from "react";
import { Listbox, ListboxItem, Input } from "@heroui/react";
import { Button } from "@heroui/button";

export const ListboxWrapper = ({ children }) => (
  <div className="w-full border-small px-1 py-2 rounded-small border-default-200 dark:border-default-100">
    {children}
  </div>
);

export default function SingleSelectionTypeInlineButton() {
  const [selectedKeys, setSelectedKeys] = React.useState(new Set(["text"]));
  const [selectedType, setSelectedType] = React.useState("text");
  const [inputValue, setInputValue] = React.useState("");

  const selectedValue = React.useMemo(() => Array.from(selectedKeys).join(", "), [selectedKeys]);

  // Обработчик изменения выбранного типа
  const handleSelectionChange = (keys) => {
    setSelectedKeys(keys);
    const newType = Array.from(keys).join(", ");
    setSelectedType(newType);
    setInputValue(""); // Сброс значения при изменении типа
  };

  // Обработчик изменения значения в Input
  const handleInputChange = (e) => {
    setInputValue(e.target.value);
  };

  return (
    <div className="w-full max-w-lg mx-auto p-4 bg-blue-900 rounded-lg shadow-md">

      {/* Верхний Input для текста */}
      <Input
        className="w-full mb-4"
        label="Text"
        value={inputValue}
        onChange={handleInputChange}
        placeholder="Enter Text"
      />

      {/* Listbox для выбора типа */}
      <ListboxWrapper>
        <Listbox
          className="w-full"
          disallowEmptySelection
          aria-label="Single selection"
          selectedKeys={selectedKeys}
          selectionMode="single"
          variant="flat"
          onSelectionChange={handleSelectionChange}
        >
          <ListboxItem key="url">url</ListboxItem>
          <ListboxItem key="callback_data">callback_data</ListboxItem>
          <ListboxItem key="switch_inline_query">switch_inline_query</ListboxItem>
        </Listbox>
      </ListboxWrapper>

      <div className="mt-4"></div>

      {/* Динамически отображаемый Input */}
      {selectedType === "url" && (
        <Input
          label="URL"
          value={inputValue}
          onChange={handleInputChange}
          placeholder="Enter URL"
          className="w-full mt-2"
        />
      )}

      {selectedType === "callback_data" && (
        <Input
          label="Callback Data"
          value={inputValue}
          onChange={handleInputChange}
          placeholder="Enter Callback Data"
          className="w-full mt-2"
        />
      )}

      {selectedType === "switch_inline_query" && (
        <Input
          label="Switch Inline Query"
          value={inputValue}
          onChange={handleInputChange}
          placeholder="Enter Switch Inline Query"
          className="w-full mt-2"
        />
      )}

      {/* Кнопка */}
      <Button className="mt-4 w-full" size="md" color="warning">
        Add Button
      </Button>
    </div>
  );
}
