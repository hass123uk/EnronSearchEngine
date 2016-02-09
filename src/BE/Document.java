package BE;

import java.util.Date;
import java.util.Map;

public class Document {

    private int document_ID;
    private String document_URL;
    private Date document_IndexTime;

    private Map<Integer, Term> position_Term;

    public Document(int ID, String url) {
        this.document_ID = ID;
        this.document_URL = url;
    }

    public Document(int ID, String url, Date indexTime,
            Map<Integer, Term> position_Term) {

        this.document_ID = ID;
        this.document_URL = url;
        this.document_IndexTime = indexTime;
        this.position_Term = position_Term;
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

    public Map<Integer, Term> getPosition_Term() {
        return position_Term;
    }

    public void setPosition_Term(Map<Integer, Term> position_Term) {
        this.position_Term = position_Term;
    }

}
