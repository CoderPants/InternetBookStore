package com.donteco.internetbookstore.request;

import android.os.Handler;
import android.util.Log;

import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.help.ConstantsForApp;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestSender {

    private RequestCallBack callBack;
    private int totalNumberOfBooks;

    public RequestSender(RequestCallBack callBack) {
        this.callBack = callBack;
    }

    public void sentGetTotalBooksAmount(String userInput)
    {
        GetDataService service = RetrofitClientInstance.getInstance().create(GetDataService.class);

        Call<ServerSearchResponse> getBooksCall = service
                .getAllBooks(userInput);

        getBooksCall.enqueue(new Callback<ServerSearchResponse>() {
            @Override
            public void onResponse(@NotNull Call<ServerSearchResponse> call, @NotNull Response<ServerSearchResponse> response) {
                ServerSearchResponse serverSearchResponse = response.body();
                totalNumberOfBooks = serverSearchResponse.getTotal();

                if(totalNumberOfBooks == 0)
                    callBack.onNoBooksCondition();
                else
                {
                    System.out.println("Got into response!");
                    RequestBackground requestBackground = new RequestBackground
                            (userInput,RequestSender.this, totalNumberOfBooks);
                    requestBackground.startRequest();
                    /*Timer timer = new Timer(true);

                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            RequestBackground requestBackground = new RequestBackground
                                    (userInput,
                                            RequestSender.this,
                                            timer::cancel,
                                            totalNumberOfBooks);
                            requestBackground.execute();
                        }
                    };

                    timer.schedule(timerTask, 0,10000);*/
                }
            }

            @Override
            public void onFailure(@NotNull Call<ServerSearchResponse> call, @NotNull Throwable t) {
                Log.e(ConstantsForApp.LOG_TAG,
                        "Failure in getting response from server! " +
                                "Caused in RequestSender in sentGetTotalBooksCount." +
                                " Call itself " + call.request().toString(), t);
            }
        });
    }

    //Page number, cos api sending packages by 10 books
    public void sentGetBooksRequest(String userInput, int pageNumber)
    {
        GetDataService service = RetrofitClientInstance.getInstance().create(GetDataService.class);

        Call<ServerSearchResponse> getBooksCall = service
                .getAllBooks(userInput, pageNumber);

        getBooksCall.enqueue(new Callback<ServerSearchResponse>() {
            @Override
            public void onResponse(@NotNull Call<ServerSearchResponse> call, @NotNull Response<ServerSearchResponse> response) {
                ServerSearchResponse serverSearchResponse = response.body();
                System.out.println("Call from the server " + serverSearchResponse);
                callBack.onGetBooksResponse(serverSearchResponse.getShortenedBooksInfo());
            }

            @Override
            public void onFailure(@NotNull Call<ServerSearchResponse> call, @NotNull Throwable t) {
                Log.e(ConstantsForApp.LOG_TAG,
                        "Failure in getting response from server! " +
                                "Caused in RequestSender in sentGetBooksRequest." +
                                " Call itself " + call.request().toString(), t);
            }
        });
    }

    public void sentGetFullBookInfo(long bookID)
    {
        GetDataService service = RetrofitClientInstance.getInstance().create(GetDataService.class);
        Call<FullBookInfo> getBookFullInfo = service.getBookById(bookID);

        getBookFullInfo.enqueue(new Callback<FullBookInfo>()
        {
            @Override
            public void onResponse(@NotNull Call<FullBookInfo> call, @NotNull Response<FullBookInfo> response)
            {
                FullBookInfo fullBookInfo = response.body();
                callBack.onGetBookInfoResponse(fullBookInfo);
            }

            @Override
            public void onFailure(@NotNull Call<FullBookInfo> call, @NotNull Throwable t)
            {
                Log.e(ConstantsForApp.LOG_TAG,
                        "Failure in getting response from server! " +
                                "Caused in RequestSender in sentGetFullBookInfo." +
                                " Call itself " + call.request().toString(), t);
            }
        });
    }

    public int getTotalNumberOfBooks() {
        return totalNumberOfBooks;
    }

    public interface RequestCallBack {
        void onGetBooksResponse(List<ShortenedBookInfo> receivedShortenedBooksInfo);
        void onGetBookInfoResponse(FullBookInfo fullBookInfo);
        void onNoBooksCondition();
    }
}
