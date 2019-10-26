package com.donteco.internetbookstore.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

    //For recycler view scroll
    private RecyclerView recyclerView;

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
        searchBarKeyLogic();

        searchBooksBtnLogic(view.findViewById(R.id.book_search_iv));

        recyclerView = view.findViewById(R.id.rv_books);
        recyclerViewCreation();

        setLiveDataObserver();

        //If there is no new books in db
        if(getArguments() != null
                && getArguments().getBoolean(IntentKeys.NEED_NEW_BOOKS)
                && adapter.getItemCount() == 0)
        {
            requestSender.sentGetNewBooks();
            loadingIndicator.setVisibility(View.VISIBLE);
            scrollRV(0);
        }
    }

    private void searchBarKeyLogic()
    {
        searchBar.setOnKeyListener((view, keyCode, keyEvent) ->
        {
            if(keyCode == EditorInfo.IME_ACTION_SEARCH ||
                    keyCode == EditorInfo.IME_ACTION_DONE ||
                    keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                            keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            {
                //Hide keyboard
                InputMethodManager imm = (InputMethodManager)
                        activity.getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                startSearching();

                return true;
            }
            return false;
        });
    }

    private void setLiveDataObserver(){
        viewModel.getShortenedInfoBooks().observe(SearchBooksFragment.this, bookInfos ->
                adapter.setBooks(bookInfos));
    }

    private void searchBooksBtnLogic(ImageView imageView) {
        imageView.setOnClickListener(view -> startSearching());
    }

    private void startSearching()
    {
        userInput = searchBar.getText().toString();
        requestSender.stopRequesting();
        scrollRV(0);

        //IF user didn't enter anything
        if(userInput.trim().length() == 0)
            return;

        if(SearchBooksHelper.isNetworkAvailable(activity))
        {
            //To different requests for new books and for standart one
            if(userInput.equals("new"))
            {
                requestSender.sentGetNewBooks();
                loadingIndicator.setVisibility(View.VISIBLE);
            }
            else
                requestSender.sentGetTotalBooksAmount(userInput);
        }

        viewModel.setUserInputLiveData(userInput);
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
                    viewModel.insertShortenedBooksInfoAsync(receivedShortenedBooksInfo);
                    viewModel.insertCachedBooksInfoAsync(
                            SearchBooksHelper.convertToCachedBooks(receivedShortenedBooksInfo, userInput) );
                }
                loadingIndicator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onGetBookInfoResponse(FullBookInfo fullBookInfo) {
                //Nothing
            }

            @Override
            public void onNoBooksCondition() {
                loadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(activity, "Sorry, no books found!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGetNewBooks(List<ShortenedBookInfo> receivedShortenedBooksInfo)
            {
                String stringForBar = "new";
                searchBar.setText(stringForBar);

                if(SearchBooksHelper.noNewBooksInDb(receivedShortenedBooksInfo, adapter.getBooks()))
                {
                    viewModel.insertShortenedBooksInfoAsync(receivedShortenedBooksInfo);
                    viewModel.insertCachedBooksInfoAsync(
                            SearchBooksHelper.convertToCachedBooks(receivedShortenedBooksInfo, userInput) );
                }
                loadingIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void recyclerViewCreation()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        adapter = new ChooseBooksAdapter(new ChooseBooksAdapter.ChooseAdapterCallBack()
        {
            @Override
            public Drawable onNoIconCondition() {
                return getResources().getDrawable(R.drawable.no_book_image_icon, null);
            }

            @Override
            public void uploadFullBookInfo(long id)
            {
                Intent intent = new Intent(activity, FullBookDescription.class);
                intent.putExtra(IntentKeys.FULL_BOOK_ISBN13, id);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void scrollRV(int position){
        recyclerView.post(() -> recyclerView.scrollToPosition(position));
    }

    private int getRVIndex(){
        return ((LinearLayoutManager)recyclerView.getLayoutManager()).
                findFirstCompletelyVisibleItemPosition();
    }

    @Override
    public void onResume()
    {
        if(getArguments() != null && getArguments().getBoolean(IntentKeys.NEED_NEW_BOOKS))
        {
            userInput = "new";
            searchBar.setText(userInput);
        }
        else
        {
            userInput = Storage.getUserInput();
            searchBar.setText(userInput);
        }
        //Log.i(ConstantsForApp.LOG_TAG, "On resume " + getArguments() + " userInput " + userInput );
        if(userInput.length() != 0)
        {
            viewModel.setUserInputLiveData(userInput);
            setLiveDataObserver();
        }

        //Get to the last position;
        scrollRV(Storage.getLastRVPosition());

        super.onResume();
    }

    @Override
    public void onPause()
    {
        //Save lastPosition
        Storage.setLastRVPosition(getRVIndex());
        Storage.setUserInput(searchBar.getText().toString());
        super.onPause();
    }

    @Override
    public void onStop() {
        Storage.setLastRVPosition(getRVIndex());
        Storage.setUserInput(searchBar.getText().toString());
        super.onStop();
    }
}
