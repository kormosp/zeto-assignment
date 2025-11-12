package com.zeto.edf_processor.model;

import lombok.Getter;

import java.util.*;

/*
* Value object representing the Channel information
*/
@Getter
public class Channels {

    public record Channel (String label, String type){}

    private final List<Channel> signalChannels;

    //Constructor
    private Channels(List<Channel> signalChannels) {
        // return an immutable list
        this.signalChannels = List.copyOf(signalChannels);
    }

    //Create and return a list
    public static Channels from(String[] labels, String[] types) {
        if (labels == null || labels.length == 0)
            return new Channels(Collections.emptyList());

        if (types == null || types.length == 0) {
            var channelsWithOnlyLabels = Arrays.stream(labels)
                    .map(e -> new Channel(e, ""))
                    .toList();
            return new Channels(channelsWithOnlyLabels);
        }

        List<Channel> channelsWithLabelAndType = new LinkedList<>();
        for (int i = 0; i < labels.length; i++) {
            String labelTrimmed = labels[i].trim();
            String typeTrimmed = i < types.length ? types[i] : "";
            channelsWithLabelAndType.add(new Channel(labelTrimmed, typeTrimmed));
        }
        return new Channels(channelsWithLabelAndType);
    }

    //Create and return an empty list
    public static Channels empty() {
        return new Channels(Collections.emptyList());
    }

    public int count() {
        return signalChannels.size();
    }

}
