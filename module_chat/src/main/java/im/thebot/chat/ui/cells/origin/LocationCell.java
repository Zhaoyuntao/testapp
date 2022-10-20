package im.thebot.chat.ui.cells.origin;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.thebot.chat.api.chat.message.LocationMessageForUI;
import im.thebot.chat.ui.cells.origin.base.BaseMessageCell;
import im.thebot.chat.ui.view.ChatImageView;

/**
 * created by zhaoyuntao
 * on 18/05/2020
 * description:
 */
public class LocationCell extends BaseMessageCell<LocationMessageForUI> {
    private ChatImageView mapView;

    @Override
    public int setLayout() {
        return R.layout.layout_chat_cell_item_location;
    }

    @Override
    public boolean isMaxWidth() {
        return true;
    }

    @Override
    public void initView(@NonNull Context context) {
        mapView = findViewById(R.id.location_cell_preview);
    }

    @Override
    public void onMessageInit(@NonNull LocationMessageForUI messageBean) {
        if (!TextUtils.isEmpty(messageBean.getLocationPreviewURL())) {
//            mapView.setImageURI(messageBean.getLocationPreviewURL());
        }
    }
}
