package org.cidarlab.OwlPackager.adaptors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.cidarlab.OwlPackager.Util.Utilities;
import org.cidarlab.OwlPackager.dom.GeneticConstruct;
import org.cidarlab.OwlPackager.dom.Part;
import org.cidarlab.OwlPackager.dom.PartType;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.Getter;

public class PigeonClient {
	
	@Getter private static Map<String, Integer> colors = new HashMap<>();

    public static void requestPigeon(String pigeonCodeEncoded) throws UnirestException, IOException {
        HttpResponse<String> response = Unirest.post("http://synbiotools.bu.edu:5801/dev/perch2.php")
                .header("content-type", "application/x-www-form-urlencoded")
                .body("specification=" + pigeonCodeEncoded)
                .asString();

        //System.out.println("============Pigeon API=============");
        //System.out.println(response.getBody().toString());

        JsonNode body = new JsonNode(response.getBody());
        String pigeonImageUrl = body.getObject().get("fileURL").toString();

        //System.out.println(pigeonImageUrl);

        //System.out.println("downloading a Pigeon image...");
        BufferedImage image = null;
        URL url = new URL(pigeonImageUrl);
        image = ImageIO.read(url);
        File outputFile = new File("image.png");

        //System.out.println("saving an image...");
        ImageIO.write(image, "png", outputFile);
        //System.out.println(outputFile.toString() + " was saved successfully.");
        //System.out.println(outputFile.getCanonicalPath());
    }

    public static String generatePigeonScript(GeneticConstruct device) {
        String script = "";
        
        for(Part part: device.getPartList()){
            	
                if (part.getPartType()==PartType.PROMOTER) {
                    script += "p "+part.getPartInstance() + " " + getColor(part.getPartProperties().getName()) + "%0D%0A";
                } else if (part.getPartType()==PartType.RBS) {
                    script += "r "+part.getPartInstance() + " " + getColor(part.getPartProperties().getName()) + "%0D%0A";
                } else if (part.getPartType()==PartType.RIBOZYME) {
                    script += "z "+part.getPartInstance() + " " + getColor(part.getPartProperties().getName()) + "%0D%0A";
                } else if (part.getPartType()==PartType.CDS) {
                    script += "c "+part.getPartInstance() + " " + getColor(part.getPartProperties().getName()) + "%0D%0A";
                } else if (part.getPartType()==PartType.TERMINATOR) {
                    script += "t "+part.getPartInstance() + " " + getColor(part.getPartProperties().getName()) + "%0D%0A";
                } else {
                    System.err.println("Urecognized part type: " + part);
                    System.exit(1);
                }
                
            }
            script += "# Arcs";
        
        return script;
    }

    public static Map<String,String> generateFile(Map<String, String> devicePigeonMap, File projectDir, int limit) throws UnirestException, IOException {
        
        Map<String,String> pigeonFilepath = new LinkedHashMap<String,String>();
        
        Iterator<String> iterator = devicePigeonMap.keySet().iterator();
        for (int i = 0; iterator.hasNext() && i < limit; i++){
        		
        		String key = iterator.next();
	            //System.out.println(key + " : " + devicePigeonMap.get(key));
	            HttpResponse<String> response = Unirest.post("http://synbiotools.bu.edu:5801/dev/perch2.php")
	                    .header("content-type", "application/x-www-form-urlencoded")
	                    .body("specification=" + devicePigeonMap.get(key))
	                    .asString();
	
	            JsonNode body = new JsonNode(response.getBody());
	            String pigeonImageUrl = body.getObject().get("fileURL").toString();
	
	            //System.out.println(pigeonImageUrl);
	
	            //System.out.println("downloading a Pigeon image...");
	            BufferedImage image = null;
	            URL url = new URL(pigeonImageUrl);
	            image = ImageIO.read(url);
	            final String pathToFile = projectDir.getPath() + Utilities.getFileDivider() + key + ".png";
	            File outputFile = new File(pathToFile);
	
	            //System.out.println("saving " + key + ".png image...");
	            ImageIO.write(image, "png", outputFile);
	            //System.out.println(outputFile.toString() + " was saved successfully.\n");
	            pigeonFilepath.put(key, outputFile.getAbsolutePath());
        }
        
        /*for(String s: pigeonFilepath.keySet()){
        	System.err.println(s + " - " + pigeonFilepath.get(s));
        }*/

        return pigeonFilepath;
    }
     
    /**
	 * This private method gives a new color for a new Part or a corresponding color or the existing Part.
	 * 
	 * @param s  ... the name of a Part
	 * @return   int color ... the color code of the Part
	 */
	private static int getColor(String s) {
		if(colors.containsKey(s)) {
			int color = colors.get(s);
			if(color <= 1) {
				return 1;
			} else if(color >= 14) {
				return 14;
			}
			return color;
		}

		/*
		 * otherwise, we put the name into the coloring map
		 */
		int color = getRandomColor();
		colors.put(s, color);
		return color;
	}
	
	private static final int COLOR_MIN = 1;
	private static final int COLOR_MAX = 14;
	
	private static int getRandomColor() {
		return COLOR_MIN + (int)(Math.random() * ((COLOR_MAX - COLOR_MIN) + 1));
	}
	
}
