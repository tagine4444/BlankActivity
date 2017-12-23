package coursea.project.com.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.TextView;

/**
 * Created by chidra on 12/22/17.
 */

public class QuestionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question, container, false);

        TextView question = (TextView) view.findViewById(R.id.text_quest_id);
        question.setText("When is Ramadan?");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
