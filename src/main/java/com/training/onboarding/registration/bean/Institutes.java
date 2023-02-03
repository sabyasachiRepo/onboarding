package com.training.onboarding.registration.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Institutes {

    private List<InstituteResponse> institutes;
    public void setInstitutes(List<InstituteResponse> institutes) {
        this.institutes=institutes;
    }
}
