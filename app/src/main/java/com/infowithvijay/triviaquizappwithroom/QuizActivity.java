package com.infowithvijay.triviaquizappwithroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonA, buttonB, buttonC, buttonD;
    TextView questionText, txtTotalQuestion;

    ImageView imgSkipabble, imgAudienceVote, imgTelePhone, imgFiftyFifty;

    ImageView imgHome;
    TextView txtSeeMoneyProgress;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

    private QuestionsViewModel questionsViewModelob;
    List<Questions> list;
    Questions currentQuestion;
    int qid = 1;
    int sizeOfQuiz = 2;

    Handler handler = new Handler();
    Handler handler2 = new Handler();

    AnimationDrawable anim;
    GameOverDialog gameOverDialog;
    GameWonDialog gameWonDialog;
    String correctAns = "";
    Animation shakeAnimation;
    Animation correctAnsAnimation;

    int MUSIC_FLAG = 0;
    PlaySound playSound;

    int FLAG = 0;
    String moneyWon = "";
    TextView txt[] = new TextView[16];
    TextView txtMoneyWon;
    Handler moneyHandler = new Handler();

    int RANGE = 0;
    int MAX_OUT = 0;

    Dialog dialog;
    Button btAudienceDialogOk;

    Dialog telePhoneDialog;
    Button bttelePhoneDialogOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.txtTriviaQuestion);
        txtTotalQuestion = findViewById(R.id.txtTotalQuestions);

        txtSeeMoneyProgress = findViewById(R.id.txtShowMoney);
        txtMoneyWon = findViewById(R.id.txtMoneyWonText);
        imgHome = findViewById(R.id.imgHome);

        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        mNavigationView = findViewById(R.id.navigation_view);


        buttonA = findViewById(R.id.buttonA);
        buttonB = findViewById(R.id.buttonB);
        buttonC = findViewById(R.id.buttonC);
        buttonD = findViewById(R.id.buttonD);

        imgAudienceVote = findViewById(R.id.img_audience_vote_life_line);
        imgFiftyFifty = findViewById(R.id.img_fifty_fifty_life_line);
        imgSkipabble = findViewById(R.id.img_skippable_life_line);
        imgTelePhone = findViewById(R.id.img_telephone_life_line);


        dialog = new Dialog(this);
        telePhoneDialog = new Dialog(this);


        setUpTextViewsofMoney();

        // setting the listener
        txtSeeMoneyProgress.setOnClickListener(this);
        imgHome.setOnClickListener(this);

        imgTelePhone.setOnClickListener(this);
        imgSkipabble.setOnClickListener(this);
        imgFiftyFifty.setOnClickListener(this);
        imgAudienceVote.setOnClickListener(this);

        txtTotalQuestion.setText(qid + "/" + sizeOfQuiz);

        gameOverDialog = new GameOverDialog(this);
        gameWonDialog = new GameWonDialog(this);

        playSound = new PlaySound(this);

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.incorrect_animation);
        shakeAnimation.setRepeatCount(3);

        correctAnsAnimation = AnimationUtils.loadAnimation(this, R.anim.right_ans_animation);
        correctAnsAnimation.setRepeatCount(3);


        questionsViewModelob = ViewModelProviders.of(this).get(QuestionsViewModel.class);
        questionsViewModelob.getAllQuestions().observe(this, new Observer<List<Questions>>() {
            @Override
            public void onChanged(List<Questions> questions) {

                fetchQuestions(questions);

            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.txtShowMoney: {

                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            }

            case R.id.imgHome: {

                Intent intent = new Intent(QuizActivity.this, PlayScreen.class);
                startActivity(intent);

                break;
            }

            case R.id.img_audience_vote_life_line: {

                audienceVoteSystem();

                break;
            }

            case R.id.img_telephone_life_line: {

                telephoneLifeLine();

                break;
            }

            case R.id.img_fifty_fifty_life_line: {

                fiftyfifty();

                break;
            }

            case R.id.img_skippable_life_line:{

                skippableLifeLine();

                break;
            }

        }

    }


    private void fetchQuestions(List<Questions> questions) {

        list = questions;

        Collections.shuffle(list);

        currentQuestion = list.get(qid);

        updateQueAnsOptions();


    }

    private void updateQueAnsOptions() {

        buttonA.setBackgroundResource(R.drawable.default_option_a);
        buttonB.setBackgroundResource(R.drawable.default_option_b);
        buttonC.setBackgroundResource(R.drawable.default_option_c);
        buttonD.setBackgroundResource(R.drawable.default_option_d);


        questionText.setText(currentQuestion.getQuestion());
        buttonA.setText(currentQuestion.getOptA());
        buttonB.setText(currentQuestion.getOptB());
        buttonC.setText(currentQuestion.getOptC());
        buttonD.setText(currentQuestion.getOptD());


    }

    private void SetNewQuestion() {

        qid++;

        txtTotalQuestion.setText(qid + "/" + sizeOfQuiz);

        currentQuestion = list.get(qid);

        enableButtons();

        displayButtons();

        updateQueAnsOptions();

    }


    public void buttonA(View view) {


        disableButtons();

        buttonA.setBackgroundResource(R.drawable.flash_animation_a);
        anim = (AnimationDrawable) buttonA.getBackground();
        anim.start();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (currentQuestion.getOptA().equals(currentQuestion.getAnswer())) {

                    buttonA.setBackgroundResource(R.drawable.when_correct_a);
                    buttonA.startAnimation(correctAnsAnimation);


                    MUSIC_FLAG = 1;
                    playSound.seAudioforAnswers(MUSIC_FLAG);

                    moneyWonByUser();
                    FLAG++;


                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (qid != sizeOfQuiz) {

                                mDrawerLayout.openDrawer(GravityCompat.END);
                                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                                    Handler justDely = new Handler();
                                    justDely.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            mDrawerLayout.closeDrawer(GravityCompat.END);

                                        }
                                    }, 3000);

                                }


                                SetNewQuestion();

                            } else {

                                gameWonDialog.GameWonDialog(moneyWon);

                            }

                        }
                    }, 3000);

                } else {

                    buttonA.setBackgroundResource(R.drawable.when_incorrect_a);
                    buttonA.startAnimation(shakeAnimation);

                    MUSIC_FLAG = 2;
                    playSound.seAudioforAnswers(MUSIC_FLAG);

                    Handler handler3 = new Handler();
                    handler3.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (currentQuestion.getOptB().equals(currentQuestion.getAnswer())) {
                                buttonB.setBackgroundResource(R.drawable.when_correct_b);
                            } else if (currentQuestion.getOptC().equals(currentQuestion.getAnswer())) {
                                buttonC.setBackgroundResource(R.drawable.when_correct_c);
                            } else {
                                buttonD.setBackgroundResource(R.drawable.when_correct_d);
                            }

                        }
                    }, 2000);


                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (qid != sizeOfQuiz) {

                                // Play scrren because ans is incorrect
                                correctAns = currentQuestion.getAnswer();
                                gameOverDialog.GameOverDialog(correctAns);

                            }

                        }
                    }, 3000);

                }


            }
        }, 5000);


    }

    public void buttonB(View view) {


        disableButtons();

        buttonB.setBackgroundResource(R.drawable.flash_animation_b);
        anim = (AnimationDrawable) buttonB.getBackground();
        anim.start();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (currentQuestion.getOptB().equals(currentQuestion.getAnswer())) {

                    buttonB.setBackgroundResource(R.drawable.when_correct_b);
                    buttonB.startAnimation(correctAnsAnimation);

                    MUSIC_FLAG = 1;
                    playSound.seAudioforAnswers(MUSIC_FLAG);

                    moneyWonByUser();
                    FLAG++;

                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (qid != sizeOfQuiz) {


                                mDrawerLayout.openDrawer(GravityCompat.END);
                                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                                    Handler justDely = new Handler();
                                    justDely.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            mDrawerLayout.closeDrawer(GravityCompat.END);

                                        }
                                    }, 3000);

                                }


                                SetNewQuestion();

                            } else {

                                gameWonDialog.GameWonDialog(moneyWon);

                            }

                        }
                    }, 3000);

                } else {

                    buttonB.setBackgroundResource(R.drawable.when_incorrect_b);
                    buttonB.startAnimation(shakeAnimation);

                    MUSIC_FLAG = 2;
                    playSound.seAudioforAnswers(MUSIC_FLAG);


                    Handler handler3 = new Handler();
                    handler3.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (currentQuestion.getOptA().equals(currentQuestion.getAnswer())) {
                                buttonA.setBackgroundResource(R.drawable.when_correct_a);
                            } else if (currentQuestion.getOptC().equals(currentQuestion.getAnswer())) {
                                buttonC.setBackgroundResource(R.drawable.when_correct_c);
                            } else {
                                buttonD.setBackgroundResource(R.drawable.when_correct_d);
                            }

                        }
                    }, 2000);




                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (qid != sizeOfQuiz) {
                                // Play scrren because ans is incorrect
                                correctAns = currentQuestion.getAnswer();
                                gameOverDialog.GameOverDialog(correctAns);
                            }

                        }
                    }, 3000);

                }


            }
        }, 5000);


    }

    public void buttonC(View view) {


        disableButtons();

        buttonC.setBackgroundResource(R.drawable.flash_animation_c);
        anim = (AnimationDrawable) buttonC.getBackground();
        anim.start();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (currentQuestion.getOptC().equals(currentQuestion.getAnswer())) {

                    buttonC.setBackgroundResource(R.drawable.when_correct_c);
                    buttonC.startAnimation(correctAnsAnimation);

                    MUSIC_FLAG = 1;
                    playSound.seAudioforAnswers(MUSIC_FLAG);

                    moneyWonByUser();
                    FLAG++;

                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (qid != sizeOfQuiz) {

                                mDrawerLayout.openDrawer(GravityCompat.END);
                                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                                    Handler justDely = new Handler();
                                    justDely.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            mDrawerLayout.closeDrawer(GravityCompat.END);

                                        }
                                    }, 3000);


                                    SetNewQuestion();

                                }

                            } else {

                                gameWonDialog.GameWonDialog(moneyWon);

                            }

                        }
                    }, 3000);

                } else {

                    buttonC.setBackgroundResource(R.drawable.when_incorrect_c);
                    buttonC.startAnimation(shakeAnimation);

                    MUSIC_FLAG = 2;
                    playSound.seAudioforAnswers(MUSIC_FLAG);
                    Handler handler3 = new Handler();
                    handler3.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (currentQuestion.getOptB().equals(currentQuestion.getAnswer())) {
                                buttonB.setBackgroundResource(R.drawable.when_correct_b);
                            } else if (currentQuestion.getOptA().equals(currentQuestion.getAnswer())) {
                                buttonA.setBackgroundResource(R.drawable.when_correct_a);
                            } else {
                                buttonD.setBackgroundResource(R.drawable.when_correct_d);
                            }

                        }
                    }, 2000);


                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (qid != sizeOfQuiz) {
                                // Play scrren because ans is incorrect
                                correctAns = currentQuestion.getAnswer();
                                gameOverDialog.GameOverDialog(correctAns);
                            }

                        }
                    }, 3000);

                }


            }
        }, 5000);


    }

    public void buttonD(View view) {


        disableButtons();

        buttonD.setBackgroundResource(R.drawable.flash_animation_d);
        anim = (AnimationDrawable) buttonD.getBackground();
        anim.start();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (currentQuestion.getOptD().equals(currentQuestion.getAnswer())) {

                    buttonD.setBackgroundResource(R.drawable.when_correct_d);
                    buttonD.startAnimation(correctAnsAnimation);

                    MUSIC_FLAG = 1;
                    playSound.seAudioforAnswers(MUSIC_FLAG);

                    moneyWonByUser();
                    FLAG++;

                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (qid != sizeOfQuiz) {


                                mDrawerLayout.openDrawer(GravityCompat.END);
                                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                                    Handler justDely = new Handler();
                                    justDely.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            mDrawerLayout.closeDrawer(GravityCompat.END);

                                        }
                                    }, 3000);

                                }

                                SetNewQuestion();

                            } else {

                                gameWonDialog.GameWonDialog(moneyWon);

                            }

                        }
                    }, 3000);

                } else {

                    buttonD.setBackgroundResource(R.drawable.when_incorrect_d);
                    buttonD.startAnimation(shakeAnimation);


                    MUSIC_FLAG = 2;
                    playSound.seAudioforAnswers(MUSIC_FLAG);

                    Handler handler3 = new Handler();
                    handler3.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (currentQuestion.getOptB().equals(currentQuestion.getAnswer())) {
                                buttonB.setBackgroundResource(R.drawable.when_correct_b);
                            } else if (currentQuestion.getOptC().equals(currentQuestion.getAnswer())) {
                                buttonC.setBackgroundResource(R.drawable.when_correct_c);
                            } else {
                                buttonA.setBackgroundResource(R.drawable.when_correct_a);
                            }

                        }
                    }, 2000);


                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (qid != sizeOfQuiz) {
                                // Play scrren because ans is incorrect
                                correctAns = currentQuestion.getAnswer();
                                gameOverDialog.GameOverDialog(correctAns);
                            }

                        }
                    }, 3000);

                }


            }
        }, 5000);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(QuizActivity.this, PlayScreen.class);
        startActivity(intent);
        finish();

    }


    private void disableButtons() {

        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);

    }

    private void enableButtons() {
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
    }


    private void setUpTextViewsofMoney() {

        txt[0] = findViewById(R.id.txt);
        txt[1] = findViewById(R.id.txt0);
        txt[2] = findViewById(R.id.txt1);
        txt[3] = findViewById(R.id.txt2);
        txt[4] = findViewById(R.id.txt3);
        txt[5] = findViewById(R.id.txt4);
        txt[6] = findViewById(R.id.txt5);
        txt[7] = findViewById(R.id.txt6);
        txt[8] = findViewById(R.id.txt7);
        txt[9] = findViewById(R.id.txt8);
        txt[10] = findViewById(R.id.txt9);
        txt[11] = findViewById(R.id.txt10);
        txt[12] = findViewById(R.id.txt11);
        txt[13] = findViewById(R.id.txt12);
        txt[14] = findViewById(R.id.txt13);
        txt[15] = findViewById(R.id.txt14);


    }


    private void moneyWonByUser() {
        moneyHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (FLAG == 1) {

                    moneyIndicator(0);
                    moneyIndicator2(1);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "500";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 2) {

                    moneyIndicator(1);
                    moneyIndicator2(2);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "1000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 3) {

                    moneyIndicator(2);
                    moneyIndicator2(3);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "2000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 4) {

                    moneyIndicator(3);
                    moneyIndicator2(4);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "3000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 5) {

                    moneyIndicator(4);
                    moneyIndicator2(5);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "5000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 6) {

                    moneyIndicator(5);
                    moneyIndicator2(6);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "10,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 7) {

                    moneyIndicator(6);
                    moneyIndicator2(7);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "15,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 8) {

                    moneyIndicator(7);
                    moneyIndicator2(8);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "25,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 9) {

                    moneyIndicator(8);
                    moneyIndicator2(9);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "50,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 10) {

                    moneyIndicator(9);
                    moneyIndicator2(10);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "1,00,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 11) {

                    moneyIndicator(10);
                    moneyIndicator2(11);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "2,00,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 12) {

                    moneyIndicator(11);
                    moneyIndicator2(12);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "4,00,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 13) {

                    moneyIndicator(12);
                    moneyIndicator2(13);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "8,00,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 14) {

                    moneyIndicator(13);
                    moneyIndicator2(14);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "1,500,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                } else if (FLAG == 15) {

                    moneyIndicator(14);
                    moneyIndicator2(13);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            moneyWon = "30,00,000";
                            txtMoneyWon.setText("$" + moneyWon);

                        }
                    }, 1200);
                }


            }
        }, 2000);
    }


    private void moneyIndicator(int number) {

        Drawable firstDrawable = getDrawable(R.drawable.new_state);
        Drawable secondDrawable = getDrawable(R.drawable.original_state);

        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{
                firstDrawable, secondDrawable});

        txt[number].setBackground(transitionDrawable);
        transitionDrawable.startTransition(1000);

    }


    private void moneyIndicator2(int number) {

        Drawable firstDrawable = getDrawable(R.drawable.new_state);
        Drawable secondDrawable = getDrawable(R.drawable.original_state);

        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{
                firstDrawable, secondDrawable});

        txt[number].setBackground(transitionDrawable);
        transitionDrawable.startTransition(1000);
        transitionDrawable.reverseTransition(1000);
    }


    private void audienceVoteSystem() {

        final Random random = new Random();
        final Set<Integer> mySet = new HashSet<>();
        RANGE = 15;
        int No_of_RANDOM_NOs = 4;
        MAX_OUT = 85;

        while (mySet.size() < No_of_RANDOM_NOs) {
            int randomNum = random.nextInt(RANGE) + 1;
            mySet.add(randomNum);
        }

        ArrayList<Integer> Elements = new ArrayList<>(mySet);

        showAudienceDialog(Elements);

    }

    private void showAudienceDialog(ArrayList<Integer> AudienceVoteAnswers) {

        String CorrectAnswer = "";
        CorrectAnswer = (String) currentQuestion.getAnswer();

        dialog.setContentView(R.layout.dialog_for_audience);

        TextView txtA, txtB, txtC, txtD;
        btAudienceDialogOk = dialog.findViewById(R.id.btOkAudienceDialog);
        txtA = dialog.findViewById(R.id.txtA);
        txtB = dialog.findViewById(R.id.txtB);
        txtC = dialog.findViewById(R.id.txtC);
        txtD = dialog.findViewById(R.id.txtD);

        int SUM_OF_RANDOM_NUMBERS = AudienceVoteAnswers.get(0) + AudienceVoteAnswers.get(1)
                + AudienceVoteAnswers.get(2) + AudienceVoteAnswers.get(3);


        // for A Button
        if (CorrectAnswer.contentEquals(buttonA.getText())) {

            int INCREASE_THE_VALUE_OF_A = (MAX_OUT - SUM_OF_RANDOM_NUMBERS) + RANGE;

            int CORRECT_IS_CHOICE_A = INCREASE_THE_VALUE_OF_A + AudienceVoteAnswers.get(0);


            txtA.setText(Integer.toString(CORRECT_IS_CHOICE_A));
            txtB.setText(Integer.toString(AudienceVoteAnswers.get(1)));
            txtC.setText(Integer.toString(AudienceVoteAnswers.get(2)));
            txtD.setText(Integer.toString(AudienceVoteAnswers.get(3)));


        }

        // for B Button
        if (CorrectAnswer.contentEquals(buttonB.getText())) {

            int INCREASE_THE_VALUE_OF_B = (MAX_OUT - SUM_OF_RANDOM_NUMBERS) + RANGE;

            int CORRECT_IS_CHOICE_B = INCREASE_THE_VALUE_OF_B + AudienceVoteAnswers.get(1);


            txtA.setText(Integer.toString(AudienceVoteAnswers.get(0)));
            txtB.setText(Integer.toString(CORRECT_IS_CHOICE_B));
            txtC.setText(Integer.toString(AudienceVoteAnswers.get(2)));
            txtD.setText(Integer.toString(AudienceVoteAnswers.get(3)));

        }

        // for C Button
        if (CorrectAnswer.contentEquals(buttonC.getText())) {

            int INCREASE_THE_VALUE_OF_C = (MAX_OUT - SUM_OF_RANDOM_NUMBERS) + RANGE;

            int CORRECT_IS_CHOICE_C = INCREASE_THE_VALUE_OF_C + AudienceVoteAnswers.get(2);


            txtA.setText(Integer.toString(AudienceVoteAnswers.get(0)));
            txtB.setText(Integer.toString(AudienceVoteAnswers.get(1)));
            txtC.setText(Integer.toString(CORRECT_IS_CHOICE_C));
            txtD.setText(Integer.toString(AudienceVoteAnswers.get(3)));

        }

        // for D Button
        if (CorrectAnswer.contentEquals(buttonD.getText())) {

            int INCREASE_THE_VALUE_OF_D = (MAX_OUT - SUM_OF_RANDOM_NUMBERS) + RANGE;

            int CORRECT_IS_CHOICE_D = INCREASE_THE_VALUE_OF_D + AudienceVoteAnswers.get(3);


            txtA.setText(Integer.toString(AudienceVoteAnswers.get(0)));
            txtB.setText(Integer.toString(AudienceVoteAnswers.get(1)));
            txtC.setText(Integer.toString(AudienceVoteAnswers.get(2)));
            txtD.setText(Integer.toString(CORRECT_IS_CHOICE_D));

        }

        btAudienceDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        disableaudienceVoteSystem();


    }


    private void telephoneLifeLine() {


        String mainAns="";
        String theCorrectAns = "";
        theCorrectAns = currentQuestion.getAnswer();
        telePhoneDialog.setContentView(R.layout.dialog_for_telephone);
        TextView txtTelePhone;
        bttelePhoneDialogOk = telePhoneDialog.findViewById(R.id.btOkTelePhoneDialog);
        txtTelePhone = telePhoneDialog.findViewById(R.id.txtCorrectAnsTelePhone);


        Random random = new Random();
        int i = random.nextInt(4);

        switch (i){

            case 0:
                mainAns = "I dont know the answer sorry :(. Good Luck. ";
            break;

            case 1:
                mainAns = "I searched on the Internet ^^ definetly. "+ theCorrectAns;
            break;

            case 2:
                mainAns = "I am not sure may this is the correct one. " + theCorrectAns;
            break;

            case 3:
                mainAns = "If I were you, I would say. " + theCorrectAns;
            break;

        }


        txtTelePhone.setText(mainAns);

        bttelePhoneDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                telePhoneDialog.dismiss();
            }
        });

        telePhoneDialog.show();
        telePhoneDialog.setCancelable(false);
        telePhoneDialog.setCanceledOnTouchOutside(false);
        telePhoneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        disabletelephoneLifeLine();

    }


    private void fiftyfifty(){

        if (currentQuestion.getAnswer().contentEquals(buttonD.getText())){


            buttonA.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.INVISIBLE);
            buttonC.setVisibility(View.VISIBLE);
        }

        if (currentQuestion.getAnswer().contentEquals(buttonC.getText())){


            buttonA.setVisibility(View.INVISIBLE);
            buttonD.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.VISIBLE);
        }
        if (currentQuestion.getAnswer().contentEquals(buttonB.getText())){

            buttonC.setVisibility(View.INVISIBLE);
            buttonD.setVisibility(View.INVISIBLE);
            buttonA.setVisibility(View.VISIBLE);

        }
        if (currentQuestion.getAnswer().contentEquals(buttonA.getText())){

            buttonC.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.INVISIBLE);
            buttonD.setVisibility(View.VISIBLE);

        }

        disablefiftyfiftyLifeLine();

    }

    private void displayButtons(){

        buttonA.setVisibility(View.VISIBLE);
        buttonB.setVisibility(View.VISIBLE);
        buttonC.setVisibility(View.VISIBLE);
        buttonD.setVisibility(View.VISIBLE);
    }

    private void skippableLifeLine(){

        SetNewQuestion();
        Toast.makeText(this, "Skippable is Used", Toast.LENGTH_SHORT).show();

        disableSkippableLifeLine();
    }





    private void disableSkippableLifeLine(){

        imgSkipabble.setClickable(false);
        imgSkipabble.setImageResource(R.drawable.unselect_skippable_bg);

    }

    private void disablefiftyfiftyLifeLine(){

        imgFiftyFifty.setClickable(false);
        imgFiftyFifty.setImageResource(R.drawable.unselect_fifty_fifty_bg);

    }

    private void disabletelephoneLifeLine(){

        imgTelePhone.setClickable(false);
        imgTelePhone.setImageResource(R.drawable.unselect_telephone_bg);

    }

    private void disableaudienceVoteSystem(){

        imgAudienceVote.setClickable(false);
        imgAudienceVote.setImageResource(R.drawable.unselect_audience_pool_bg);

    }

}
