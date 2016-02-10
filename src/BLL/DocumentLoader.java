/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BLL;

import BE.Document;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface DocumentLoader {

    public List<Document> loadAllDocumentsWithTerms(String baseFilePath)
            throws IOException;

    public List<Document> loadAllDocuments(String baseFilePath)
            throws IOException;
}
