package org.cidarlab.OwlPackager.Util;

import java.io.File;

import com.beust.jcommander.Parameter;

import lombok.Getter;

public class CmdLineParser {
	
	@Parameter(names = "-i", description = "<INPUT FILE> Eugene array[] (e.g., \"projectID.eug\" file)", converter = FileConverter.class)
	@Getter File eugeneFile;
	
	@Parameter(names = "-project", description = "Project name")
	@Getter private String projectName;
	
	@Parameter(names = { "-help", "-h" }, description = "print this message")
	@Getter private boolean help = false;
	
	@Parameter(names = "-o", description = "<Output dir>  example: -o src/main/resources/result", converter = DirConverter.class)
	@Getter File output;
	
	@Parameter(names = "-e", description = "<Error-log file>", converter = FileConverter.class)
	@Getter File errors;
	
	@Parameter(names = "-latex", description = "Path to a directory where pdflatex executable is located. Example: -latex /src/main/resources/texlive/bin", converter = LatexPathConverter.class)
	@Getter File latex;
	
	@Parameter(names = "-pigeon", description = "(optional) use PigeonCAD instead of DNAplotlib")
	@Getter private boolean withPigeon = false;
	
	@Parameter(names = "-dnaplotlib", description = "Path to a directory where dnaplotlib quick.py script is located. Example: -dnaplotlib /dnaplotlib/apps/", converter = DNAplotlibPathConverter.class)
	@Getter File dnaplotlib;
	
}
