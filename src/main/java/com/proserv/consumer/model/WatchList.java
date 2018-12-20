package com.proserv.consumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchList implements Serializable {

    private Set<Watch> watches;

    public WatchList() {
    }

    public Set<Watch> getWatches() {
        return watches;
    }
    public void setWatches(Set<Watch> watches) {
        this.watches = watches;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        watches.iterator().forEachRemaining(watch -> sb.append("'" + watch.getWatchText()+"', "));
        return sb.toString().substring(0, sb.length() - 2);
    }
}
