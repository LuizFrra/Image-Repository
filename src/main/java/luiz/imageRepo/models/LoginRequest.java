package luiz.imageRepo.models;

public class LoginRequest {

    public String username;

    public String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest() {

    }
}
