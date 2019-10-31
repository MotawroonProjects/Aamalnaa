package com.creative.share.apps.aamalnaa.models;

import java.io.Serializable;
import java.util.List;

public class AllMessageModel implements Serializable {
private List<MessageModel.SingleMessageModel> data;

    public List<MessageModel.SingleMessageModel> getData() {
        return data;
    }

}
