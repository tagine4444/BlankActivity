package coursea.project.com.myapplication;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import coursea.project.com.myapplication.application.services.TestService;
import coursea.project.com.myapplication.application.services.impl.TestImpl;
import coursea.project.com.myapplication.domain.model.QuestionWithAnswers;
import coursea.project.com.myapplication.domain.model.Test;

public class MainActivity extends AppCompatActivity implements DownloadCallback{

    public final static int MY_PERMISSIONS_WRITE_STORAGE_CODE = 99;
    String[] WRITE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    TestService testService = new TestImpl();
    coursea.project.com.myapplication.domain.services.TestService mTestService = new coursea.project.com.myapplication.domain.services.TestService();
    Test test;
    QuestionWithAnswers currentQuestionWithAnswers = null;

    QuestionFragment questionFrag ;
    ResponseFragment responseFrag ;

    Button mPrevious ;
    Button mNextButton ;

    String questionIndexDisplay = "";

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;
    public final static String FILE_NAME = "test_1.json";


    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    private String mTest;
    private Test mTest1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");
         // mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "http://10.0.2.2:8080/quizz/28");
          mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "http://192.168.1.2:8080/quizz/1");



        this.questionFrag =  new QuestionFragment();
        this.responseFrag =  new ResponseFragment();

        FragmentTransaction txn = getFragmentManager().beginTransaction();
        txn.add(R.id.questionFragContainer, questionFrag);
        txn.add(R.id.responseFragContainer, responseFrag);
        getSupportFragmentManager().executePendingTransactions();
        txn.commit();

        if(this.test==null || currentQuestionWithAnswers ==null ){

            boolean fileExists = mTestService.fileExists(FILE_NAME);

            storagePermissionRequest();
//            mNetworkFragment.startDownload();
            questionFrag.initialized(null, "");
            responseFrag.initialized(null);

        }


        this.mPrevious   = (Button) findViewById(R.id.previous_btn);
        this.mNextButton = (Button) findViewById(R.id.next_btn);



        if(mNextButton!= null){



            mNextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if(!test.hasReachedLastQuestion()) {
//                        mNetworkFragment.startDownload();
                        test.moveToNextQuestion();
                        QuestionWithAnswers currentQuestionWithAnswers = test.getCurrentQuestionWithAnswers();
                        final String newQuestion = currentQuestionWithAnswers.getQuestion();
                        questionFrag.questionTextView.setText(test.getQuestionCounterMsg() + newQuestion);

                        responseFrag.cb1.setText(currentQuestionWithAnswers.getAnswer1());
                        responseFrag.cb2.setText(currentQuestionWithAnswers.getAnswer2());
                        responseFrag.cb3.setText(currentQuestionWithAnswers.getAnswer3());
                        responseFrag.cb4.setText(currentQuestionWithAnswers.getAnswer4());
                    }else{
                        Toast.makeText(MainActivity.this, "C'etait la derniere question", Toast.LENGTH_SHORT).show();
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
                    }else{
                        Toast.makeText(MainActivity.this, "C'etait la premiere question", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void storagePermissionRequest() {



        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        WRITE_PERMISSIONS,
                        MY_PERMISSIONS_WRITE_STORAGE_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mNetworkFragment.startDownload();

                } else {

                    System.out.println("pemission denied");

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Show an explanation to the user *asynchronously*

                        ActivityCompat.requestPermissions(MainActivity.this,
                                WRITE_PERMISSIONS,
                                MY_PERMISSIONS_WRITE_STORAGE_CODE);

                    }else{
                        //Never ask again and handle your app without permission.
                    }

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                System.out.println("OK pemission denied");
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



    @Override
    public void updateFromDownload(Object result, Object result1) {


         this.mTest = (String) result;
//         this.mTest1 = (Test) result1;
         this.test = (Test) result1;
         this.currentQuestionWithAnswers = test.getCurrentQuestionWithAnswers();


        if(this.test==null ){
            this.test = testService.getTest();
            currentQuestionWithAnswers = test.getCurrentQuestionWithAnswers();
        }

        questionFrag.initialized(currentQuestionWithAnswers, test.getQuestionCounterMsg());
        responseFrag.initialized(currentQuestionWithAnswers);

        final String newQuestion = currentQuestionWithAnswers.getQuestion();
        questionFrag.questionTextView.setText(test.getQuestionCounterMsg() + newQuestion);

        responseFrag.cb1.setText(currentQuestionWithAnswers.getAnswer1());
        responseFrag.cb2.setText(currentQuestionWithAnswers.getAnswer2());
        responseFrag.cb3.setText(currentQuestionWithAnswers.getAnswer3());
        responseFrag.cb4.setText(currentQuestionWithAnswers.getAnswer4());

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                System.out.println(">>>>>>>>>>>>>>>>>>>>> progress download ERROR" );
                break;
            case Progress.CONNECT_SUCCESS:
                System.out.println(">>>>>>>>>>>>>>>>>>>>> progress download CONNECT_SUCCESS" );
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                System.out.println(">>>>>>>>>>>>>>>>>>>>> progress download GET_INPUT_STREAM_SUCCESS" );
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                System.out.println(">>>>>>>>>>>>>>>>>>>>> progress download PROCESS_INPUT_STREAM_IN_PROGRESS" );
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                System.out.println(">>>>>>>>>>>>>>>>>>>>> progress download PROCESS_INPUT_STREAM_SUCCESS" );
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }



}
