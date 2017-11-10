package mobop.skicompass;

/**
 * Created by Martin on 06.11.2017.
 */

public enum SortPriority {
    LOCATION(0),
    OPERATING(1),
    WEATHER(2),
    OPENED_LIFTS(3),
    OPENED_SLOPS(4);

    private int value;

    SortPriority(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
