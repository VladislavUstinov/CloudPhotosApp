package com.laserdiffraction01.laserdiffraction01.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordDTO {
    private String username = "";
    private String oldPassword = "";
    private String newPassword = "";
    private String newPasswordConfirmed = "";

    public UserChangePasswordDTO (){

    }
}
