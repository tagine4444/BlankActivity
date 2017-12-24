package coursea.project.com.myapplication.infrastructure.mock;

import java.util.ArrayList;
import java.util.List;

import coursea.project.com.myapplication.domain.model.Answer;
import coursea.project.com.myapplication.domain.model.QuestionWithAnswers;

/**
 * Created by chidra on 12/23/17.
 */

public class QuestionWithAnswersFactory {

    public static QuestionWithAnswers createQuestionWithAnswers(String aQuestion, String[]... answers){

        ArrayList<Answer> result = new ArrayList<Answer>(answers.length+1);

        for (String[] anAswer: answers){
            Answer newAnswer = new Answer(anAswer[0], anAswer[1]);
            result.add(newAnswer);
        }


        QuestionWithAnswers questionWithAnswers = new QuestionWithAnswers(aQuestion, result);

        return questionWithAnswers;

    }

}
