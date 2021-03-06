package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    // Member variables holding references to the UI widgets
    private TextView mAlsoKnownAsTextView;
    private TextView mPlaceOfOriginTextView;
    private TextView mDescriptionTextView;
    private TextView mIngredientsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get the reference to the widgets
        mAlsoKnownAsTextView = findViewById(R.id.also_known_tv);
        mPlaceOfOriginTextView = findViewById(R.id.origin_tv);
        mDescriptionTextView = findViewById(R.id.description_tv);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        // Display the names vertically stacked by building
        // a simple String that contains a new line character
        // after each name.

        /*
         * Use an efficient way to build a String from a List<> as
         * described in the following StackOverflow post:
         * https://stackoverflow.com/questions/599161/best-way-to-convert-an-arraylist-to-a-string
         * (retrieved on February, Monday 19th, 2018)
         */
        List<String> alsoKnownAsStrings = sandwich.getAlsoKnownAs();
        if (!alsoKnownAsStrings.isEmpty()) {
            StringBuilder alsoKnownAsStringBuilder = new StringBuilder();
            for (String alsoKnowAs : alsoKnownAsStrings) {
                alsoKnownAsStringBuilder.append(alsoKnowAs);
                alsoKnownAsStringBuilder.append("\n");
            }
            mAlsoKnownAsTextView.setText(alsoKnownAsStringBuilder.toString());
        }

        String placeOfOrigin = sandwich.getPlaceOfOrigin();
        mPlaceOfOriginTextView.setText(placeOfOrigin);

        String description = sandwich.getDescription();
        mDescriptionTextView.setText(description);

        // Display the ingredients vertically stacked by building
        // a simple String that contains a new line character
        // after each ingredient.

        /*
         * Use an efficient way to build a String from a List<> as
         * described in the following StackOverflow post:
         * https://stackoverflow.com/questions/599161/best-way-to-convert-an-arraylist-to-a-string
         * (retrieved on February, Monday 19th, 2018)
         */
        List<String> ingredientStrings = sandwich.getIngredients();
        if (!ingredientStrings.isEmpty()) {
            StringBuilder ingredientStringBuilder = new StringBuilder();
            for (String ingredient : ingredientStrings) {
                ingredientStringBuilder.append(ingredient);
                ingredientStringBuilder.append("\n");
            }
            mIngredientsTextView.setText(ingredientStringBuilder.toString());
        }
    }
}
