package EnronSearchEngine;

import BE.Document;
import BE.Term;
import BLL.DocumentLoader;
import BLL.DocumentLoaderImpl;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class EnronSearchEngineGUI extends Application {

    private static final String HOME_DIR = "/home/linux/Desktop";
    private static final String FILE_NAME = "/maildir";

    private static final String ALL_DOCS = "";
    private static final String FEW_DOCS = "/zufferli-j/all_documents";

    private static final String ENRON_DATASET_DIR
            = HOME_DIR
            + FILE_NAME
            + FEW_DOCS;

    @Override
    public void start(Stage primaryStage) {
        Label lbl = new Label();
        lbl.setText("Enter in Dictory to Index");
        TextField txtfield = new TextField();

        Button btn = new Button();
        btn.setText("Start");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                DocumentLoader loader = new DocumentLoaderImpl();

                try {
                    List<Document> allDocuments
                            = loader.loadAllDocumentsWithTerms(txtfield.getText());

                    System.out.println(allDocuments.size());
                    System.out.println(allDocuments.get(0).getDocument_URL());
                    allDocuments.get(0).getDocument_Terms().stream()
                            .forEach((Term t) -> System.out.println(t.getTerm_Value()));
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                //System.out.println("Hello World!");
            }
        });

        FlowPane root = new FlowPane();
        root.getChildren().add(lbl);
        root.getChildren().add(txtfield);
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        // primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
