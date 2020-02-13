package io.billal.ppmtool.validator;

import io.billal.ppmtool.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        User user = (User) object;

        if(user.getPassword() == null){
            user.setPassword("");
        }
        if(user.getValidationKey() == null){
            user.setValidationKey("");
        }
        if(user.getPassword().length() <6){
            if(user.getPassword().isEmpty())
                errors.rejectValue("password","Miss", "Password is required");
            else
                errors.rejectValue("password","Length", "Password must be at least 6 characters");
        }

        if(!user.getPassword().equals(user.getConfirmPassword())){
            if(user.getPassword().isEmpty())
                errors.rejectValue("confirmPassword","Miss", "Password is required");
            else
                errors.rejectValue("confirmPassword","Match", "Passwords must match");
        }

        if(!user.getValidationKey().equals("6zy5ht")){
            if(user.getValidationKey().isEmpty()){
                errors.rejectValue("validationKey","Miss", "Validation Key is required");
            }
            else{
                errors.rejectValue("validationKey","Match", "Wrong Validation Key, please ask Billal for new Validation Key");
            }
        }


    }
}
