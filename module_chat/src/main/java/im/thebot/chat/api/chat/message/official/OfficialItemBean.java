package im.thebot.chat.api.chat.message.official;

import java.io.Serializable;

/**
 * created by zhaoyuntao
 * on 06/09/2021
 * description:
 */
public class OfficialItemBean implements Serializable {
    private String cover;
    private String articleNumber;
    private String digest;
    private String numberIndex;
    private String title;
    private String type;
    private String articleUrl;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getNumberIndex() {
        return numberIndex;
    }

    public void setNumberIndex(String numberIndex) {
        this.numberIndex = numberIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
