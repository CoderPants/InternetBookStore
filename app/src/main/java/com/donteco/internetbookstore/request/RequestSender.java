package com.donteco.internetbookstore.request;

import android.util.Log;

import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.constants.ConstantsForApp;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestSender {

    private RequestCallBack callBack;
    private int totalNumberOfBooks;

    //For canceling requesting
    private RequestBackground requestBackground;

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
                    requestBackground = new RequestBackground
                            (userInput,RequestSender.this, totalNumberOfBooks);

                    requestBackground.startRequest();
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

    public void stopRequesting(){
        if(requestBackground != null)
            requestBackground.cancelTimer();
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

    public void sentGetNewBooks()
    {
        GetDataService service = RetrofitClientInstance.getInstance().create(GetDataService.class);

        Call<ServerSearchResponceNewBooks> getNewBooksCall = service.getNewBooks();

        getNewBooksCall.enqueue(new Callback<ServerSearchResponceNewBooks>() {
            @Override
            public void onResponse(Call<ServerSearchResponceNewBooks> call,
                                   Response<ServerSearchResponceNewBooks> response)
            {
                ServerSearchResponceNewBooks serverSearchResponceNewBooks = response.body();
                callBack.onGetNewBooks(serverSearchResponceNewBooks.getShortenedBooksInfo());
            }

            @Override
            public void onFailure(Call<ServerSearchResponceNewBooks> call, Throwable t) {
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
        void onGetNewBooks(List<ShortenedBookInfo> receivedShortenedBooksInfo);
        void onNoBooksCondition();
    }
}
