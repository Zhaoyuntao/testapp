package com.test.test3app.mention;

import static com.test.test3app.mention.MessageMentionHelper.MENTION;

import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.test.test3app.R;
import im.turbo.basetools.utils.InputMethodUtils;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * created by zhaoyuntao
 * on 2020/7/15
 * description:
 */
public class TTextWatcher implements TextWatcher, ChatEditText.OnSelectionChangeListener {
    private int countAfterChange;
    private int countBeforeChange;
    private int positionChange;
    private int positionOfMentionSymbol = -1;
    private final List<MentionClickableSpan> mentions = new ArrayList<>();
    private final ChatEditText chatEditText;
    private MentionListener mentionListener;
    private final Pattern emailTailPattern;
    private final int color;

    public TTextWatcher(ChatEditText chatEditText) {
        this.chatEditText = chatEditText;
        this.emailTailPattern = Pattern.compile("[0-9a-zA-Z]");
        this.color = ContextCompat.getColor(chatEditText.getContext(), R.color.color_primary);
    }

    @NonNull
    public TMention popMention() {
        Editable editable = chatEditText.getText();
        List<String> mentionUidList = new ArrayList<>(mentions.size());
        Collections.sort(mentions, new Comparator<MentionClickableSpan>() {
            @Override
            public int compare(MentionClickableSpan o1, MentionClickableSpan o2) {
                return Integer.compare(o1.getStart(), o2.getStart());
            }
        });
        if (editable != null) {
            for (MentionClickableSpan mention : mentions) {
                int spanStart = editable.getSpanStart(mention);
                int spanEnd = editable.getSpanEnd(mention);
                String uid = mention.getUid();
                editable.replace(spanStart, spanEnd, getMentionUID(uid));
                mentionUidList.add(uid);
            }
        }
        chatEditText.setText("");
        mentions.clear();
        return new TMention(editable, mentionUidList);
    }

