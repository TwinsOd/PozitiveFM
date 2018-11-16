package ua.od.radio.pozitivefm.data.model;

public class SettingsModel {
    private String url;
    private Boolean isActiveRadio;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActiveRadio() {
        return isActiveRadio;
    }

    public void setActiveRadio(Boolean activeRadio) {
        isActiveRadio = activeRadio;
    }
}
