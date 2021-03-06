package rso.football.igrisca.models.converters;

import rso.football.igrisca.lib.IgriscaMetadata;
import rso.football.igrisca.models.entities.IgriscaMetadataEntity;

public class IgriscaMetadataConverter {

    public static IgriscaMetadata toDto(IgriscaMetadataEntity entity){
        IgriscaMetadata dto = new IgriscaMetadata();
        dto.setIgrisceId(entity.getId());
        dto.setName(entity.getName());
        dto.setLongitude(entity.getLongitude());
        dto.setLatitude(entity.getLatitude());

        return dto;
    }

    public static IgriscaMetadataEntity toEntity(IgriscaMetadata dto){
        IgriscaMetadataEntity entity = new IgriscaMetadataEntity();
        entity.setName(dto.getName());
        entity.setLongitude(dto.getLongitude());
        entity.setLatitude(dto.getLatitude());

        return entity;
    }
}
