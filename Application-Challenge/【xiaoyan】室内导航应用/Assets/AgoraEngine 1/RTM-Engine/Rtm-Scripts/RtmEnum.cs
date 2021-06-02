using System;

namespace agora_rtm {

    public enum COMMON_ERR_CODE {
          ERROR_NULL_PTR = -7
    }

    public enum INIT_ERR_CODE {
          
          /**
           0: Initialization succeeds.
           */
          INIT_ERR_OK = 0,
          
          /**
           1: A common failure occurs during initialization.
           */
          INIT_ERR_FAILURE = 1,
          
          /**
           2: The SDK is already initialized.
           */
          INIT_ERR_ALREADY_INITIALIZED = 2,
          
          /**
           3: The App ID is invalid.
           */
          INIT_ERR_INVALID_APP_ID = 3,
          
          /**
           4: The event handler is empty.
           */
          INIT_ERR_INVALID_ARGUMENT = 4,
    };
     
      
    /**
     @brief Error codes related to login.
     */
    public enum LOGIN_ERR_CODE {
      
      /**
       0: The method call succeeds, or login succeeds. 
       */
      LOGIN_ERR_OK = 0,
        
      /**
       1: Login fails. The reason is unknown.
       */
      LOGIN_ERR_UNKNOWN = 1,
      
      /**
       2: Login is rejected by the server.
       */
      LOGIN_ERR_REJECTED = 2, // Occurs when not initialized or server reject
        
      /**
       3: Invalid login argument.
       */
      LOGIN_ERR_INVALID_ARGUMENT = 3,
        
      /**
       4: The App ID is invalid.
       */
      LOGIN_ERR_INVALID_APP_ID = 4,
        
      /**
       5: The token is invalid.
       */
      LOGIN_ERR_INVALID_TOKEN = 5,
       
      /**
       6: The token has expired, and hence login is rejected.
       */
      LOGIN_ERR_TOKEN_EXPIRED = 6,
       
      /**
       7: Unauthorized login.
       */
      LOGIN_ERR_NOT_AUTHORIZED = 7,
       
      /**
       8: The user has already logged in or is logging in the Agora RTM system, or the user has not called the \ref agora::rtm::IRtmService::logout "logout" method to leave the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" state.
       */
      LOGIN_ERR_ALREADY_LOGGED_IN = 8,
       
      /**
       9: The login times out. The current timeout is set as six seconds. You need to log in again.
       */
      LOGIN_ERR_TIMEOUT = 9,
       
      /**
       10: The call frequency of the \ref agora::rtm::IRtmService::login "login" method exceeds the limit of two queries per second.
       */
      LOGIN_ERR_TOO_OFTEN = 10,
        
      /**
       101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
      */
      LOGIN_ERR_NOT_INITIALIZED = 101,
    };
      
    /**
     @brief Error codes related to logout.
     */
    public enum LOGOUT_ERR_CODE {
       
      /**
       0: The method call succeeds, or logout succeeds.
       */
      LOGOUT_ERR_OK = 0,
      
      /**
       1: **RESERVED FOR FUTURE USE**
       */
      LOGOUT_ERR_REJECTED = 1,
        
      /**
       101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
       */
      LOGOUT_ERR_NOT_INITIALIZED = 101,
        
      /**
       102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before the user logs out of the Agora RTM system.
       */
      LOGOUT_ERR_USER_NOT_LOGGED_IN = 102,
    };

    /**
     @brief Error codes related to renewing the RTM Token.
     */
    public enum RENEW_TOKEN_ERR_CODE {
        
      /**
       0: The method call succeeds, or the renewing operation succeeds. 
       */
      RENEW_TOKEN_ERR_OK = 0,
        
      /**
       1: Common failure. The user fails to renew the token.
       */
      RENEW_TOKEN_ERR_FAILURE = 1,
 
      /**
       2: The method call fails. The argument is invalid. 
       */
      RENEW_TOKEN_ERR_INVALID_ARGUMENT = 2,
        
      /**
       3: **RESERVED FOR FUTURE USE**
       */
      RENEW_TOKEN_ERR_REJECTED = 3,
 
      /**
       4: The method call frequency exceeds the limit of two queries per second.
       */
      RENEW_TOKEN_ERR_TOO_OFTEN = 4,
  
      /**
       5: The token has expired.
       */
      RENEW_TOKEN_ERR_TOKEN_EXPIRED = 5,
  
      /**
       6: The token is invalid.
       */
      RENEW_TOKEN_ERR_INVALID_TOKEN = 6,
         
      /**
       101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
       */
      RENEW_TOKEN_ERR_NOT_INITIALIZED = 101,
        
      /**
       102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before renewing the token.
       */
      RENEW_TOKEN_ERR_USER_NOT_LOGGED_IN = 102,
    };
 
    /**
     @brief Connection states between the SDK and the Agora RTM system.
     */
    public enum CONNECTION_STATE {
        
      /**
       1: The initial state. The SDK is disconnected from the Agora RTM system.
       
       When the user calls the \ref agora::rtm::IRtmService::login "login" method, the SDK starts to log in the Agora RTM system, triggers the \ref agora::rtm::IRtmServiceEventHandler::onConnectionStateChanged "onConnectionStateChanged" callback, and switches to the \ref agora::rtm::CONNECTION_STATE_CONNECTING "CONNECTION_STATE_CONNECTING" state.
       */
      CONNECTION_STATE_DISCONNECTED = 1,
        
