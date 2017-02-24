package org.cidarlab.EugeneParser.tests;

import java.io.File;
import java.io.IOException;

import org.cidarlab.OwlPackager.Util.ShellExec;
import org.cidarlab.OwlPackager.Util.Utilities;
import org.junit.Test;

public class DatasheetTest {

	@Test
	public void testLatex(){
		
/*        Map<String, String> images = new LinkedHashMap<String, String>();
        Map<String, String> gcContentMap = new HashMap<>();
        Map<String, Integer> deviceLengths = new HashMap<>();
        Map<String, String> deviceCompositions = new HashMap<>();
        
        images = PigeonClient.generateFile(devicePigeonMap, projectDir)
        
        
        Datasheet pdf = new Datasheet("test", images, deviceCompositions, gcContentMap, deviceLengths);
        
        LatexAdaptor.generateLatexCode(pdf);*/
		
		File f = new File("C:\\texlive\\2016\\bin\\win32\\pdflatex.exe");
		System.out.println(f.getAbsolutePath());
		System.out.println(Utilities.getOutputFilepath());
		String texPath = Utilities.getResourcesFilepath()+"Monocistronic_new1.tex";
		System.out.println(texPath);
		ShellExec exec = new ShellExec(true, false);
        try {
            int exitCode = exec.execute("pdflatex", Utilities.getOutputFilepath(), true, texPath);
            if(exitCode == 0){
            	System.out.println("pdf file was successfully generated in "+Utilities.getOutputFilepath()+" folder.");
            } else {
            	System.err.println("ERROR: Owl-packager returned exit code = " + exitCode + " while executing PDF-latex.");
            	System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("ERROR. PDF-latex failed. Reason: " + e.getMessage());
        }
        
        Utilities.removeFile(Utilities.getOutputFilepath()+Utilities.getFileDivider()+"Monocistronic_new1.aux");
        Utilities.removeFile(Utilities.getOutputFilepath()+Utilities.getFileDivider()+"Monocistronic_new1.log");
        Utilities.removeFile(Utilities.getOutputFilepath()+Utilities.getFileDivider()+"Monocistronic_new1.out");
        Utilities.removeFile(Utilities.getOutputFilepath()+Utilities.getFileDivider()+"Monocistronic_new1.tex");
	}
	
}
