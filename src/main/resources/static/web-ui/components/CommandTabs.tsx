import axios from "axios";
import { useState, useEffect } from "react";
import { Tabs, Tab } from "@heroui/react";
import { CommandResponseDTO } from "@/types/CommandDTO";
import UpdateCommandForm from "@/components/UpdateCommandForm";
import AddCommandForm from "@/components/AddCommandForm";
import SingleSelectionTypeInlineButton from "./SingleSelectionTypeInlineButton";
import EditInlineKeyboardsForm from "@/components/EditInlineKeyboardsForm";

interface Keyboard {
  id: number;
  label: string;
}

interface SelectInlineKeysProps {
  keyboards: Keyboard[];
  onKeyboardUpdated: () => void;
}
interface CommandTabsProps {
  selectedCommand: CommandResponseDTO | null;
  showAddForm: boolean;
  showKeyboardForm: boolean;
  onAddFormToggle: () => void;
  onCancel: () => void;
  onCommandUpdated: () => void;
  onSelectCommand: (command: CommandResponseDTO) => void;
}
const CommandTabs = ({
  selectedCommand,
  showAddForm,
  showKeyboardForm,
  onAddFormToggle,
  onCancel,
  onCommandUpdated,
  onSelectCommand,
}: CommandTabsProps) => {
  const [keyboards, setKeyboards] = useState<Keyboard[]>([]);
  const [selectedKeyboard, setSelectedKeyboard] = useState<number | null>(null);

  const fetchKeyboards = async () => {
    try {
      const response = await axios.get("http://localhost:9090/inline-keyboards");
      setKeyboards(response.data);
    } catch (error) {
      console.error("Error fetching keyboards:", error);
    }
  };

  useEffect(() => {
    fetchKeyboards();
  }, []);

  const handleSelectKeyboard = (keyboardId: number | null) => {
    setSelectedKeyboard(keyboardId);
  };

  return (
    <Tabs aria-label="Command Options">
      <Tab key="Commands" title="Commands">
        {selectedCommand ? (
          <UpdateCommandForm
            selectedCommand={selectedCommand}
            onCommandUpdated={onCommandUpdated}
          />
        ) : (
          <AddCommandForm onCommandUpdated={onCommandUpdated} />
        )}
      </Tab>

      {showKeyboardForm && (
        <Tab key="InlineKeyboard" title="InlineKeyboard">
    <EditInlineKeyboardsForm
      keyboards={keyboards} // Передаем реальные данные
      selectedKeyboard={selectedKeyboard} // Передаем выбранную клавиатуру
      onKeyboardUpdated={onCommandUpdated} // Функция обновления
      onSelect={handleSelectKeyboard} // Функция выбора клавиатуры
/>
        </Tab>
      )}

      <Tab key="InlineButton" title="InlineButton">
      <SingleSelectionTypeInlineButton/>

      </Tab>
    </Tabs>
  );
};


export default CommandTabs;
