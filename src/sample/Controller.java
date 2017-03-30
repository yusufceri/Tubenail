package sample;

import com.project.gethumbnails.ThumbUtilities;
import com.project.gethumbnails.TubeSources;
import com.project.ui.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class Controller implements IController{

    public Button BtnDownload;
    public TextField EtxtUrl;
    public ImageView ImgViewNail;
    public Label TxtDestinationFolder;


    public void initialize(){
        TxtDestinationFolder.setText(System.getProperty("user.home")+"\\Downloads");
    }

    @FXML
    void onDownloadNail(ActionEvent actionEvent){
        //EtxtUrl.setText("TEST");

        if(EtxtUrl.getText().isEmpty() || TxtDestinationFolder.getText().isEmpty())
            return;

        String videoUrl = EtxtUrl.getText(); //"https://www.youtube.com/watch?v=ig_qpNfXHIU";
        String destinationFolder =  TxtDestinationFolder.getText(); // "C:\\Users\\Ceri\\Downloads";
        int imageQuality = ThumbUtilities.QUALITY_LARGE_IMAGE;

        TubeSources tube = new TubeSources(videoUrl, destinationFolder, imageQuality, 0);
        if(tube.downloadTubeImage(this)) {
            System.out.println(" == Download Completed ==");

            File file = new File("C:\\Users\\Ceri\\Downloads\\test.jpg");
            Image image = new Image(file.toURI().toString());
            ImgViewNail.setImage(image);
        }else{
            System.out.println("ERROR !!!!!!! ");
        }
    }

    public void onSelectDestFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("JavaFX Projects");
        File defaultDirectory = new File("c:/dev/javafx");
        File selectedDirectory = directoryChooser.showDialog(null);

        if(selectedDirectory == null){
            String defPath = System.getProperty("user.home")+"\\Downloads";
            TxtDestinationFolder.setText(defPath);
        }else{
            TxtDestinationFolder.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @Override
    public void downloadImageProgress(double progress, int counter) {

    }
}
