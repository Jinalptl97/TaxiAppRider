package com.taxibookingrider.Model;

/**
 * Created by admin on 05-Feb-18.
 */

public class Reason {

    String reason,selected;

    public Reason(String reason, String selected) {
        this.reason = reason;
        this.selected = selected;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
