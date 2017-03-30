package com.project.ui;

import com.project.gethumbnails.ThumbUtilities;
import com.project.gethumbnails.TubeSources;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Controller implements IController{

    public Button BtnDownload;
    public TextField EtxtUrl;
    public ImageView ImgViewNail;
    public Label TxtDestinationFolder;
    public ProgressIndicator PBDownload;
    public ProgressBar PBDownloadImage;
    public boolean barType = false;

    public ToggleGroup radioGroup, radioGroupSelect;
    public int imageQuality;

    public TextField EtxtUrlFilePath;
    public Button BtnDownloadImages;
    public Pagination PageUrlName;
    public VBox VBDownloadedUrls;
    public int urlCounter = 0;
    public List<TubeSources> tubeSourceList;
    public ImageView ImgViewNail1;

    public Button BtnSelectSourceFile;
    private boolean isSourceURL = true;

    public void initialize(){
        TxtDestinationFolder.setText(System.getProperty("user.home") + "\\Downloads");

        radioGroup = new ToggleGroup();
        radioGroupSelect = new ToggleGroup();

        setBackgroundImage();
    }

    private void setBackgroundImage(){
        ImgViewNail.setImage(new Image("assets/no_image.png"));
        ImgViewNail.setOpacity(0.4);
    }

    @FXML
    void onDownloadNail(ActionEvent actionEvent){
        if(EtxtUrl.getText().isEmpty() || TxtDestinationFolder.getText().isEmpty())
            return;

        if(!barType) {
            PBDownload.setProgress(0.0);
            Task copyWorker = createWorker();
            PBDownload.progressProperty().unbindBidirectional((Property<Number>) copyWorker.progressProperty());
            PBDownload.progressProperty().bindBidirectional((Property<Number>) copyWorker.progressProperty());

            new Thread(copyWorker).start();
        }else{
            PBDownloadImage.setProgress(0.0);
            Task copyWorker = createWorker();

            copyWorker.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {
                    System.out.println(newValue);
                }
            });
            new Thread(copyWorker).start();
        }
    }

    @FXML
    void onDownloadNailFromURLs(ActionEvent actionEvent){
        if(EtxtUrlFilePath.getText().isEmpty() || TxtDestinationFolder.getText().isEmpty())
            return;

        if(!barType) {
            PBDownload.setProgress(0.0);
            Task copyFileWorker = createFileWorker();
            PBDownload.progressProperty().unbindBidirectional((Property<Number>) copyFileWorker.progressProperty());
            PBDownload.progressProperty().bindBidirectional((Property<Number>) copyFileWorker.progressProperty());

            new Thread(copyFileWorker).start();
        }else{
            PBDownloadImage.setProgress(0.0);
            Task copyFileWorker = createFileWorker();

            copyFileWorker.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {
                    System.out.println(newValue);
                }
            });
            new Thread(copyFileWorker).start();
        }
    }

    public Task createWorker() {
        System.out.println("==> createWorker()");
        if(isSourceURL) {
            //System.out.println("==> isSourceURL - createURLWorker()");
            return createURLWorker();
        }else {
            //System.out.println("==> isSourceURL - createFileWorker()");
            return createFileWorker();
        }
    }

    public Task createURLWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                String videoUrl = EtxtUrl.getText();
                String destinationFolder =  TxtDestinationFolder.getText();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        PageUrlName.setPageFactory(new Callback<Integer, Node>() {
                            @Override
                            public Node call(Integer pageIndex) {
                                return null;
                            }
                        });
                        PageUrlName.setPageCount(1);
                    }
                });

                PageUrlName.setDisable(true);
                setBackgroundImage();

                System.out.println("==>" + videoUrl + "   ==>" + destinationFolder);
                if(!barType) {
                    PBDownload.setVisible(true);
                }else{
                    PBDownloadImage.setVisible(true);
                }
                TubeSources tube = new TubeSources(videoUrl, destinationFolder, imageQuality, 0);
                if(tube.downloadTubeImage(Controller.this)) {
                    System.out.println(" == Download Completed ==");

                    File file = new File(destinationFolder+File.separator+tube.getVideoName());
                    Image image = new Image(file.toURI().toString());
                    ImgViewNail.setImage(image);
                    ImgViewNail.setOpacity(1.0);
                } else {
                    System.out.println("ERROR !!!!!!! ");
                }

                return true;
            }
        };
    }

    public Task createFileWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                String urlFilePath = EtxtUrl.getText();
                String destinationFolder = TxtDestinationFolder.getText();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        PageUrlName.setPageFactory(new Callback<Integer, Node>() {
                            @Override
                            public Node call(Integer pageIndex) {
                                return null;
                            }
                        });
                        PageUrlName.setPageCount(1);
                    }
                });
                PageUrlName.setDisable(false);

                setBackgroundImage();

                if(!barType) {
                    PBDownload.setVisible(true);
                }else{
                    PBDownloadImage.setVisible(true);
                }

                try {
                    // FileReader reads text files in the default encoding.
                    FileReader fileReader =
                            new FileReader(urlFilePath);

                    // Always wrap FileReader in BufferedReader.
                    BufferedReader bufferedReader =
                            new BufferedReader(fileReader);
                    String videoUrl = null;
                    tubeSourceList = new ArrayList<TubeSources>();
                    urlCounter = 0;
                    while((videoUrl = bufferedReader.readLine()) != null) {
                        System.out.println("url --> " + videoUrl);

                        TubeSources tube = new TubeSources(videoUrl, destinationFolder, imageQuality, urlCounter);
                        if (tube.downloadTubeImage(Controller.this)) {
                            System.out.println(" == Download Completed ==");
                            urlCounter++;
                            tubeSourceList.add(tube);


                            final String finalVideoUrl = tube.getVideoName();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    PageUrlName.setPageFactory(new Callback<Integer, Node>() {
                                        @Override
                                        public Node call(Integer pageIndex) {
                                            return createPage(pageIndex, finalVideoUrl, urlCounter);
                                        }
                                    });

                                    if (urlCounter % itemsPerPage() == 0)
                                        PageUrlName.setPageCount(urlCounter / itemsPerPage());
                                    else
                                        PageUrlName.setPageCount((urlCounter / itemsPerPage()) + 1);
                                }
                            });

                        } else {
                            System.out.println("ERROR !!!!!!! ");
                        }
                    }

                    if(urlCounter < itemsPerPage() ) {
                        final String finalVideoUrl = "";
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                PageUrlName.setPageFactory(new Callback<Integer, Node>() {
                                    @Override
                                    public Node call(Integer pageIndex) {
                                        return createPage(pageIndex, finalVideoUrl, urlCounter);
                                    }
                                });
                            }
                        });
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (urlCounter > itemsPerPage()) {
                                if (urlCounter % itemsPerPage() == 0)
                                    PageUrlName.setPageCount((urlCounter / itemsPerPage()));
                                else
                                    PageUrlName.setPageCount((urlCounter / itemsPerPage()) + 1);
                            } else {
//                                PageUrlName.setPageCount(1);
                            }
                        }
                    });


                    bufferedReader.close();
                }
                catch(FileNotFoundException ex) {
                    System.out.println(
                            "Unable to open file '" +
                                    urlFilePath + "'");
                }
                catch(IOException ex) {
                    System.out.println(
                            "Error reading file '"
                                    + urlFilePath + "'");
                }

                return true;
            }
        };
    }

    public void onSelectDestFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(ThumbUtilities.WINDOW_TITLE_DOWNLOAD_FOLDER);

        File selectedDirectory = directoryChooser.showDialog(null);

        if(selectedDirectory == null){
            String defPath = System.getProperty("user.home")+ThumbUtilities.DOWNLOAD_IMAGE_FOLDER;
            TxtDestinationFolder.setText(defPath);
        }else{
            TxtDestinationFolder.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void onSelectDownloadSourceFileChooser(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ThumbUtilities.WINDOW_TITLE_SELECT_DOWNLOAD_SOURCE_FILE);
        File file = fileChooser.showOpenDialog(BtnSelectSourceFile.getScene().getWindow());

        //System.out.println("=========>>> "+file.getAbsolutePath());
        EtxtUrl.setText(file.getAbsolutePath());
    }

    public void onSelectDownloadSourceURL(ActionEvent actionEvent){
        //downloadSourceType = ThumbUtilities.TubeDownloadSourceType.URL;
        BtnSelectSourceFile.setDisable(true);
        EtxtUrl.setText("");
        isSourceURL = true;
    }

    public void onSelectDownloadSourceFile(ActionEvent actionEvent){
        //downloadSourceType = ThumbUtilities.TubeDownloadSourceType.FILE;
        BtnSelectSourceFile.setDisable(false);
        EtxtUrl.setText("");
        isSourceURL = false;
    }

    public void onImageQualityLOW(ActionEvent actionEvent) {
        //System.out.println("==========  LOW ============ ");
        imageQuality = ThumbUtilities.QUALITY_SMALL_IMAGE;
    }

    public void onImageQualitySTANDARD(ActionEvent actionEvent) {
        //System.out.println("==========  STANDARD ============ ");
        imageQuality = ThumbUtilities.QUALITY_STANDARD_IMAGE;
    }

    public void onImageQualityLARGE(ActionEvent actionEvent) {
        //System.out.println("==========  LARGE ============ ");
        imageQuality = ThumbUtilities.QUALITY_LARGE_IMAGE;
    }

    @Override
    public void downloadImageProgress(double progress, int counter) {
        //System.out.println("Progress="+progress+"   counter="+counter);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(!barType) {
                    if(!PBDownload.isVisible())
                        PBDownload.setVisible(true);
                    PBDownload.setProgress(progress);

                    if (progress == 1)
                        PBDownload.setVisible(false);
                }else{
                    if(!PBDownloadImage.isVisible())
                        PBDownloadImage.setVisible(true);
                    PBDownloadImage.setProgress(progress);

                    if (progress == 1)
                        PBDownloadImage.setVisible(false);
                }
            }
        });
    }


    public int itemsPerPage() {
        return 5;
    }

    public VBox createPage(int pageIndex, String videoUrl, int urlCounter) {
        VBox VBDownloadedUrls = new VBox(itemsPerPage());
        int page = pageIndex * itemsPerPage();
        System.out.println(pageIndex + " - "+ videoUrl +" - " +urlCounter);

        Hyperlink text = null;
        for (int i = page; i < page + itemsPerPage() && i<urlCounter; i++) {
            //System.out.println("========> VName ="+ tubeSourceList.get(i).getVideoName()+"   "+tubeSourceList.get(i).getDestinationFolder());
            text = new Hyperlink(tubeSourceList.get(i).getVideoName());
            final int finalI = i;
            //text.setStyle("-fx-text-fill: #0B6332;-fx-padding: 0; -fx-line-spacing: 0em;");  //set the hyperlink to black.
            //text.setLineSpacing(0);
            text.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    File file = new File(tubeSourceList.get(finalI).getDestinationFolder() + File.separator + tubeSourceList.get(finalI).getVideoName());
                    Image image = new Image(file.toURI().toString());
                    ImgViewNail.setImage(image);
                    ImgViewNail.setOpacity(1.0);
                }
            });

            VBDownloadedUrls.getChildren().add(text);
        }

        return VBDownloadedUrls;
    }
}
