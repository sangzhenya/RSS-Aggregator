package com.abitalo.www.rss_aggregator.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by Lancelot on 2016/5/4.
 */
public class MeetUser extends BmobUser {

    //已包括
    // objectId,username,password,
    // mobilePhoneNumberVerified,mobilePhoneNumber,
    //emailVerified,email,
    //以及BmobObject的属性

    private String gender;
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}