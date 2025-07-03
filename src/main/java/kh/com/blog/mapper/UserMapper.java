package kh.com.blog.mapper;

import kh.com.blog.dto.response.LoginResponseDTO;
import kh.com.blog.dto.response.UserDetailResponseDTO;
import kh.com.blog.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
		componentModel = MappingConstants.ComponentModel.SPRING,
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
	UserDetailResponseDTO from(UserEntity userEntity);

	LoginResponseDTO fromLogin(UserEntity userEntity);
}
