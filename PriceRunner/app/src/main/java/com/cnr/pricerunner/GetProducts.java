package com.cnr.pricerunner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

class MyListAdapter extends ArrayAdapter<String>{
    Activity context;
    String[] title;
    String[] merchant;
    String[] price;
    String priceSymbol="₺";
    String[] iconLink;
    String[] buyLink;

    public MyListAdapter(Activity context, String[] title,String[] merchant, String[] price,String[] iconLink,String[] buyLink) {
        super(context, R.layout.product_list,title);
        this.context=context;
        this.title=title;
        this.merchant=merchant;
        this.price=price;
        this.iconLink=iconLink;
        this.buyLink=buyLink;
    }
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.product_list,null,true);

        ImageView imgg = rowView.findViewById(R.id.imgg);
        TextView titlee = rowView.findViewById(R.id.titlee);
        TextView pricee = rowView.findViewById(R.id.pricee);
        TextView merchantt = rowView.findViewById(R.id.merchantt);
        TextView buylinkk= rowView.findViewById(R.id.buylinkk);




        Glide.with(context).load(iconLink[position]).into(imgg);
        titlee.setText(title[position]);
        pricee.setText(price[position]+priceSymbol);
        merchantt.setText(merchant[position]);
        buylinkk.setText(buyLink[position]);



        return rowView;
    }

}


class Products{
    String[] title;
    String[] merchant;
    String[] price;
    String priceSymbol="₺";
    String[] iconLink;
    String[] buyLink;
    Products(int i){
        title= new String[i];
        merchant=new String[i];
        price= new String[i];
        iconLink= new String[i];
        buyLink= new String[i];
    }

}

