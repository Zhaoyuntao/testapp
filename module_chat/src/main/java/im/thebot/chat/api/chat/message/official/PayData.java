package im.thebot.chat.api.chat.message.official;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * created by zhaoyuntao
 * on 07/04/2022
 * description:
 */
public class PayData implements Cloneable {
    private String key;
    private String name;
    private String value;
    private String type;

    public void parseFromJson(@NonNull JSONObject jsonObject) {
        this.key = jsonObject.optString("key");

        JSONObject contentObject = jsonObject.optJSONObject("content");
        if (contentObject != null) {
            this.name = contentObject.optString("name");
            this.value = contentObject.optString("value");
            this.type = contentObject.optString("type");
        }
    }

    public JSONObject buildToJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", key);

        JSONObject contentObject = new JSONObject();
        contentObject.put("name", name);
        contentObject.put("value", value);
        contentObject.put("type", type);
        jsonObject.put("content", contentObject);
        return jsonObject;
    }

    @NonNull
    @Override
    public PayData clone() {
        try {
            return (PayData) super.clone();
        } catch (CloneNotSupportedException e) {
            return new PayData();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
