package com.ubisoft.streaming.testtask.p2pmediator.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

/**
 * Class representing peer, used during auth and through app layers.
 *
 * @author yvovc
 * @since 2020/26/09
 */
public class Peer extends User {
    private UUID id;

    public Peer(final UUID id,
                final String username,
                final String password,
                final Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public Peer(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UUID getId() {
        return id;
    }
}
