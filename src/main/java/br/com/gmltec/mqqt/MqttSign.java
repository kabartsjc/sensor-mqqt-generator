package br.com.gmltec.mqqt;

import br.com.gmltec.utils.CryptoUtil;

public class MqttSign {
	private String username = "";

	private String password = "";

	private String clientid = "";

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getClientid() {
		return this.clientid;
	}

	public void calculate(String productKey, String deviceName, String deviceSecret) {
		if (productKey == null || deviceName == null || deviceSecret == null) {
			return;
		}

		try {
			this.username = deviceName + "&" + productKey;

			String timestamp = Long.toString(System.currentTimeMillis());
			String plainPasswd = "clientId" + productKey + "." + deviceName + "deviceName" + deviceName + "productKey"
					+ productKey + "timestamp" + timestamp;
			this.password = CryptoUtil.hmacSha256(plainPasswd, deviceSecret);

			this.clientid = productKey + "." + deviceName + "|" + "timestamp=" + timestamp
					+ ",_v=paho-java-1.0.0,securemode=2,signmethod=hmacsha256|";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}