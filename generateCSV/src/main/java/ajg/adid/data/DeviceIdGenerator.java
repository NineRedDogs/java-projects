package ajg.adid.data;

public class DeviceIdGenerator extends BaseGenerator {
	
	
	private static final int MAX_DEVICE_ID = 99999999;
	
	
	public DeviceIdGenerator() {
	}
	
	public String generateDeviceId() {
		int dlnId = rnd.nextInt(MAX_DEVICE_ID);
		StringBuilder sb = new StringBuilder("d");
		sb.append(dlnId);
		return sb.toString();

	}
	
	public static void main(String[] args) {
		DeviceIdGenerator dig = new DeviceIdGenerator();
		
		for (int i = 0; i < 5; i++) {
			System.out.println(dig.generateDeviceId());
		}
	}

}
