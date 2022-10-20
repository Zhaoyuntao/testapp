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
        MessageCellCode.TYPE_CELL_DELETED,
        MessageCellCode.TYPE_CELL_UNKNOWN,
        MessageCellCode.TYPE_CELL_TEXT,
        MessageCellCode.TYPE_CELL_TEXT_WEB,
        MessageCellCode.TYPE_CELL_IMAGE,
        MessageCellCode.TYPE_CELL_MULTI_MEDIA,
        MessageCellCode.TYPE_CELL_GROUP_EVENT,
        MessageCellCode.TYPE_CELL_RICH_MEDIA,
        MessageCellCode.TYPE_CELL_AUDIO,
        MessageCellCode.TYPE_CELL_CONTACT_CARD,
        MessageCellCode.TYPE_CELL_FILE,
        MessageCellCode.TYPE_CELL_VIDEO,
        MessageCellCode.TYPE_CELL_OFFICIAL,
        MessageCellCode.TYPE_CELL_GROUP_CALL,
        MessageCellCode.TYPE_CELL_P2P_CALL,
        MessageCellCode.TYPE_CELL_LOCATION,
        MessageCellCode.TYPE_CELL_WEB_SHARE_CARD_IMAGE_LEFT,
        MessageCellCode.TYPE_CELL_WEB_SHARE_CARD_IMAGE_RIGHT,
        MessageCellCode.TYPE_CELL_WEB_SHARE_CARD_IMAGE_LARGE,
        MessageCellCode.TYPE_CELL_WEB_SHARE_CARD_AUDIO,
        MessageCellCode.TYPE_CELL_WEB_SHARE_CARD_VIDEO,
        MessageCellCode.TYPE_CELL_WEB_CLIP,
        MessageCellCode.TYPE_CELL_OFFICIAL_NOTIFY,
        MessageCellCode.TYPE_CELL_OFFICIAL_NOTIFY_OLD,
        MessageCellCode.TYPE_CELL_SERVER_MESSAGE,
        MessageCellCode.TYPE_CELL_RICH_MEDIAS,
        MessageCellCode.TYPE_CELL_PAYMENT_GROUP_CASH_GIFT,
        MessageCellCode.TYPE_CELL_PAYMENT_MESSAGE,
        MessageCellCode.TYPE_CELL_PAYMENT_CASH_NOTIFY,
        MessageCellCode.TYPE_CELL_PAYMENT_TRANSFER,
        MessageCellCode.TYPE_CELL_STEPS_LIKE,
        MessageCellCode.TYPE_CELL_STEPS_RANKING,
        MessageCellCode.TYPE_CELL_MICROPROGRAM_SHARE_CARD,
        MessageCellCode.TYPE_CELL_ROBOT_TEMPLATE_CARD,
        MessageCellCode.TYPE_CELL_ROBOT_GREET_CARD,
})
public @interface MessageCellCode {
    //God type.
    int TYPE_CELL_UNKNOWN = 1;
    int TYPE_CELL_DELETED = 2;
    int TYPE_CELL_DEBUG_LOG = 3;
    int TYPE_CELL_DEBUG_COMMAND = 4;

    //Basic type.
    int TYPE_CELL_TEXT = 10;
    int TYPE_CELL_IMAGE = 11;
    int TYPE_CELL_VIDEO = 12;
    int TYPE_CELL_MULTI_MEDIA = 13;
    int TYPE_CELL_FILE = 14;
    int TYPE_CELL_AUDIO = 15;
    int TYPE_CELL_CONTACT_CARD = 16;
    int TYPE_CELL_RICH_MEDIA = 17;
    int TYPE_CELL_RICH_MEDIAS = 18;
    int TYPE_CELL_LOCATION = 19;
    int TYPE_CELL_TEXT_WEB = 20;

    //Event.
    int TYPE_CELL_GROUP_EVENT = 100;
    int TYPE_CELL_SERVER_MESSAGE = 101;

    //Web share.
    int TYPE_CELL_WEB_CLIP = 200;
    int TYPE_CELL_WEB_SHARE_CARD_IMAGE_LEFT = 201;
    int TYPE_CELL_WEB_SHARE_CARD_IMAGE_RIGHT = 202;
    int TYPE_CELL_WEB_SHARE_CARD_IMAGE_LARGE = 203;
    int TYPE_CELL_WEB_SHARE_CARD_AUDIO = 204;
    int TYPE_CELL_WEB_SHARE_CARD_VIDEO = 205;

    //Voip.
    int TYPE_CELL_GROUP_CALL = 300;
    int TYPE_CELL_P2P_CALL = 301;

    //Official.
    int TYPE_CELL_OFFICIAL = 400;
    int TYPE_CELL_OFFICIAL_NOTIFY_OLD = 401;
    int TYPE_CELL_OFFICIAL_NOTIFY = 402;
    int TYPE_CELL_STEPS_LIKE = 403;
    int TYPE_CELL_STEPS_RANKING = 404;

    //Payment
    int TYPE_CELL_PAYMENT_MESSAGE = 500;
    int TYPE_CELL_PAYMENT_GROUP_CASH_GIFT = 501;
    int TYPE_CELL_PAYMENT_CASH_NOTIFY = 502;
    int TYPE_CELL_PAYMENT_TRANSFER = 503;

    //Micro program
    int TYPE_CELL_MICROPROGRAM_SHARE_CARD = 602;

    //Robot
    int TYPE_CELL_ROBOT_TEMPLATE_CARD = 700;
    int TYPE_CELL_ROBOT_GREET_CARD = 701;
}
