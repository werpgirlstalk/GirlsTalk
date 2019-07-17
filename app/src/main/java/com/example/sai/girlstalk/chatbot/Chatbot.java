package com.example.sai.girlstalk.chatbot;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sai.GirlsTalk.R;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;

import java.io.InputStream;
import java.util.ArrayList;


public class Chatbot extends AppCompatActivity {


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int RECORD_REQUEST_CODE = 101;
    private static String TAG = "MainActivity";
    //private Map<String,Object> context = new HashMap<>();
    com.ibm.watson.developer_cloud.assistant.v1.model.Context context = null;
    StreamPlayer streamPlayer;
    //
    android.speech.tts.TextToSpeech mTTS;
    SpeechRecognizer speechRecognizer;
    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;
    private ArrayList messageArrayList;
    private EditText inputMessage;
    private ImageButton btnSend;
    private boolean initialRequest;
    private boolean permissionToRecordAccepted = false;
    private boolean listening = false;
    private SpeechToText speechService;
    private MicrophoneInputStream capture;
    private SpeakerLabelsDiarization.RecoTokens recoTokens;
    private MicrophoneHelper microphoneHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat_room);

        inputMessage = findViewById(R.id.message);
        btnSend = findViewById(R.id.btn_send);
        String customFont = "Montserrat-Regular.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        inputMessage.setTypeface(typeface);
        recyclerView = findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();
        mAdapter = new ChatAdapter(messageArrayList);
        microphoneHelper = new MicrophoneHelper(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        this.inputMessage.setText("");
        this.initialRequest = true;
        sendMessage();

        //Watson Text-to-Speech Service on IBM Cloud
        final TextToSpeech textService = new TextToSpeech();
        //Use "apikey" as username and apikey values as password
        textService.setUsernameAndPassword("apikey", "4JYsCtSv0zuJriTcx4bGShcBfBj4Zxx-qclsgJrTy0t");
        textService.setEndPoint("https://gateway-lon.watsonplatform.net/assistant/api");


        btnSend.setOnClickListener(v -> {
            if (checkInternetConnection()) {
                sendMessage();
            }
        });


    }


    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                MicrophoneHelper.REQUEST_PERMISSION);
    }


    // Sending a message to Watson Conversation Service
    private void sendMessage() {

        final String inputmessage = this.inputMessage.getText().toString().trim();
        if (!this.initialRequest) {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
        } else {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("100");
            this.initialRequest = false;
        }

        this.inputMessage.setText("");
        mAdapter.notifyDataSetChanged();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {

                    Assistant assistantservice = new Assistant("2018-02-16");
                    //If you like to use USERNAME AND PASSWORD
                    //Your Username: "apikey", password: "<APIKEY_VALUE>"
                    assistantservice.setUsernameAndPassword("apikey", "4JYsCtSv0zuJriTcx4bGShcBfBj4Zxx-qclsgJrTy0tX");

                    //TODO: Uncomment this line if you want to use API KEY
                    //assistantservice.setApiKey("<API_KEY_VALUE>");

                    //Set endpoint which is the URL. Default value: https://gateway.watsonplatform.net/assistant/api
                    assistantservice.setEndPoint("https://gateway-lon.watsonplatform.net/assistant/api");
                    InputData input = new InputData.Builder(inputmessage).build();
                    //WORKSPACES are now SKILLS
                    MessageOptions options = new MessageOptions.Builder().workspaceId("1a89af48-9456-494a-8abd-1ca529fa0601").input(input).context(context).build();
                    MessageResponse response = assistantservice.message(options).execute();
                    Log.i(TAG, "run: " + response);

                    String outputText = "";
                    int length = response.getOutput().getText().size();
                    Log.i(TAG, "run: " + length);
                    if (length > 1) {
                        for (int i = 0; i < length; i++) {
                            outputText += '\n' + response.getOutput().getText().get(i).trim();
                        }
                    } else
                        outputText = response.getOutput().getText().get(0);

                    Log.i(TAG, "run: " + outputText);
                    //Passing Context of last conversation
                    if (response.getContext() != null) {
                        //context.clear();
                        context = response.getContext();

                    }
                    Message outMessage = new Message();
                    if (response != null) {
                        if (response.getOutput() != null && response.getOutput().containsKey("text")) {
                            ArrayList responseList = (ArrayList) response.getOutput().get("text");
                            if (null != responseList && responseList.size() > 0) {
                                outMessage.setMessage(outputText);
                                outMessage.setId("2");
                            }
                            messageArrayList.add(outMessage);
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                if (mAdapter.getItemCount() > 1) {
                                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);

                                }

                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }


    /**
     * Check Internet Connection
     *
     * @return
     */
    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Check for network connections
        if (isConnected) {
            return true;
        } else {
            Toast.makeText(this, " No Internet Connection available ", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    //Private Methods - Speech to Text
    private RecognizeOptions getRecognizeOptions(InputStream audio) {
        return new RecognizeOptions.Builder()
                .audio(audio)
                .contentType(ContentType.OPUS.toString())
                .model("en-US_BroadbandModel")
                .interimResults(true)
                .inactivityTimeout(2000)
                //TODO: Uncomment this to enable Speaker Diarization
                //.speakerLabels(true)
                .build();
    }


}

