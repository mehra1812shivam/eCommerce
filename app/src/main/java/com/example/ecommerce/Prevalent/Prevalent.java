package com.example.ecommerce.Prevalent;

import com.example.ecommerce.Model.Users;

public class Prevalent {
    public static Users onlineusers;
    public static final String phonekey="UserPhone";
    public static final String passwordkey="UserPassword";

    public static Users getOnlineusers() {
        return onlineusers;
    }

    public static void setOnlineusers(Users onlineusers) {
        Prevalent.onlineusers = onlineusers;
    }
}
