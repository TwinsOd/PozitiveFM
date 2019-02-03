package ua.od.radio.pozitivefm.data.model;

public class SettingsModel {
    private String id;//0
    private String catid;//8228
    private String shout;//<tr class="shout-row odd  " id="shout-8228">                        <td class="shout-avatar" ><a title="Просмотреть профиль"><img src="components/com_smartshoutbox/assets/images/avatar.png" title="TwinsOD"/></a></td><td class="at-tag"><a title="Ответить TwinsOD" onclick="addName(this, '8228')">TwinsOD</a><input type="hidden" id="fullname-8228" name="fullname" value="TwinsOD"></td><td class="shout-text">I want Imagine Dragons)</td><td class="shout-category"><a id="category-0" class="search-category" title="Фильтр по этой категории">Без категории</a></td><td class="shout-likes"><span title="Нравится сообщение">0</span></td><script type="text/javascript">function like_custom(id){jQuery("#like-"+id).trigger( "click" );}</script><td class="shout-actions"><div id="actions-8228" class="shout-hide"></div><a id="menu-8228" class="button-menu" title="Открыть меню"><i class="fa fa-navicon"></i></a></td><td class="shout-date"><span class="full-date"><b> 14:51</b><br>08.12.18</span><span class="short-date">(03:00)</span></td></tr>"

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getShout() {
        return shout;
    }

    public void setShout(String shout) {
        this.shout = shout;
    }

//    private String url;
//    private Boolean isActiveRadio;
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public Boolean getActiveRadio() {
//        return isActiveRadio;
//    }
//
//    public void setActiveRadio(Boolean activeRadio) {
//        isActiveRadio = activeRadio;
//    }
}
