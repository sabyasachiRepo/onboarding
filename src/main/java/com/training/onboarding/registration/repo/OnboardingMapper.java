package com.training.onboarding.registration.repo;

import com.training.onboarding.registration.bean.Institute;
import com.training.onboarding.registration.bean.InstituteResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OnboardingMapper {

    public List<InstituteResponse> fromEntity(List<Institute> instituteEntities){
        List<InstituteResponse> instituteResponseList =new ArrayList<>(instituteEntities.size());
        for(Institute institute : instituteEntities){
            InstituteResponse instituteResponse =new InstituteResponse();
            BeanUtils.copyProperties(institute, instituteResponse);
            instituteResponseList.add(instituteResponse);
        }
        return instituteResponseList;
    }
}
