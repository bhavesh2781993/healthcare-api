package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.common.utils.FileCompressionUtil;
import com.test.api.healthcare.configurations.models.ClinicUserImageModel;
import com.test.api.healthcare.configurations.models.ClinicUserModel;
import com.test.api.healthcare.configurations.models.entities.ClinicUser;
import com.test.api.healthcare.configurations.models.entities.ClinicUserImage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ClinicUserMapper {

    @Mapping(target = "clinicDepartment.id", source = "clinicDepartmentId")
    @Mapping(target = "id", source = "clinicUserId")
    ClinicUser toClinicUser(ClinicUserModel clinicUserModel);

    default ClinicUser toClinicUser(ClinicUserModel clinicUserModel, MultipartFile image) {
        final ClinicUserImage clinicUserImage = new ClinicUserImage();
        clinicUserImage.setImageName(image.getOriginalFilename());
        clinicUserImage.setImageContentType(image.getContentType());
        clinicUserImage.setIsActive(Boolean.TRUE);
        clinicUserImage.setImage(FileCompressionUtil.compressFile(image));

        final ClinicUser clinicUser = toClinicUser(clinicUserModel);
        clinicUser.setClinicUserImages(List.of(clinicUserImage));
        return clinicUser;
    }

    default ClinicUser toClinicUser(Long clinicUserId, ClinicUserModel clinicUserModel, MultipartFile image) {
        clinicUserModel.setClinicUserId(clinicUserId);
        return toClinicUser(clinicUserModel, image);
    }

    @Mapping(target = "clinicDepartmentId", source = "clinicDepartment.id")
    @Mapping(target = "clinicUserId", source = "id")
    @Mapping(target = "clinicUserImage", source = "clinicUserImages", qualifiedByName = "decompressImages")
    ClinicUserModel toClinicUserModel(ClinicUser clinicUser);

    @Named("decompressImages")
    default ClinicUserImageModel decompressImages(List<ClinicUserImage> clinicUserImages) {
        return clinicUserImages.stream()
            .map(clinicUserImage -> {
                final ClinicUserImageModel clinicUserImageModel = new ClinicUserImageModel();
                clinicUserImageModel.setImageName(clinicUserImage.getImageName());
                clinicUserImageModel.setImageContentType(clinicUserImage.getImageContentType());
                clinicUserImageModel.setIsActive(clinicUserImage.getIsActive());
                clinicUserImageModel.setImage(FileCompressionUtil.decompressData(clinicUserImage.getImage()));
                return clinicUserImageModel;
            })
            .findAny()
            .orElse(null);
    }

    List<ClinicUserModel> toClinicUserModelList(List<ClinicUser> clinicUsers);

    //ClinicUserImage
    @Mapping(target = "id", source = "clinicUserImageId")
    ClinicUserImage toClinicUserImage(ClinicUserImageModel clinicUserImageModel);

    @Mapping(target = "clinicUserImageId", source = "id")
    ClinicUserImageModel toClinicUserImageModel(ClinicUserImage clinicUserImage);

    List<ClinicUserImageModel> toClinicUserImageModelList(List<ClinicUserImage> clinicUserImages);

}
