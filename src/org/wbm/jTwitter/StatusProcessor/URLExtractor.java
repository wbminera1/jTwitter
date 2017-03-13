package org.wbm.jTwitter.StatusProcessor;

import com.twitter.Extractor;
import org.wbm.jTwitter.URLProcessor.IURLProcessor;
import twitter4j.MediaEntity;
import twitter4j.Status;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

/**
  */
public class URLExtractor implements IStatusProcessor{

    private final IURLProcessor URLProcessor;

    URLExtractor(IURLProcessor URLProcessor) {
        this.URLProcessor = URLProcessor;
    }

    public static String expandUrl(String shortenedUrl) throws IOException {
        URL url = new URL(shortenedUrl);
        // open connection
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);

        // stop following browser redirect
        httpURLConnection.setInstanceFollowRedirects(false);

        // extract location header containing the actual destination StatusProcessor
        String expandedURL = httpURLConnection.getHeaderField("Location");
        httpURLConnection.disconnect();

        return expandedURL;
    }

    @Override
    public void onStatus(Status status) {
        //System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText() + " - " + status.getLang());
        //console.Put(status.getUser().getScreenName() + " - " + status.getText() + " - " + status.getLang());
        Extractor extr = new Extractor();
        String str = new String("|");
        List<Extractor.Entity> ent = extr.extractEntitiesWithIndices(status.getText());
        for(Extractor.Entity e : ent) {
            if (e.getType() == Extractor.Entity.Type.URL) {
                try {
                    str += e.getValue() /*e.toString()*/ + "|" + expandUrl(e.getValue()) + "|";
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        //console.Put(str);
        String mstr = new String("|");
        MediaEntity[] me = status.getMediaEntities();
        for(MediaEntity m : me) {
            mstr += m.getMediaURL();
            try {
                URL url = new URL(m.getMediaURL());
                mstr += " " + url.getFile();
                this.URLProcessor.ProcessURL(url);
/*
                    try {
                        openWebpage(url.toURI());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
*/
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        //console.Put(mstr);

    }
}
