package org.cidarlab.EugeneParser.tests;

import org.cidarlab.OwlPackager.Util.CmdLineParser;
import org.cidarlab.OwlPackager.Util.Utilities;
import org.junit.Assert;
import org.junit.Test;

import com.beust.jcommander.JCommander;

public class CmdLineParserTest {

	@Test
	public void testCommandlineParser(){
		
		CmdLineParser cmd = new CmdLineParser();
		
		String[] argv = { "-i", Utilities.getResourcesFilepath()+"output.out", "-project", "FP-2016", "-o", "src/main/resources/result" 
						};
		
		JCommander jc = new JCommander(cmd, argv);
		jc.setProgramName("EugeneParser");
		jc.usage();
		
		Assert.assertEquals(cmd.getProjectName(), "FP-2016");
		System.out.println("Project name: " + cmd.getProjectName());
		System.out.println("output folder: " + cmd.getOutput().getAbsolutePath());
		
		Utilities.deleteFolder(cmd.getOutput());
				
	}
	
}
