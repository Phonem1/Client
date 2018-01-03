package com.anewtech.phone.client.QuizActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.anewtech.phone.client.QuizActivity.Fragments.AnswersFragment;
import com.anewtech.phone.client.QuizActivity.Fragments.CommonEvent;
import com.anewtech.phone.client.QuizActivity.Fragments.CustomActivityMessageEvent;
import com.anewtech.phone.client.QuizActivity.Fragments.FragmentToActivityInterface;
import com.anewtech.phone.client.QuizActivity.Fragments.QuestionFragment;
import com.anewtech.phone.client.QuizActivity.Models.reports.SurveyAnswerReport;
import com.anewtech.phone.client.QuizActivity.Models.services.Answernaire;
import com.anewtech.phone.client.QuizActivity.Models.services.Finaire;
import com.anewtech.phone.client.QuizActivity.Models.services.Questionaire;
import com.anewtech.phone.client.QuizActivity.Services.SpeechService;
import com.anewtech.phone.client.QuizActivity.Services.SurveyBervice;
import com.anewtech.phone.client.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.beans.speech.Grammar;
import com.qihancloud.opensdk.function.unit.SpeechManager;
import com.qihancloud.opensdk.function.unit.interfaces.speech.RecognizeListener;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static org.greenrobot.eventbus.ThreadMode.MAIN;

/**
 * Created by heriz on 26/12/2017.
 */

public class FragmentLayout extends TopBaseActivity implements FragmentToActivityInterface, QuestionFragment.OnQuestionPass{

    private final static boolean LOG_ON_ACTIVITY_LIFECYCLE = true;

    private QuestionFragment questionFragment;
    private AnswersFragment answersFragment;
    private ArrayList<Finaire> thankyouguys = new ArrayList<>();

    private ArrayList<String> answ;
    private ArrayList<String> answRef;
    private ArrayList<String> isAnsCorrect;
    private Questionaire mquest;
    private SendDataToFragment sdf;

    private SurveyBervice brain;
    private SpeechManager sm;
    private int score, totalScore;
    private boolean isNextQn, reading, nextLayer = false;

    private EventBus myevent = EventBus.getDefault();

    private LovelyStandardDialog Thanks;
    private CountDownTimer timer;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private Intent serviceIntent;
    public SpeechService speechService;

    private String filename;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SpeechService.MyBinder binder = (SpeechService.MyBinder) service;
            speechService = binder.getService();
//            Log.e("xxx", "[MainActivity]: onServiceConnected()");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            speechService    = null;
//            Log.e("xxx", "[MainActivity]: onServiceDisconnected()");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_layout);

