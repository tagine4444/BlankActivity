package coursea.project.com.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import coursea.project.com.myapplication.domain.model.QuestionWithAnswers;

/**
 * Created by chidra on 12/22/17.
 */

public class QuestionFragment extends Fragment {

    TextView questionTextView;
    QuestionWithAnswers initialQuestionWithAnswers;
    String initialCounter ="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question, container, false);
        this.questionTextView = (TextView) view.findViewById(R.id.text_quest_id);
        this.questionTextView.setText(initialQuestionWithAnswers.getQuestion());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initialized(QuestionWithAnswers initialQuestionWithAnswers, String initialCounter){
        if(this.initialQuestionWithAnswers ==null){
            this.initialQuestionWithAnswers = initialQuestionWithAnswers;
            this.initialCounter = initialCounter;
        }
    }
}
