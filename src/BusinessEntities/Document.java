package BusinessEntities;

import java.util.Date;
import java.util.List;

public class Document {

    private int document_ID;
    private String document_URL;
    private Date document_IndexTime;
    private List<Term> document_Terms;

    public Document(int ID, String url) {
        this.document_ID = ID;
        this.document_URL = url;
    }

    public Document(String url, Date indexTime, List<Term> document_Terms) {
        this.document_URL = url;
        this.document_IndexTime = indexTime;
    }

    public Document(int ID, String url, Date indexTime,
            List<Term> document_Terms) {

        this.document_ID = ID;
        this.document_URL = url;
        this.document_IndexTime = indexTime;
        this.document_Terms = document_Terms;
    }

    public int getDocument_ID() {
        return document_ID;
    }

    public void setDocument_ID(int document_ID) {
        this.document_ID = document_ID;
    }

    public String getDocument_URL() {
        return document_URL;
    }

    public void setDocument_URL(String document_URL) {
        this.document_URL = document_URL;
    }

    public Date getDocument_IndexTime() {
        return document_IndexTime;
    }

    public void setDocument_IndexTime(Date document_IndexTime) {
        this.document_IndexTime = document_IndexTime;
    }

    public List<Term> getDocument_Terms() {
        return document_Terms;
    }

    public void setDocument_Terms(List<Term> document_Terms) {
        this.document_Terms = document_Terms;
    }
}
