package ua.od.radio.pozitivefm.data.model;

public class TrackModel {
    private long ts;
    private String metadata;
    private String author;
    private String title;
    private String dj;
    private String img_url;
    private String img_medium_url;
    private String img_large_url;

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDj() {
        return dj;
    }

    public void setDj(String dj) {
        this.dj = dj;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_medium_url() {
        return img_medium_url;
    }

    public void setImg_medium_url(String img_medium_url) {
        this.img_medium_url = img_medium_url;
    }

    public String getImg_large_url() {
        return img_large_url;
    }

    public void setImg_large_url(String img_large_url) {
        this.img_large_url = img_large_url;
    }
}
