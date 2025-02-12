export interface CommandRequestDTO {
  command: string;
  response: string;
  inline_keyboard_id: number | null; // Используем inline_keyboard_id вместо inlineKeyboard
}
export  interface CommandResponseDTO {
  id: number;
  command: string;
  response: string;
  inline_keyboard: InlineKeyboardDTO | null;
}

export  interface InlineKeyboardDTO {
  id: number;
  inline_keyboard_name: string;
  buttons: Array<{
    text: string;
    url?: string;
    callback_data?: string;
    switch_inline_query?: string;
    row: number;
    position:number;
  }>;
}

