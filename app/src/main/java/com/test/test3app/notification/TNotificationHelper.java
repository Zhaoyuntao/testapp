package com.test.test3app.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.content.LocusIdCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;

import com.test.test3app.R;
import com.test.test3app.notification.actions.BaseAction;
import com.test.test3app.notification.actions.ReadAction;
import com.test.test3app.notification.actions.ReplyAction;
import com.test.test3app.notification.constant.NotificationActionKeys;
import com.test.test3app.notification.content.NotificationClickEvent;
import com.test.test3app.notification.summary.NotificationSummary;
import com.test.test3app.observer.VLog;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import im.turbo.baseui.utils.UiUtils;
import im.turbo.utils.ResourceUtils;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 08/08/2023
 */
public class TNotificationHelper {

    private final Map<String, TNotificationHolder> notificationCache = new ConcurrentHashMap<>();
    private NotificationManagerCompat manager;
    private static volatile TNotificationHelper TNotificationHelper;

    private TNotificationHelper() {}

    public static TNotificationHelper getInstance() {
        if (TNotificationHelper == null) {
            synchronized (TNotificationHelper.class) {
                if (TNotificationHelper == null) {
                    TNotificationHelper = new TNotificationHelper();
                }
            }
        }
        return TNotificationHelper;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void post(@NonNull TNotificationItem item) {
        _post(item);
    }

    private void _post(@NonNull TNotificationItem item) {
        Context context = ResourceUtils.getContext();
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            VLog.e(LogTag.TAG_APPPLICATION, "post missing POST_NOTIFICATIONS");
//            return;
//        }
        if (manager == null) {
            manager = NotificationManagerCompat.from(context);
        }
        try {
            manager.notify(item.getTag(), 0, findOrCreateNotification(context, item));
            if (item.needGroupSummary() && notificationCache.size() > 0) {
                manager.notify(item.getGroupTag(), 0, createGroupSummary(context, item));
            } else {
                manager.cancel(item.getGroupTag(), 0);
            }
        } catch (Throwable e) {
            S.e(e);
            VLog.e(LogTag.TAG_APPPLICATION, e);
            item.onError(e);
        }
    }

    public void initNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "ChatNotificationChannel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Chat notification channel");
            ResourceUtils.getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    public void recall(@NonNull String tag, @NonNull String uuid) {
        Context context = ResourceUtils.getContext();
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            VLog.e(LogTag.TAG_APPPLICATION, "recall missing POST_NOTIFICATIONS");
//            return;
//        }
        TNotificationHolder holder = notificationCache.get(tag);
        if (holder != null) {
            NotificationCompat.MessagingStyle messageStyle = holder.getMessageStyle();
            List<NotificationCompat.MessagingStyle.Message> messages = messageStyle.getMessages();
            if (!messages.isEmpty()) {
                for (int i = 0; i < messages.size(); i++) {
                    NotificationCompat.MessagingStyle.Message oldMessage = messages.get(i);
                    Bundle extras = oldMessage.getExtras();
                    if (extras.containsKey("uuid")) {
                        String uuidCurrent = extras.getString("uuid");
                        if (TextUtils.equals(uuid, uuidCurrent)) {
                            messages.remove(oldMessage);
                            if (messages.isEmpty()) {
                                notifyClear(tag);
                            } else {
                                holder.removeItem(uuid);
                                TNotificationItem item = holder.getItem();
                                if (item != null) {
                                    _post(item);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean contains(@NonNull String tag, @NonNull String uuid) {
        TNotificationHolder holder = notificationCache.get(tag);
        if (holder != null) {
            NotificationCompat.MessagingStyle messageStyle = holder.getMessageStyle();
            List<NotificationCompat.MessagingStyle.Message> messages = messageStyle.getMessages();
            if (!messages.isEmpty()) {
                for (int i = 0; i < messages.size(); i++) {
                    NotificationCompat.MessagingStyle.Message oldMessage = messages.get(i);
                    Bundle extras = oldMessage.getExtras();
                    if (extras.containsKey("uuid")) {
                        String uuidCurrent = extras.getString("uuid");
                        if (TextUtils.equals(uuid, uuidCurrent)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Notification findOrCreateNotification(@NonNull Context context, @NonNull TNotificationItem item) {
        TNotificationHolder holder = notificationCache.get(item.getTag());
        if (holder == null) {
            Intent intent = new Intent(context, TNotificationActionReceiver.class);
            intent.setAction(NotificationActionKeys.ACTION_CLEAR);
            intent.putExtra(TNotificationActionReceiver.KEY_NOTIFY_TAG, item.getTag());
            PendingIntent actionIntent = PendingIntent.getBroadcast(context, item.getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            Integer iconRes = item.getIcon();
            Integer iconColor = item.getIconColor();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, item.getChannelId())
                    .setSmallIcon(iconRes == null ? R.drawable.b_svg_logo_notification : iconRes)
                    .setColor(iconColor == null ? 0x4FB1F3 : iconColor)
                    .setAutoCancel(true)
                    .setGroupSummary(false)
                    .setShowWhen(true)
                    .setDeleteIntent(actionIntent)
                    .setGroup(item.getGroupTag())
                    .setBubbleMetadata(createBubbleMetadata(context, item))
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);
            NotificationCompat.MessagingStyle messageStyle = new NotificationCompat.MessagingStyle("");
            messageStyle.setConversationTitle(item.getConversationTitle());
            builder.setStyle(messageStyle);
            notificationCache.put(item.getTag(), holder = new TNotificationHolder(item.getChannelId(), builder, messageStyle));
        }
        TNotificationItem lastItem = holder.getItem();
        boolean previewLast = lastItem == null || lastItem.isPreview();
        holder.addItem(item);
        NotificationCompat.Builder builder = holder.getBuilder().clearActions();
        if (item.isCancel()) {
            holder.clear();
            builder.setStyle(null).setBubbleMetadata(createBubbleMetadata(context, item));
            notificationCache.remove(item.getTag());
        } else {
            NotificationCompat.MessagingStyle messageStyle = holder.getMessageStyle();
            messageStyle.setGroupConversation(item.isGroupConversation());
            Person person = new Person.Builder()
                    .setName(item.getSenderName())
                    .setIcon(item.getAvatar())
                    .setKey(item.getSenderKey())
                    .setImportant(true)
                    .build();
            builder.setShortcutInfo(createShortcutInfo(context, person, item));
            NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(item.getText(), item.getTime(), person);
            List<NotificationCompat.MessagingStyle.Message> messages = messageStyle.getMessages();
            if (item.isPreview()) {
                if (!previewLast) {
                    messages.clear();
                }
                boolean newMessage = true;
                if (!messages.isEmpty()) {
                    for (int i = 0; i < messages.size(); i++) {
                        NotificationCompat.MessagingStyle.Message oldMessage = messages.get(i);
                        Bundle extras = oldMessage.getExtras();
                        if (extras.containsKey("uuid")) {
                            String uuid = extras.getString("uuid");
                            if (TextUtils.equals(uuid, item.getUuid())) {
                                newMessage = false;
                                if (item.getImageUri() != null) {
                                    oldMessage.setData("image/*", item.getImageUri());
                                }
                                break;
                            }
                        }
                    }
                }
                if (newMessage) {
                    if (item.getImageUri() != null) {
                        message.setData("image/*", item.getImageUri());
                    }
                    Bundle extras = message.getExtras();
                    extras.putString("uuid", item.getUuid());
                    extras.putString("tag", item.getTag());
                    messageStyle.addMessage(message);
                }
            } else {
                messages.clear();
                messageStyle.addMessage(item.getText(), item.getTime(), person);
            }
            BaseAction<?>[] actions = item.getActions();
            if (actions != null && actions.length > 0) {
                for (BaseAction<?> action : actions) {
                    if (!action.isEnable(item)) {
                        continue;
                    }
                    Intent intent = new Intent(context, TNotificationActionReceiver.class);
                    intent.setAction(action.getAction());
                    intent.putExtra(TNotificationActionReceiver.KEY_NOTIFY_TAG, item.getTag());
                    PendingIntent actionIntent = PendingIntent.getBroadcast(context, item.getUniqueId(), intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Action.Builder actionBuilder = new NotificationCompat.Action.Builder(action.getIcon(), action.getTitle(), actionIntent);
                    if (action instanceof ReplyAction) {
                        ReplyAction replyAction = (ReplyAction) action;
                        actionBuilder.addRemoteInput(new RemoteInput.Builder(replyAction.getReplyKey()).setLabel(replyAction.getReplyHint()).build())
                                .setAllowGeneratedReplies(false)
                                .setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_REPLY);
                    } else if (action instanceof ReadAction) {
                        actionBuilder.setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_MARK_AS_READ)
                                .setShowsUserInterface(false);
                    } else {
                        continue;
                    }
                    builder.addAction(actionBuilder.build());
                }
            }
            NotificationClickEvent clickEvent = item.getClickEvent();
            if (clickEvent != null) {
                Intent intent = new Intent();
                clickEvent.onSetIntent(item, intent);
                intent.setAction(context.getPackageName() + Math.random() + Integer.MAX_VALUE);
                builder.setContentIntent(PendingIntent.getActivity(context, item.getUniqueId(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));
            }
        }
        builder.setContentTitle(item.getConversationTitle())
                .setContentText(item.getText())
                .setWhen(item.getTime())
                .setSound(item.getSoundUri())
                .setVibrate(item.getVibration())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setTicker(item.getText())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setNumber(item.getCustomMessageCount() > 0 ? item.getCustomMessageCount() : holder.getMessageCount())
        ;
        return builder.build();
    }

    private ShortcutInfoCompat createShortcutInfo(@NonNull Context context, @NonNull Person person, @NonNull TNotificationItem item) {
        String label = TextUtils.isEmpty(item.getConversationTitle()) ? item.getSenderName() : item.getConversationTitle();
        ShortcutInfoCompat.Builder builder = new ShortcutInfoCompat.Builder(context, item.getSenderKey())
                .setShortLabel(label)
                .setLongLabel(label)
                .setLongLived(true)
                .setLocusId(new LocusIdCompat(item.getSenderKey()))
                .setPerson(person);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DEFAULT);
        builder.setIntent(intent);
        ShortcutInfoCompat shortcutInfo = builder.build();
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcutInfo);
        return shortcutInfo;
    }

    private NotificationCompat.BubbleMetadata createBubbleMetadata(@NonNull Context context, @NonNull TNotificationItem item) {
        Intent intent = new Intent();
        intent.setAction(context.getPackageName() + Math.random() + Integer.MAX_VALUE);
        NotificationClickEvent bubbleClickEvent = item.getBubbleClickEvent();
        if (bubbleClickEvent != null) {
            bubbleClickEvent.onSetIntent(item, intent);
        }
        return new NotificationCompat.BubbleMetadata.Builder(
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT), item.getBubbleAvatar())
                .setSuppressNotification(item.isCancel())
                .setAutoExpandBubble(false)
                .setDesiredHeight(UiUtils.dp(640)).build();
    }

    private Notification createGroupSummary(@NonNull Context context, @NonNull TNotificationItem item) {
        NotificationSummary summary = item.getSummary();
        CharSequence summaryContent = summary == null ? "" : summary.onGetSummary(item, notificationCache.size(), getMessageCount());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle().setSummaryText(summaryContent);
        Integer iconRes = item.getSummaryIcon();
        Integer iconColor = item.getSummaryIconColor();
        return new NotificationCompat.Builder(context, item.getChannelId())
                .setSmallIcon(iconRes == null ? R.drawable.b_svg_logo_notification : iconRes)
                .setColor(iconColor == null ? 0x4FB1F3 : iconColor)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setGroup(item.getGroupTag())
                .setGroupSummary(true)
                .build();
    }

    public int getMessageCount() {
        int count = 0;
        for (TNotificationHolder holder : notificationCache.values()) {
            TNotificationItem item = holder.getItem();
            if (item != null) {
                int messageCount = item.getCustomMessageCount();
                if (messageCount <= 0) {
                    messageCount = holder.getMessageCount();
                }
                count += messageCount;
            }
        }
        return count;
    }

    void notifyActions(String tag, @NonNull Intent intent) {
        for (TNotificationHolder holder : notificationCache.values()) {
            TNotificationItem item = holder.getItem();
            if (item != null) {
                if (TextUtils.equals(tag, item.getTag())) {
                    item.notifyActionCallback(intent);
                    post(item.setCancel(true));
                    break;
                }
            }
        }
    }

    public void notifyClear(String tag) {
        notificationCache.remove(tag);
        if (manager == null) {
            manager = NotificationManagerCompat.from(ResourceUtils.getContext());
        }
        manager.cancel(tag, 0);
    }

    public void clearAll() {
        notificationCache.clear();
        if (manager == null) {
            manager = NotificationManagerCompat.from(ResourceUtils.getContext());
        }
        manager.cancelAll();
    }

    public void notifyClearByChannelId(String channelId) {
        for (TNotificationHolder holder : notificationCache.values()) {
            if (holder != null && TextUtils.equals(channelId, holder.getChannelId())) {
                TNotificationItem item = holder.getItem();
                if (item != null) {
                    _post(item.setCancel(true));
                }
            }
        }
    }
}
