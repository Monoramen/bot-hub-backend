import SelectInlineKeys from "./SelectInlineKeys";
import EditInlineKeyboardsForm from "./EditInlineKeyboardsForm";
interface InlineButtonRequestDTO {
    text: string;
    url?: string;
    callback_data?: string;
    switch_inline_query?: string;
  }
  
  interface Keyboard {
    id: number;
    label: string;
  }

interface SelectInlineKeysProps {
    keyboards: Keyboard[];
    onKeyboardUpdated: () => void;
    selectedKeyboard: number | null;
    onSelect: (keyboardId: number | null) => void;
  }
export default function KeyboardManagement({
    keyboards,
    selectedKeyboard,
    onKeyboardUpdated,
    onSelect,
  }: SelectInlineKeysProps & { 
    selectedKeyboard: number | null; 
    onSelect: (keyboardId: number | null) => void; 
  }) {
    return (
      <div className="w-full pt-2 mt-2 py-2">
        <SelectInlineKeys
          keyboards={keyboards}
          selectedKeyboard={selectedKeyboard}
          onSelect={onSelect}
          onKeyboardUpdated={onKeyboardUpdated}
        />
        <EditInlineKeyboardsForm
          keyboards={keyboards}
          selectedKeyboard={selectedKeyboard}
          onKeyboardUpdated={onKeyboardUpdated}
          onSelect={onSelect}
        />
      </div>
    );
  }
  