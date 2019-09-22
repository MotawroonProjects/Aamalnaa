package com.creative.share.apps.aamalnaa.singleton;

public class Singleton {
    private static Singleton instance = null;
    private boolean isEventAdded = false;
    private boolean isSentNewOrder = false;

    private Singleton() {
    }

    public static synchronized Singleton newInstance()
    {
        if (instance==null)
        {
            instance = new Singleton();
        }
        return instance;
    }


    public boolean isEventAdded() {
        return isEventAdded;
    }

    public void setEventAdded(boolean eventAdded) {
        isEventAdded = eventAdded;
    }

    public boolean isSentNewOrder() {
        return isSentNewOrder;
    }

    public void setSentNewOrder(boolean sentNewOrder) {
        isSentNewOrder = sentNewOrder;
    }
}
