package org.wbm.jTwitter;

import twitter4j.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
*/
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
public class LimitRates {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(LimitRates.class.getName());

    Map<String ,RateLimitStatus>    LimitStatus;
    Map<String, Integer>            LimitUsed;
    Date                            LastStatusUpdate;

    public LimitRates() {
        LastStatusUpdate = Calendar.getInstance().getTime();
        LimitUsed = new HashMap<String, Integer>();
    }

    void updateActualStatus() throws TwitterException {
        log.log(Level.INFO, "updateActualStatus()");
        Twitter twitter = new TwitterFactory().getInstance();
        LimitStatus = twitter.getRateLimitStatus();
        LimitUsed = new HashMap<String, Integer>();

    }

    public void waitFor(String endpoint) throws TwitterException {
        Date current = Calendar.getInstance().getTime();
        long delta = current.getTime() - LastStatusUpdate.getTime();
        log.log(Level.INFO, "Delta " + delta);
        if ((LimitStatus == null) || (delta > (10L * 60L * 1000L)))
        {
            updateActualStatus();
        }
        RateLimitStatus status = LimitStatus.get(endpoint);
        if (!LimitUsed.containsKey(endpoint))
        {
            LimitUsed.put(endpoint, 0);
        }
        Integer val = LimitUsed.get(endpoint) + 1;
        if (val < status.getLimit())
        {
            LimitUsed.put(endpoint,val);
        }
        else
        {
            long timeLeft = LimitStatus.get(endpoint).getResetTimeInSeconds() * 1000L - delta;
            if (timeLeft > 0)
            {
                try {
                    Thread.sleep(timeLeft);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            updateActualStatus();
        }
    }

    public void getRateLimitStatus()
    {
        LastStatusUpdate = Calendar.getInstance().getTime();
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            LimitStatus = twitter.getRateLimitStatus();
            for (String endpoint : LimitStatus.keySet()) {
                RateLimitStatus status = LimitStatus.get(endpoint);
                //if (status.getLimit() != status.getRemaining())
                {
                    //System.out.println("Endpoint: " + endpoint);
                    //System.out.println(" Limit: " + status.getLimit());
                    //System.out.println(" Remaining: " + status.getRemaining());
                    //System.out.println(" ResetTimeInSeconds: " + status.getResetTimeInSeconds());
                    //System.out.println(" SecondsUntilReset: " + status.getSecondsUntilReset());
                }
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get rate limit status: " + te.getMessage());
        }
    }
}
