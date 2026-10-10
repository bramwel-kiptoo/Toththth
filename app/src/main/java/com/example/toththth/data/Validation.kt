package com.example.toththth.data

object Validation {
    fun validateEmail(email: String): Possibility{
        if (email.trim().isBlank()){
            return Possibility(
                isValid = false,
                errorMessage = "Email is Empty"
            )
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return Possibility(
                isValid = false,
                errorMessage = "Incorrect email format"
            )
        } else {
            return Possibility(isValid = true)
        }
    }
    fun validatePassword(password: String): Possibility{
        if (password.isBlank()){
            return Possibility(
                isValid = false,
                errorMessage = "password is empty"
            )
        } else {
            return Possibility(isValid = true)
        }
    }
    fun confirmPasswordValidation(password: String, confirmPassword: String): Possibility {
        if (password != confirmPassword){
            return Possibility(
                isValid = false,
                errorMessage = "Password don't match"
            )
        }
        if (confirmPassword.isEmpty()){
            return Possibility(
                isValid = false
            )
        } else {
            return Possibility (isValid = true     )
        }
    }
}
data class Possibility(
    val isValid: Boolean,
    val errorMessage: String ? = null
)