      /**
       2: The SDK is logging in the Agora RTM system.

       - If the user successfully logs in the Agora RTM system and receives the \ref agora::rtm::IRtmServiceEventHandler::onLoginSuccess "onLoginSuccess" callback, the SDK triggers the \ref agora::rtm::IRtmServiceEventHandler::onConnectionStateChanged "onConnectionStateChanged" callback and switches to the \ref agora::rtm::CONNECTION_STATE_CONNECTED "CONNECTION_STATE_CONNECTED" state.
       - If the user fails to login the Agora RTM system and receives the \ref agora::rtm::IRtmServiceEventHandler::onLoginFailure "onLoginFailure" callback, the SDK triggers the \ref agora::rtm::IRtmServiceEventHandler::onConnectionStateChanged "onConnectionStateChanged" callback and switches to the \ref agora::rtm::CONNECTION_STATE_DISCONNECTED "CONNECTION_STATE_DISCONNECTED" state.
       */
      CONNECTION_STATE_CONNECTING = 2,
        
      /**
       3: The SDK has logged in the Agora RTM system.

       - If the connection state between the SDK and the Agora RTM system is interrupted because of network issues, the SDK triggers the \ref agora::rtm::IRtmServiceEventHandler::onConnectionStateChanged "onConnectionStateChanged" callback and switches to the \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state.
       - If the login is banned by the server because, for example, another instance logs in the Agora RTM system with the same user ID, the \ref agora::rtm::IRtmServiceEventHandler::onConnectionStateChanged "onConnectionStateChanged" callback is triggered, and the SDK is switched to the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" state.
       - If the user calls the \ref agora::rtm::IRtmService::logout "logout" method to log out of the Agora RTM system and receives the \ref agora::rtm::IRtmServiceEventHandler::onLogout "onLogout" callback (error code = `LOGOUT_ERR_OK`), the SDK triggers the \ref agora::rtm::IRtmServiceEventHandler::onConnectionStateChanged "onConnectionStateChanged" callback and switches to the \ref agora::rtm::CONNECTION_STATE_DISCONNECTED "CONNECTION_STATE_DISCONNECTED" state.
       */
      CONNECTION_STATE_CONNECTED = 3,
        
      /**
       4: The connection state between the SDK and the Agora RTM system is interrupted due to network issues, and the SDK keeps re-logging in the Agora RTM system.

       - If the SDK successfully re-logs in the Agora RTM system, the SDK triggers the \ref agora::rtm::IRtmServiceEventHandler::onConnectionStateChanged "onConnectionStateChanged" callback and switches to the \ref agora::rtm::CONNECTION_STATE_CONNECTED "CONNECTION_STATE_CONNECTED" state. The SDK automatically adds the user back to the channels he or she was in when the connection was interrupted, and synchronizes the local user's attributes with the server. 
       - If the SDK cannot re-log in the Agora RTM system, it stays in the \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state and keeps re-logging in the system.
       */
      CONNECTION_STATE_RECONNECTING = 4,
        
      /**
       5: The SDK gives up logging in the Agora RTM system, mainly because another instance has logged in the Agora RTM system with the same user ID.
       
       You must call the \ref agora::rtm::IRtmService::logout "logout" method to leave this state before calling the \ref agora::rtm::IRtmService::login "login" method again.
       */
      CONNECTION_STATE_ABORTED = 5,
    };

    /**
     @brief Reasons for a connection state change.
     */
    public enum CONNECTION_CHANGE_REASON {
        
      /**
       1: The SDK is logging in the Agora RTM system.
       */
      CONNECTION_CHANGE_REASON_LOGIN = 1,
        
      /**
       2: The SDK has logged in the Agora RTM system.
       */
      CONNECTION_CHANGE_REASON_LOGIN_SUCCESS = 2,
        
      /**
       3: The SDK fails to log in the Agora RTM system.
       */
      CONNECTION_CHANGE_REASON_LOGIN_FAILURE = 3,
        
      /**
       4: The SDK fails to log in the Agora RTM system within six seconds and gives up.
       */
      CONNECTION_CHANGE_REASON_LOGIN_TIMEOUT = 4,
        
      /**
       5: The connection between the SDK and the Agora RTM system is interrupted. The system defines an interruption when the SDK loses connection with the Agora RTM system for network reasons and cannot recover in four seconds.
       */
      CONNECTION_CHANGE_REASON_INTERRUPTED = 5,
        
      /**
       6: The user has called the \ref agora::rtm::IRtmService::logout "logout" method to log out of the Agora RTM system.
       */
      CONNECTION_CHANGE_REASON_LOGOUT = 6,
        
      /**
       7: The SDK login to the Agora RTM system is banned by Agora.
       */
      CONNECTION_CHANGE_REASON_BANNED_BY_SERVER = 7,
        
      /**
       8: Another user is logging in the Agora RTM system with the same User ID.
       */
      CONNECTION_CHANGE_REASON_REMOTE_LOGIN = 8,
    };

    /**
     @brief Error codes related to sending a peer-to-peer message.
     */
    public enum PEER_MESSAGE_ERR_CODE {
        
      /**
       0: The method call succeeds, or the specified user receives the peer-to-peer message.
       */
      PEER_MESSAGE_ERR_OK = 0,

      /**
       1: The sender fails to send the peer-to-peer message.
       */
      PEER_MESSAGE_ERR_FAILURE = 1,
        
      /**
       2: A timeout occurs when sending the peer-to-peer message. The current timeout is set as 10 seconds. Possible reasons: The user is in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" or \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state.
       */
      PEER_MESSAGE_ERR_SENT_TIMEOUT = 2,
        
      /**
       3: The specified user is offline and does not receive this peer-to-peer message.
       */
      PEER_MESSAGE_ERR_PEER_UNREACHABLE = 3,
        
