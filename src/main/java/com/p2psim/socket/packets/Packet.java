package com.p2psim.socket.packets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

public abstract class Packet implements IData{
    private static final HashMap<Integer,Class<? extends Packet>> packets = new HashMap<Integer, Class<? extends Packet>>();
	@Getter
	private static final HashMap<String, Integer> packet_ids = new HashMap<>();
	@Getter
	private static int packetIdMax =1;

    @Getter
    @Setter
	private byte[] data;
    protected int id;
    @Getter
    @Setter
	private long receive_time;
    @Getter
    @Setter
	private long delay;

    public String getPacketName(){
		String simpleName = this.getClass().getSimpleName();
		simpleName = simpleName.substring(0, simpleName.indexOf("Packet"));
		return simpleName.toUpperCase();
	}

    public byte[] toByteArray(){
		if(this instanceof UnknownPacket)return ((UnknownPacket)this).getData();
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream( baos );
			writeToOutput(out);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}
	
	public static void writeBytes(byte[] ar, DataOutputStream out) {
		try {
			out.writeInt(ar.length);
			out.write(ar);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] readBytes(DataInputStream in) throws IOException {
		int length = in.readInt();
		byte[] byteArray = new byte[length];
		in.readFully(byteArray, 0, length);
		return byteArray;
	}
	
	public static String getPacketName(Class<?> clazz) {
		String simpleName = clazz.getSimpleName();
		simpleName = simpleName.substring(0, simpleName.indexOf("Packet"));
		return simpleName.toUpperCase();
	}
	
	public static String getPacketName(int packetId) {
		if(packets.get(packetId)==null) {
			for(String packetName : packet_ids.keySet()) {
				if(packet_ids.get(packetName) == packetId) {
					//Main.printf("Found packet "+packetId+" "+packetName);
					return packetName;
				}
			}
			
			throw new NullPointerException("Couldn't find packet!? ID:"+packetId+", packets_size="+packets.size());
		}
		
		return packets.get(packetId).getSimpleName();
	}

    public static int getId(Class<?> clazz) {
		return getId(getPacketName(clazz));
	}
	
	public static int getId(String packetName) {
		return packet_ids.get(packetName);
	}
	
	public static <T extends Packet> T create(Class<T> clazz, byte[] data) {
		return create(clazz, new DataInputStream(new ByteArrayInputStream(data)));
	}
	
	public static <T extends Packet> T create(Class<T> clazz, DataInputStream in) {
		try {
			T packet = (T) clazz.getDeclaredConstructor().newInstance();
			packet.parseFromInput(in);
			
			return (T) packet;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		throw new NullPointerException();
	}
	
	public static <T extends Packet> T create(int packetId, byte[] data) {
		return create(packetId, new DataInputStream(new ByteArrayInputStream(data)));
	}
	
	@SuppressWarnings({ "unchecked", "FinalStaticMethod" })
	public static <T extends Packet> T create(int packetId, DataInputStream in) {
		if(packets.containsKey(packetId)) {
			Class<T> clazz = (Class<T>) packets.get(packetId);
			return create(clazz,in);
		}
		try {
			return (T) (new UnknownPacket(packetId, in.readAllBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (T) (new UnknownPacket(packetId, null));
	}
}