    public void initDraft(String draftContent) {
        if (draftContent == null) {
            return;
        }
        chatEditText.post(new Runnable() {
            @Override
            public void run() {
                chatEditText.removeOnSelectionChangeListener();
                chatEditText.removeTextChangedListener(TTextWatcher.this);

                chatEditText.setText(draftContent);
                chatEditText.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodUtils.forceOpenInputKeyboard(chatEditText);
                        chatEditText.requestFocus();
//                        cursorPosition = draftContent.length();
//                        setSelection(false);
                    }
                });

                chatEditText.addTextChangedListener(TTextWatcher.this);
                chatEditText.setOnSelectionChangeListener(TTextWatcher.this);
            }
        });
    }

    public void setMentionListener(MentionListener mentionListener) {
        this.mentionListener = mentionListener;
        chatEditText.addTextChangedListener(this);
        chatEditText.setOnSelectionChangeListener(this);
    }

    public void addMention(String uid, String name) {
        ////S.s("insertReference at:" + referenceSymbolPosition);
        //Remove listener.
        chatEditText.removeOnSelectionChangeListener();
        chatEditText.removeTextChangedListener(this);
        //Change content.
        insertMention(name, uid);
        //Re add listener.
        chatEditText.addTextChangedListener(this);
        chatEditText.setOnSelectionChangeListener(this);
    }

    private void insertMention(CharSequence name, String uid) {
        Editable editable = chatEditText.getText();
        if (editable == null) {
            return;
        }
        String mentionContent = getMentionContent(name);
        int startPosition = getMentionSymbolPosition();
        int endPosition = chatEditText.getSelectionEnd();

        MentionSpannableString spannableString = new MentionSpannableString(mentionContent + " ");
        MentionClickableSpan clickableSpan = new MentionClickableSpan(mentionContent, color, uid, spannableString, null, null);
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editable.replace(startPosition, endPosition, spannableString);

        mentions.add(clickableSpan);
        positionOfMentionSymbol = -1;
    }

    /**
     * When delete a char, find if it is in a reference range.
     *
     * @param editable
     * @return
     */
    private int deleteMention(Editable editable) {
        MentionClickableSpan[] s = editable.getSpans(0, editable.length(), MentionClickableSpan.class);
        for (MentionClickableSpan ss : s) {
            int start = editable.getSpanStart(ss);
            int end = editable.getSpanEnd(ss);
            S.s("[" + ss + "] start:" + start + "  end:" + end + " origin length:" + ss.length() + " now:" + (end - start));
            if ((end - start) < ss.length()) {
                editable.delete(start, end);
                mentions.remove(ss);
            }
        }
        return 0;
    }

    private int[] resizeSelection(int startPosition, int endPosition) {
        Editable editable = chatEditText.getText();
        if (editable != null) {
            MentionClickableSpan[] mentionClickableSpans = editable.getSpans(startPosition, endPosition, MentionClickableSpan.class);
            for (MentionClickableSpan span : mentionClickableSpans) {
                int spanStart = editable.getSpanStart(span);
                int spanEnd = editable.getSpanEnd(span);
                float middle = (spanEnd - spanStart) / 2f;
                if (startPosition > spanStart && startPosition < spanEnd) {
                    S.s("start is in mention");
                    startPosition = startPosition <= middle ? spanStart : spanEnd;
                }
                if (endPosition > spanStart && endPosition < spanEnd) {
                    S.s("end is in mention");
                    endPosition = endPosition < middle ? spanStart : spanEnd;
                }
            }
        }
        return new int[]{startPosition, endPosition};
    }

    private boolean isInMention(int startPosition, int endPosition) {
        Editable editable = chatEditText.getText();
        if (editable != null) {
            MentionClickableSpan[] mentionClickableSpans = editable.getSpans(startPosition, endPosition, MentionClickableSpan.class);
            for (MentionClickableSpan span : mentionClickableSpans) {
                int spanStart = editable.getSpanStart(span);
                int spanEnd = editable.getSpanEnd(span);
                if (startPosition > spanStart && startPosition < spanEnd) {
                    return true;
                }
                if (endPosition > spanStart && endPosition < spanEnd) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getMentionContent(CharSequence name) {
        return MENTION + name;
    }

    private String getMentionUID(String uid) {
        return MessageMentionHelper.getMentionUidContent(uid);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int positionChange, int countBeforeChange, int countAfterChange) {
        this.countBeforeChange = countBeforeChange;
        this.positionChange = positionChange;
        //S.s("beforeTextChanged:[" + charSequence + "] positionChange:" + positionChange + " countBeforeChange:" + countBeforeChange + " countAfterChange:" + countAfterChange);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int positionChange, int countBeforeChange, int countAfterChange) {
        this.countAfterChange = countAfterChange;
        this.positionChange = positionChange;
        //S.s("onTextChanged:[" + charSequence + "] positionChange:" + positionChange + " countBeforeChange:" + countBeforeChange + " countAfterChange:" + countAfterChange);
    }

    @Override
    public void afterTextChanged(Editable editable) {
//        try {
        processEditable(editable);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //S.e("afterTextChanged.e:" + e.getMessage());
//        }
    }

    private void processEditable(Editable editable) {
        //S.s("afterTextChanged processEditable:[" + editable + "]");
        MentionListener listener = this.mentionListener;
        if (listener == null) {
            return;
        }

        if (chatEditText == null) {
            return;
        }
        chatEditText.removeOnSelectionChangeListener();
        chatEditText.removeTextChangedListener(this);

        //After add char.
        int countChange = countAfterChange - countBeforeChange;
        //S.s("countChange:" + countChange + "  countAfterChange:" + countAfterChange + " countBeforeChange:" + countBeforeChange);

        boolean hasMentionSymbol;
        if (editable.length() == 0) {
            //S.s("empty");
//            setSelection(false);
            positionOfMentionSymbol = -1;
            listener.setKeepSearch(false);
            listener.onStopMentionPeople();
        } else {
            S.s("countChange:" + countChange);
            if (countChange < 0) {
                int replaceLength = deleteMention(editable);
                //Delete a reference.
                if (Math.abs(replaceLength) > 0) {
                    countChange = replaceLength;
                    positionChange = positionChange + countChange + 1;
                    countAfterChange = 0;
                }
            } else {
                //S.s("is add");
            }

            boolean previousCharIsReferenceSymbol = false;
            boolean previousCharIsSpace = false;
            //If the char before cursor is '@'.
            int cursorPosition = chatEditText.getSelectionEnd();
            if (editable.length() > 0 && cursorPosition > 0) {
                previousCharIsReferenceSymbol = editable.charAt(cursorPosition - 1) == '@';
                if (!previousCharIsReferenceSymbol) {
                    previousCharIsSpace = editable.charAt(cursorPosition - 1) == ' ';
                }
            }
            if (previousCharIsReferenceSymbol) {
                hasMentionSymbol = true;
                listener.setKeepSearch(true);
                positionOfMentionSymbol = cursorPosition - 1;
            } else if (previousCharIsSpace) {
                hasMentionSymbol = false;
                listener.setKeepSearch(false);
            } else {
                int positionReferenceBefore = getMentionSymbolPosition();
                hasMentionSymbol = positionReferenceBefore != -1;
            }

            //Email detect.
            if (hasMentionSymbol && positionOfMentionSymbol > 0) {
                hasMentionSymbol = !emailTailPattern.matcher(String.valueOf(editable.charAt(positionOfMentionSymbol - 1))).matches();
            }

            //If find a @ symbol.
            if (hasMentionSymbol) {
                if (listener.isKeepSearch()) {
                    listener.onStartMentionPeople();
                }
            } else {
                positionOfMentionSymbol = -1;
                listener.onStopMentionPeople();
            }
            CharSequence contentAfterReferenceSymbol;
            //Get content after @ symbol.
            if (hasMentionSymbol) {
                //If @ symbol is in first position.
                int start = getMentionSymbolPosition();
                int end = positionChange + countAfterChange;

                if (start < end) {
                    contentAfterReferenceSymbol = editable.subSequence(start + 1, end);
                } else {
                    contentAfterReferenceSymbol = "";
                }
                listener.onSearchPeople(contentAfterReferenceSymbol.toString());
            }

            chatEditText.requestFocus();
        }

        chatEditText.addTextChangedListener(this);
        chatEditText.setOnSelectionChangeListener(this);
    }

    @Override
    public void onSelectionChanged(int start, int end) {
        Editable editable = chatEditText.getText();
        if (editable == null || TextUtils.isEmpty(editable)) {
            return;
        }
        int[] rangeResize = resizeSelection(start, end);
        chatEditText.removeOnSelectionChangeListener();
        chatEditText.setSelection(rangeResize[0], rangeResize[1]);
        chatEditText.setOnSelectionChangeListener(this);
    }

    @NonNull
    private String getText() {
        Editable editable = chatEditText.getText();
        return editable == null ? "" : editable.toString();
    }

    private int getMentionSymbolPosition() {
        if (alreadyHasAReferenceSymbol()) {
            return this.positionOfMentionSymbol;
        } else {
            positionOfMentionSymbol = -1;
            return -1;
        }
    }

    /**
     * If '@' symbol is already exists in the view.
     *
     * @return
     */
    private boolean alreadyHasAReferenceSymbol() {
        Editable editable = chatEditText.getText();
        return editable != null
                && editable.length() > 0
                && this.positionOfMentionSymbol >= 0
                && positionOfMentionSymbol < editable.length()
                && editable.charAt(positionOfMentionSymbol) == '@'
                && !isInMention(positionOfMentionSymbol, positionOfMentionSymbol);
    }
}
