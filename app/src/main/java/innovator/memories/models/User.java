package innovator.memories.models;

/**
 * Created by SachendraSingh on 2/11/16.
 */
public class User {

    private String userName;
    private String userEmail;
    private String userProfileURL;

    private boolean selected;

    public User(String userName, String userEmail, String userProfileURL){
        this.userName = userName;
        this.userEmail = userEmail;
        this.userProfileURL = userProfileURL;
    }

    public String getUserProfileURL() {
        return userProfileURL;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setIsSelected(boolean isSelecte) {
        this.selected = isSelecte;
    }
}
