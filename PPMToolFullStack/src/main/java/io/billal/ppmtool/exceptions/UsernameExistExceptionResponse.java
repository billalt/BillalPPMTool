package io.billal.ppmtool.exceptions;

public class UserExistExceptionResponse {

    private String userAlreadyExist;

    public UserExistExceptionResponse(String userAlreadyExist) {
        this.userAlreadyExist = userAlreadyExist;
    }

    public String getUserAlreadyExist() {
        return userAlreadyExist;
    }

    public void setUserAlreadyExist(String userAlreadyExist) {
        this.userAlreadyExist = userAlreadyExist;
    }
}
