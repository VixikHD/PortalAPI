package test;

import vixikhd.portal.network.PacketBuffer;

import java.util.Arrays;
import java.util.Random;

public class PacketBufferTest {

    public static void main(String[] args) {
        PacketBuffer buffer = new PacketBuffer();

        Random random = new Random();

        byte[] testBytes = new byte[5];
        random.nextBytes(testBytes);
        long testLong = random.nextLong();
        int testLInt = random.nextInt(256);
        int testUnsignedInt = random.nextInt();
        String testString = "Test string";

        System.out.println("INPUT:");
        System.out.println(Arrays.toString(testBytes));
        System.out.println(testBytes[0]);
        System.out.println(testLong);
        System.out.println(testLInt);
        System.out.println(testUnsignedInt);
        System.out.println(testString);


        buffer.writeBytes(testBytes);
        buffer.writeByte(testBytes[0]);
        buffer.writeLLong(testLong);
        buffer.writeLInt(testLInt);
        buffer.writeUnsignedVarInt(testUnsignedInt);
        buffer.writeString(testString);

        System.out.println("\nOUTPUT:");
        System.out.println(Arrays.toString(buffer.readBytes(5)));
        System.out.println(buffer.readByte());
        System.out.println(buffer.readLLong());
        System.out.println(buffer.readLInt());
        System.out.println(buffer.readUnsignedVarInt());
        System.out.println(buffer.readString());
    }
}
