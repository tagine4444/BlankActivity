package coursea.project.com.myapplication.domain.model;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

//import static coursea.project.com.myapplication.domain.model.Test.ANSWER_KEY;

/**
 * Created by chidra on 12/23/17.
 */

public class QuestionWithAnswers {

    private String question;
    private ArrayList<Answer> answers;


    public QuestionWithAnswers(String question, ArrayList<Answer> answers) {
        this.answers = answers;
        this.question = question;
    }


    public String getAnswer1(){
        return this.answers.get(0).getValue();
    }
    public String getAnswer2(){
        return this.answers.get(1).getValue();
    }
    public String getAnswer3(){
        return this.answers.get(2).getValue();
    }
    public String getAnswer4(){
        return this.answers.get(3).getValue();
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getQuestion() {
        return question;
    }

}
