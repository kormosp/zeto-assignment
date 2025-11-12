package com.zeto.edf_processor.dto;

import com.zeto.edf_processor.model.EdfData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * MapStruct mapper for converting between EDF domain entities and DTOs.
 *
 * <p>This mapper provides the boundary between the domain layer and the API layer,
 * transforming rich domain objects ({@link EdfData}) into simple DTOs ({@link EdfDto})
 * suitable for JSON serialization and external consumption.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Map domain entities to DTOs for API responses</li>
 *   <li>Handle null values gracefully for invalid files</li>
 *   <li>Extract and transform nested domain objects (PatientInfo, Channels, etc.)</li>
 * </ul>
 *
 * <p><b>MapStruct configuration:</b></p>
 * <ul>
 *   <li>Component model: Spring (generates Spring bean)</li>
 *   <li>Unmapped targets: Ignored (only explicitly mapped fields are processed)</li>
 *   <li>Compile-time code generation for performance</li>
 * </ul>
 *
 * @author Peter Kormos
 * @version 1.0
 * @see EdfData
 * @see EdfDto
 */
@Mapper(componentModel = "spring",
        uses = ChannelMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EdfMapper {

    /**
     * Converts a single EDF domain entity to a DTO.
     *
     * <p>This method performs the main mapping from the rich domain model to
     * a flat DTO structure. Complex mappings use custom helper methods to
     * handle null safety and data extraction from nested domain objects.
     * Uses imported ChannelMapper to convert between Channel value objects and ChannelDto.</p>
     *
     * <p><b>Mapping strategy:</b></p>
     * <ul>
     *   <li>Simple fields: Direct property mapping (fileName, validEdf, etc.)</li>
     *   <li>Nested objects: Custom expressions to safely navigate and extract</li>
     *   <li>Calculated fields: Java expressions calling domain methods</li>
     * </ul>
     *
     * @param edfData the domain entity to convert (must not be null)
     * @see ChannelMapper
     * @see ChannelDto
     * @return DTO representation of the EDF file data
     */
    @Mapping(target = "fileName", source = "edfFileProperties.fileName")
    @Mapping(target = "validEdf", source = "edfFileProperties.validEdf")
    @Mapping(target = "errorMessage", source = "edfFileProperties.errorMessage")
    @Mapping(target = "recordingDate", expression = "java(edfData.getRecordingDateTime())")
    @Mapping(target = "patientName", expression = "java(edfData.getPatientName())")
    @Mapping(target = "channels", source = ".", qualifiedByName = "mapChannels")
    @Mapping(target = "numberOfChannels", expression = "java(edfData.getNumberOfChannels())")
    //@Mapping(target = "numberOfChannels", source = ".", qualifiedByName = "channelCount")
    @Mapping(target = "recordingLength", expression = "java(edfData.getRecordingLengthInSeconds())")
    EdfDto toDto(EdfData edfData);

    List<EdfDto> toDto(List<EdfData> edfDataList);

    }

