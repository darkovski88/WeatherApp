package darko.com.weatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Petkovski on 01.02.2016.
 */
public class MultipleCities {
    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private List<Response> list = new ArrayList<Response>();

    /**
     *
     * @return
     * The cnt
     */
    public Integer getCnt() {
        return cnt;
    }

    /**
     *
     * @param cnt
     * The cnt
     */
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    /**
     *
     * @return
     * The list
     */
    public List<Response> getCities() {
        return list;
    }

    /**
     *
     * @param list
     * The list
     */
    public void setCities(List<Response> list) {
        this.list = list;
    }

}
