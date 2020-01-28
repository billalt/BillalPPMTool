package io.billal.ppmtool.exceptions;

public class UsernameExistExceptionResponse {

    private String usernameAlreadyExist;

    public UsernameExistExceptionResponse(String userAlreadyExist) {
        this.usernameAlreadyExist = userAlreadyExist;
    }

    public String getUsernameAlreadyExist() {
        return usernameAlreadyExist;
    }

    public void setUsernameAlreadyExist(String usernameAlreadyExist) {
        this.usernameAlreadyExist = usernameAlreadyExist;
    }
}
