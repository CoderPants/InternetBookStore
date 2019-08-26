package com.donteco.internetbookstore.request;

import android.util.Log;

import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.help.ConstantsForApp;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestSender {

    private RequestCallBack callBack;
    private int totalNumberOfBooks;

    public RequestSender(RequestCallBack callBack) {
        this.callBack = callBack;
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
                totalNumberOfBooks = serverSearchResponse.getTotal();
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
                System.out.println("Full book info request " + fullBookInfo.toString());
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
    }
}
