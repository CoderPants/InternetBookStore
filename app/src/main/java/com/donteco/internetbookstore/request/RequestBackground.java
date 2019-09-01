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
                new BackGroundAsync().execute();
            }
        };

        timer.schedule(timerTask, 0,10000);
    }

   /* @Override
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
            for (int i = amountOfPages; i <= totalAmountOfPages; i++)
                requestSender.sentGetBooksRequest(userRequest, i);

            //We get all books, sent callback to finish timer
            callBack.finished();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        System.out.println("Progress " + values[0]);
    }*/

    private class BackGroundAsync extends AsyncTask<Void, Integer, Void>
    {
        private BackGroundAsync() {
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