        // Get filename for app
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("asset") != null){
            filename = bundle.getString("asset");
        }

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sm = (SpeechManager)getUnitManager(FuncConstant.SPEECH_MANAGER);

        serviceIntent = new Intent(this, SpeechService.class);
        serviceIntent.setPackage(getPackageName());
        // To call onServiceConnected() if the service already started
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        if(savedInstanceState == null){
            questionFragment = QuestionFragment.newInstance("question");
            answersFragment = AnswersFragment.newInstance("answers");
        }

        addFragments();
        Logger.addLogAdapter(new AndroidLogAdapter());

        this.brain = new SurveyBervice();
        this.brain.loadJsonData(loadJsonFromAsset());
        this.brain.doFirst();
        thankyouguys = this.brain.doThankYou();

        Thanks = new LovelyStandardDialog(this);

        timer = new CountDownTimer(40000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                if(!brain.isFirstQuestion()){
                    brain.restartQuestion();
                    myevent.post(new CustomActivityMessageEvent(1));
                }else {
                    finish();
                }
                try{
                    timer.start();
                }catch(Exception ex){
                    // Log.e("Error", "Error: " + ex.toString());
                }
            }
        }.start();

        initSpeechListener();
        updateScore(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(serviceIntent);
        mUser = mAuth.getCurrentUser();
        if( mUser == null ) {
            mAuth.signInWithEmailAndPassword("surveyclientappdemo@anewtech.com.sg", "Abc123!@#")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if( task.isSuccessful() ){
                                mUser = mAuth.getCurrentUser();
                                //Toast.makeText(FragmentLayout.this, "Authenticated", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //Toast.makeText(FragmentLayout.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                                //store local.
                                //try again every second.
                                //update to firestore.
                            }
                        }
                    });
        }
        myevent.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        nextLayer = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null){
            timer.cancel();
        }
        nextLayer = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.signOut();
        myevent.unregister(this);
    }

    @Override
    public void onDestroy() {
        if (speechService != null) {
            unbindService(connection);
        }
        stopService(serviceIntent);
        super.onDestroy();
    }

    @Subscribe(threadMode = MAIN)
    public void onEvent(CommonEvent event) {
        Thread init = new Thread(getanswersfromFragment);
        init.start();
        totalScore = event.getTotalQuestions();
        mquest = event.getMessage();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.brain.currentQuestion();
    }

    @Override
    protected void onMainServiceConnected() {

    }

    protected void addFragments(){
        sdf = answersFragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.questions_holder, questionFragment);
        ft.add(R.id.answers_holder, answersFragment);
        ft.commit();
    }

    @Override
    public void OnItemPickedInFragment(int position, Answernaire answer) {
        isAnsCorrect = new ArrayList<>();
        //Get correct answer id
        Questionaire q = mquest;
        String queId = q.correctAId;
        isAnsCorrect.add(0,queId);

        //Get input answer id
        String inputAnsId;
        for(Answernaire a : mquest.answers){
            int seq = a.id;
            if(position == seq){
                inputAnsId = a.answerid;
                isAnsCorrect.add(1, inputAnsId);

                getObservable(isAnsCorrect)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getAnsObserver());
                break;
            }
        }
        //Toast.makeText(this, "FragmentLayout: " + "is Last question", Toast.LENGTH_SHORT).show();
        restartTimer();
        if(this.brain.isLastQuestion()) {
            speechService.speak = false;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String thxMsg = thankyouguys.get(doRandom(thankyouguys.size())).message;
                    String msg = thxMsg+"\nYou got "+getFinalScore()+" questions right!";
                    String htmlMsg = thxMsg+"\nYou got "+"<b>"+getFinalScore()+"</b>"+" questions right!";
                    speechService.doThankYou = true;
                    speechService.setThankYouMsg(msg);
                    Thanks.setTopColorRes(R.color.Mahogany)
                            .setIcon(R.drawable.ic_sentiment_very_satisfied_black_48dp)
                            .setTitle("Hey you!")
                            .setMessage(Html.fromHtml(htmlMsg))
                            .setPositiveButton("I sure did!", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            })
                            .show();
                }
            }, 200);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Thanks.dismiss();
                    finish();
                }
            }, 6000);

        }else{
            speechService.speak = true;
        }

        this.brain.nextQuestion();
        this.brain.currentQuestion();
    }

    private Observable<ArrayList<String>> getObservable(ArrayList<String> input){
        return Observable.just(input);
    }

    private Observer<ArrayList<String>> getAnsObserver(){
        return new Observer<ArrayList<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ArrayList<String> checkAnswer) {
                String correctID = checkAnswer.get(0);
                String inputID = checkAnswer.get(1);
                if(correctID.equals(inputID)){
                    updateScore(1);
                    toLog("Answer chosen correct! Score: "+getFinalScore());
                }else{
                    toLog("Answer chosen is wrong! Score: "+getFinalScore());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void updateScore(int value){
        switch (value){
            case 1:{
                score = score + 1;
                break;
            }
            case 0:{
                score = 0;
            }
        }
    }

    public String getFinalScore(){
        return score+" out of "+totalScore;
    }
    private void restartTimer() {
        timer.cancel();
        timer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (data != null ){
            if(requestCode == 2){
                if(data.getStringExtra("result").contains("normal")){
                    // Toast.makeText(this, data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            brain.nextQuestion();
                            brain.currentQuestion();
                            restartTimer();
                        }
                    });
                }
                if(data.getStringExtra("result").contains("finish")){
//                brain.restartQuestion();
                    finish();
                }
            }
        }

    }

    private int doRandom(int size) {
        if(size > 0){
            Random r = new Random();
            int low = 0;
            int high = size;
            return (r.nextInt(high - low) + low);
        }
        return 0;
    }

    private String loadJsonFromAsset() {
        String json = null;
        try{
            InputStream is = getAssets().open(filename+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String (buffer,"UTF-8");
        }catch (IOException ex) {
            this.toLog(ex.toString());
            return null;
        }
        return json;
    }

    private void toLog(String msg) {
        if(LOG_ON_ACTIVITY_LIFECYCLE){
            Logger.d("Quiz(fl) :" + msg);
        }
    }

    @Override
    public void onQuestionPass(String question) {
        sm.doSleep();
        sm.stopSpeak();
        if(!nextLayer && question != null){
            speechService.readQuestion(question);
        }
    }

    public void initSpeechListener() {
        sm.setOnSpeechListener(new RecognizeListener() {
            @Override
            public boolean onRecognizeResult(Grammar grammar) {
                String inputSpeech = wordFirstCap(grammar.getText());
                toLog("Voice input: "+inputSpeech);
                if(timer != null){
                    timer.cancel();
                }
                timer.start();
                sendFeedbackFromVoice(inputSpeech);
                sdf.passDataArrayList(answ, inputSpeech);
                return true;
            }

            @Override
            public void onRecognizeVolume(int i) {

            }
        });
    }

    public void sendFeedbackFromVoice(String inputSpeech) {
        // Report: iterate through answers if found match send to db
        int i=0;
        for(i=0;i<answ.size();i++){
            if(answ.get(i).contains(inputSpeech)){
                toLog("Report: {\n QnId:"+brain.returnQustionId()+"\n Qn:"+brain.returnQuestion()+"\n AnsId:"+brain.returnAnswer().get(i).answerid+"\n Ans:"+brain.returnAnswer().get(i).answer+" }");
                SurveyAnswerReport local = new SurveyAnswerReport(brain.returnQustionId(),brain.returnQuestion(),brain.returnAnswer().get(i).answerid,brain.returnAnswer().get(i).answer);
                brain.doFeedback(local);
            }
        }
    }

    private Runnable getanswersfromFragment = new Runnable() {
        @Override
        public void run() {

            if(answRef != answ && reading){ //
                isNextQn = true;
                answRef = answ;
                speechService.setNextQuestion(isNextQn);
            }
//            Log.e("xxx", "[FragmentLayout]: check if reading answers...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Answernaire> myanswers = answersFragment.getanswersFromFragment();
            answ = new ArrayList<>();
            for(Answernaire mytext : myanswers){
//                Toast.makeText(this, mytext.answer, Toast.LENGTH_SHORT).show();
                answ.add(mytext.answer)  ;
            }
//            Log.e("xxx", "[FragmentLayout] nextLayer: "+nextLayer);
            if(!nextLayer){
                speechService.setAnsList(answ);
            }
//            Log.e("xxx", "[FragmentLayout]: getAnswersFromFragment");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public String wordFirstCap (String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public interface SendDataToFragment {
        void passDataArrayList(ArrayList<String> list, String inputSpeech);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            boolean isReading = b.getBoolean("isItReading");
            reading = isReading;
            Log.e("xxx", "[FragmentLayout] Reading: "+isReading);
        }
    }

    public void passBoolean(boolean passBoolean){
//        Log.e("xxx", "[FragmentLayout] PassBoolean: "+passBoolean);
        this.nextLayer = passBoolean;
//        Log.e("xxx", "[FragmentLayout] PassedNextLayer: "+nextLayer);
    }

}

