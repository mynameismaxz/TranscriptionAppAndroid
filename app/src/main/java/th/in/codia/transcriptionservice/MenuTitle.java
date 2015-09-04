package th.in.codia.transcriptionservice;

/**
 * Created by mynameismaxz on 7/15/15 AD.
 */
public class MenuTitle {
    private String Title;
    private String User;
    private String StartT;
    private String StopT;
    private String Date;

    public MenuTitle(String T,String U,String SaT,String SoT,String D){
        this.Title = T;
        this.User = U;
        this.StartT = SaT;
        this.StopT = SoT;
        this.Date = D;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getStartT() {
        return StartT;
    }

    public void setStartT(String startT) {
        StartT = startT;
    }

    public String getStopT() {
        return StopT;
    }

    public void setStopT(String stopT) {
        StopT = stopT;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
