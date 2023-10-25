package com.test.test3app.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.graphics.drawable.IconCompat;

import com.test.test3app.R;
import com.test.test3app.notification.actions.BaseAction;
import com.test.test3app.notification.actions.ReadAction;
import com.test.test3app.notification.actions.ReplyAction;
import com.test.test3app.notification.constant.NotificationActionKeys;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import im.turbo.utils.ResourceUtils;

/**
 * created by zhaoyuntao
 * on 08/08/2023
 */
public class TNotificationHelper {

    public String ChannelId = "ChatChannelId";
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

    public void show(@NonNull TNotificationItem item) {
        Context context = ResourceUtils.getApplicationContext();
        if (manager == null) {
            manager = NotificationManagerCompat.from(context);
        }
        if (item.isCancel()) {
            manager.cancel(item.getTag(), 0);
        } else {
            manager.notify(item.getTag(), 0, findOrCreateNotification(context, item));
        }
        if (item.needGroupSummary() && notificationCache.size() > 0) {
            manager.notify(item.getGroupTag(), 0, createGroupSummary(context, item));
        } else {
            manager.cancel(item.getGroupTag(), 0);
        }
    }

    public int getMessageCount() {
        int count = 0;
        for (TNotificationHolder holder : notificationCache.values()) {
            count += holder.getMessageCount();
        }
        return count;
    }

    public void initNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "ChatNotificationChannel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Chat notification channel");
            ResourceUtils.getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    public Notification findOrCreateNotification(@NonNull Context context, @NonNull TNotificationItem item) {
        String personName = item.getSenderName();

        TNotificationHolder holder = notificationCache.get(item.getTag());
        if (holder == null) {
            Intent intent = new Intent(context, TNotificationActionReceiver.class);
            intent.setAction(NotificationActionKeys.ACTION_CLEAR);
            intent.putExtra(TNotificationActionReceiver.KEY_NOTIFY_TAG, item.getTag());
            PendingIntent actionIntent = PendingIntent.getBroadcast(context, 0, intent, getFlag());

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, item.getChannelId())
                    .setSmallIcon(R.drawable.anime_girl)
                    .setAutoCancel(false)
                    .setGroupSummary(false)
                    .setShowWhen(true)
                    .setDeleteIntent(actionIntent)
                    .setGroup(item.getGroupTag())
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            NotificationCompat.MessagingStyle messageStyle = new NotificationCompat.MessagingStyle("");
            messageStyle.setConversationTitle(item.getConversationTitle());
            messageStyle.setGroupConversation(false);
            builder.setStyle(messageStyle);
            notificationCache.put(item.getTag(), holder = new TNotificationHolder(builder, messageStyle));
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
                .setSmallIcon(R.drawable.b_app_logo_white)
                .setAutoCancel(true)
                .setNumber(2)
                .setColor(0xff11acfa)
                .setGroupSummary(false)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
//                .setStyle(messagingStyle)
//                .setContentIntent(contentIntent)
//                .extend(wearableExtender)
                .setSortKey(String.valueOf(Long.MAX_VALUE - System.currentTimeMillis()))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)

//                .setSmallIcon(IconCompat.createWithResource(context,R.drawable.anime_girl))
                .setTicker(item.getText())
                .addPerson(person)
                .clearActions();
        BaseAction<?>[] actions = item.getActions();
        if (actions != null && actions.length > 0) {
            for (BaseAction<?> action : actions) {
                Intent intent = new Intent(context, TNotificationActionReceiver.class);
                intent.setAction(action.getAction());
                intent.putExtra(TNotificationActionReceiver.KEY_NOTIFY_TAG, item.getTag());
                PendingIntent actionIntent = PendingIntent.getBroadcast(context, 0, intent, getFlag());
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

    private int getFlag() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : PendingIntent.FLAG_ONE_SHOT;
    }

    private Notification createGroupSummary(@NonNull Context context, @NonNull TNotificationItem item) {
        CharSequence summary = item.getSummary(notificationCache.size(), getMessageCount());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle().setSummaryText(summary);
        return new NotificationCompat.Builder(context, item.getChannelId())
                .setSmallIcon(item.getSummaryIcon())
                .setStyle(inboxStyle)
                .setGroup(item.getGroupTag())
                .setGroupSummary(true)
                .build();
    }

    public void notifyActions(String tag, @NonNull Intent intent) {
        for (TNotificationHolder holder : notificationCache.values()) {
            TNotificationItem item = holder.getItem();
            if (TextUtils.equals(tag, item.getTag())) {
                item.notifyActionCallback(intent);
                manager.cancel(tag, 0);
                break;
            }
        }
    }

    public void notifyClear(String tag, Intent intent) {
        TNotificationHolder holder = notificationCache.remove(tag);
        if (holder != null) {
            show(holder.getItem().setCancel(true));
        }
    }
}