     /**
      4: The receiver is offline and does not receive this offline peer-to-peer message, but the server has cached it and will re-send it once he/she is back online.
      */
      PEER_MESSAGE_ERR_CACHED_BY_SERVER = 4,
        
     /**
      5: The method call frequency exceeds the limit of (RTM SDK for Windows C++) 180 calls every three seconds or (RTM SDK for Linux C++) 1500 calls every three seconds, with channel and peer messages taken together..
      */
      PEER_MESSAGE_ERR_TOO_OFTEN = 5,
        
     /**
      6: The user ID is invalid.
      */
      PEER_MESSAGE_ERR_INVALID_USERID = 6,
        
     /**
      7: The message is null or exceeds 32 KB in length.       
	  */
      PEER_MESSAGE_ERR_INVALID_MESSAGE = 7,
      
      /**
       8: The message receiver‘s SDK is of an earlier version and hence cannot recognize this message.
       */
      PEER_MESSAGE_ERR_IMCOMPATIBLE_MESSAGE = 8,
        
     /**
      101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
      */
      PEER_MESSAGE_ERR_NOT_INITIALIZED = 101,
    
     /**
      102: The sender does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before sending the peer-to-peer message.
      */
      PEER_MESSAGE_ERR_USER_NOT_LOGGED_IN = 102,
    };

    /**
     @brief Error codes related to joining a channel.
     */
    public enum JOIN_CHANNEL_ERR {
        
      /**
       0: The method call succeeds, or the user joins the channel successfully.
       */
      JOIN_CHANNEL_ERR_OK = 0,

      /**
       1: Common failure. The user fails to join the channel.
       */
      JOIN_CHANNEL_ERR_FAILURE = 1,
        
      /**
       2: For v1.0.0 and earlier, the SDK returns this error when you try to join a channel that you have already joined. The SDK does not return this error code after v1.0.0.
       */
      JOIN_CHANNEL_ERR_REJECTED = 2, // Usually occurs when the user is already in the channel
        
      /**
       3: The user fails to join the channel because the argument is invalid.
       */
      JOIN_CHANNEL_ERR_INVALID_ARGUMENT = 3,
        
      /**
       4: A timeout occurs when joining the channel. The current timeout is set as five seconds. Possible reasons: The user is in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" or \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state.
       */
      JOIN_CHANNEL_TIMEOUT = 4,
        
     /**
      5: The number of the RTM channels you are in exceeds the limit of 20.
      */
      JOIN_CHANNEL_ERR_EXCEED_LIMIT = 5,
        
     /**
      6: The user is joining or has joined the channel.
      */
      JOIN_CHANNEL_ERR_ALREADY_JOINED = 6,
        
      /**
      7: The method call frequency exceeds 50 queries every three seconds.
      */
      JOIN_CHANNEL_ERR_TOO_OFTEN = 7,

      /**
       8: The frequency of joining the same channel exceeds two times every five seconds.
       */
      JOIN_CHANNEL_ERR_JOIN_SAME_CHANNEL_TOO_OFTEN = 8,
        
     /**
      101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
      */
      JOIN_CHANNEL_ERR_NOT_INITIALIZED = 101,
        
     /**
      102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before joining the channel.
      */
      JOIN_CHANNEL_ERR_USER_NOT_LOGGED_IN = 102,
    };

    /**
     @brief Error codes related to leaving a channel.
     */
    public enum LEAVE_CHANNEL_ERR {
      
      /**
       0: The method call succeeds, or the user leaves the channel successfully.
       */
      LEAVE_CHANNEL_ERR_OK = 0,
        
      /**
       1: Common failure. The user fails to leave the channel.
       */
      LEAVE_CHANNEL_ERR_FAILURE = 1,
        
      /**
       2: **RESERVED FOR FUTURE USE**
       */
      LEAVE_CHANNEL_ERR_REJECTED = 2,
        
      /**
       3: The user is not in the channel.
       */
      LEAVE_CHANNEL_ERR_NOT_IN_CHANNEL = 3,
        
      /**
       101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
       */
      LEAVE_CHANNEL_ERR_NOT_INITIALIZED = 101,
    
      /**
       102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before calling the \ref agora::rtm::IChannel::leave "leave" method.
       */
      LEAVE_CHANNEL_ERR_USER_NOT_LOGGED_IN = 102,
    };
      
    /**
     @brief Reasons why a user leaves the channel.
     */
    public enum LEAVE_CHANNEL_REASON {
        
      /**
       1: The user has quit the call.
       */
      LEAVE_CHANNEL_REASON_QUIT = 1,
        
      /**
       2: The user is banned by the server.
       */
      LEAVE_CHANNEL_REASON_KICKED = 2,
    };

    /**
     @brief Error codes related to sending a channel message.
     */
    public enum CHANNEL_MESSAGE_ERR_CODE {
        
      /**
       0: The method call succeeds, or the server receives the channel message.
       */
      CHANNEL_MESSAGE_ERR_OK = 0,

      /**
       1: Common failure. The user fails to send the channel message.
       */
      CHANNEL_MESSAGE_ERR_FAILURE = 1,
             
      /**
       2: The SDK does not receive a response from the server in 10 seconds. The current timeout is set as 10 seconds. Possible reasons: The user is in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" or \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state.
       */
      CHANNEL_MESSAGE_ERR_SENT_TIMEOUT = 2,
        
      /**
       3: The method call frequency exceeds the limit of (RTM SDK for Windows C++) 180 calls every three seconds or (RTM SDK for Linux C++) 1500 calls every three seconds, with channel and peer messages taken together..
       */
      CHANNEL_MESSAGE_ERR_TOO_OFTEN = 3,
        
