package com.example.comp9900_commercialize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.comp9900_commercialize.adapters.StaggerAdapter;
import com.example.comp9900_commercialize.bean.ItemExplore;
import com.example.comp9900_commercialize.bean.Recipe;
import com.example.comp9900_commercialize.databinding.ActivityGuestSearchResultBinding;
import com.example.comp9900_commercialize.utilities.MacroDef;
import com.example.comp9900_commercialize.utilities.Preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class GuestSearchResultActivity extends AppCompatActivity {

    private ActivityGuestSearchResultBinding binding;
    private Preferences preferences;
    private RecyclerView mList;
    private StaggerAdapter mAdapter;
    private List<ItemExplore> mData;
    private Recipe recipe;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityGuestSearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mList = binding.lvSearchResult;
        init();
        setListeners();
        if(preferences.getString(MacroDef.KEY_SEARCH_MODE).equals("By keywords")){

            searchByKeywords(preferences.getString(MacroDef.KEY_SEARCH_CONTENT));
        }else{
            searchByType(preferences.getString(MacroDef.KEY_SEARCH_TYPE));
        }

    }

    private void init(){

        preferences = new Preferences(getApplicationContext());
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    private void setListeners(){
        binding.btCancel.setOnClickListener(v -> onBackPressed());
    }

    private void searchByKeywords(String keywords) {

        mData = new ArrayList<>();
        CollectionReference collectionReference = firebaseFirestore.collection("recipes");
        Query query = collectionReference
                .orderBy("recipeLikesNum", Query.Direction.DESCENDING);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ItemExplore explore = new ItemExplore();
                                recipe = document.toObject(Recipe.class);
                                if(preferences.getString(MacroDef.KEY_SEARCH_CONTENT).equals("")){
                                    if(recipe.recipeContributorAvatar != null){
                                        byte[] bytes = Base64.decode(recipe.recipeContributorAvatar, Base64.DEFAULT);
                                        explore.avatar = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    } else {
                                        @SuppressLint("ResourceType") InputStream img_avatar = getResources().openRawResource(R.drawable.default_avatar);
                                        explore.avatar = BitmapFactory.decodeStream(img_avatar);
                                    }
                                    byte[] bytes = Base64.decode(recipe.recipeCover, Base64.DEFAULT);
                                    explore.icon =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    explore.tv_contributor_name = recipe.recipeContributorName;
                                    explore.tv_like_num = String.valueOf(recipe.recipeLikesNum);
                                    explore.tv_comment_num = String.valueOf(recipe.recipeCommentsNum);
                                    explore.title = recipe.recipeName;
                                    explore.id = document.getId();
                                    explore.icon_comment = R.drawable.ic_comment;
                                    explore.icon_like = R.drawable.ic_like;
                                    mData.add(explore);
                                }else{
                                    Collection<String> fields = new ArrayList<String>();
                                    fields.add(recipe.recipeName);
                                    fields.add(recipe.recipeDescription);
                                    fields.add(recipe.recipeContributorName);
                                    if(FuzzySearch
                                            .extractOne(preferences.getString(MacroDef.KEY_SEARCH_CONTENT), fields)
                                            .getScore() >= 90){
                                        if(recipe.recipeContributorAvatar != null){
                                            byte[] bytes = Base64.decode(recipe.recipeContributorAvatar, Base64.DEFAULT);
                                            explore.avatar = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        } else {
                                            @SuppressLint("ResourceType") InputStream img_avatar = getResources().openRawResource(R.drawable.default_avatar);
                                            explore.avatar = BitmapFactory.decodeStream(img_avatar);
                                        }
                                        byte[] bytes = Base64.decode(recipe.recipeCover, Base64.DEFAULT);
                                        explore.icon =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        explore.tv_contributor_name = recipe.recipeContributorName;
                                        explore.tv_like_num = String.valueOf(recipe.recipeLikesNum);
                                        explore.tv_comment_num = String.valueOf(recipe.recipeCommentsNum);
                                        explore.title = recipe.recipeName;
                                        explore.id = document.getId();
                                        explore.icon_comment = R.drawable.ic_comment;
                                        explore.icon_like = R.drawable.ic_like;
                                        mData.add(explore);
                                    }
                                }
                            }
                            binding.resProgressBar.setVisibility(View.GONE);
                            binding.tvResLoading.setVisibility(View.GONE);
                            showStagger(true, false);
                        } else { // error handling
                            Toast.makeText(GuestSearchResultActivity.this, "Error getting documents."+task.getException(), Toast.LENGTH_SHORT).show();
                            System.out.println("Error getting documents."+task.getException());
                        }
                    }
                });
    }

    private void searchByType(String recipeType) {

        mData = new ArrayList<>();
        CollectionReference collectionReference = firebaseFirestore.collection("recipes");
        Query query = collectionReference.whereEqualTo("recipeType", recipeType)
                .orderBy("recipeLikesNum", Query.Direction.DESCENDING);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ItemExplore explore = new ItemExplore();
                                recipe = document.toObject(Recipe.class);
                                if(recipe.recipeContributorAvatar != null){
                                    byte[] bytes = Base64.decode(recipe.recipeContributorAvatar, Base64.DEFAULT);
                                    explore.avatar = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                } else {
                                    @SuppressLint("ResourceType") InputStream img_avatar = getResources().openRawResource(R.drawable.default_avatar);
                                    explore.avatar = BitmapFactory.decodeStream(img_avatar);
                                }
                                byte[] bytes = Base64.decode(recipe.recipeCover, Base64.DEFAULT);
                                explore.icon =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                explore.tv_contributor_name = recipe.recipeContributorName;
                                explore.tv_like_num = String.valueOf(recipe.recipeLikesNum);
                                explore.tv_comment_num = String.valueOf(recipe.recipeCommentsNum);
                                explore.title = recipe.recipeName;
                                explore.id = document.getId();
                                explore.icon_comment = R.drawable.ic_comment;
                                explore.icon_like = R.drawable.ic_like;
                                mData.add(explore);
                            }
                            binding.resProgressBar.setVisibility(View.GONE);
                            binding.tvResLoading.setVisibility(View.GONE);
                            showStagger(true, false);
                        } else { // error handling
                            Toast.makeText(GuestSearchResultActivity.this, "Error getting documents."+task.getException(), Toast.LENGTH_SHORT).show();
                            System.out.println("Error getting documents."+task.getException());
                        }
                    }
                });
    }

    private void showStagger(boolean isVertical, boolean isReverse) {

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, isVertical?StaggeredGridLayoutManager.VERTICAL:StaggeredGridLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(isReverse);
        mList.setLayoutManager(layoutManager);
        mAdapter = new StaggerAdapter(mData);
        mList.setAdapter(mAdapter);
        mList.postInvalidate();
        initListener();

    }

    private void initListener() {

        mAdapter.setOnItemClickListener(new StaggerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                preferences.putString(MacroDef.KEY_RECIPE_ID, mData.get(position).id);
                startActivity(new Intent(getApplicationContext(), GuestRecipeDetailActivity.class));
            }
        }) ;

    }

}