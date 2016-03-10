package com.enron.search.domainmodels;

public class Term {

    private String term_ID;
    private String term_Value;

    public Term(String Value) {
        this.term_Value = Value;
    }

    public Term(String ID, String Value) {
        this.term_ID = ID;
        this.term_Value = Value;
    }

    public String getTerm_ID() {
        return term_ID;
    }

    public void setTerm_ID(String term_ID) {
        this.term_ID = term_ID;
    }

    public String getTerm_Value() {
        return term_Value;
    }

    public void setTerm_Value(String term_Value) {
        this.term_Value = term_Value;
    }
}
