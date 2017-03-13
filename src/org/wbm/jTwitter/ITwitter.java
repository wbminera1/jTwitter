package org.wbm.jTwitter;

//import jak.Main;
//import jak.MySQLAccess;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
  */
public class ITwitter {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(ITwitter.class.getName());

//    private MySQLAccess mSQL;
/*
    public ITwitter(MySQLAccess mSQL) {
        this.mSQL = mSQL;
    }
*/

    void addStatus(Status status)
    {
        long tId = status.getId();
        User tUser= status.getUser();
        java.util.Date date = status.getCreatedAt();
        String text = status.getText();
        String source = status.getSource();
        boolean isT = status.isTruncated();
        long inReplyStatus = status.getInReplyToStatusId();
        long inReplyUser= status.getInReplyToUserId();
        String inReplyName = status.getInReplyToScreenName();
        GeoLocation loc = status.getGeoLocation();
        Place place = status.getPlace();
        boolean isF = status.isFavorited();
        boolean isR = status.isRetweeted();
        int gfc = status.getFavoriteCount();
        boolean isRt = status.isRetweet();
        Status st = status.getRetweetedStatus();
        long[] cont = status.getContributors();
        int rtc = status.getRetweetCount();
        boolean isrtm = status.isRetweetedByMe();
        long gcur = status.getCurrentUserRetweetId();
        boolean ips = status.isPossiblySensitive();
        String lang = status.getLang();
        Scopes scopes = status.getScopes();

        System.out.println(
                "ID " + tId + " " +                     // long
                        "User " + tUser.getId() + " " +         // long
                        "Date " + date + " " +                  // Date
                        "Text " + text + " " +                  // string
                        "Source " + source + " " +              // string
                        "Truncated " + isT + " " +              // boolean
                        "InReplyStatus " + inReplyStatus + " " +// long
                        "InReplyUser " + inReplyUser + " " +    // long
                        "InReplyName " + inReplyName + " " +    // string
                        "Loc " + loc + " " +                    // string
                        "Place " + place + " " +                // string
                        "IsFavorited " + isF + " " +            // bool
                        "IsRetweet " + isR + " " +              // bool
                        "FavoriteCount " + gfc + " " +          // int
                        "IsRetweet" + isRt + " " +              // bool
                        "RetweetStatus " + (st != null ? st.getId() : -1) + " " + // long
                        "Contributors " + cont + " " +          // string
                        "RetweetCount " + rtc + " " +           // int
                        "RetweetedByMe " + isrtm + " " +        // bool
                        "CurrentUserRetweetId " + gcur + " " +  // long
                        "PossiblySensitive " + ips + " " +      // bool
                        "Lang " + lang + " " +                  // string
                        "Scopes " + scopes + " ");              // string

        //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getId() + " - " + status.getText());
        //mSQL.insertTweet(status);
    }

