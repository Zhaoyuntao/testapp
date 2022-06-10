package im.turbo.basetools.pattern;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import im.turbo.basetools.span.ItemProcessor;
import im.turbo.basetools.span.TClickableSpan;
import im.turbo.basetools.util.ValueSafeTransfer;
import im.turbo.callback.CommonDataCallback;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;

/**
 * created by zhaoyuntao
 * on 18/05/2022
 * description:
 */
public class PatternSpannableBuilder {
    private final List<ItemProcessor> patterns = new ArrayList<>(10);
    private final CharSequence charSequence;

    private PatternSpannableBuilder(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public static PatternSpannableBuilder newBuilder(CharSequence charSequence) {
        return new PatternSpannableBuilder(charSequence);
    }

    public PatternSpannableBuilder addPattern(ItemProcessor... processors) {
        patterns.addAll(Arrays.asList(processors));
        return this;
    }

    public void build(Context context, CommonDataCallback<CharSequence> callback) {
        ThreadPool.runIO(() -> {
            CharSequence charSequence = build();
            ThreadPool.runUi(new SafeRunnable(context) {
                @Override
                protected void runSafely() {
                    if (callback != null) {
                        callback.onSuccess(charSequence);
                    }
                }
            });
        });
    }

    public CharSequence build() {
        if (TextUtils.isEmpty(charSequence)) {
            return charSequence;
        }
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(this.charSequence);
        List<RangePair> rangePairs = new ArrayList<>();
        for (ItemProcessor processor : patterns) {
            replace(processor, stringBuilder, stringBuilder.length(), rangePairs);
        }
        Collections.sort(rangePairs);
        for (int i = rangePairs.size() - 1; i >= 0; i--) {
            RangePair rangePair = rangePairs.get(i);
//            S.s("final item:[" + rangePair.content + "] [pattern:" + rangePair.pattern + "]");
            int start = rangePair.start;
            int end = rangePair.end;
            String contentOrigin = rangePair.content;
            String contentReplace;
            ItemProcessor itemProcessor = rangePair.itemProcessor;
            CharSequence replaceContent = itemProcessor.onReplace(stringBuilder, start, end, contentOrigin);
            if (replaceContent != null) {
                contentReplace = replaceContent.toString();
                stringBuilder.replace(start, end, replaceContent);
                end = start + replaceContent.length();
            } else {
                contentReplace = contentOrigin;
            }
            Object clickableSpan = itemProcessor.generateSpan(contentOrigin, contentReplace);
            if (clickableSpan == null) {
                clickableSpan = new TClickableSpan(contentReplace, itemProcessor.getColor(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemProcessor.onClick(v, contentOrigin, contentReplace);
                    }
                }, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return itemProcessor.onLongClick(v, contentOrigin, contentReplace);
                    }
                }) {
                    @Override
                    public boolean canClick(@NonNull View view) {
                        return itemProcessor.canClick(view);
                    }
                };
            }
            stringBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return stringBuilder;
    }

    private void replace(ItemProcessor itemProcessor, CharSequence text, int maxLength, List<RangePair> rangePairs) {
        String patternString = itemProcessor.getPatternString();
        if (TextUtils.isEmpty(patternString) || TextUtils.isEmpty(text) || text.length() < 3 || text.length() > Math.max(maxLength, 1000)) {
            return;
        }
        List<TPatternGroup> groupBeans = PatternUtils.match(patternString, text, text.length());

        if (groupBeans != null) {
//            S.s("PatternSpannableBuilder.replace:[" + text + "][" + patternString + "][group count:" + groupBeans.size() + "]");
            for (TPatternGroup groupBean : groupBeans) {
//                S.s("group:[" + groupBean.getContent() + "] <---------------");
                groupBean.iterate(new ValueSafeTransfer.ElementIterator<TPatternItem>() {
                    @Override
                    public TPatternItem element(int position, TPatternItem itemBean) {
                        if (itemBean != null) {
//                        S.s("item:[" + itemBean.getContent() + "]");
                            int start = itemBean.getStart();
                            int end = itemBean.getEnd();
                            String childContent = itemBean.getContent();
                            RangePair rangePair = new RangePair();
                            rangePair.start = start;
                            rangePair.end = end;
                            for (RangePair rangePairLast : rangePairs) {
                                if (touch(rangePairLast, rangePair)) {
//                                S.e("touch last, dropped");
                                    return null;
                                }
                            }
                            rangePair.content = childContent;
                            rangePair.pattern = patternString;
                            rangePair.itemProcessor = itemProcessor;
                            rangePairs.add(rangePair);
                        }
                        return null;
                    }
                });
            }
//        } else {
//            S.e("PatternSpannableBuilder.replace:[" + text + "][" + patternString + "][group empty]");
        }
    }

    public Boolean touch(RangePair thisRange, RangePair rangePair) {
        return (rangePair.start <= thisRange.start && rangePair.end >= thisRange.end)
                || (rangePair.start >= thisRange.start && rangePair.start < thisRange.end)
                || (rangePair.end > thisRange.start && rangePair.end <= thisRange.end);
    }

    public static class RangePair implements Comparable<RangePair> {
        public int start;
        public int end;
        public String content;
        public ItemProcessor itemProcessor;
        public String pattern;

        @Override
        public int compareTo(RangePair o) {
            return Integer.compare(start, o.start);
        }
    }
}
