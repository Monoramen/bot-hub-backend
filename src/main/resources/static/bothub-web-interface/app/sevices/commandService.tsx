import apiClientCommands from "../../lib/apiClientCommands";

import { CommandRequestDTO, CommandResponseDTO } from "../types/command";

export const getAllCommands = async (): Promise<CommandResponseDTO[]> => {
  const response = await apiClientCommands.get<CommandResponseDTO[]>("/all");
  return response.data;
};

export const getCommandById = async (id: number): Promise<CommandResponseDTO> => {
  const response = await apiClientCommands.get<CommandResponseDTO>(`/${id}`);
  return response.data;
};

export const getCommandByName = async (commandName: string): Promise<CommandResponseDTO> => {
  const response = await apiClientCommands.get<CommandResponseDTO>("/command", { params: { command: commandName } });
  return response.data;
};

export const createCommand = async (commandData: CommandRequestDTO): Promise<void> => {
  await apiClientCommands.post("/add", commandData);
};

export const updateCommand = async (id: number, commandData: CommandRequestDTO): Promise<void> => {
  await apiClientCommands.put(`/update/${id}`, commandData);
};

export const deleteCommand = async (id: number): Promise<void> => {
  await apiClientCommands.delete(`/delete/${id}`);
};
