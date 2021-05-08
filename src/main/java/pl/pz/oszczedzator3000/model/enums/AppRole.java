package pl.pz.oszczedzator3000.model.enums;

public enum AppRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String roleName;

    AppRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
