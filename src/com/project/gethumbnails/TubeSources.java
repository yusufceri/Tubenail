package com.project.gethumbnails;

import com.project.ui.IController;

public class TubeSources {

	private String videoUrl;
//	private String channelUrl;
	private String destinationFolder;
	private TubeImage tubeImage;
	private int tubeImageQuality;
	private int counter;
	
	public TubeSources(String videoUrl, String destinationFolder, int imageQuality, int counter){
		this.videoUrl = videoUrl;
		this.destinationFolder = destinationFolder;
		tubeImageQuality = imageQuality;
		tubeImage = new TubeImage();
		this.counter = counter;
	}
	
	public boolean downloadTubeImage(IController ui){
		String videoId;
		if((videoId=getVideoID(videoUrl)) == null){
			return false;
		}
		
		if((videoUrl = getImageUrl(videoId)) == null){
			return false;
		}
		
		String imageName;
		if((imageName = getVideoName(videoId, tubeImageQuality)) == null){
			return false;
		}
		
		return tubeImage.downloadTubeImage(ui,imageName+".jpg", destinationFolder, videoUrl, counter);
		
		
	}
	
	private String getVideoID(String videoUrl){
		String str = "/watch?v="; // "?v=";
		String videoID;
		
		if(videoUrl.contains(str)){
			videoID = videoUrl.substring(videoUrl.lastIndexOf(str)+str.length());
			//videoID = videoUrl.split(str);
			System.out.println("VIDEOID = "+videoID);
			return videoID;
		}else{
			return null;
		}
	}
	
	private String getImageUrl(String videoId){
		String imageUrl;
		
		switch(tubeImageQuality){
		case ThumbUtilities.QUALITY_SMALL_IMAGE:
			imageUrl = "http://img.youtube.com/vi/"+videoId+"/mqdefault.jpg";
			break;
				
		case ThumbUtilities.QUALITY_STANDARD_IMAGE:
			imageUrl = "http://img.youtube.com/vi/"+videoId+"/0.jpg";
			break;
			
		case ThumbUtilities.QUALITY_LARGE_IMAGE:
			imageUrl = "http://img.youtube.com/vi/"+videoId+"/maxresdefault.jpg";
			break;
			
		default:
			imageUrl = null;
		}
		
		return imageUrl;
	}
	
	private String getVideoName(String videoId, int tubeImageQuality){
		//
		//TODO find video name, connect to page extract videoname from html
		//

		String imageName;
		if(tubeImageQuality == ThumbUtilities.QUALITY_SMALL_IMAGE){
			imageName = videoId + "_SMALL";
		}else if(tubeImageQuality == ThumbUtilities.QUALITY_STANDARD_IMAGE){
			imageName = videoId + "_STANDARD";
		}else if(tubeImageQuality == ThumbUtilities.QUALITY_LARGE_IMAGE){
			imageName = videoId + "_LARGE";
		}else{
			imageName = videoId;
			System.out.println("ERROR - IMAGE QUALITY IS NOT DEFINED !!!!");
		}

		return imageName;
		//return "test"; //Temporary
	}

	public String getVideoName(){
		return tubeImage.getImageName();
	}

	public String getDestinationFolder(){
		return destinationFolder;
	}
}