      /**
       4: The message is null or exceeds 32 KB in length.
       */
      CHANNEL_MESSAGE_ERR_INVALID_MESSAGE = 4,
        
      /**
       101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
       */
      CHANNEL_MESSAGE_ERR_NOT_INITIALIZED = 101,
    
      /**
       102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before sending out a channel message.
       */
      CHANNEL_MESSAGE_ERR_USER_NOT_LOGGED_IN = 102,
    };

    /**
     @brief Error codes related to retrieving a channel member list.
     */
    public enum GET_MEMBERS_ERR {
  
      /**
       0: The method call succeeds, or the operation succeeds.
       */
      GET_MEMBERS_ERR_OK = 0,
    
      /**
       1: Common failure. The user fails to retrieve a member list of the channel.
       */
      GET_MEMBERS_ERR_FAILURE = 1,
        
      /**
       2: **RESERVED FOR FUTURE USE**
       */
      GET_MEMBERS_ERR_REJECTED = 2,
        
      /**
       3: A timeout occurs when retrieving a member list of the channel. The current timeout is set as five seconds. Possible reasons: The user is in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" or \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state.
       */
      GET_MEMBERS_ERR_TIMEOUT = 3,
        
      /**
       4: The method call frequency exceeds the limit of five queries every two seconds.
       */
      GET_MEMBERS_ERR_TOO_OFTEN = 4,
    
      /**
       5: The user is not in channel.
       */
      GET_MEMBERS_ERR_NOT_IN_CHANNEL = 5,
        
      /**
       101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
       */
      GET_MEMBERS_ERR_NOT_INITIALIZED = 101,
        
      /**
       102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before retrieving a member list.
       */
      GET_MEMBERS_ERR_USER_NOT_LOGGED_IN = 102,
    };

    /**
     @brief Error codes related to querying the online status of the specified peers.
     */
    public enum QUERY_PEERS_ONLINE_STATUS_ERR {
      
      /**
       0: The method call succeeds, or the operation succeeds.
       */
      QUERY_PEERS_ONLINE_STATUS_ERR_OK = 0,

      /**
       1: Common failure. The user fails to query the online status of the specified peers.
       */
      QUERY_PEERS_ONLINE_STATUS_ERR_FAILURE = 1,
        
      /**
       2: The method call fails. The argument is invalid. 
       */
      QUERY_PEERS_ONLINE_STATUS_ERR_INVALID_ARGUMENT = 2,
        
      /**
       3: **RESERVED FOR FUTURE USE**
       */
      QUERY_PEERS_ONLINE_STATUS_ERR_REJECTED = 3,
        
      /**
       4: The SDK fails to receive a response from the server in 10 seconds. The current timeout is set as 10 seconds. Possible reasons: The user is in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" or \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state.
       */
      QUERY_PEERS_ONLINE_STATUS_ERR_TIMEOUT = 4,
        
      /**
       5: The method call frequency exceeds the limit of (RTM SDK for Windows C++) 10 calls every five seconds or (RTM SDK for Linux C++) 100 calls every five seconds.
       */
      QUERY_PEERS_ONLINE_STATUS_ERR_TOO_OFTEN = 5,
        
      /**
       101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
       */
      QUERY_PEERS_ONLINE_STATUS_ERR_NOT_INITIALIZED = 101,
    
      /**
       102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before querying the online status.
       */
      QUERY_PEERS_ONLINE_STATUS_ERR_USER_NOT_LOGGED_IN = 102,
    };

    /**
     @brief The online states of a peer.
     */
    public enum PEER_ONLINE_STATE {

      /**
       0: The peer is online (the SDK has logged in the Agora RTM system).
       */
      PEER_ONLINE_STATE_ONLINE = 0,

      /**
       1: The peer is temporarily unreachable (the server has not received a packet from the SDK for more than six seconds).
       */
      PEER_ONLINE_STATE_UNREACHABLE = 1,

      /**
       2: The peer is offline (the SDK has not logged in the Agora RTM system, or it has logged out of the system, or the server has not received a packet from the SDK for more than 30 seconds).
       */
      PEER_ONLINE_STATE_OFFLINE = 2,
    };
      
    /**
     @brief Subscription types.
     */
    public enum PEER_SUBSCRIPTION_OPTION {
      /**
       0: Takes out a subscription to the online status of specified users.
       */
      PEER_SUBSCRIPTION_OPTION_ONLINE_STATUS = 0,
    };

    /**
     @brief Error codes related to subscribing to or unsubscribing from the status of specified peers.
     */
    public enum PEER_SUBSCRIPTION_STATUS_ERR {

      /**
       0: The method call succeeds, or the operation succeeds.
       */
      PEER_SUBSCRIPTION_STATUS_ERR_OK = 0,

      /**
       1: Common failure. The user fails to subscribe to or unsubscribe from the status of the specified peers.
       */
      PEER_SUBSCRIPTION_STATUS_ERR_FAILURE = 1,

      /**
       2: The method call fails. The argument is invalid.
       */
      PEER_SUBSCRIPTION_STATUS_ERR_INVALID_ARGUMENT = 2,

      /**
       3: **RESERVED FOR FUTURE USE**
       */
      PEER_SUBSCRIPTION_STATUS_ERR_REJECTED = 3,

      /**
       4: The SDK fails to receive a response from the server within 10 seconds. The current timeout is set as 10 seconds. Possible reasons: The user is in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" or \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state.
       */
      PEER_SUBSCRIPTION_STATUS_ERR_TIMEOUT = 4,

      /**
       5: The method call frequency exceeds the limit of 10 queries every five seconds.
       */
      PEER_SUBSCRIPTION_STATUS_ERR_TOO_OFTEN = 5,

