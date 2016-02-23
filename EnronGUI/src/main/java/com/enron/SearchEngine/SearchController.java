/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enron.SearchEngine;

import com.enron.search.domainmodels.Document;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class SearchController implements Initializable {

    private static final String HOME_DIR = "/Users/HassanMahmud";
    private static final String FILE_NAME = "/EnronDataSet";

    private static final String ALL_DOCS = "/MailDir_FullSet";
    private static final String HALF_ALL_DOCS = "/MailDir_HalfSet";
    private static final String FEW_DOCS = "/MailDir_SubSet";

    private static final String ENRON_DATASET_DIR
            = HOME_DIR
            + FILE_NAME
            + ALL_DOCS;

    @FXML
    private TreeTableView<Document> documentsTreeView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        FileLoader loader = new FileLoaderImpl();
//        TermSplitter splitter = new TermSplitterImpl("//s");
//
//        setUpTreeTable(loadDocuments(loader, splitter));
    }

//    private List<Document> loadDocuments(FileLoader loader, TermSplitter splitter) {
////        DocumentLoader documentsFactory
////                = new DocumentLoaderImpl(loader, splitter);
////        Path basePath = Paths.get(ENRON_DATASET_DIR);
////
////        return documentsFactory.loadDocuments(basePath);
//    }
    private void setUpTreeTable(List<Document> documents) {
        TreeItem<Document> rootNode = createTreeItems(documents);
        TreeTableColumn<Document, String> docURLColumn
                = createColumn();

        documentsTreeView.setRoot(rootNode);
        documentsTreeView.showRootProperty().set(false);
        documentsTreeView.getColumns().add(docURLColumn);
    }

    private TreeItem< Document> createTreeItems(List<Document> documents) {
        TreeItem<Document> rootNode = new TreeItem<>();
        createTreeItemPerDocument(documents, rootNode);
        return rootNode;
    }

    private void createTreeItemPerDocument(List<Document> documents,
            TreeItem<Document> rootNode) {
        documents.stream().forEach((Document document) -> {
            TreeItem<Document> documentTreeItem = new TreeItem<>(document);
            rootNode.getChildren().add(documentTreeItem);
        });
    }

    private TreeTableColumn<Document, String> createColumn() {

        TreeTableColumn<Document, String> catalogueColumn
                = new TreeTableColumn<>("Document");
        catalogueColumn.setPrefWidth(400);
        catalogueColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Document, String> param)
                -> new ReadOnlyStringWrapper(param.getValue().getValue().getDocument_URL())
        );
        return catalogueColumn;
    }
}
