package org.jboss.errai.demo.client.local;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;

public class SuperDevModeHelper {

    /** Configure devmode and try to compile and reload the app, if it fails do nothing. */
    public static void devModeOn() {
        String serverUrl = Window.Location.getParameter("sdm");
        if (serverUrl == null || serverUrl.isEmpty()) serverUrl = "http://localhost:9876/";
        devModeOn(serverUrl, GWT.getModuleName());

        Scheduler.get().scheduleFixedDelay(() -> {
            killOverlay(); return false;
        }, 1000);
    }

    private static native void devModeOn(String serverUrl, String moduleName)/*-{
        $wnd.__gwt_bookmarklet_params = {'server_url': serverUrl, 'module_name': moduleName};
        var s = $doc.createElement('script'); s.src = serverUrl + 'dev_mode_on.js';
        $doc.getElementsByTagName('head')[0].appendChild(s);
    }-*/;

    private static native void killOverlay() /*-{
        var body = $doc.getElementsByTagName('body')[0], childNodes = body.childNodes;
        for (var i = 0; i < childNodes.length; i++) {
            var el = childNodes[i];
            if (el.nodeType == 1 && el.style.zIndex == 1000000 && el.style.position == 'absolute') {
                body.removeChild(el);
            }
        }
    }-*/;

    /** Or you can just close and open the app in a new tab to discard sessionStorage and so disable devmode. */
    public static native void devModeOff() /*-{
        var toRemove = [];
        for (var i = 0; i < $wnd.sessionStorage.length; i++) {
            var key = $wnd.sessionStorage.key(i);
            if (key.indexOf('__gwtDevModeHook:') === 0) toRemove.push(key);
        }
        for (var j = 0; j < toRemove.length; j++) {
            $wnd.sessionStorage.removeItem(toRemove[j]);
        }
        $wnd.location.reload();
    }-*/;
}