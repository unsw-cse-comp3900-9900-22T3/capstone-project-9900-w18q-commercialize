package com.example.comp9900_commercialize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.example.comp9900_commercialize.adapters.StaggerAdapter;
import com.example.comp9900_commercialize.bean.ItemExplore;
import com.example.comp9900_commercialize.databinding.ActivitySearchResultBinding;
import com.example.comp9900_commercialize.utilities.MacroDef;
import com.example.comp9900_commercialize.utilities.Preferences;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {

    private ActivitySearchResultBinding binding;
    private Preferences preferences;
    private RecyclerView mList;
    private StaggerAdapter mAdapter;
    private List<ItemExplore> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mList = this.findViewById(R.id.lv_search_result);
        init();
        setListeners();
        sendFeedbackJob job = new sendFeedbackJob();
        job.execute();

    }

    private void init(){

        preferences = new Preferences(getApplicationContext());

    }

    private void setListeners(){
        binding.btCancel.setOnClickListener(v -> onBackPressed());
    }

    private void search(){

        mData = new ArrayList<>();
        SearchClient client =
                DefaultSearchClient.create("FDLN8FK4N4", "8f5a258d5e61b56ca5830c933af87498");
        SearchIndex index = client.initIndex("recipes");
        com.algolia.search.models.indexing.Query query = new com.algolia.search.models.indexing.Query(preferences.getString(MacroDef.KEY_SEARCH_CONTENT))
                .setAttributesToRetrieve(Arrays.asList("recipeCover", "recipeName", "recipeLikesNum", "recipeCommentsNum", "recipeContributorName", "recipeContributorAvatar"))
                .setRestrictSearchableAttributes(Arrays.asList("recipeName", "recipeDescription", "recipeIngredientList.ingredientName", "recipeContributorName"));
        List hits = index.search(query).getHits();
        for (int i = 0; i < hits.size(); i++){
            ItemExplore explore = new ItemExplore();
            Map<String, Object> hashMap =  (HashMap)hits.get(i);
            for (Map.Entry<String, Object> mapElement : hashMap.entrySet()){
                if (mapElement.getKey().equals("recipeContributorAvatar")){
                    byte[] bytes = Base64.decode(mapElement.getValue().toString(), Base64.DEFAULT);
                    explore.avatar = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
                else if (mapElement.getKey().equals("recipeCover")){
                    byte[] bytes = Base64.decode(mapElement.getValue().toString(), Base64.DEFAULT);
                    explore.icon = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
                else if (mapElement.getKey().equals("recipeContributorName")){
                    explore.tv_contributor_name = mapElement.getValue().toString();
                }
                else if (mapElement.getKey().equals("recipeLikesNum")){
                    explore.tv_like_num = String.valueOf(mapElement.getValue());
                }
                else if (mapElement.getKey().equals("recipeCommentsNum")){
                    explore.tv_comment_num = String.valueOf(mapElement.getValue());
                }
                else if (mapElement.getKey().equals("recipeName")){
                    explore.title = mapElement.getValue().toString();
                }
                else if (mapElement.getKey().equals("objectID")){
                    explore.id= mapElement.getValue().toString();
                }
            }
            if(explore.avatar == null){
                @SuppressLint("ResourceType") InputStream img_avatar = getResources().openRawResource(R.drawable.default_avatar);
                explore.avatar = BitmapFactory.decodeStream(img_avatar);
            }
            explore.icon_comment = R.drawable.ic_comment;
            explore.icon_like = R.drawable.ic_like;
            mData.add(explore);
        }

    }

    private void showStagger(boolean isVertical, boolean isReverse) {

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, isVertical?StaggeredGridLayoutManager.VERTICAL:StaggeredGridLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(isReverse);
        mList.setLayoutManager(layoutManager);
        mAdapter = new StaggerAdapter(mData);
        mList.setAdapter(mAdapter);
        initListener();

    }

    private void initListener() {

        mAdapter.setOnItemClickListener(new StaggerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                preferences.putString(MacroDef.KEY_RECIPE_ID, mData.get(position).id);
                startActivity(new Intent(getApplicationContext(), RecipeDetailActivity.class));
            }
        }) ;

    }

    private class sendFeedbackJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here
            search();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
            showStagger(true, false);
        }

    }

}