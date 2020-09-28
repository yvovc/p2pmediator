# P2PMediator
P2PMediator - server-side application that performs coordination and service discovery in a hybrid P2P game streaming network.
General terms:
- Streaming session - P2P game streaming session initiated be some streaming peer.
- View request - viewer peer's request to consume the streaming session.
- Peer - agent of the system that comunicates with the P2PMediator.
- Peer role: Viewer and Streamer.
- Video Game - streamable object that is linked to Streaming session.
- Streaming session endpoint - IP address and port of the Streamer agent (peer).
- View request endpoint - IP address and port of the Viewer agent (peer).
- Streaming session status:
NEW - newly created, unconfigured and inactive streaming session
READY - ready to accept view requests
ACTIVE - streaming is in progress
FINISHED - streamer peer has finished streaming session.
- View request status:
NEW - newly created, unconfigured and unprocessed view request
READY - request ready to be processed
PROCESSED - request processed by streaming peer

1) Configuration
- DatabaseMigrationConfig - configuration component that migrates database to actual version using Flyway in runtime.
- DataServicesConfiguration - configuration component that initializes data services implementations according to the provided env configuration.
- DataSourceConfiguration - configuration component to initialize application with Embedded Postgres data source, configurable using env vars.
- SecurityConfiguration - authorization and authentication configuration.

2) Architecture/Design
Standard Spring Boot service with multi-layered design:
controller -> service(BL) -> DAL.
Controllers:
- StreamingSessionController (exposes streaming sessions related API to peers)
- ViewRequestController (exposing view requests related API to peers)

Services (core business logic components, validation rules):
- GameService (games related ops)
- StreamingSessionService
- ViewRequestService
- StreamingSessionFieldAccessValidator
- ViewRequestFieldAccessValidator
- DBPeerUserDetailsService (used during auth process to fetch user details for further access control and validation)
All service components rely on abstract DAL interfaces and secured from changes in implementations.

DAL:
Abstract interfaces were introduced in order to support different data sources:
- IStreamingSessionDataService
- IViewRequestDataService

Currently application supports only one type of data source - JOOQ-powered DAL components backed by SQL DB (Embedded PostgreSQL).
To initialize different implementations of DAL components factory components are used that accept config values to initialize concrete data services.
To communicate with DB application uses JOOQ - lightweight ORM framework.


3) Security
Peers need to pass authentication to the server in order to be identified as legal system agent and stream/view content and get another peers locations. Currently BASIC auth is used that should be performed via HTTPS connection on production envs.


4) **Peers and server interaction description** (example with 2 peer - viewer and streamer - communicating with the server and with each other):
Let's call peers just Streamer and Viewer


1. Every peer performs standard initialization protocol (obtaining public IP address and port from STUN server, then authenticating to the P2PMediator using username and password).
2. Streamer pushes to the server streaming session containing information about desired game to stream (streaming session's status - NEW).
3. Streamer pushes to the server it's endpoint details (public host:port), after that streaming session moves to READY state which means it's eligible to be visible in the viewer list.
4. After successful streaming session creation Streamer peer "asks" server about available view requests every 5 secs.
4. Viewer lists all the sessions that could be viewed.
5. Viewer selects streaming session by creating NEW view request.
6. Viewer pushes it's public host:port pair and it's linked with view request.
7. Viewer fetches Streamer's host:port pair and performs initial direct request to pass Viewer's stateful firewall.
8. Viewer notify server about that by updating his/her view request status to READY.
9. Streamer that fetches new READY view requests every 5 secs gets the our Viewer's one.
10. Streamer notifies the server that Streamer is processing the view request by updating view request status to PROCESSED.
11. In parallel, Streamer starts streaming data to the viewer location successfuly passing Viewer's firewall.
12. After streaming session finished Streamer notifies server about that.
13. Done.
