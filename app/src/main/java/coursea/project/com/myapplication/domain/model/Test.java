package coursea.project.com.myapplication.domain.model;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chidra on 12/23/17.
 */

public class Test {

//    public final static String TEST_QUESTION="TEST_QUESTION";
//    public final static String TEST_ANSWERS="TEST_ANSWERS";

    private String name;
    private String description;
    List<QuestionWithAnswers> questionWithAnswers;
    int questionIndex = 0;
    int maxQuestions = 0;

    private int timePerQuestion;

    public Test(String name, String description,  int timePerQuestion) {
        this.name = name;
        this.description = description;
        this.timePerQuestion = timePerQuestion;
        this.maxQuestions = questionWithAnswers==null?0:questionWithAnswers.size()-1;
    }

    public Test(String name, String description, List<QuestionWithAnswers> questionWithAnswers, int timePerQuestion) {
        this.name = name;
        this.description = description;
        this.questionWithAnswers = questionWithAnswers;
        this.timePerQuestion = timePerQuestion;
        this.maxQuestions = questionWithAnswers.size()-1;
    }

    public boolean hasReachedLastQuestion(){
        if( this.questionIndex == this.maxQuestions ){
            return true;
        }
        return false;
    }

    public boolean hasReachedFirstQuestion(){
        if( this.questionIndex == 0 ){
            return true;
        }
        return false;
    }

    public void moveToNextQuestion(){
        if( this.questionIndex <= this.maxQuestions){
            this.questionIndex++;
        }
    }

    public void moveToPreviousQuestion(){
        if( this.questionIndex > 0){
            this.questionIndex--;
        }
    }

    public String getQuestionCounterMsg(){
        return ( this.questionIndex +1) +"/" + (this.maxQuestions +1) +": ";

    }

    public QuestionWithAnswers getCurrentQuestionWithAnswers(){
        return this.getQuestionWithAnswers().get(this.questionIndex);
    }

    public List<QuestionWithAnswers> getQuestionWithAnswers() {
        return questionWithAnswers;
    }

    public int getTimePerQuestion() {
        return timePerQuestion;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
