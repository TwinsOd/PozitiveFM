package ua.od.radio.pozitivefm.data.model;

public class SettingsAppModel {
    private int video;
    private int menu;

    public SettingsAppModel(int menu, int video) {
        this.menu = menu;
        this.video = video;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }
}
