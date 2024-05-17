package cannolicat.addiction.addict;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class AddictionData implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529545798267757690L;
    private Date date;
    private final Addictions addiction;

    public AddictionData(Addictions addiction) {
        date = new Date();
        this.addiction = addiction;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Addictions getAddiction() {
        return addiction;
    }
}
