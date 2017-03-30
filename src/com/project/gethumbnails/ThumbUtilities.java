package com.project.gethumbnails;

public final class ThumbUtilities {
	public enum TubeImageQuality{
		SMALL_IMAGE,
		STANDARD_IMAGE,
		LARGE_IMAGE
	}

	public enum TubeDownloadSourceType{
		URL,
		FILE
	}
	
	public static final int QUALITY_SMALL_IMAGE = 0;
	public static final int QUALITY_STANDARD_IMAGE = 1;
	public static final int QUALITY_LARGE_IMAGE = 2;

	public static final int DOWNLOAD_SOURCE_TYPE_URL = 111;
	public static final int DOWNLOAD_SOURCE_TYPE_FILE = 112;

	public static final String DOWNLOAD_IMAGE_FOLDER = "\\Downloads";
	public static final String WINDOW_TITLE_DOWNLOAD_FOLDER = "Select Download Folder";
	public static final String WINDOW_TITLE_NAME = "Download Youtube Thumbnail";
	public static final String WINDOW_TITLE_SELECT_DOWNLOAD_SOURCE_FILE = "Select Source File" ;
	
}
