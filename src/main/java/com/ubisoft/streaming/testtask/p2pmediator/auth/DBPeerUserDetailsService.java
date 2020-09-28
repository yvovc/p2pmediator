package com.ubisoft.streaming.testtask.p2pmediator.auth;

import com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.tables.records.PeerRecord;
import org.jooq.DSLContext;
import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.ubisoft.streaming.testtask.p2pmediator.p2pmediatordb.Tables.PEER;

/**
 * User details service that fetches peers data from DB and uses it during authentication.
 *
 * @author yvovc
 * @since 2020/26/09
 */
@Service
public class DBPeerUserDetailsService implements UserDetailsService {

    @Autowired
    private DSLContext dslContext;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new AccessDeniedException("Username cannot be blank");
        }
        final PeerRecord peerRecord = dslContext.selectFrom(PEER)
                .where(PEER.USERNAME.eq(username))
                .fetchOneInto(PeerRecord.class);
        if (peerRecord == null) {
            throw new AccessDeniedException(String.format("User '%s' wasn't found", username));
        }
        return new Peer(peerRecord.getId(),
                peerRecord.getUsername(), peerRecord.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_PEER"));
    }
}
