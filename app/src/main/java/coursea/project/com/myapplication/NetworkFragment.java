package coursea.project.com.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import coursea.project.com.myapplication.domain.model.Answer;
import coursea.project.com.myapplication.domain.model.QuestionWithAnswers;
import coursea.project.com.myapplication.domain.model.Test;
import coursea.project.com.myapplication.domain.services.TestService;


public class NetworkFragment extends Fragment {


    public static final String TAG = "NetworkFragment";

    private static final String URL_KEY = "UrlKey";

    private DownloadCallback mCallback;
    private DownloadTask mDownloadTask;
    private String mUrlString;



    public NetworkFragment() {
        // Required empty public constructor
    }

    public static NetworkFragment getInstance(FragmentManager fragmentManager, String url) {
        // Recover NetworkFragment in case we are re-creating the Activity due to a config change.
        // This is necessary because NetworkFragment might have a task that began running before
        // the config change occurred and has not finished yet.
        // The NetworkFragment is recoverable because it calls setRetainInstance(true).
        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(NetworkFragment.TAG);
        if (networkFragment == null) {
            networkFragment = new NetworkFragment();
            Bundle args = new Bundle();
            args.putString(URL_KEY, url);
            networkFragment.setArguments(args);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }
        return networkFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this Fragment across configuration changes in the host Activity.
        setRetainInstance(true);
        mUrlString = getArguments().getString(URL_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        mCallback = (DownloadCallback) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        mCallback = null;

    }

    /**
     * Start non-blocking execution of DownloadTask.
     */
    public void startDownload() {
        cancelDownload();
        mDownloadTask = new DownloadTask(mCallback);
        mDownloadTask.execute(mUrlString);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload() {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
        }
    }


    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class DownloadTask extends AsyncTask<String, Integer, DownloadTask.Result> {
        TestService testService = new TestService();

        private DownloadCallback<String,Test> mCallback;

        DownloadTask(DownloadCallback<String,Test> callback) {
            setCallback(callback);
        }

        void setCallback(DownloadCallback<String,Test> callback) {
            mCallback = callback;
        }

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the download
         * task has completed, either the result value or exception can be a non-null value.
         * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
         */
         class Result {
            public String mResultValue;
            public Test mResultTestValue;
            public Exception mException;

            public Result(Test test) {
                mResultTestValue = test;
            }
            public Result(String resultValue) {
                mResultValue = resultValue;
            }
            public Result(Exception exception) {
                mException = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (mCallback != null) {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    mCallback.updateFromDownload(null,null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected DownloadTask.Result doInBackground(String... urls) {
            Result result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    if(testService.fileExists(MainActivity.FILE_NAME)){
                        InputStream stream = testService.openFile(MainActivity.FILE_NAME);
                        Test resultString =  getTest(stream);
                        result = new Result(resultString);
                    }else {
                        Test resultString = downloadUrl(url);
                        if (resultString != null) {
                            result = new Result(resultString);
                        } else {
                            throw new IOException("No response received.");
                        }
                    }
                } catch(Exception e) {
                    result = new Result(e);
                    e.printStackTrace();
                }
            }
            return result;
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null && mCallback != null) {
                if (result.mException != null) {
                    mCallback.updateFromDownload(result.mException.getMessage(), null);
                } else if (result.mResultTestValue != null) {
                    mCallback.updateFromDownload(result.mResultValue, result.mResultTestValue);
                }
                mCallback.finishDownloading();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {
        }


        public List<QuestionWithAnswers> readQuestionWithAnswersArray(JsonReader reader) throws IOException {
            List<QuestionWithAnswers> questionWithAnswers = new ArrayList<QuestionWithAnswers>();

            reader.beginArray();
            while (reader.hasNext()) {
                questionWithAnswers.add(readQuestionWithAnswer(reader));
            }
            reader.endArray();
            return questionWithAnswers;
        }

        public QuestionWithAnswers readQuestionWithAnswer(JsonReader reader) throws IOException {


            String question = null;
            ArrayList<Answer> responses = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("question")) {
                    question = reader.nextString();
                }  else if (name.equals("responses")) {
                    responses = readResponsesArray(reader);
                } else {
                    reader.skipValue();
                }
            }
            QuestionWithAnswers questionWithAnswers = new QuestionWithAnswers(question,responses);
            reader.endObject();
            return questionWithAnswers;
        }

        private ArrayList<Answer> readResponsesArray(JsonReader reader) throws IOException {
            ArrayList<Answer> responses = new ArrayList<Answer>();

            reader.beginArray();
            while (reader.hasNext()) {
                responses.add(readResponses(reader));
            }
            reader.endArray();

            return responses;
        }

        private Answer readResponses(JsonReader reader) throws IOException{


            String value =null;
            boolean correct = false;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("value")) {
                    value = reader.nextString();
                }  else if (name.equals("correct")) {
                    correct = reader.nextBoolean();
                } else {
                    reader.skipValue();
                }
            }

            Answer response = new Answer(value,String.valueOf(correct));
            reader.endObject();

            return response;
        }


        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */
        private Test downloadUrl(URL url) throws IOException {
            InputStream httpStream = null;
            //InputStream stream = null;
            HttpURLConnection connection = null;
            //String result = null;
            //Test test = null;
            JsonReader jsonReader = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(9000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(10000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);



                String basicAuth = "Basic " + new String(Base64.encode("user:pwd".getBytes(),Base64.NO_WRAP ));
                connection.setRequestProperty ("Authorization", basicAuth);

                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                httpStream = connection.getInputStream();
                publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);


                String name = null;
                String desc = null;
                List<QuestionWithAnswers>  questionWithAnswers =null;

                if (httpStream != null) {
                    // Converts Stream to String with max length of 500.

                    final String fileName = MainActivity.FILE_NAME;
                    testService.saveTestToDevice(httpStream,fileName);
//                    stream = testService.openFile(fileName);


//                    Reader  reader = new InputStreamReader(stream, "UTF-8");
//                    jsonReader = new JsonReader(reader);
//
//                    jsonReader.beginObject();
//
//                    while (jsonReader.hasNext()){
//                        String nextName = jsonReader.nextName();
//
//                        if(nextName.equals("name")){
//                            name = jsonReader.nextString();
//                        }else if(nextName.equals("description")){
//                            desc = jsonReader.nextString();
//                        }else if(nextName.equals("questions")){
//                            questionWithAnswers = readQuestionWithAnswersArray( jsonReader);
//                        }
//
//                        else {
//                            jsonReader.skipValue();
//                        }
//                    }
//
//                    jsonReader.endObject();
//
//
//                    test = new Test(name,desc, questionWithAnswers,1);



                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {

                if(jsonReader!=null){
                    jsonReader.close();
                }
                // Close Stream and disconnect HTTPS connection.
                if (httpStream != null) {
                    httpStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
//            return test;
            return  getTest(httpStream);
        }



        private Test getTest(InputStream stream) throws IOException {

            Reader  reader = new InputStreamReader(stream, "UTF-8");
            JsonReader jsonReader = new JsonReader(reader);

            jsonReader.beginObject();

            String name = null;
            String desc = null;
            List<QuestionWithAnswers>  questionWithAnswers =null;
            while (jsonReader.hasNext()){
                String nextName = jsonReader.nextName();

                if(nextName.equals("name")){
                    name = jsonReader.nextString();
                }else if(nextName.equals("description")){
                    desc = jsonReader.nextString();
                }else if(nextName.equals("questions")){
                    questionWithAnswers = readQuestionWithAnswersArray( jsonReader);
                }

                else {
                    jsonReader.skipValue();
                }
            }

            jsonReader.endObject();


            Test test = new Test(name,desc, questionWithAnswers,1);
            return test;
        }

        /**
         * Converts the contents of an InputStream to a String.
         */
        public String readStreamOLD(InputStream stream, int maxReadSize)
                throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] rawBuffer = new char[maxReadSize];
            int readSize;
            StringBuffer buffer = new StringBuffer();
            while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
                if (readSize > maxReadSize) {
                    readSize = maxReadSize;
                }
                buffer.append(rawBuffer, 0, readSize);
                maxReadSize -= readSize;
            }
            return buffer.toString();
        }

        public String readStream(InputStream stream, int maxReadSize)
                throws IOException, UnsupportedEncodingException {
            StringBuffer buffer = new StringBuffer();


            BufferedReader in = new BufferedReader(new InputStreamReader(
                    stream));
            String inputLine;
            while ((inputLine = in.readLine()) != null){

                buffer.append(inputLine);
            }

            in.close();

            return buffer.toString();
        }

    }




}
