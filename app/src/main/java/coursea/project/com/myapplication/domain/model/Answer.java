package coursea.project.com.myapplication.domain.model;

import java.util.List;

/**
 * Created by chidra on 12/23/17.
 */

public class Answer {

    private String correct;
    private String value;

    public Answer( String value, String correct) {
        this.value = value;
        this.correct = correct;
    }

    public boolean isCorrect() {
        return new Boolean(correct);
    }

    public String getCorrect() {
        return correct;
    }

    public String getValue() {
        return value;
    }
}
