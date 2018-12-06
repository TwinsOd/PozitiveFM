package ua.od.radio.pozitivefm.data.model;

import java.util.List;

public class ChatModel {
    private String nick;
    private String message;
    private List<String> smiles;

    public ChatModel() {
    }

    public ChatModel(String login, String message) {
        nick = login;
        this.message = message;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getSmiles() {
        return smiles;
    }

    public void setSmiles(List<String> smiles) {
        this.smiles = smiles;
    }
}
