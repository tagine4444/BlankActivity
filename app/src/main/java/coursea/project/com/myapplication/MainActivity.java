package coursea.project.com.myapplication;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import coursea.project.com.myapplication.application.services.TestService;
import coursea.project.com.myapplication.application.services.impl.TestImpl;
import coursea.project.com.myapplication.domain.model.QuestionWithAnswers;
import coursea.project.com.myapplication.domain.model.Test;

public class MainActivity extends AppCompatActivity {

    TestService testService = new TestImpl();
    Test test;
    QuestionWithAnswers currentQuestionWithAnswers = null;

    QuestionFragment questionFrag ;
    ResponseFragment responseFrag ;

    Button mPrevious ;
    Button mNextButton ;

    String questionIndexDisplay = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.questionFrag =  new QuestionFragment();
        this.responseFrag =  new ResponseFragment();

        FragmentTransaction txn = getFragmentManager().beginTransaction();
        txn.add(R.id.questionFragContainer, questionFrag);
        txn.add(R.id.responseFragContainer, responseFrag);
        getSupportFragmentManager().executePendingTransactions();
        txn.commit();

        if(this.test==null || currentQuestionWithAnswers ==null ){
            this.test = testService.getTest();
            currentQuestionWithAnswers = test.getCurrentQuestionWithAnswers();
            questionFrag.initialized(currentQuestionWithAnswers, test.getQuestionCounterMsg());
            responseFrag.initialized(currentQuestionWithAnswers);
        }


        this.mPrevious   = (Button) findViewById(R.id.previous_btn);
        this.mNextButton = (Button) findViewById(R.id.next_btn);


        if(mNextButton!= null){
            mNextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if(!test.hasReachedLastQuestion()) {

                        test.moveToNextQuestion();
                        QuestionWithAnswers currentQuestionWithAnswers = test.getCurrentQuestionWithAnswers();
                        final String newQuestion = currentQuestionWithAnswers.getQuestion();
                        questionFrag.questionTextView.setText(test.getQuestionCounterMsg() + newQuestion);

                        responseFrag.cb1.setText(currentQuestionWithAnswers.getAnswer1());
                        responseFrag.cb2.setText(currentQuestionWithAnswers.getAnswer2());
                        responseFrag.cb3.setText(currentQuestionWithAnswers.getAnswer3());
                        responseFrag.cb4.setText(currentQuestionWithAnswers.getAnswer4());
                    }

                }
            });
        }

        if(mPrevious!=null){
            mPrevious.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    if(!test.hasReachedFirstQuestion()){

                        test.moveToPreviousQuestion();
                        QuestionWithAnswers currentQuestionWithAnswers = test.getCurrentQuestionWithAnswers();
                        final String newQuestion = currentQuestionWithAnswers.getQuestion();
                        questionFrag.questionTextView.setText(test.getQuestionCounterMsg() + newQuestion);

                        responseFrag.cb1.setText(currentQuestionWithAnswers.getAnswer1());
                        responseFrag.cb2.setText(currentQuestionWithAnswers.getAnswer2());
                        responseFrag.cb3.setText(currentQuestionWithAnswers.getAnswer3());
                        responseFrag.cb4.setText(currentQuestionWithAnswers.getAnswer4());
                    }
                }
            });
        }



    }
}
