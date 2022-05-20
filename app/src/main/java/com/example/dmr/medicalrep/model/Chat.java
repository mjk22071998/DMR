package com.example.dmr.medicalrep.model;

public class Chat {
    String repCNIC;
    String docCNIC;
    String repName;
    String docName;
    String repToken;
    String docToken;

    public Chat() {
    }

    public String getRepCNIC() {
        return repCNIC;
    }

    public void setRepCNIC(String repCNIC) {
        this.repCNIC = repCNIC;
    }

    public String getDocCNIC() {
        return docCNIC;
    }

    public void setDocCNIC(String docCNIC) {
        this.docCNIC = docCNIC;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getRepToken() {
        return repToken;
    }

    public void setRepToken(String repToken) {
        this.repToken = repToken;
    }

    public String getDocToken() {
        return docToken;
    }

    public void setDocToken(String docToken) {
        this.docToken = docToken;
    }
}
