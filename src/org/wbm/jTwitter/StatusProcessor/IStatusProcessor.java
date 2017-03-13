package org.wbm.jTwitter.StatusProcessor;

import twitter4j.Status;

/**
  */
public interface IStatusProcessor {
    void onStatus(Status status);
}
