package com.test.test3app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.test.test3app.R;

import base.ui.BaseActivity;
import im.turbo.basetools.pattern.LinkifyPort;
import im.turbo.basetools.pattern.PatternSpannableBuilder;
import im.turbo.basetools.span.ItemProcessor;
import im.turbo.utils.log.S;

public class Activity_99995_webURL extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_99995_weburl);
        TextView textView = findViewById(R.id.textview_web_url1);

        String[] textArray = {
                "a.b.c.d.com",
                "a.h.ht",
                "a.h.http",
                "a.-b.-.d.com",
                "www.baidu.com",
                "a.www.baidu.com",
                "aa.www.baidu.com",
                "1.www.baidu.com",
                "111.www.baidu.com",
                "111aaa.www.baidu.com",
                "a,www.baidu.com",
                "aa,www.baidu.com",
                "www.baidu.com你好",
                "你好www.baidu.comjsbdhj",
                "你好http://www.baidu.comjsbdhj",
                "你好https://www.baidu.comjsbdhj",
                "你好htt://www.baidu.comjsbdhj",
                "你好http:www.baidu.comjsbdhj",
                "你好http//www.baidu.comjsbdhj",
                "你好:www.baidu.com",
                "你好www.baidu.com jsbdhj",
                "你好www.baidu.com 你好",
                "你好www.baidu.com你好",
                "a.www.baidu.com",
                "the link is www.baidu.com/a=a,if you like",
                "the link iswww.baidu.com/a=a,if you like",
                "https://3im.totok.ai/shares/invites?id=3645a15c8b26c8200016ac078&sid=_____46jhJ0",
                "a.http://www.baidu.com",
                "a.https://www.baidu.com",
                "1.http://www.baidu.com",
                "a.http://www.a.baidu.com",
                "abchttp://www.a.baidu.com",
                "://www.a.baidu.com",
                "//www.a.baidu.com",
                "/www.a.baidu.com",
                "//www.a.baidu.com/a=(a) ",
                "//www.a.baidu.com/a=(a)/c=(b) ",
                "//www.a.baidu.com/a=你好,abc) sjkadnkjas",
                "a.http://www.google.com",
                "Dubai’s desert climate means it’s a sure bet for sultry weather all year round. See our month-by-mon https://article.botim.me/a/K2dFRtHrnSdK - Best time to visit Dubai",
                "你家啊说不定就会把时间的哈baidu.com",
                "https://a.baidu.a-ba.com:5000/sjbjbsa/a-asda/-asd-/-/ad?a=a-a",
                "1   5207E8C8-4D9D-43DD-88A9-A4748FFA8E8D\n" +
                        "[ Body :   Waiting for this mes... ]\n" +
                        "Conversation Uid:G80297175220659699 ][ Sender : U086218008250888269 ]\n" +
                        "[ Type : TEXT ][ Device : Ios ]\n" +
                        "[ Time : 1682919843145  2023-05-01 09:44:03:145 ]\n" +
                        "Error : PayloadEncryptor E2EE decryptGroup failed:\n" +
                        "[G80297175220659699][1]java.lang.RuntimeException: groupDecrypt fail\n" +
                        " at ai.bitcall.e2ee.RustKeyHelper.groupDecrypt(Native Method)\n" +
                        " at e2e.helper.E2EHelper._decryptGroup(E2EHelper.java:505)\n" +
                        " at e2e.helper.E2EHelper.decryptGroup(E2EHelper.java:487)\n" +
                        " at e2e.sdk.E2eeSdkImpl.decryptGroup(E2eeSdkImpl.java:224)\n" +
                        " at ae.majlis.messenger.app.startup.AppStartHelper$1.decryptGroup(AppStartHelper.java:257)\n" +
                        " at ae.majlis.messenger.push.data.PayloadEncryptHelper.decryptGroup(PayloadEncryptHelper.java:66)\n" +
                        " at ae.majlis.messenger.push.data.PushMessage.parsePayloadsBytes(PushMessage.java:203)\n" +
                        " at ae.majlis.messenger.push.data.PushPayloadBuilder.readOnPushMessageFromStream(PushPayloadBuilder.java:105)\n" +
                        " at ae.majlis.messenger.push.data.PushPayloadBundle.fromCipherPayloads(PushPayloadBundle.java:220)\n" +
                        " at ae.majlis.messenger.push.data.PushPayloadBundle.readFromStream(PushPayloadBundle.java:386)\n" +
                        " at ae.majlis.messenger.push.push.PushChannelThread.waitForIncomingMessage(PushChannelThread.java:1016)\n" +
                        " at ae.majlis.messenger.push.push.PushChannelThread.runPushLoop(PushChannelThread.java:443)\n" +
                        " at ae.majlis.messenger.push.push.PushChannelThread.runImpl(PushChannelThread.java:348)\n" +
                        " at ae.majlis.messenger.push.push.PushChannelThread.run(PushChannelThread.java:336)",
                "https://en.m.wikipedia.org/wiki/Jiuzhang_(quantum_computer) ",
                "== User Feedback ==\n" +
                        "\n" +
                        "Time: 2023-05-05 00:04:52\n" +
                        "Uid: 971119214908930983\n" +
                        "OS: android\n" +
                        "OSVer: 29\n" +
                        "AppVer: 1.8.8.414\n" +
                        "————————\n" +
                        "Category: Other\n" +
                        "SubCat: Find contacts\n" +
                        "Content: \n" +
                        "Hi there, once time I blocked this contact by mistake, and I  couldn't remove the block from this contact, would u mind if u help me to remove the blocked from this #+971528895069.\n" +
                        "Thanx :) \n" +
                        "\n" +
                        "Screenshots:\n" +
                        "https://totok-app.oss-me-east-1.aliyuncs.com/totok-report-logfiles/971119214908930983/1683230637258.jpg?Expires=1683835492&OSSAccessKeyId=LTAIe4EmdMyxiexR&Signature=INU2H9rSO6m90Ep1%2FHJfCwP2RDM%3D\n" +
                        "https://totok-app.oss-me-east-1.aliyuncs.com/totok-report-logfiles/971119214908930983/1683230637360.jpg?Expires=1683835492&OSSAccessKeyId=LTAIe4EmdMyxiexR&Signature=gOEARzCTo4FUuFK7cdVrgzcJc6w%3D\n" +
                        "https://totok-app.oss-me-east-1.aliyuncs.com/totok-report-logfiles/971119214908930983/1683230637388.jpg?Expires=1683835492&OSSAccessKeyId=LTAIe4EmdMyxiexR&Signature=sNZaLhM522HNbiXENj24Vq8WCT8%3D\n" +
                        "DebugLog:\n" +
                        "https://totok-app.oss-me-east-1.aliyuncs.com/totok-report-logfiles/971119214908930983/807111968202092991_6561.414.reportAll.2023-5-5_0-3-56.xlog.gz?Expires=1683835492&OSSAccessKeyId=LTAIe4EmdMyxiexR&Signature=IPzYrsnUvBmYLoP7QxMgRixBmcw%3D\n" +
                        "\n" +
                        "@Colin"
        };
        for (String text : textArray) {
            S.i(text);
            textView.append(PatternSpannableBuilder.newBuilder(text)
                    .addPattern(new ItemProcessor(LinkifyPort.WEB_URL_PATTERN) {
                        @Override
                        public Integer getColor() {
                            return Color.rgb(155, 155, 255);
                        }
                    }).build());
            textView.append("\n");
        }

//        textView.append("\n");
//        textView.append("\n");
//        textView.append(PatternSpannableBuilder.newBuilder("好")
//                .addPattern(new ItemProcessor(LinkifyPort.get()) {
//                    @Override
//                    public Integer getColor() {
//                        return Color.rgb(255, 80, 80);
//                    }
//                }).build());
//        textView.append("\n");
//        textView.append(PatternSpannableBuilder.newBuilder("0")
//                .addPattern(new ItemProcessor(LinkifyPort.get()) {
//                    @Override
//                    public Integer getColor() {
//                        return Color.rgb(255, 80, 80);
//                    }
//                }).build());
//        textView.append("\n");
//        textView.append(PatternSpannableBuilder.newBuilder("a")
//                .addPattern(new ItemProcessor(LinkifyPort.get()) {
//                    @Override
//                    public Integer getColor() {
//                        return Color.rgb(255, 80, 80);
//                    }
//                }).build());
//        textView.append("\n");
    }
}
