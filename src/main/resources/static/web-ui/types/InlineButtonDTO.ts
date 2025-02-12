// types/InlineButtonDTO.ts
export interface InlineButtonDTO {
    text: string;
    url: string | null;
    callback_data: string | null;
    switch_inline_query: string | null;
  }
