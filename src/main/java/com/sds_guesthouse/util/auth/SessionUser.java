package com.sds_guesthouse.util.auth;

import com.sds_guesthouse.model.entity.Guest;
import com.sds_guesthouse.model.entity.Host;

public class SessionUser {
    private Long userPk;
    private String userId;
    private String userType; // GUEST, HOST, ADMIN
    private String name;

    public static SessionUser fromGuest(Guest guest) {
        SessionUser user = new SessionUser();
        user.userPk = guest.getGuestId();
        user.userId = guest.getUserId();
        user.userType = "GUEST";
        user.name = guest.getName();
        return user;
    }

    public static SessionUser fromHost(Host host) {
        SessionUser user = new SessionUser();
        user.userPk = host.getHostId();
        user.userId = host.getUserId();
        user.userType = "HOST";
        user.name = host.getName();
        return user;
    }
}
