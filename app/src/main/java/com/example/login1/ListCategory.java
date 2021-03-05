package com.example.login1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import static androidx.recyclerview.widget.RecyclerView.*;

public class ListCategory extends AppCompatActivity
{

    private class Category
    {
        int id;
        String name;

        public Category(int id, String name)
        {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString()
        {
            return "Category{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    RecyclerView rv_cat_list;

    List<Category> cat_list;

    class ListCat extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... params)
        {
            try
            {
                String result = ServiceClient.get("/Getallcategory");

                JSONArray objects = new JSONArray(result);

                cat_list = new ArrayList<>();

                for (int i = 0; i < objects.length(); i++)
                {
                    JSONObject o = objects.getJSONObject(i);
                    cat_list.add(new Category(o.getInt("cat_id"), o.getString("name")));
                }
                return true;
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                //start next activity
                // Toast.makeText(ListCategory.this, "Login Successful", Toast.LENGTH_LONG).show();

                rv_cat_list.setAdapter(new CategoryAdapter());
                rv_cat_list.setLayoutManager(new LinearLayoutManager(ListCategory.this));
            } //else
            //{
            //show error message
            //Toast.makeText(ListCategory.this, "Login Failed", Toast.LENGTH_LONG).show();
            //}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        rv_cat_list = findViewById(R.id.cat_list);


        //start async task
        //getallcategorylist
        new ListCat().execute();
        //onpostexecute

//        rv_cat_list.setAdapter(new RecyclerView.Adapter<ViewHolder>());
    }


    private class CategoryAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.cat_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {

            Category cat = cat_list.get(position);

            System.out.println("bindViewHolder:" + position + ":" + cat);
            holder.textView.setText(cat.name);

            new ImageLoadTask(ServiceClient.BASE_URL + "/images/category/" + cat.id + ".jpg", holder.imageView).execute();

//            holder.imageView.setImageBitmap(getBitmapFromURL(ServiceClient.BASE_URL + "/images/category/" + cat.id + ".jpg"));
            //holder.imageView.setImageURI(Uri.parse("https://5.imimg.com/data5/SX/BY/MY-30972701/dining-table-500x500.jpg"));
            //holder.imageView.setImageResource(R.drawable.a1);
//                holder.imageView.setImageResource(listdata[position].getImgId());
            holder.constraintLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(view.getContext(), "click on item: " + cat.id, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ListCategory.this, ListItem.class);
                    i.putExtra("cat_id", cat.id);
                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount()
        {
            System.out.println("getCategoryCount:" + cat_list.size());
            return cat_list.size();
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap>
    {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView)
        {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params)
        {
            try
            {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

    public static Bitmap getBitmapFromURL(String src)
    {
        try
        {
//            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e)
        {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public TextView textView;
        public ConstraintLayout constraintLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.cl);
        }
    }
}