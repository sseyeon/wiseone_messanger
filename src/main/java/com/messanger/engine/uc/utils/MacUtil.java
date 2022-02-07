package com.messanger.engine.uc.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class MacUtil {
	public static void main(String[] args) {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			/* IP 주소 가져오기 */
			String ipAddr = addr.getHostAddress();
			System.out.println(ipAddr);

			/* 호스트명 가져오기 */
			String hostname = addr.getHostName();
			System.out.println(hostname);

			/* NetworkInterface를 이용하여 현재 로컬 서버에 대한 하드웨어 어드레스를 가져오기 */
			NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
			byte[] mac = ni.getHardwareAddress();
			String macAddr = "";
			for (int i = 0; i < mac.length; i++) {
				macAddr += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
			}
			System.out.println(macAddr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
