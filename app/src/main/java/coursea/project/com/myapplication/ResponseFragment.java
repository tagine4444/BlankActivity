package coursea.project.com.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import coursea.project.com.myapplication.domain.model.Answer;
import coursea.project.com.myapplication.domain.model.QuestionWithAnswers;


/**
 * Created by chidra on 12/22/17.
 */

public class ResponseFragment extends Fragment {

    ArrayList<Answer> answers;
    private QuestionWithAnswers initialQuestionWithAnswers = null;

    CheckBox cb1 ;
    CheckBox cb2 ;
    CheckBox cb3 ;
    CheckBox cb4 ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_response, container, false);

        this.cb1 = (CheckBox)view.findViewById(R.id.response1);
        this.cb2 = (CheckBox)view.findViewById(R.id.response2);
        this.cb3 = (CheckBox)view.findViewById(R.id.response3);
        this.cb4 = (CheckBox)view.findViewById(R.id.response4);

        if(initialQuestionWithAnswers!=null){
            this.cb1.setText(initialQuestionWithAnswers.getAnswer1());
            this.cb2.setText(initialQuestionWithAnswers.getAnswer2());
            this.cb3.setText(initialQuestionWithAnswers.getAnswer3());
            this.cb4.setText(initialQuestionWithAnswers.getAnswer4());
        }




        return  view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initialized(QuestionWithAnswers initialQuestionWithAnswers) {
        if(this.initialQuestionWithAnswers==null){
            this.initialQuestionWithAnswers = initialQuestionWithAnswers;
        }
    }
}
