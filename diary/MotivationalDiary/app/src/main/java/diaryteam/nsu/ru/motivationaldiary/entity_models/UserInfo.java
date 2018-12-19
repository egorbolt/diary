package diaryteam.nsu.ru.motivationaldiary.entity_models;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String userId;
    private String name;
    private String email;
    private int hashPassword;

    public UserInfo(String userId, String name, String email, int hashPssword) {
        this.userId = userId;
        this.name = name;
        this.email =  email;
        this.hashPassword = hashPssword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(int hashPassword) {
        this.hashPassword = hashPassword;
    }
}
