package gr.pfizer.restapi.security;

import gr.pfizer.restapi.Configuration;

public enum Role {
    ROLE_NA("n/a"),
    ROLE_CHIEF_DOCTOR(Configuration.USER_ROLE_ADMIN),
    ROLE_PATIENT(Configuration.USER_ROLE_PATIENT),
    ROLE_DOCTOR(Configuration.USER_ROLE_DOCTOR);


    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static Role getRoleValue(String roleParameter) {
        for (Role role : Role.values()) {
            if (roleParameter.equals(role.getRoleName()))
                return role;
        }
        return ROLE_NA;
    }
}