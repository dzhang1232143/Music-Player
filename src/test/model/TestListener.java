package model;

import ui.tabs.QueueListener;

public class TestListener implements QueueListener {
    
    boolean notified;
    public TestListener() {
        notified = false;
    }

    @Override
    public void onQueueChange() {
        notified = true;
    }

    public boolean isNotified() {
        return notified;
    }
}
