/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enron.SearchEngine;

import com.enron.search.domainmodels.Document;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableView;

public class SearchController implements Initializable {

    @FXML
    private TreeTableView<Document> documentsTreeView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        SearchDB searchDB = new SearchDB();
//        Multimap<String, String> terms_DocPaths
//                = searchDB.getSimilarTermsWithDocumentPath("enron");
//
//        setUpTreeTable(terms_DocPaths);
    }

//    private void setUpTreeTable(Map<String, String> documents) {
//        TreeItem<Document> rootNode = createTreeItems(documents);
//        TreeTableColumn<Document, String> docURLColumn
//                = createColumn();
//
//        documentsTreeView.setRoot(rootNode);
//        documentsTreeView.showRootProperty().set(false);
//        documentsTreeView.getColumns().add(docURLColumn);
//    }
//
//    private TreeItem< Document> createTreeItems(List<Document> files) {
//        TreeItem<Document> rootNode = new TreeItem<>();
//        createTreeItemPerDocument(files, rootNode);
//        return rootNode;
//    }
//
//    private void createTreeItemPerDocument(List<Document> documents,
//            TreeItem<Document> rootNode) {
//        documents.stream().forEach((Document document) -> {
//            TreeItem<Document> fileTreeItem = new TreeItem<>(document);
//            rootNode.getChildren().add(fileTreeItem);
//        });
//    }
//
//    private TreeTableColumn<Document, String> createColumn() {
//
//        TreeTableColumn<Document, String> fileColumn
//                = new TreeTableColumn<>("Document");
//        fileColumn.setPrefWidth(400);
//        fileColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Document, String> param)
//                -> new ReadOnlyStringWrapper(param.getValue().getValue().getDocument_URL())
//        );
//        return fileColumn;
//    }
}
