package stucom.com.petitscamperols;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stucom.com.petitscamperols.model.ApiResponse;
import stucom.com.petitscamperols.model.Jugador;

import static stucom.com.petitscamperols.MainActivity.player;

public class actRank extends AppCompatActivity {

    TextView textView;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_rank);

        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadUsers();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        downloadUsers();
    }

    final static String URL = "https://api.flx.cat/dam2game/ranking";

    public void downloadUsers(){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String json = response.toString();
                        Gson gson = new Gson();
                        Type typeToken = new TypeToken<ApiResponse<List<Jugador>>>() {}.getType();
                    }
                }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", MainActivity.player.getToken());
                return params;
            }
        };
        MyVolley.getInstance(this).add(request);
    }



    class UsersViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        UsersViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }

    }

    class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder>{

        private List<Jugador> users;

        UsersAdapter(List<Jugador> users){
            super();
            this.users = users;
        }

        @NonNull @Override
        public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new UsersViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull UsersViewHolder viewHolder, int position){
            Jugador user = users.get(position);
            viewHolder.textView.setText(user.getName());
            Picasso.get().load(user.getAvatar()).into(viewHolder.imageView);
        }

        @Override
        public int getItemCount(){ return users.size(); }
    }
}