    public void getTweets(long[] ids)
    {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            User user = twitter.verifyCredentials();
            System.out.println("Req tweets from " + ids[0] + " to " + ids[99]);
            ResponseList<Status> statuses = twitter.lookup(ids);
            for (Status status : statuses) {
                    addStatus(status);
                }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public void getFollowers(String id)
    {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            User user = twitter.verifyCredentials();
            System.out.println("Req followers for " + id);
            long cursor = -1;
            IDs ids;
            do {
                while (ifLimitExceeded("/followers/ids"))
                {
                    System.out.println("Limit exceeded, waiting...");
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ids = twitter.getFollowersIDs(id, cursor);
                //twitter.friendsFollowers().
                System.out.println("Received " + ids.getIDs().length);
/*
                for (long lid : ids.getIDs()) {
                    System.out.print(" " + lid);
                    //addStatus(status);
                }
*/
                cursor = ids.getNextCursor();
            } while (ids.hasNext());
            System.out.println();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public void getFollowing(String id)
    {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            User user = twitter.verifyCredentials();
            System.out.println("Req following for " + id);
            long cursor = -1;
            IDs ids;
            do {
                while (ifLimitExceeded("/friends/ids"))
                {
                    System.out.println("Limit exceeded, waiting...");
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ids = twitter.getFriendsIDs(id, cursor);
                //twitter.friendsFollowers().
                System.out.println("Received " + ids.getIDs().length);
/*
                for (long lid : ids.getIDs()) {
                    System.out.print(" " + lid);
                    //addStatus(status);
                }
*/
                cursor = ids.getNextCursor();
            } while (ids.hasNext());
            System.out.println();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public long getHomeline(long id) {
        try {
            // gets jTwitter instance with default credentials
            Twitter twitter = new TwitterFactory().getInstance();
            User user = twitter.verifyCredentials();
            //long id = 565267941105860608L;
            waitIfLimitExceeded("/statuses/home_timeline");
            List<Status> statuses = twitter.getHomeTimeline(new Paging(id));
            System.out.println("Showing @" + user.getScreenName() + "'s home timeline. At " + id);
            for (Status status : statuses) {
                addStatus(status);
                long statusId = status.getId();
                if(statusId > id)
                {
                    id = statusId;
                }
            }
            if (!statuses.isEmpty())
            {
                //getFollowers(statuses.get(0).getUser().getScreenName());
                getFollowing(statuses.get(0).getUser().getScreenName());
            }
            return id;
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
        return 0;
    }

    public static void getToken(String[] args) {
	// write your code here

        File file = new File("twitter4j.properties");
        Properties prop = new Properties();
        FileInputStream is = null;
        FileOutputStream os = null;

        try {
            if(file.exists()) {
                is = new FileInputStream(file);
                prop.load(is);
            }

            if(args.length < 2) {
                if(null == prop.getProperty("oauth.consumerKey") && null == prop.getProperty("oauth.consumerSecret")) {
                    System.out.println("Usage: java twitter4j.examples.oauth.GetAccessToken [consumer key] [consumer secret]");
                    System.exit(-1);
                }
            } else {
                prop.setProperty("oauth.consumerKey", args[0]);
                prop.setProperty("oauth.consumerSecret", args[1]);
                os = new FileOutputStream("twitter4j.properties");
                prop.store(os, "twitter4j.properties");
            }
        } catch (IOException var53) {
            var53.printStackTrace();
            System.exit(-1);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException var50) {
                }
            }

            if(os != null) {
                try {
                    os.close();
                } catch (IOException var49) {
                }
            }

        }

        try {
            Twitter ioe = (new TwitterFactory()).getInstance();
            RequestToken requestToken = ioe.getOAuthRequestToken();
            System.out.println("Got request token.");
            System.out.println("Request token: " + requestToken.getToken());
            System.out.println("Request token secret: " + requestToken.getTokenSecret());
            AccessToken accessToken = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while(null == accessToken) {
                System.out.println("Open the following StatusProcessor and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());

                try {
                    Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
                } catch (UnsupportedOperationException var54) {
                } catch (IOException var55) {
                } catch (URISyntaxException var56) {
                    throw new AssertionError(var56);
                }

                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                String ignore = br.readLine();

                try {
                    if(ignore.length() > 0) {
                        accessToken = ioe.getOAuthAccessToken(requestToken, ignore);
                    } else {
                        accessToken = ioe.getOAuthAccessToken(requestToken);
                    }
                } catch (TwitterException var59) {
                    if(401 == var59.getStatusCode()) {
                        System.out.println("Unable to get the access token.");
                    } else {
                        var59.printStackTrace();
                    }
                }
            }

            System.out.println("Got access token.");
            System.out.println("Access token: " + accessToken.getToken());
            System.out.println("Access token secret: " + accessToken.getTokenSecret());

            try {
                prop.setProperty("oauth.accessToken", accessToken.getToken());
                prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
                os = new FileOutputStream(file);
                prop.store(os, "twitter4j.properties");
                os.close();
            } catch (IOException var52) {
                var52.printStackTrace();
                System.exit(-1);
            } finally {
                if(os != null) {
                    try {
                        os.close();
                    } catch (IOException var51) {
                    }
                }

            }

            System.out.println("Successfully stored access token to " + file.getAbsolutePath() + ".");
            System.exit(0);
        } catch (TwitterException var60) {
            var60.printStackTrace();
            System.out.println("Failed to get accessToken: " + var60.getMessage());
            System.exit(-1);
        } catch (IOException var61) {
            var61.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }

    }

    public boolean ifLimitExceeded(String endpoint)
    {
        boolean status = false;
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            Map<String ,RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
            status = rateLimitStatus.get(endpoint).getRemaining() == 0;
            System.out.println("Endpoint: " + endpoint);
            System.out.println(" Limit: " + rateLimitStatus.get(endpoint).getLimit());
            System.out.println(" Remaining: " + rateLimitStatus.get(endpoint).getRemaining());
            System.out.println(" ResetTimeInSeconds: " + rateLimitStatus.get(endpoint).getResetTimeInSeconds());
            System.out.println(" SecondsUntilReset: " + rateLimitStatus.get(endpoint).getSecondsUntilReset());
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get rate limit status: " + te.getMessage());
        }
        return status;
    }

    public boolean waitIfLimitExceeded(String endpoint)
    {
        boolean status = false;
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            Map<String ,RateLimitStatus> rateLimitStatus;
            do {
                rateLimitStatus = twitter.getRateLimitStatus();
                status = (rateLimitStatus.get(endpoint).getRemaining() == 0);
                System.out.println("Endpoint: " + endpoint);
                System.out.println(" Limit: " + rateLimitStatus.get(endpoint).getLimit());
                System.out.println(" Remaining: " + rateLimitStatus.get(endpoint).getRemaining());
                System.out.println(" ResetTimeInSeconds: " + rateLimitStatus.get(endpoint).getResetTimeInSeconds());
                System.out.println(" SecondsUntilReset: " + rateLimitStatus.get(endpoint).getSecondsUntilReset());
                if (rateLimitStatus.get(endpoint).getRemaining() == 0)
                {
                    try {
                        Thread.sleep(rateLimitStatus.get(endpoint).getResetTimeInSeconds() * 1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (rateLimitStatus.get(endpoint).getRemaining() == 0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get rate limit status: " + te.getMessage());
        }
        return status;
    }

    public void getRateLimitStatus()
    {
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            Map<String ,RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus();
            for (String endpoint : rateLimitStatus.keySet()) {
                RateLimitStatus status = rateLimitStatus.get(endpoint);
                if (status.getLimit() != status.getRemaining())
                {
                    System.out.println("Endpoint: " + endpoint);
                    System.out.println(" Limit: " + status.getLimit());
                    System.out.println(" Remaining: " + status.getRemaining());
                    System.out.println(" ResetTimeInSeconds: " + status.getResetTimeInSeconds());
                    System.out.println(" SecondsUntilReset: " + status.getSecondsUntilReset());
                }
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get rate limit status: " + te.getMessage());
        }
    }

    public void PrintRawSampleStream() throws TwitterException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        RawStreamListener listener = new RawStreamListener() {
            @Override
            public void onMessage(String rawJSON) {
                System.out.println(rawJSON);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        twitterStream.sample();
    }
}
