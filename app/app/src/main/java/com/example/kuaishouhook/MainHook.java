package com.example.kuaishouhook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    private static final String PK = "com.smile.gifmaker";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!lpparam.packageName.equals(PK)) return;
        XposedBridge.log("[Kuaishou] 模块已加载");

        // 解除下载限制
        try {
            XposedHelpers.findAndHookMethod("com.smile.gifmaker.util.CommonUtils",
                lpparam.classLoader, "isCanDownload", new XC_MethodHook() {
                    @Override protected void beforeHookedMethod(MethodHookParam p) {
                        p.setResult(true);
                    }
                });
        } catch (Throwable e) {}

        // 视频去水印
        try {
            XposedHelpers.findAndHookMethod("com.smile.gifmaker.model.FeedInfo",
                lpparam.classLoader, "getPlayUrl", new XC_MethodHook() {
                    @Override protected void afterHookedMethod(MethodHookParam p) {
                        if (p.getResult() != null) {
                            String u = p.getResult().toString();
                            u = u.replace("watermark=1","watermark=0")
                                 .replace("&watermark=1","").replace("watermark=1&","");
                            p.setResult(u);
                        }
                    }
                });
        } catch (Throwable e) {}

        // 图片去水印
        try {
            XposedHelpers.findAndHookMethod("com.smile.gifmaker.model.PhotoInfo",
                lpparam.classLoader, "getUrl", new XC_MethodHook() {
                    @Override protected void afterHookedMethod(MethodHookParam p) {
                        if (p.getResult() != null) {
                            String u = p.getResult().toString();
                            u = u.replace("watermark=1","watermark=0")
                                 .replace("&watermark=1","").replace("watermark=1&","");
                            p.setResult(u);
                        }
                    }
                });
        } catch (Throwable e) {}
    }
}

