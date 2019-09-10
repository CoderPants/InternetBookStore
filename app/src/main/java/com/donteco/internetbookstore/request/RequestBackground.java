package com.donteco.internetbookstore.request;

import android.os.AsyncTask;

import com.donteco.internetbookstore.constants.ConstantsForApp;

import java.util.Timer;
import java.util.TimerTask;

public class RequestBackground
{

    private String userRequest;
    private int startingPage;
    private int amountOfPages;
    private RequestSender requestSender;
    //private int totalAmountOfBooks;
    private int totalAmountOfPages;

    private Timer timer;

    public RequestBackground(String userRequest, RequestSender requestSender, int totalAmountOfBooks)
    {
        this.userRequest = userRequest;
        this.requestSender = requestSender;
        //this.totalAmountOfBooks = totalAmountOfBooks;

        totalAmountOfPages = (int)Math.ceil(totalAmountOfBooks/(double)ConstantsForApp.AMOUNT_OF_BOOKS_PER_PAGE);

        startingPage = 0;
        amountOfPages = 0;
    }

    public void startRequest()
    {
        timer = new Timer(true);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run()
            {
                new BackGroundAsync(userRequest, startingPage, amountOfPages,
                        requestSender, totalAmountOfPages, timer).execute();
            }
        };

        timer.schedule(timerTask, 0,10000);
    }

    public void cancelTimer(){
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }


    private static class BackGroundAsync extends AsyncTask<Void, Integer, Void>
    {
        private String userRequest;
        private int startingPage;
        private int amountOfPages;
        private RequestSender requestSender;
        private int totalAmountOfPages;
        private Timer timer;

        public BackGroundAsync(String userRequest, int startingPage, int amountOfPages, RequestSender requestSender, int totalAmountOfPages, Timer timer) {
            this.userRequest = userRequest;
            this.startingPage = startingPage;
            this.amountOfPages = amountOfPages;
            this.requestSender = requestSender;
            this.totalAmountOfPages = totalAmountOfPages;
            this.timer = timer;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            //If there is 5 full pages
            if(totalAmountOfPages - amountOfPages >= ConstantsForApp.REQUEST_PAGE_AMOUNT)
            {
                amountOfPages += ConstantsForApp.REQUEST_PAGE_AMOUNT;

                for (int i = startingPage + 1; i <= amountOfPages ; i++)
                    requestSender.sentGetBooksRequest(userRequest, i);

                startingPage = amountOfPages;
            }
            //If there is < than 5 pages
            else
            {
                for (int i = amountOfPages+1; i <= totalAmountOfPages; i++)
                    requestSender.sentGetBooksRequest(userRequest, i);

                //We get all books, sent callback to finish timer
                timer.cancel();
            }

            return null;
        }
    }
}
