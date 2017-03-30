package com.project.gethumbnails;

import com.project.ui.IController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TubeImage {
	private String imageName;
	public enum TubeImageQuality{
		SMALL_IMAGE,
		STANDARD_IMAGE,
		LARGE_IMAGE
	};
	
	private String folderName;
	private String imageUrl;

	public TubeImage(){
		
	}
	
	public boolean downloadTubeImage(IController ui, String imageName, String folderName, String imageUrl, int counter){
		this.imageName = imageName;
		String destinationFile = folderName+"/"+imageName;
		
		URL url;
		try {
			url = new URL(imageUrl);
			
			InputStream is = url.openStream();
		    OutputStream os = new FileOutputStream(destinationFile);

		    byte[] b = new byte[2048];
		    int length;

			long sizeOfWrite = 0;
			long fileSize = getFileSize(url);
			System.out.println("fileSize = "+fileSize);
		    while ((length = is.read(b)) != -1) {
		        os.write(b, 0, length);

				sizeOfWrite = sizeOfWrite + length;
				//System.out.println("sizeOfWrite= "+sizeOfWrite+ "   fileSize = " + fileSize);
				double r = (double)sizeOfWrite/(double)fileSize;
				ui.downloadImageProgress(r, counter);
		    }

		    is.close();
		    os.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	    
	}

	public String getImageName(){
		return imageName;
	}

	private long getFileSize(URL url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
			return conn.getContentLengthLong();
		} catch (IOException e) {
			return -1;
			// Or wrap into a (custom, if desired) RuntimeException so exceptions are propagated.
			// throw new RuntimeException(e);
			// Alternatively you can just propagate IOException, but, urgh.
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
