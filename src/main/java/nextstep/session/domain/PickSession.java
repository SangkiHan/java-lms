package nextstep.session.domain;

public enum PickSession {
    PICK,
    NON_PICK;

    public boolean checkPickSession() {
        return this.equals(PICK);
    }
}
