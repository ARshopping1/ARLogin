package com.example.login1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class ListItem extends AppCompatActivity
{
    private class Item
    {
        int item_id;
        int cat_id;
        String name;
        int rate;
        String path_to_modelfile;
        String details_json;

        public Item(int item_id, int cid, String name, int rate, String path_to_modelfile, String details_json)
        {
            this.item_id =item_id;
            this.cat_id=cid;
            this.name=name;
            this.rate=rate;
            this.path_to_modelfile=path_to_modelfile;
            this.details_json=details_json;
        }
        @Override
        public String toString()
        {
            return "Item{" +
                    "item_id=" + item_id +
                    ", cat_id=" + cat_id +
                    ", name='" + name + '\'' +
                    ", rate=" + rate +
                    ", path_to_modelfile='" + path_to_modelfile + '\'' +
                    ", details_json='" + details_json + '\'' +
                    '}';
        }
    }
    RecyclerView rv_cat_list;
    List<Item> item_list;

    class Listitem extends AsyncTask<Integer,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Integer... params)
        {
            try
            {
                int cat_id=params[0];
                String result = ServiceClient.get("/Getallitem?id="+cat_id);
                JSONArray objects = new JSONArray(result);
                item_list=new ArrayList<>();
                for (int i = 0; i < objects.length(); i++)
                {
                    JSONObject o = objects.getJSONObject(i);
                    item_list.add(new Item(o.getInt("item_id"),o.getInt("cat_id"), o.getString("name"),o.getInt("rate"),o.getString("path_to_modelfile"),o.getString("details_json")));
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

                rv_cat_list.setAdapter(new ListItem.ItemAdapter());
                rv_cat_list.setLayoutManager(new LinearLayoutManager(ListItem.this));
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
        setContentView(R.layout.activity_list_item);
        rv_cat_list = findViewById(R.id.list_item);
        int cat_id=getIntent().getIntExtra("cat_id",-1);
        new Listitem().execute(cat_id);
    }
    private class ItemAdapter extends RecyclerView.Adapter<ListItem.ViewHolder>
    {
        @NonNull
        @Override
        public ListItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.item_list_item, parent, false);
            ListItem.ViewHolder viewHolder = new ListItem.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListItem.ViewHolder holder, int position)
        {

           // ListCategory.Category cat = cat_list.get(position);
            Item it=item_list.get(position);
            System.out.println("bindViewHolder:"+position+":"+it);

            holder.textView.setText(it.name);
            holder.textView2.setText(String.valueOf(it.rate))   ;
//                holder.imageView.setImageResource(listdata[position].getImgId());

            new ImageLoadTask(ServiceClient.BASE_URL + "/images/item/" + it.item_id + ".jpg", holder.imageView).execute();


            holder.constraintLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                   // Toast.makeText(view.getContext(), "click on item: " + cat.id, Toast.LENGTH_LONG).show();
                    Intent i=new Intent(ListItem.this,Arshopping.class);
                    i.putExtra("modelfile",it.path_to_modelfile);

                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount()
        {
            System.out.println("getItemCount:"+item_list.size());
            return item_list.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public TextView textView;
        public ConstraintLayout constraintLayout;
        public TextView textView2;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.textView2 = (TextView) itemView.findViewById(R.id.textView2);
            constraintLayout = (ConstraintLayout)itemView.findViewById(R.id.cl1);
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

}