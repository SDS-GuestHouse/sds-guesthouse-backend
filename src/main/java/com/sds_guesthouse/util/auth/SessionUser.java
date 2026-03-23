package com.sds_guesthouse.util.auth;

import com.sds_guesthouse.model.entity.Guest;
import com.sds_guesthouse.model.entity.Host;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class SessionUser {
    private Long id;
    private String name;
    private String role; // ROLE_GUEST, ROLE_HOST

    public static SessionUser fromGuest(Guest guest) {
        return SessionUser.builder()
                .id(guest.getGuestId())
                .name(guest.getName())
                .role("ROLE_GUEST")
                .build();
    }

    public static SessionUser fromHost(Host host) {
        return SessionUser.builder()
                .id(host.getHostId())
                .name(host.getName())
                .role("ROLE_HOST")
                .build();
    }
}