      /**
       6: The number of peers, to whom you subscribe, exceeds the limit of 512.
       */
      PEER_SUBSCRIPTION_STATUS_ERR_OVERFLOW = 6,

      /**
       101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
       */
      PEER_SUBSCRIPTION_STATUS_ERR_NOT_INITIALIZED = 101,

      /**
       102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before this operation.
       */
      PEER_SUBSCRIPTION_STATUS_ERR_USER_NOT_LOGGED_IN = 102,
    };

    /**
     @brief Error codes related to getting a list of the peers by subscription option type.
     */
    public enum QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR {

      /**
       0: The method call succeeds, or the operation succeeds.
       */
      QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR_OK = 0,

      /**
       1: Common failure. The user fails to query peers by subscription option type.
       */
      QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR_FAILURE = 1,

      /**
       2: The SDK fails to receive a response from the server within 5 seconds. The current timeout is set as 5 seconds. Possible reasons: The user is in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" or \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state.
       */
      QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR_TIMEOUT = 2,

      /**
       3: The method call frequency exceeds the limit of 10 queries every five seconds.
       */
      QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR_TOO_OFTEN = 3,

      /**
       101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
       */
      QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR_NOT_INITIALIZED = 101,

      /**
       102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before the query.
       */
      QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR_USER_NOT_LOGGED_IN = 102,
    };
      
    /**
     @brief Error codes related to attrubute operations.
     */
    public enum ATTRIBUTE_OPERATION_ERR {
        
        /**
         0: The method call succeeds, or the attribute operation succeeds.
         */
        ATTRIBUTE_OPERATION_ERR_OK = 0,
        
        /**
         1: @deprecated
         */
        ATTRIBUTE_OPERATION_ERR_NOT_READY = 1,
        
        /**
         2: Common failure. The attribute operation fails.
         */
        ATTRIBUTE_OPERATION_ERR_FAILURE = 2,
        
        /**
         3: The argument you put for this attribute operation is invalid. For example, you cannot set a user or channel attribute as "".
         */
        ATTRIBUTE_OPERATION_ERR_INVALID_ARGUMENT = 3,
        
        /**
         4: The attribute size exceeds the limit.
         
         - For user attribute operations: The user's overall attribute size would exceed the limit of 16 KB, one of the user's attributes would exceeds 8 KB in size, or the number of this user's attributes would exceed 32 after this attribute operation.
         - For channel attribute operations: The channel's overall attribute size would exceed the limit of 32 KB, one of the channel attributes would exceed 8 KB in size, or the number of this channel's attributes would exceed 32 after this attribute operation.
         */
        ATTRIBUTE_OPERATION_ERR_SIZE_OVERFLOW = 4,
        
        /**
         5: The method call frequency exceeds the limit.
         
         - For \ref agora::rtm::IRtmService::setLocalUserAttributes "setLocalUserAttributes", \ref agora::rtm::IRtmService::addOrUpdateLocalUserAttributes "addOrUpdateLocalUserAttributes", \ref agora::rtm::IRtmService::deleteLocalUserAttributesByKeys "deleteLocalUserAttributesByKeys" and \ref agora::rtm::IRtmService::clearLocalUserAttributes "clearLocalUserAttributes" taken together: the maximum call frequency is (RTM SDK for Windows C++) 10 calls every five seconds or (RTM SDK for Linux C++) 100 calls every five seconds.
         - For \ref agora::rtm::IRtmService::getUserAttributes "getUserAttributes" and \ref agora::rtm::IRtmService::getUserAttributesByKeys "getUserAttributesByKeys" taken together, the maximum call frequency is (RTM SDK for Windows C++) 40 calls every five seconds or (RTM SDK for Linux C++) 400 calls every five seconds.
         - For \ref agora::rtm::IRtmService::setChannelAttributes "setChannelAttributes", \ref agora::rtm::IRtmService::addOrUpdateChannelAttributes "addOrUpdateChannelAttributes", \ref agora::rtm::IRtmService::deleteChannelAttributesByKeys "deleteChannelAttributesByKeys" and \ref agora::rtm::IRtmService::clearChannelAttributes "clearChannelAttributes" taken together: the maximum call frequency is (RTM SDK for Windows C++) 10 calls every five seconds or (RTM SDK for Linux C++) 100 calls every five seconds.
         - For \ref agora::rtm::IRtmService::getChannelAttributes "getChannelAttributes" and \ref agora::rtm::IRtmService::getChannelAttributesByKeys "getChannelAttributesByKeys" taken together, the maximum call frequency is (RTM SDK for Windows C++) 10 calls every five seconds or (RTM SDK for Linux C++) 400 calls every five seconds.
         */
        ATTRIBUTE_OPERATION_ERR_TOO_OFTEN = 5,
        
        /**
         6: The specified user is not found, either because the user is offline or because the user does not exist.
         */
        ATTRIBUTE_OPERATION_ERR_USER_NOT_FOUND = 6,
        
        /**
         7: A timeout occurs during the attribute operation. The current timeout is set as five seconds. Possible reasons: The user is in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" or \ref agora::rtm::CONNECTION_STATE_RECONNECTING "CONNECTION_STATE_RECONNECTING" state.
         */
        ATTRIBUTE_OPERATION_ERR_TIMEOUT = 7,
        
        /**
         101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
         */
        ATTRIBUTE_OPERATION_ERR_NOT_INITIALIZED = 101,
        
        /**
         102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before the attribute operation.
         */
        ATTRIBUTE_OPERATION_ERR_USER_NOT_LOGGED_IN = 102,
    };
     
     /**
      @brief Error codes related to retrieving the channel member count of specified channels.
      */
    public enum GET_CHANNEL_MEMBER_COUNT_ERR_CODE {
         
