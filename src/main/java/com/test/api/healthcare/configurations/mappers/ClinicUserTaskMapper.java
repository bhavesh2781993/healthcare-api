package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.ClinicUserTaskModel;
import com.test.api.healthcare.configurations.models.entities.ClinicUser;
import com.test.api.healthcare.configurations.models.entities.ClinicUserTask;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ClinicUserTaskMapper {

    @Mapping(target = "clinicUserTaskId", source = "id")
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "taskAssignedTo", source = "taskAssignedTo.id")
    @Mapping(target = "patientName", source = "patient.firstName")
    @Mapping(target = "patientMrn", source = "patient.mrn")
    ClinicUserTaskModel toClinicUserTaskModel(ClinicUserTask clinicUserTask);

    @Mapping(target = "id", source = "clinicUserTaskId")
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "taskAssignedTo", source = "taskAssignedTo")
    ClinicUserTask toClinicUserTask(ClinicUserTaskModel clinicUserTaskModel);

    default ClinicUserTask toClinicUserTask(ClinicUserTaskModel clinicUserTaskModel, Long clinicId) {
        return toClinicUserTask(clinicUserTaskModel, null, clinicId);
    }

    default ClinicUserTask toClinicUserTask(ClinicUserTaskModel clinicUserTaskModel, Long clinicUserTaskId, Long clinicId) {
        clinicUserTaskModel.setClinicId(clinicId);
        clinicUserTaskModel.setClinicUserTaskId(clinicUserTaskId);
        return toClinicUserTask(clinicUserTaskModel);
    }

    /**
     * <p>
     *     This method is used by {@link ClinicUserTaskMapper#toClinicUserTask(ClinicUserTaskModel)} <br>
     *     This is a special method that assigns {@link ClinicUserTaskModel#taskAssignedTo} to {@link ClinicUserTask#taskAssignedTo}.
     * </p>
     * <p>
     *     Mapstruct by default creates a new object even if ClinicUserTaskModel.taskAssignedTo == null.
     *     This method overrides that behaviour.
     * </p>
     * @param taskAssignedTo accepts clinic user id.
     * @return returns clinic user.
     */
    default ClinicUser mapTaskAssignTo(Long taskAssignedTo) {
        if (taskAssignedTo == null) {
            return null;
        }
        final ClinicUser clinicUser = new ClinicUser();
        clinicUser.setId(taskAssignedTo);
        return clinicUser;
    }

    List<ClinicUserTaskModel> toClinicUserTaskModelList(List<ClinicUserTask> clinicUserTaskList);

    List<ClinicUserTask> toClinicUserTaskList(List<ClinicUserTaskModel> clinicUserTaskModelList);

}
