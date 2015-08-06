package org.mule.tooling.devkit.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipUtils {
	public static boolean extractAllFiles(String zipPath,String desPath){
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			// Extracts all files to the path specified
			zipFile.extractAll(desPath);
			return true;
		} catch (ZipException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static void addFileToExistZip(ZipFile zipFile , File fileToAdd, String fileNameInZip){
		
		InputStream is = null;
		try {
			// Initiate Zip Parameters which define various properties such
			// as compression method, etc. More parameters are explained in other
			// examples
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

			// below two parameters have to be set for adding content to a zip file 
			// directly from a stream
			
			// this would be the name of the file for this entry in the zip file
			parameters.setFileNameInZip(fileNameInZip);
			
			// we set this flag to true. If this flag is true, Zip4j identifies that
			// the data will not be from a file but directly from a stream
			parameters.setSourceExternalStream(true);
			
			// For this example I use a FileInputStream but in practise this can be 
			// any inputstream
			is = new FileInputStream(fileToAdd);
			
			// Creates a new entry in the zip file and adds the content to the zip file
			zipFile.addStream(is, parameters);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