        /**
         0: The method call succeeds, or the operation succeeds. 
         */
        GET_CHANNEL_MEMBER_COUNT_ERR_OK = 0,
         
        /**
         1: Unknown common failure. 
         */
        GET_CHANNEL_MEMBER_COUNT_ERR_FAILURE = 1,
        
        /**
         2: One or several of your channel IDs is invalid, or @p channelCount &lt; 0.
         */
        GET_CHANNEL_MEMBER_COUNT_ERR_INVALID_ARGUMENT = 2,
        
        /**
         3: The method call frequency exceeds the limit of one query per second.
         */
        GET_CHANNEL_MEMBER_COUNT_ERR_TOO_OFTEN = 3,
         
        /**
         4: A timeout occurs during this operation. The current timeout is set as five seconds.
         */
        GET_CHANNEL_MEMBER_COUNT_ERR_TIMEOUT = 4,
        
        /**
         5:@p channelCount is greater than 32. 
         */
        GET_CHANNEL_MEMBER_COUNT_ERR_EXCEED_LIMIT = 5,
         
        /**
         101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
         */
        GET_CHANNEL_MEMBER_COUNT_ERR_NOT_INITIALIZED = 101,
        
        /**
         102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before this operation.
         */
        GET_CHANNEL_MEMBER_COUNT_ERR_USER_NOT_LOGGED_IN = 102,
    };
      
      /**
       @brief Error codes related to downloading a file or image.
       */
    public enum DOWNLOAD_MEDIA_ERR_CODE {
          /**
           0: The method call succeeds, or the operation succeeds.
           */
          DOWNLOAD_MEDIA_ERR_OK = 0,
          
          /**
           1: Unknown common failure. Check whether you have write access.
           */
          DOWNLOAD_MEDIA_ERR_FAILURE = 1,
          
          /**
           2: An argument you put is invalid. For example, `mediaId` is in the wrong format or `filePath` is set as `null`.
           */
          DOWNLOAD_MEDIA_ERR_INVALID_ARGUMENT = 2,
          /**
           3: A timeout occurs. The current timeout is set as 120 seconds. The SDK assumes that a timeout occurs if it has not detected any file transmission between the SDK and the file server for 120 seconds.
           */
          DOWNLOAD_MEDIA_ERR_TIMEOUT = 3,
          
          /**
           4: The file or image to download does not exist, either because the media ID you input is incorrect or because the validity of the media ID has expired. 
           */
          DOWNLOAD_MEDIA_ERR_NOT_EXIST = 4,
          
          /**
           5: You have exceeded the upper limit for file download. You can initiate a maximum of nine file download or upload tasks at the same time (download and upload tasks count together).
           */
          DOWNLOAD_MEDIA_ERR_CONCURRENCY_LIMIT_EXCEEDED = 5,

          /**
           6: The file or image download task is aborted for either of the following reasons:

           - The user is in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" state. 
           - The user has cancelled the download task.
           */
          DOWNLOAD_MEDIA_ERR_INTERRUPTED = 6,
          
          /**
           101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
           */
          DOWNLOAD_MEDIA_ERR_NOT_INITIALIZED = 101,
          
          /**
           102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before this operation.
           */
          DOWNLOAD_MEDIA_ERR_NOT_LOGGED_IN = 102,
    };
      
      
      /**
       @brief Error codes related to uploading a file or image.
       */
    public enum UPLOAD_MEDIA_ERR_CODE {
          /**
           0: The method call succeeds, or the operation succeeds.
           */
          UPLOAD_MEDIA_ERR_OK = 0,
          
          /**
           1: Unknown common failure. Please check whether the file exists and whether you can access the file.
           */
          UPLOAD_MEDIA_ERR_FAILURE = 1,
          
          /**
           2: The argument you put is invalid. For example, `mediaId` is in the wrong format. 
           */
          UPLOAD_MEDIA_ERR_INVALID_ARGUMENT = 2,
          
          /**
           3: A timeout occurs. The current timeout is set as 120 seconds. The SDK assumes that a timeout occurs if it has not detected any file transmission between the SDK and the file server for 120 seconds.
           */
          UPLOAD_MEDIA_ERR_TIMEOUT = 3,
          
          /**
           4: The size of the file or image to upload exceeds 30 MB.
           */
          UPLOAD_MEDIA_ERR_SIZE_OVERFLOW = 4,
          /**
           5: You have exceeded the upper limit for file upload. You can initiate a maximum of nine file upload or download tasks at the same time (upload and download tasks count together).
           */
          UPLOAD_MEDIA_ERR_CONCURRENCY_LIMIT_EXCEEDED = 5,
          /**
           6: The file or image upload task is aborted for either of the following reasons:
           
           - The user in the \ref agora::rtm::CONNECTION_STATE_ABORTED "CONNECTION_STATE_ABORTED" state. 
           - The user has cancelled the upload task.
           */
          UPLOAD_MEDIA_ERR_INTERRUPTED = 6,
          
          /**
           101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
           */
          UPLOAD_MEDIA_ERR_NOT_INITIALIZED = 101,
          
          /**
           102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before this operation.
           */
          UPLOAD_MEDIA_ERR_NOT_LOGGED_IN = 102,
      };
      
      /**
       @brief Error codes related to cancelling a download task or cancelling an upload task.
       */
      public enum CANCEL_MEDIA_ERR_CODE {
          /**
           0: The method call succeeds, or the operation succeeds.
           */
          CANCEL_MEDIA_ERR_OK = 0,
          
          /**
           1: Unknown common failure.
           */
          CANCEL_MEDIA_ERR_FAILURE = 1,
          
