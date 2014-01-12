/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.incallui;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.UserHandle;

/**
 * Top-level Application class for the InCall app.
 */
public class InCallApp extends Application {

    /**
     * Intent Action used for hanging up the current call from Notification bar. This will
     * choose first ringing call, first active call, or first background call (typically in
     * HOLDING state).
     */
    public static final String ACTION_HANG_UP_ONGOING_CALL =
            "com.android.incallui.ACTION_HANG_UP_ONGOING_CALL";
    public static final String ADD_CALL_MODE_KEY = "add_call_mode";
    public static final String ADD_PARTICIPANT_KEY = "add_participant";

    /**
     * Intent Action used for dismiss the current incoming call from Notification bar.
     */
    public static final String ACTION_DISMISS_ICOMING_CALL =
            "com.android.incallui.ACTION_DISMISS_ICOMING_CALL";

    /**
     * Intent Action used for answer the current incoming call from Notification bar.
     */
    public static final String ACTION_ANSWER_ICOMING_CALL =
            "com.android.incallui.ACTION_ANSWER_ICOMING_CALL";

    /**
     * Intent Action used for dismiss the current incoming call from Notification bar.
     */
    public static final String ACTION_DISMISS_ICOMING_CALL =
            "com.android.incallui.ACTION_DISMISS_ICOMING_CALL";

    /**
     * Intent Action used for answer the current incoming call from Notification bar.
     */
    public static final String ACTION_ANSWER_ICOMING_CALL =
            "com.android.incallui.ACTION_ANSWER_ICOMING_CALL";

    public InCallApp() {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Accepts broadcast Intents which will be prepared by {@link StatusBarNotifier} and thus
     * sent from framework's notification mechanism (which is outside Phone context).
     * This should be visible from outside, but shouldn't be in "exported" state.
     *
     */
    public static class NotificationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.i(this, "Broadcast from Notification: " + action);

            if (action.equals(ACTION_HANG_UP_ONGOING_CALL)) {
                // TODO: Commands of this nature should exist in the CallList or a
                //       CallController class that has access to CallCommandClient and
                //       CallList.
                InCallPresenter.getInstance().hangUpOngoingCall(context);
            } else if (action.equals(ACTION_DISMISS_ICOMING_CALL)) {
                CallCommandClient.getInstance().rejectCall(
                        CallList.getInstance().getIncomingCall(), false, null);
            } else if (action.equals(ACTION_ANSWER_ICOMING_CALL)) {
                closeSystemDialogs(context);
                CallCommandClient.getInstance().answerCall(
                        CallList.getInstance().getIncomingCall().getCallId());
                InCallPresenter.getInstance().startIncomingCallUi();
            }
        }
    }

    private static void closeSystemDialogs(Context context) {
        Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcastAsUser(intent, UserHandle.CURRENT_OR_SELF);
    }
}
