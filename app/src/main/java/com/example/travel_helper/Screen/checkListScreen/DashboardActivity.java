package com.example.travel_helper.Screen.checkListScreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_helper.R;
import com.example.travel_helper.Screen.checkListScreen.DTO.ToDo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.travel_helper.Screen.checkListScreen.Const.INTENT_TODO_ID;
import static com.example.travel_helper.Screen.checkListScreen.Const.INTENT_TODO_NAME;

public class DashboardActivity extends AppCompatActivity {

    DBHandler dbHandler;
    DashboardActivity activity;
    RecyclerView rv_dashboard;
    FloatingActionButton fab_dashboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        rv_dashboard = findViewById(R.id.rv_dashboard);
        fab_dashboard = findViewById(R.id.fab_dashboard);
        setTitle("Check List");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        activity = this;
        dbHandler = new DBHandler(activity);
        rv_dashboard.setLayoutManager(new LinearLayoutManager(activity));

        fab_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Add ToDo");
                View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
                final EditText toDoName = view.findViewById(R.id.ev_todo);
                dialog.setView(view);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (toDoName.getText().toString().length() > 0) {
                            ToDo toDo = new ToDo();
                            toDo.setName(toDoName.getText().toString());
                            dbHandler.addToDo(toDo);
                            refreshList();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void updateToDo(final ToDo toDo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Update ToDo");
        View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
        final EditText toDoName = view.findViewById(R.id.ev_todo);
        toDoName.setText(toDo.getName());
        dialog.setView(view);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (toDoName.getText().toString().length() > 0) {
                    toDo.setName(toDoName.getText().toString());
                    dbHandler.updateToDo(toDo);
                    refreshList();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    public void refreshList() {
        rv_dashboard.setAdapter(new DashboardAdapter(activity, dbHandler.getToDos()));
    }

    class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
        ArrayList<ToDo> list;
        DashboardActivity activity;

        DashboardAdapter(DashboardActivity activity, ArrayList<ToDo> list) {
            this.list = list;
            this.activity = activity;
            Log.d("DashboardAdapter" , "DashboardAdapter");
            Log.d("DashboardAdapter" , "list : " + list.size());
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_dashboard, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
            holder.toDoName.setText(list.get(i).getName());

            holder.toDoName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ItemActivity.class);
                    intent.putExtra(INTENT_TODO_ID, list.get(i).getId());
                    intent.putExtra(INTENT_TODO_NAME, list.get(i).getName());
                    activity.startActivity(intent);
                }
            });

            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(activity, holder.menu);
                    popup.inflate(R.menu.dashboard_child);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_edit: {
                                    Log.d("index_edit:", i+"");
                                    activity.updateToDo(list.get(i));
                                    break;
                                }
                                case R.id.menu_delete: {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                                    dialog.setTitle("Are you sure");
                                    dialog.setMessage("Do you want to delete this task ?");
                                    dialog.setPositiveButton("Continue", (dialogInterface, i1) -> {
                                        i1 = i;
                                        activity.dbHandler.deleteToDo(list.get(i1).getId());
                                        activity.refreshList();
                                    });
                                    dialog.setNegativeButton("Cancel", (dialogInterface, i12) -> {

                                    });
                                    dialog.show();
                                }
                                case R.id.menu_mark_as_completed: {
                                    activity.dbHandler.updateToDoItemCompletedStatus(list.get(i).getId(), true);
                                    break;
                                }
                                case R.id.menu_reset: {
                                    activity.dbHandler.updateToDoItemCompletedStatus(list.get(i).getId(), false);
                                    break;
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView toDoName;
            ImageView menu;

            ViewHolder(View v) {
                super(v);
                toDoName = v.findViewById(R.id.tv_todo_name);
                menu = v.findViewById(R.id.iv_menu);
            }
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        return intent;
    }

}
