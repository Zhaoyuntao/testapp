package com.test.test3app.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;

import com.test.test3app.R;
import com.test.test3app.notification.actions.BaseAction;
import com.test.test3app.notification.actions.ReadAction;
import com.test.test3app.notification.actions.ReplyAction;
import com.test.test3app.notification.constant.NotificationActionKeys;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import im.turbo.utils.ResourceUtils;
import im.turbo.utils.log.S;

/**
 * created by zhaoyuntao
 * on 08/08/2023
 */
public class TNotificationHelper {

    public static final String CHANNEL_ID = "TChannelId";
    private final Map<Integer, TNotificationHolder> notificationCache = new ConcurrentHashMap<>();
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

    public void show(@NonNull TNotificationItem item) {
        Context context = ResourceUtils.getApplicationContext();
        if (manager == null) {
            manager = NotificationManagerCompat.from(context);
        }
        if (item.isCancel()) {
            manager.cancel(item.getNotifyId());
        } else {
            manager.notify(item.getNotifyId(), findOrCreateNotification(context, item));
        }
        if (item.needGroupSummary()) {
            int groupNotifyId = item.getNotifyIdGroup();
            manager.notify(groupNotifyId, createGroupSummary(context, item));
        }
    }

    public int getMessageCount() {
        int count = 0;
        S.llll();
        for (TNotificationHolder holder : notificationCache.values()) {
            S.s("holder:"+holder.getItem().getNotifyId()+" "+holder.getMessageCount());
            count += holder.getMessageCount();
        }
        return count;
    }

    public void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "ChatNotificationChannel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Chat notification channel");
            ResourceUtils.getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    public Notification findOrCreateNotification(@NonNull Context context, @NonNull TNotificationItem item) {
        String personName = item.getSenderName();

        TNotificationHolder holder = notificationCache.get(item.getNotifyId());
        if (holder == null) {
            Intent intent = new Intent(context, TNotificationActionReceiver.class);
            intent.setAction(NotificationActionKeys.ACTION_CLEAR);
            intent.putExtra(TNotificationActionReceiver.KEY_NOTIFY_ID, item.getNotifyId());
            PendingIntent actionIntent = PendingIntent.getBroadcast(context, item.getNotifyId(), intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.anime_girl)
                    .setAutoCancel(false)
                    .setGroupSummary(false)
                    .setShowWhen(true)
                    .setDeleteIntent(actionIntent)
                    .setGroup(item.getGroupKey())
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            NotificationCompat.MessagingStyle messageStyle = new NotificationCompat.MessagingStyle("");
            messageStyle.setConversationTitle(item.getConversationTitle());
            messageStyle.setGroupConversation(true);
            builder.setStyle(messageStyle);
            notificationCache.put(item.getNotifyId(), holder = new TNotificationHolder(builder, messageStyle));
        }
        holder.addItem(item);

        NotificationCompat.MessagingStyle messageStyle = holder.getMessageStyle();
        Person person = new Person.Builder()
                .setName(personName)
                .setIcon(item.getAvatar())
                .setKey(item.getSenderKey())
                .setImportant(true)
                .build();
        NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(item.getText(), item.getTime(), person);
        boolean newMessage = true;
        List<NotificationCompat.MessagingStyle.Message> messages = messageStyle.getMessages();
        if (!messages.isEmpty()) {
            for (int i = 0; i < messages.size(); i++) {
                NotificationCompat.MessagingStyle.Message oldMessage = messages.get(i);
                Bundle extras = oldMessage.getExtras();
                if (extras.containsKey("uuid")) {
                    String uuid = extras.getString("uuid");
                    if (TextUtils.equals(uuid, item.getUuid())) {
                        newMessage = false;
                        if (item.isRecall()) {
                            messages.remove(oldMessage);
                        } else {
                            if (item.getUri() != null) {
                                oldMessage.setData("image/*", item.getUri());
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (!item.isRecall() && newMessage) {
            message.setData("image/*", item.getUri());
            Bundle extras = message.getExtras();
            extras.putString("uuid", item.getUuid());
            messageStyle.addMessage(message);
        }
        NotificationCompat.Builder builder = holder.getBuilder()
                .setContentTitle(item.getConversationTitle())
                .setContentText(item.getText())
                .setWhen(item.getTime())
                .setTicker(item.getText())
                .clearActions();
        BaseAction<?>[] actions = item.getActions();
        if (actions != null && actions.length > 0) {
            for (BaseAction<?> action : actions) {
                Intent intent = new Intent(context, TNotificationActionReceiver.class);
                intent.setAction(action.getAction());
                intent.putExtra(TNotificationActionReceiver.KEY_NOTIFY_ID, item.getNotifyId());
                PendingIntent actionIntent = PendingIntent.getBroadcast(context, item.getNotifyId(), intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Action.Builder actionBuilder = new NotificationCompat.Action.Builder(action.getIcon(), action.getTitle(), actionIntent);
                if (action instanceof ReplyAction) {
                    ReplyAction replyAction = (ReplyAction) action;
                    actionBuilder.addRemoteInput(new RemoteInput.Builder(replyAction.getReplyKey()).setLabel(replyAction.getReplyHint()).build())
                            .setAllowGeneratedReplies(false)
                            .setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_REPLY);
                } else if (action instanceof ReadAction) {
                    actionBuilder.setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_MARK_AS_READ)
                            .setShowsUserInterface(false)
                            .build();
                } else {
                    continue;
                }
                builder.addAction(actionBuilder.build());
            }
        }

        return builder.build();
    }

    private Notification createGroupSummary(@NonNull Context context, @NonNull TNotificationItem item) {
        CharSequence summary = item.getSummary(notificationCache.size(), getMessageCount());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle().setSummaryText(summary);
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(item.getSummaryIcon())
                .setStyle(inboxStyle)
                .setGroup(item.getGroupKey())
                .setGroupSummary(true)
                .build();
    }

    public void notifyActions(int notifyId, @NonNull Intent intent) {
        for (TNotificationHolder holder : notificationCache.values()) {
            TNotificationItem item = holder.getItem();
            if (notifyId == item.getNotifyId()) {
                item.notifyActionCallback(intent);
                manager.cancel(notifyId);
                break;
            }
        }
    }

    public void notifyClear(int notifyId, Intent intent) {
        S.s("clear:" + notifyId);
        TNotificationHolder holder = notificationCache.remove(notifyId);
        if (holder != null) {
            show(holder.getItem().setCancel(true));
        }
    }
}