          /**
           2: The task to cancel does not exist. You can only cancel an ongoing download or upload task. If the download or upload task completes, the corresponding @p requestId is no longer valid. 
           */
          CANCEL_MEDIA_ERR_NOT_EXIST = 2,
          
          /**
           101: \ref agora::rtm::IRtmService "IRtmService" is not initialized.
           */
          CANCEL_MEDIA_ERR_NOT_INITIALIZED = 101,
          
          /**
           102: The user does not call the \ref agora::rtm::IRtmService::login "login" method, or the method call of \ref agora::rtm::IRtmService::login "login" does not succeed before this operation.
           */
          CANCEL_MEDIA_ERR_NOT_LOGGED_IN = 102,
      };
      
      	public enum MESSAGE_TYPE {
        
        /**
        0: The message type is undefined.
        */
        MESSAGE_TYPE_UNDEFINED = 0,

        /**
        1: A text message.
        */
        MESSAGE_TYPE_TEXT = 1,
        
        /**
        2: A raw message. A raw message is a binary message whose size does not exceed 32 KB. 
        */
        MESSAGE_TYPE_RAW = 2,
        
        /**
         3: A file message. The size of a file message must be less than 32 KB.
         */
        MESSAGE_TYPE_FILE = 3,
        
        /**
         4: An image message. The size of an image message must be less than 32 KB.
         */
        MESSAGE_TYPE_IMAGE = 4,
    };

        /**
     @brief The data structure holding an RTM user and the user's online status.
     */
    public struct PeerOnlineStatus
    {
        
      /**
       The user ID of the peer.
       */
      public string peerId;
        
      /**
       @deprecated Deprecated as of v1.2.0. Use \ref agora::rtm::PeerOnlineStatus::onlineState "onlineState" instead.
       
       The online status of the peer.
       
       - true: The user is online (the user has logged in the Agora RTM system).
       - false: The user is offline (the user has logged out of the Agora RTM system, has not logged in, or has failed to logged in).
       */
      public bool isOnline;

      /**
       The online state of the peer. See #PEER_ONLINE_STATE.
       
       @note
       - The server will never return the `unreachable` state, if you <i>query</i> the online status of specified peers (\ref agora::rtm::IRtmService::queryPeersOnlineStatus "queryPeersOnlineStatus"). See also: \ref agora::rtm::IRtmServiceEventHandler::onQueryPeersOnlineStatusResult "onQueryPeersOnlineStatusResult".
       - The server may return the `unreachable` state, if you <i>subscribe to</i> the online status of specified peers (\ref agora::rtm::IRtmService::subscribePeersOnlineStatus "subscribePeersOnlineStatus"). See also: \ref agora::rtm::IRtmServiceEventHandler::onPeersOnlineStatusChanged "onPeersOnlineStatusChanged".
       */
      public PEER_ONLINE_STATE onlineState;
    };

        /**
     @brief A data structure representing the upload ratio or download ratio.
     */
    public struct MediaOperationProgress
    {
        /**
         The total size of the file or image being loaded.
         */
        public Int64 totalSize;
        /**
         The size of the loaded part of the file or image. 
         */
        public Int64 currentSize;
    };

     /**
      @brief Data structure holding channel attribute-specific options.
      */
    public struct ChannelAttributeOptions{  
        /**
         Indicates whether or not to notify all channel members of a channel attribute change.
         
         @note This flag is valid only within the current method call.
         
         - true: Notify all channel members of a channel attribute change.
         - false: (Default) Do not notify all channel members of a channel attribute change.
         */
        public bool enableNotificationToChannelMembers;
    };


    /**
     @brief A data structure holding a user attribute key and its value.
     */
    public struct RtmAttribute
    {
    
        /**
         User attribute name. Must be visible characters and not exceed 32 Bytes.
         */
        public string key;
  
        /**
         Value of the user attribute. Must not exceed 8 KB.
         */
        public string value;
    };

        /**
     @brief The data structure holding a channel ID and the current channel member count.
     */  
    public struct ChannelMemberCount
    {
      /**
       The ID of the channel.
       */
      public string channelId;
       // count of channel, 0 if channel not found.
      /**
       The current member count of the channel.

       @note @p count is 0, if a channel with the specified @p channelId is not found. 
       */ 
      public int count;
    };

  /**
     @brief <b>RETURNED TO THE CALLER.</b> States of an outgoing call invitation.
     */
    public enum LOCAL_INVITATION_STATE {
        
      /**
       0: <b>RETURNED TO THE CALLER.</b> The initial state of a call invitation (idle).
       */
      LOCAL_INVITATION_STATE_IDLE = 0,
        
      /**
       1: <b>RETURNED TO THE CALLER.</b> The call invitation is sent to the callee.
       */
      LOCAL_INVITATION_STATE_SENT_TO_REMOTE = 1,
        
      /**
       2: <b>RETURNED TO THE CALLER.</b> The call invitation is received by the callee.
       */
      LOCAL_INVITATION_STATE_RECEIVED_BY_REMOTE = 2,
        
      /**
       3: <b>RETURNED TO THE CALLER.</b> The call invitation is accepted by the callee.
       */
      LOCAL_INVITATION_STATE_ACCEPTED_BY_REMOTE = 3,
        
      /**
       4: <b>RETURNED TO THE CALLER.</b> The call invitation is declined by the callee.
       */
      LOCAL_INVITATION_STATE_REFUSED_BY_REMOTE = 4,
        
      /**
       5: <b>RETURNED TO THE CALLER.</b> You have canceled the call invitation.
       */
      LOCAL_INVITATION_STATE_CANCELED = 5,
        
