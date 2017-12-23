package coursea.project.com.myapplication;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuestionFragment questionFrag =  new QuestionFragment();
        ResponseFragment responseFrag =  new ResponseFragment();


        FragmentTransaction txn = getFragmentManager().beginTransaction();
        txn.add(R.id.questionFragContainer, questionFrag);
        txn.add(R.id.responseFragContainer, responseFrag);
        txn.commit();
    }
}