public class GetProducts extends AppCompatActivity {
    String myBarcodeNo="";
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_products);


        list=findViewById(R.id.listproducts);
        if(savedInstanceState==null){
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                myBarcodeNo= null;
            } else {
                myBarcodeNo= extras.getString("myBarcode");
            }
        }else{
            myBarcodeNo=(String) savedInstanceState.getSerializable("myBarcode");
        }
        findProducts();
    }



    public void goBackScanPage(View view) {

        Intent intent = new Intent(GetProducts.this,ScanBarcode.class);
        startActivity(intent);

    }

    public void findProducts(){

        if(checkNetworkConnection()){
            //new HTTPAsyncTask().execute("");
            new HTTPAsyncTask().execute("https://api.scaleserp.com/search?api_key=0F7CE2C2C4EF4ABCB00FFC39F9221B7E&q="+myBarcodeNo+"&google_domain=google.com.tr&location=Turkey&gl=tr&hl=tr&search_type=shopping&output=json");
        }

    }


    private class HTTPAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {

            try {
                return HTTPGet(urls[0]);
            }catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid.";

            }

        }

        @Override
        protected void onPostExecute(String result){
            /* result: aldığım json*/
            //System.out.println(result);
            String priceCheckJson=result;
            //String priceCheckJson="{\"request_info\":{\"success\":true,\"credits_used\":22,\"credits_used_this_request\":1,\"credits_remaining\":103,\"credits_reset_at\":\"2022-01-28T16:51:28.000Z\"},\"search_metadata\":{\"created_at\":\"2021-12-29T10:29:08.960Z\",\"processed_at\":\"2021-12-29T10:29:16.548Z\",\"total_time_taken\":7.59,\"engine_url\":\"https://www.google.com.tr/search?q=59079477&gl=tr&hl=tr&uule=w+CAIQICIGVHVya2V5&tbm=shop&tbs=vw:l\",\"html_url\":\"https://api.scaleserp.com/search?api_key=0F7CE2C2C4EF4ABCB00FFC39F9221B7E&q=59079477&google_domain=google.com.tr&location=Turkey&gl=tr&hl=tr&search_type=shopping&output=html&engine=google\",\"json_url\":\"https://api.scaleserp.com/search?api_key=0F7CE2C2C4EF4ABCB00FFC39F9221B7E&q=59079477&google_domain=google.com.tr&location=Turkey&gl=tr&hl=tr&search_type=shopping&output=json&engine=google\",\"location_auto_message\":\"We have automatically set the 'google_domain', 'gl' and 'hl' parameters to match the supplied location of 'Turkey'. You can stop this behaviour by setting the 'location_auto' parameter to 'false'.\"},\"search_parameters\":{\"q\":\"59079477\",\"google_domain\":\"google.com.tr\",\"location\":\"Turkey\",\"gl\":\"tr\",\"hl\":\"tr\",\"search_type\":\"shopping\",\"output\":\"json\"},\"search_information\":{\"original_query_yields_zero_results\":false,\"query_displayed\":\"59079477\",\"detected_location\":\"Türkiye\"},\"shopping_results\":[{\"title\":\"Rexona Men Invisible Black & White Roll On 50 ML\",\"group_header\":\"En iyi eşleşme\",\"id\":\"369118491518588020\",\"link\":\"https://www.google.com.tr/shopping/product/369118491518588020\",\"rating\":5,\"reviews\":1,\"merchant_count\":10,\"price\":16.99,\"price_raw\":\"₺16,99\",\"price_parsed\":{\"symbol\":\"₺\",\"value\":16.99,\"currency\":\"USD\",\"raw\":\"₺16,99\"},\"merchant\":\"Dermokozmetika\",\"snippet\":\"Rexona Invisible Black White Roll On Etkili 50 Ml Pek çok insan günlük terleme durumunu kontrol altına alacak farklı yöntemler ...\",\"delivery\":\"₺13,90 teslimat\",\"has_compare_prices\":true,\"image\":\"data:image/jpeg;base64,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\",\"position\":1,\"has_product_page\":true},{\"title\":\"Rexona Invısıble Black White 50 ml Erkek Roll On 59079477-123\",\"group_header\":\"Diğer eşleşmeler\",\"link\":\"https://www.google.com.tr/aclk?sa=L&ai=DChcSEwi4rOPG5oj1AhWEi8gKHdjCAD8YABADGgJxdQ&sig=AOD64_2nNGeOF49ap5nb37hG1HqQYVvh5w&ctype=5&q=&ved=0ahUKEwjR39_G5oj1AhXgoHIEHe4lBUwQ2CkIdg&adurl=\",\"price\":26.5,\"price_raw\":\"₺26,50\",\"price_parsed\":{\"symbol\":\"₺\",\"value\":26.5,\"currency\":\"USD\",\"raw\":\"₺26,50\"},\"merchant\":\"Trendyol\",\"snippet\":\"Invısıble Black White 50 ml Erkek Roll On 59079477-123 Rexona.\",\"delivery\":\"₺11,99 teslimat\",\"has_compare_prices\":false,\"id\":\"7477256862501940486\",\"image\":\"data:image/jpeg;base64,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\",\"position\":2,\"has_product_page\":false},{\"title\":\"Rexona Invısıble Black White 50 Ml Erkek Roll On 59079477-123\",\"group_header\":\"Diğer eşleşmeler\",\"link\":\"https://www.google.com.tr/aclk?sa=L&ai=DChcSEwi4rOPG5oj1AhWEi8gKHdjCAD8YABAFGgJxdQ&sig=AOD64_3Wu5zWwavwC2FLol3eWYDRPfmsNg&ctype=5&q=&ved=0ahUKEwjR39_G5oj1AhXgoHIEHe4lBUwQ2CkIgQE&adurl=\",\"price\":26.5,\"price_raw\":\"₺26,50\",\"price_parsed\":{\"symbol\":\"₺\",\"value\":26.5,\"currency\":\"USD\",\"raw\":\"₺26,50\"},\"merchant\":\"aZall.com\",\"snippet\":\"Invısıble Black White 50 ml Erkek Roll On 59079477-123\",\"delivery\":\"₺9,85 teslimat\",\"has_compare_prices\":false,\"id\":\"5786272137807451012\",\"image\":\"data:image/jpeg;base64,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'srpresultimg_7477256862501940486_1_2',\",\"position\":3,\"has_product_page\":false}]}";
            //parse
            try {
                JSONObject jsonObject = new JSONObject(priceCheckJson);
                Products products = new Products(jsonObject.getJSONArray("shopping_results").length());

                for(int i=0;i<jsonObject.getJSONArray("shopping_results").length();i++){
                    String title=jsonObject.getJSONArray("shopping_results").getJSONObject(i).getString("title");
                    products.title[i]=title;
                    String merchant=jsonObject.getJSONArray("shopping_results").getJSONObject(i).getString("merchant");
                    products.merchant[i]=merchant;
                    String price=jsonObject.getJSONArray("shopping_results").getJSONObject(i).getString("price");
                    products.price[i]=price;
                    String iconLink=jsonObject.getJSONArray("shopping_results").getJSONObject(i).getString("image");
                    products.iconLink[i]=iconLink;
                    String buyLink=jsonObject.getJSONArray("shopping_results").getJSONObject(i).getString("link");
                    products.buyLink[i]=buyLink;

                }

                for (int i = 0; i < products.price.length; i++) {
                    for (int j = i + 1; j < products.price.length; j++) {
                        String tmp = "";
                        if ( Double.parseDouble(products.price[i])>Double.parseDouble(products.price[j])) {
                            //price sort
                            tmp = products.price[i];
                            products.price[i] = products.price[j];
                            products.price[j] = tmp;
                            //title sort
                            tmp = products.title[i];
                            products.title[i] = products.title[j];
                            products.title[j] = tmp;
                            //merchant sort
                            tmp = products.merchant[i];
                            products.merchant[i] = products.merchant[j];
                            products.merchant[j] = tmp;
                            //iconLink sort
                            tmp = products.iconLink[i];
                            products.iconLink[i] = products.iconLink[j];
                            products.iconLink[j] = tmp;
                            //buyLink sort
                            tmp = products.buyLink[i];
                            products.buyLink[i] = products.buyLink[j];
                            products.buyLink[j] = tmp;
                        }
                    }
                }


                /*ArrayAdapter<String> adapter;
                adapter=new ArrayAdapter<String>(GetProducts.this,R.layout.support_simple_spinner_dropdown_item,products.price);*/
                MyListAdapter adapter = new MyListAdapter(GetProducts.this,products.title,products.merchant,products.price,products.iconLink,products.buyLink);

                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //System.out.println(tutorials[i]);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(products.buyLink[i]));
                        startActivity(browserIntent);
                    }
                });






                } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private String HTTPGet(String myUrl) throws IOException{
        InputStream inputStream = null;
        String result="";

        URL url = new URL(myUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.connect();

        inputStream=conn.getInputStream();

        if(inputStream!=null){
            result=convertInputStreamToString(inputStream);
        }
        else{
            result="Did not work!";
        }
        return result;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line="";
        String result = "";
        while((line=bufferedReader.readLine()) != null){
            result+=line;
        }
        inputStream.close();
        return result;
    }


    public boolean checkNetworkConnection(){
        ConnectivityManager connMgr=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if(networkInfo!=null && (isConnected=networkInfo.isConnected())){
            //connected
        } else{
            //not connected
        }
        return isConnected;
    }



}
