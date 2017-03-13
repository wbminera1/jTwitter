package org.wbm.jTwitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.*;

/**
  */

public class t4jl  {
    Twitter twitter;
    LimitRates rates;

    t4jl() throws TwitterException {
        twitter = new TwitterFactory().getInstance();
        twitter.verifyCredentials();
    }

    class account
    {

    }

/*
Endpoint: /statuses/friends
Endpoint: /statuses/home_timeline
Endpoint: /statuses/lookup
Endpoint: /statuses/mentions_timeline
Endpoint: /statuses/oembed
Endpoint: /statuses/retweeters/ids
Endpoint: /statuses/retweets/:id
Endpoint: /statuses/retweets_of_me
Endpoint: /statuses/show/:id
Endpoint: /statuses/user_timeline
 */
    class statuses
    {
        class home_timeline
        {

            public ResponseList<Status> get() throws TwitterException {
                //;;rates.
                return twitter.getHomeTimeline();
            }

            public ResponseList<Status> get(Paging paging) throws TwitterException {
                return twitter.getHomeTimeline(paging);
            }

        }
    }
    /*
Endpoint: /account/login_verification_enrollment
Endpoint: /account/settings
Endpoint: /account/update_profile
Endpoint: /account/verify_credentials
Endpoint: /application/rate_limit_status
Endpoint: /blocks/ids
Endpoint: /blocks/list
Endpoint: /contacts/addressbook
Endpoint: /contacts/delete/status
Endpoint: /contacts/uploaded_by
Endpoint: /contacts/users
Endpoint: /contacts/users_and_uploaded_by
Endpoint: /device/token
Endpoint: /direct_messages
Endpoint: /direct_messages/sent
Endpoint: /direct_messages/sent_and_received
Endpoint: /direct_messages/show
Endpoint: /favorites/list
Endpoint: /followers/ids
Endpoint: /followers/list
Endpoint: /friends/following/ids
Endpoint: /friends/following/list
Endpoint: /friends/ids
Endpoint: /friends/list
Endpoint: /friendships/incoming
Endpoint: /friendships/lookup
Endpoint: /friendships/no_retweets/ids
Endpoint: /friendships/outgoing
Endpoint: /friendships/show
Endpoint: /geo/id/:place_id
Endpoint: /geo/reverse_geocode
Endpoint: /geo/search
Endpoint: /geo/similar_places
Endpoint: /help/configuration
Endpoint: /help/languages
Endpoint: /help/privacy
Endpoint: /help/settings
Endpoint: /help/tos
Endpoint: /lists/list
Endpoint: /lists/members
Endpoint: /lists/members/show
Endpoint: /lists/memberships
Endpoint: /lists/ownerships
Endpoint: /lists/show
Endpoint: /lists/statuses
Endpoint: /lists/subscribers
Endpoint: /lists/subscribers/show
Endpoint: /lists/subscriptions
Endpoint: /mutes/users/ids
Endpoint: /mutes/users/list
Endpoint: /saved_searches/destroy/:id
Endpoint: /saved_searches/list
Endpoint: /saved_searches/show/:id
Endpoint: /search/tweets

Endpoint: /trends/available
Endpoint: /trends/closest
Endpoint: /trends/place
Endpoint: /users/derived_info
Endpoint: /users/lookup
Endpoint: /users/profile_banner
Endpoint: /users/report_spam
Endpoint: /users/search
Endpoint: /users/show/:id
Endpoint: /users/suggestions
Endpoint: /users/suggestions/:slug
Endpoint: /users/suggestions/:slug/members

 */
}

