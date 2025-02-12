"use client";

import { useState, useEffect } from "react";
import { Form, Input, Button, Select, SelectItem } from "@heroui/react";
import { ButtonGroup } from "@heroui/button";

interface InlineButtonFormProps {
  selectedButton?: {
    text: string;
    url?: string;
    callback_data?: string;
    switch_inline_query?: string;
  };
  onSave: (button: any) => void;
  onDelete: () => void;
}

export default function InlineButtonForm({
  selectedButton,
  onSave,
  onDelete,
}: InlineButtonFormProps) {
  const [text, setText] = useState("");
  const [url, setUrl] = useState("");
  const [callbackData, setCallbackData] = useState("");
  const [switchInlineQuery, setSwitchInlineQuery] = useState("");
  const [buttonType, setButtonType] = useState<"url" | "callback" | "switch">("url");

  useEffect(() => {
    if (selectedButton) {
      setText(selectedButton.text || "");
      setUrl(selectedButton.url || "");
      setCallbackData(selectedButton.callback_data || "");
      setSwitchInlineQuery(selectedButton.switch_inline_query || "");
      if (selectedButton.url) setButtonType("url");
      else if (selectedButton.callback_data) setButtonType("callback");
      else if (selectedButton.switch_inline_query) setButtonType("switch");
    }
  }, [selectedButton]);

  const handleSave = () => {
    const updatedButton = {
      text,
      url: buttonType === "url" ? url : undefined,
      callback_data: buttonType === "callback" ? callbackData : undefined,
      switch_inline_query: buttonType === "switch" ? switchInlineQuery : undefined,
    };
    onSave(updatedButton);
  };

  return (
    <Form className="flex flex-col w-full max-w-xs" validationBehavior="native">
      <h2 className="font-semibold text-lg mb-4 text-white">Edit Button</h2>
      <Input
        isRequired
        label="Button Text"
        labelPlacement="outside"
        value={text}
        onChange={(e) => setText(e.target.value)}
      />
      <Select
        label="Button Type"
        labelPlacement="outside"
        selectedKeys={[buttonType]}
        onChange={(e) => setButtonType(e.target.value as "url" | "callback" | "switch")}
      >
        <SelectItem key="url" value="url">
          URL
        </SelectItem>
        <SelectItem key="callback" value="callback">
          Callback Data
        </SelectItem>
        <SelectItem key="switch" value="switch">
          Switch Inline Query
        </SelectItem>
      </Select>

      {buttonType === "url" && (
        <Input
          label="URL"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
        />
      )}
      {buttonType === "callback" && (
        <Input
          label="Callback Data"
          value={callbackData}
          onChange={(e) => setCallbackData(e.target.value)}
        />
      )}
      {buttonType === "switch" && (
        <Input
          label="Switch Inline Query"
          value={switchInlineQuery}
          onChange={(e) => setSwitchInlineQuery(e.target.value)}
        />
      )}

      <div className="mt-4 flex justify-between">
        <ButtonGroup >
        <Button color="success" onClick={handleSave}>
          Save
        </Button>
        <Button color="danger" onClick={onDelete}>
          Delete
        </Button>
        </ButtonGroup>

      </div>
    </Form>
  );
}
