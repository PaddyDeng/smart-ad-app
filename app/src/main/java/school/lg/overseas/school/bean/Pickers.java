package school.lg.overseas.school.bean;
import java.io.Serializable;

/**
 *
 *
 */
public class Pickers implements Serializable {

    private static final long serialVersionUID = 1L;

    private String showConetnt;
    private int position;

    public String getShowConetnt() {
        return showConetnt;
    }


    public int getPosition() {
        return position;
    }

    public Pickers(int position,String showConetnt) {
        super();
        this.position=position;
        this.showConetnt = showConetnt;
    }

    public Pickers() {
        super();
    }

}
