package org.cidarlab.OwlPackager.adaptors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.text.WordUtils;
import org.cidarlab.OwlPackager.Util.Utilities;
import org.cidarlab.OwlPackager.dom.GeneticConstruct;


public class FastaFileFactory {

	public static String createDeviceFastaFile(GeneticConstruct device, File outputDir){
    	final String pathToFile = outputDir.getPath() + Utilities.getFileDivider() + device.getName() + ".fasta";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile));
            writer.write(">"+device.getName());
            writer.newLine();
            writer.write(WordUtils.wrap(device.getSequence(), 80, "\n", true));
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(FastaFileFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pathToFile;
    }
	
}