      /**
       6: <b>RETURNED TO THE CALLER.</b> The call invitation fails.
       */
      LOCAL_INVITATION_STATE_FAILURE = 6,
    };

    /**
     @brief <b>RETURNED TO THE CALLEE.</b> States of an incoming call invitation.
     */
    public enum REMOTE_INVITATION_STATE {
        
      /**
       0: <b>RETURNED TO THE CALLEE.</b> The initial state of a call invitation (idle).
       */
      REMOTE_INVITATION_STATE_IDLE = 0,
        
      /**
       1: <b>RETURNED TO THE CALLEE.</b> A call invitation from a remote caller is received.
       */
      REMOTE_INVITATION_STATE_INVITATION_RECEIVED = 1,
        
      /**
       2: <b>RETURNED TO THE CALLEE.</b> The message is sent to the caller that the call invitation is accepted.
       */
      REMOTE_INVITATION_STATE_ACCEPT_SENT_TO_LOCAL = 2,
        
      /**
       3: <b>RETURNED TO THE CALLEE.</b> You have declined the call invitation.
       */
      REMOTE_INVITATION_STATE_REFUSED = 3,
        
      /**
       4: <b>RETURNED TO THE CALLEE.</b> You have accepted the call invitation.
       */
      REMOTE_INVITATION_STATE_ACCEPTED = 4,
        
      /**
       5: <b>RETURNED TO THE CALLEE.</b> The call invitation is canceled by the remote caller.
       */
      REMOTE_INVITATION_STATE_CANCELED = 5,
        
      /**
       6: <b>RETURNED TO THE CALLEE.</b> The call invitation fails.
       */
      REMOTE_INVITATION_STATE_FAILURE = 6,
    };

    /**
     @brief <b>RETURNED TO THE CALLER.</b> Error codes of an outgoing call invitation.
     */
    public enum LOCAL_INVITATION_ERR_CODE {
        
      /**
       0: <b>RETURNED TO THE CALLER.</b> The outgoing invitation succeeds.
       */
      LOCAL_INVITATION_ERR_OK = 0,
        
      /**
       1: <b>RETURNED TO THE CALLER.</b> The callee is offline.
        
       The SDK performs the following:
       - Keeps resending the call invitation to the callee, if the callee is offline.
       - Returns this error code, if the callee is still offline 30 seconds since the call invitation is sent.
       */
      LOCAL_INVITATION_ERR_PEER_OFFLINE = 1,
        
      /**
       2: <b>RETURNED TO THE CALLER.</b> The callee is online but has not ACKed to the call invitation 30 seconds since it is sent.
       */
      LOCAL_INVITATION_ERR_PEER_NO_RESPONSE = 2,
        
      /**
       3: <b>RETURNED TO THE CALLER. SAVED FOR FUTURE USE.</b> The call invitation expires 60 seconds since it is sent, if the callee ACKs to the call invitation but neither the caller or callee takes any further action (cancel, accpet, or decline it).
       */
      LOCAL_INVITATION_ERR_INVITATION_EXPIRE = 3,
        
      /**
       4: <b>RETURNED TO THE CALLER.</b> The caller is not logged in.
       */
      LOCAL_INVITATION_ERR_NOT_LOGGEDIN = 4,
    };
      
    /**
     @brief <b>RETURNED TO THE CALLEE.</b> Error codes of an incoming call invitation.
     */
    public enum REMOTE_INVITATION_ERR_CODE {
        
      /**
       0: <b>RETURNED TO THE CALLEE.</b> The incoming calll invitation succeeds.
       */
      REMOTE_INVITATION_ERR_OK = 0,
        
      /**
       1: <b>RETURNED TO THE CALLEE.</b> The call invitation received by the callee fails: the callee is not online.
       */
      REMOTE_INVITATION_ERR_PEER_OFFLINE = 1,
        
      /**
       2: <b>RETURNED TO THE CALLEE.</b> The call invitation received by callee fails: The callee does not ACK within a set time after the callee accepts the call invitation. This is usually a result of a network interruption.
       */
      REMOTE_INVITATION_ERR_ACCEPT_FAILURE = 2,
        
      /**
       3: <b>RETURNED TO THE CALLEE.</b> The call invitation expires 60 seconds since it is sent, if the callee ACKs to the call invitation but neither the caller or callee takes any further action (cancel, accpet, or decline it).
       */
      REMOTE_INVITATION_ERR_INVITATION_EXPIRE = 3,
    };

    /**
     @brief Error codes of the call invitation methods.
     */
    public enum INVITATION_API_CALL_ERR_CODE {
        
      /**
       0: The method call succeeds.
       */
      INVITATION_API_CALL_ERR_OK = 0,
        
      /**
       1: The method call fails. The argument is invalid, for example, the value of the @p content parameter exceeds 8K bytes.
       */
      INVITATION_API_CALL_ERR_INVALID_ARGUMENT = 1,
        
      /**
       2: The method call fails. The call invitation has not started.
       */
      INVITATION_API_CALL_ERR_NOT_STARTED = 2,
        
      /**
       3: The method call fails. The call invitation has ended.
       */
      INVITATION_API_CALL_ERR_ALREADY_END = 3, // accepted, failure, canceled, refused
        
      /**
       4: The method call fails. The call invitation is already accepted.
       */
      INVITATION_API_CALL_ERR_ALREADY_ACCEPT = 4,   // more details
        
      /**
       5: The method call fails. The call invitation is already sent.
       */
      INVITATION_API_CALL_ERR_ALREADY_SENT = 5,
    };

    public struct SendMessageOptions {
      public bool enableOfflineMessaging;
      public bool enableHistoricalMessaging;
    };
}