package com.donteco.internetbookstore.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donteco.internetbookstore.R;
import com.donteco.internetbookstore.activities.FullBookDescription;
import com.donteco.internetbookstore.adapters.ChooseBooksAdapter;
import com.donteco.internetbookstore.books.FullBookInfo;
import com.donteco.internetbookstore.books.ShortenedBookInfo;
import com.donteco.internetbookstore.constants.ConstantsForApp;
import com.donteco.internetbookstore.constants.IntentKeys;
import com.donteco.internetbookstore.helper.SearchBooksHelper;
import com.donteco.internetbookstore.models.RepositoryViewModel;
import com.donteco.internetbookstore.request.RequestSender;
import com.donteco.internetbookstore.storage.Storage;

import java.util.List;

public class SearchBooksFragment extends Fragment
{
    private ChooseBooksAdapter adapter;
    private RequestSender requestSender;
    private RepositoryViewModel viewModel;

    //Fields for single creation
    private Activity activity;
    private String userInput;
    private EditText searchBar;

    private ProgressBar loadingIndicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        try {
            activity = getActivity();
            return inflater.inflate(R.layout.fragment_search_for_books, container, false);
        }
        catch (Exception e) {
            Log.wtf(ConstantsForApp.LOG_TAG, "Some terrible error in on create view in searchBooksFragment. Exception: ", e);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Models
        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        loadingIndicator = view.findViewById(R.id.main_activity_pb_loading_indicator);

        requestSenderCreation();

        searchBar = view.findViewById(R.id.book_search_bar);
        searchBooksBtnLogic(view.findViewById(R.id.book_search_iv));

        recyclerViewCreation(view.findViewById(R.id.rv_books));
    }

    private void searchBooksBtnLogic(ImageView imageView) {
        imageView.setOnClickListener(view ->
        {
            adapter.clearList();

            userInput = searchBar.getText().toString();

            //IF user didn't enter anything
            if(userInput.trim().length() == 0)
                return;

            if(SearchBooksHelper.isNetworkAvailable(activity))
                requestSender.sentGetTotalBooksAmount(userInput);

            viewModel.getBooksByUserRequest(userInput).observe(SearchBooksFragment.this, shortenedBookInfos ->
            {
                adapter.setBooks(shortenedBookInfos);

                if(adapter.getItemCount() == 0)
                    loadingIndicator.setVisibility(View.VISIBLE);
                else
                    loadingIndicator.setVisibility(View.INVISIBLE);
            });
        });
    }

    private void requestSenderCreation() {
        requestSender = new RequestSender(new RequestSender.RequestCallBack()
        {
            @Override
            public void onGetBooksResponse(List<ShortenedBookInfo> receivedShortenedBooksInfo)
            {
                if(SearchBooksHelper.noListInDB(receivedShortenedBooksInfo,
                        adapter.getBooks(),
                        requestSender.getTotalNumberOfBooks() % 10))
                {
                    viewModel.insertShortenedBooksInfo(receivedShortenedBooksInfo);
                    viewModel.insertCachedBooksInfo( SearchBooksHelper.convertToCachedBooks(receivedShortenedBooksInfo, userInput) );
                }
                loadingIndicator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onGetBookInfoResponse(FullBookInfo fullBookInfo)
            {
                loadingIndicator.setVisibility(View.INVISIBLE);

                viewModel.insertFullBookInfo(fullBookInfo);

                startFullDescriptionActivity(fullBookInfo);
                /*FullBookDescriptionDialog dialog = new FullBookDescriptionDialog(activity, fullBookInfo);
                dialog.show();*/
            }

            @Override
            public void onNoBooksCondition() {
                loadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(activity, "Sorry, no books found!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void recyclerViewCreation(RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        adapter = new ChooseBooksAdapter(new ChooseBooksAdapter.ChooseAdapterCallBack()
        {
            @Override
            public Drawable onNoIconCondition() {
                return getResources().getDrawable(R.drawable.no_book_image_icon, null);
            }

            @Override
            public void uploadFullBookInfo(long id) {
                loadingIndicator.setVisibility(View.VISIBLE);
                FullBookInfo fullBookInfo = viewModel.getFullBookInfoByRequest(id);

                if(fullBookInfo == null)
                    requestSender.sentGetFullBookInfo(id);
                else
                {
                    loadingIndicator.setVisibility(View.INVISIBLE);
                    startFullDescriptionActivity(fullBookInfo);
                    /*FullBookDescriptionDialog dialog = new FullBookDescriptionDialog(activity, fullBookInfo);
                    dialog.show();*/
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void startFullDescriptionActivity(FullBookInfo fullBookInfo)
    {
        Intent intent = new Intent(activity, FullBookDescription.class);
        intent.putExtra(IntentKeys.FULL_BOOK_TITLE, fullBookInfo.getTitle());
        intent.putExtra(IntentKeys.FULL_BOOK_SUBTITLE, fullBookInfo.getSubtitle());
        intent.putExtra(IntentKeys.FULL_BOOK_AUTHORS, fullBookInfo.getAuthors());
        intent.putExtra(IntentKeys.FULL_BOOK_PUBLISHERS, fullBookInfo.getPublisher());
        intent.putExtra(IntentKeys.FULL_BOOK_ISBN10, fullBookInfo.getIsbn10());
        intent.putExtra(IntentKeys.FULL_BOOK_ISBN13, fullBookInfo.getId());
        intent.putExtra(IntentKeys.FULL_BOOK_PAGES, fullBookInfo.getPages());
        intent.putExtra(IntentKeys.FULL_BOOK_YEAR, fullBookInfo.getYear());
        intent.putExtra(IntentKeys.FULL_BOOK_RATING, fullBookInfo.getRating());
        intent.putExtra(IntentKeys.FULL_BOOK_DESCRIPTION, fullBookInfo.getDescription());
        intent.putExtra(IntentKeys.FULL_BOOK_PRICE, fullBookInfo.getPrice());
        intent.putExtra(IntentKeys.FULL_BOOK_IMAGE, fullBookInfo.getImage());
        intent.putExtra(IntentKeys.FULL_BOOK_URL, fullBookInfo.getUrl());
        intent.putExtra(IntentKeys.FULL_BOOK_LANGUAGE, fullBookInfo.getLanguage());

        startActivity(intent);
    }

    @Override
    public void onResume()
    {
        userInput = Storage.getUserInput();
        searchBar.setText(userInput);

        viewModel.getBooksByUserRequest(userInput).observe(SearchBooksFragment.this, shortenedBookInfos ->
                adapter.setBooks(shortenedBookInfos));

        super.onResume();
    }

    @Override
    public void onPause() {
        Storage.setUserInput(searchBar.getText().toString());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Storage.setUserInput(searchBar.getText().toString());
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Storage.setUserInput(searchBar.getText().toString());
        super.onDestroyView();
    }
}
