package com.monora.personalbothub.bot_impl.util.modbus;


import com.digitalpetri.modbus.pdu.*;
import java.util.concurrent.CompletableFuture;

public interface ModbusClient {

    CompletableFuture<ReadHoldingRegistersResponse> readHoldingRegisters(int unitId, ReadHoldingRegistersRequest request);

    CompletableFuture<WriteSingleRegisterResponse> writeSingleRegister(int unitId, WriteSingleRegisterRequest request);

    CompletableFuture<WriteMultipleRegistersResponse> writeMultipleRegisters(int unitId, WriteMultipleRegistersRequest request);

    CompletableFuture<WriteSingleCoilResponse> writeSingleCoil(int unitId, WriteSingleCoilRequest request);

    boolean isConnected();

    void connect() throws Exception;

    void disconnect();
}