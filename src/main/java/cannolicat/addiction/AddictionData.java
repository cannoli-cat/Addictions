package cannolicat.addiction;

import java.io.Serializable;
import java.util.Date;

public class AddictionData implements Serializable {
    private static final long serialVersionUID = 6529545798267757690L;
    private Date date;
    private Addictions key;

    public AddictionData(Addictions key) {
        date = new Date();
        this.key = key;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Addictions getKey() {
        return key;
    }
}
