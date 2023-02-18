package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView articleListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        setArticleAdapter();
    }

    private void initWidgets() {
        articleListView = findViewById(R.id.ListView);
    }

    private void setArticleAdapter() {
        ArticleAdapter articleAdapter = new ArticleAdapter(getApplicationContext(), Article.articleArrayList);
        articleListView.setAdapter(articleAdapter);
    }

    public void newArticle(View view)
    {
        Intent newArticleIntent = new Intent(this, ChangeItem.class);
        startActivity(newArticleIntent);
    }
}