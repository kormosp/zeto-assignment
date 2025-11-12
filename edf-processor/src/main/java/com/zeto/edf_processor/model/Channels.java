package com.zeto.edf_processor.model;

import lombok.Getter;

import java.util.*;

/**
 * Represents the collection of signal channels in an EDF recording.
 * Each channel has a label and an optional type.
 */
@Getter
public class Channels {
    /**
     * Immutable record representing a single channel.
     */
    public record Channel (String label, String type){}

    /** List of signal channels */
    private final List<Channel> signalChannels;

    /**Constructor   */
    private Channels(List<Channel> signalChannels) {
        // return an immutable list
        this.signalChannels = List.copyOf(signalChannels);
    }

    /**
     * Creates a {@link Channels} object from arrays of labels and types.
     *
     * @param labels Array of channel labels
     * @param types Array of channel types (optional)
     * @return Channels object with all channels populated
     */
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

    /**
     * Returns an empty Channels object.
     *
     * @return empty Channels
     */
    public static Channels empty() {
        return new Channels(Collections.emptyList());
    }

    /**
     * Counts the number of channels.
     *
     * @return number of channels
     */
    public int count() {
        return signalChannels.size();
    }

}
