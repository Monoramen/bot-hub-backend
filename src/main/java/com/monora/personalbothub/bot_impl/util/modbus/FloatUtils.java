package com.monora.personalbothub.bot_impl.util.modbus;

public class FloatUtils {


    public static byte[] byteToFloat(float value) {
        int intBits = Float.floatToIntBits(value);
        return new byte[]{
                (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits)};
    }



    public static float byteArrayToFloat(byte[] bytes) {
        int intBits =
                bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
        return Float.intBitsToFloat(intBits);
    }
}
