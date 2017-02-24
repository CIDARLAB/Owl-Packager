package org.cidarlab.EugeneParser.tests;

import org.cidarlab.OwlPackager.adaptors.RegExpDevice;
import org.junit.Test;

public class DeviceNumberTest {

	@Test
	public void testDeviceNumber(){
		
		String eugeneFile = "The total number of devices: 2038765\n"
							+ "Array[] lod (\n"
							+ "Device Monocistronic_prgt_1(\n";
		
		RegExpDevice.getDeviceNumber(eugeneFile);
		
	}
	
}
