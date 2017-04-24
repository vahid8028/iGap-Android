/*
* This is the source code of iGap for Android
* It is licensed under GNU AGPL v3.0
* You should have received a copy of the license in this archive (see LICENSE).
* Copyright © 2017 , iGap - www.iGap.net
* iGap Messenger | Free, Fast and Secure instant messaging application
* The idea of the RooyeKhat Media Company - www.RooyeKhat.co
* All rights reserved.
*/

package com.iGap.response;

import com.iGap.G;
import com.iGap.proto.ProtoError;
import com.iGap.proto.ProtoUserProfileGender;
import com.iGap.realm.RealmUserInfo;
import io.realm.Realm;

public class UserProfileSetGenderResponse extends MessageHandler {

    public int actionId;
    public Object message;
    public String identity;

    public UserProfileSetGenderResponse(int actionId, Object protoClass, String identity) {
        super(actionId, protoClass, identity);

        this.message = protoClass;
        this.actionId = actionId;
        this.identity = identity;
    }

    @Override
    public void handler() {
        super.handler();
        final ProtoUserProfileGender.UserProfileSetGenderResponse.Builder userProfileGenderResponse = (ProtoUserProfileGender.UserProfileSetGenderResponse.Builder) message;

        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmUserInfo userInfo = realm.where(RealmUserInfo.class).findFirst();
                if (userInfo != null) {
                    userInfo.setGender(userProfileGenderResponse.getGender());
                }
            }
        });

        realm.close();

        if (G.onUserProfileSetGenderResponse != null)
        G.onUserProfileSetGenderResponse.onUserProfileGenderResponse(userProfileGenderResponse.getGender(), userProfileGenderResponse.getResponse());
    }

    @Override
    public void error() {
        super.error();
        ProtoError.ErrorResponse.Builder errorResponse = (ProtoError.ErrorResponse.Builder) message;
        int majorCode = errorResponse.getMajorCode();
        int minorCode = errorResponse.getMinorCode();

        if (G.onUserProfileSetGenderResponse != null)
        G.onUserProfileSetGenderResponse.Error(majorCode, minorCode);

    }

    @Override
    public void timeOut() {
        super.timeOut();

        if (G.onUserProfileSetGenderResponse != null)
            G.onUserProfileSetGenderResponse.onTimeOut();

    }
}

