package com.example.dmr.medicalrep.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class Request {
    String to;
    String from;
    Timestamp timestamp;
    String status;
    String docName;
    String repName;
    List<Medicine> medicines;

    public Request() {
    }

    public Request(String to, String from, Timestamp timestamp, String status, String docName, String repName, List<Medicine> medicines) {
        this.to = to;
        this.from = from;
        this.timestamp = timestamp;
        this.status = status;
        this.docName = docName;
        this.repName = repName;
        this.medicines = medicines;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }
}
