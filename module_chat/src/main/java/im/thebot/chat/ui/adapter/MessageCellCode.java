package im.thebot.chat.ui.adapter;

import androidx.annotation.IntDef;

/**
 * created by zhaoyuntao
 * on 24/08/2021
 * description:
 */
@IntDef({
        MessageCellCode.TYPE_CELL_DEBUG_LOG,
        MessageCellCode.TYPE_CELL_DEBUG_COMMAND,
        MessageCellCode.TYPE_CELL_ALL,
        MessageCellCode.TYPE_CELL_DELETED,
        MessageCellCode.TYPE_CELL_UNKNOWN,
        MessageCellCode.TYPE_CELL_TEXT,
        MessageCellCode.TYPE_CELL_TEXT_WEB,
        MessageCellCode.TYPE_CELL_IMAGE,
        MessageCellCode.TYPE_CELL_GROUP_EVENT,
        MessageCellCode.TYPE_CELL_RICH_MEDIA,
        MessageCellCode.TYPE_CELL_AUDIO,
        MessageCellCode.TYPE_CELL_CONTACT_CARD,
        MessageCellCode.TYPE_CELL_OFFICIAL_NAME_CARD,
        MessageCellCode.TYPE_CELL_FILE,
        MessageCellCode.TYPE_CELL_VIDEO,
        MessageCellCode.TYPE_CELL_OFFICIAL,
        MessageCellCode.TYPE_CELL_GROUP_CALL,
        MessageCellCode.TYPE_CELL_P2P_CALL,
        MessageCellCode.TYPE_CELL_LOCATION,
        MessageCellCode.TYPE_CELL_WEB_SHARE_CARD,
        MessageCellCode.TYPE_CELL_WEB_CLIP,
        MessageCellCode.TYPE_CELL_OFFICIAL_NOTIFY,
        MessageCellCode.TYPE_CELL_SERVER_MESSAGE,
})
public @interface MessageCellCode {
    //God type.
    int TYPE_CELL_ALL = 0;
    int TYPE_CELL_UNKNOWN = 1;
    int TYPE_CELL_DELETED = 2;
    int TYPE_CELL_DEBUG_LOG = 3;
    int TYPE_CELL_DEBUG_COMMAND = 4;

    //Basic type.
    int TYPE_CELL_TEXT = 10;
    int TYPE_CELL_IMAGE = 11;
    int TYPE_CELL_FILE = 12;
    int TYPE_CELL_VIDEO = 13;
    int TYPE_CELL_AUDIO = 14;
    int TYPE_CELL_CONTACT_CARD = 15;
    int TYPE_CELL_RICH_MEDIA = 16;
    int TYPE_CELL_LOCATION = 17;
    int TYPE_CELL_TEXT_WEB = 18;

    //Event.
    int TYPE_CELL_GROUP_EVENT = 100;
    int TYPE_CELL_SERVER_MESSAGE = 101;

    //Web share.
    int TYPE_CELL_WEB_SHARE_CARD = 200;
    int TYPE_CELL_WEB_CLIP = 201;

    //Voip.
    int TYPE_CELL_GROUP_CALL = 300;
    int TYPE_CELL_P2P_CALL = 301;

    //Official.
    int TYPE_CELL_OFFICIAL = 400;
    int TYPE_CELL_OFFICIAL_NAME_CARD = 401;
    int TYPE_CELL_OFFICIAL_NOTIFY = 402;

    //Payment
    int TYPE_CELL_PAYMENT_CASH_GIFT = 500;
    int TYPE_CELL_PAYMENT_TRANSFER = 501;
    int TYPE_CELL_PAYMENT_NOTIFY = 502;

    //Reply UI component.
    int TYPE_CELL_SPACIAL_REPLY_HEADER = 1000;

}
