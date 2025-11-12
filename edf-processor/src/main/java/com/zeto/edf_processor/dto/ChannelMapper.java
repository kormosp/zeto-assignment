package com.zeto.edf_processor.dto;

import com.zeto.edf_processor.model.Channels;
import com.zeto.edf_processor.model.EdfData;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

/**
 * MapStruct mapper for converting between convert between Channel value objects and ChannelDtos
 *
 * @author Peter Kormos
 * @version 1.0
 * @see Channels
 * @see ChannelDto
 */
@Mapper(componentModel = "spring")
public interface ChannelMapper {

    @Named("mapChannels")
    default List<ChannelDto> mapChannels(EdfData edfData) {
        return edfData.getChannels().getSignalChannels()
                .stream()
                .map(channelElement -> new ChannelDto(channelElement.label(), channelElement.type()))
                .toList();
    }

    @Named("channelCount")
    default int channelCount(EdfData edfData) {
        return edfData.getChannels().getSignalChannels().size();
    }
}
