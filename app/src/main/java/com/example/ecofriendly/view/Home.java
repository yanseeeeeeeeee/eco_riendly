package com.example.ecofriendly.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecofriendly.R;
import com.example.ecofriendly.adapters.TaskCardAdapters;
import com.example.ecofriendly.data.Repository;
import com.example.ecofriendly.data.models.Task;
import com.example.ecofriendly.data.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private TextView title, description;
    private Button save;
    private ImageButton close;
    private TaskCardAdapters adapter;
    private ActivityResultLauncher<Intent> photoLauncher;
    private String uid;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private Repository repository;
    private FirebaseAuth mAuth;
    private Task selTask;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Home() {}

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        recyclerView = view.findViewById(R.id.card_recycler);

        ViewCompat.setOnApplyWindowInsetsListener(recyclerView, (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
            );

            v.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    bars.bottom
            );

            return insets;
        });

        db = FirebaseFirestore.getInstance();
        repository = new Repository();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = "";

        if (user != null) {
            uid = user.getUid(); //проверка получения пользователя
        }

        List<Task> listTask = new ArrayList<>();
        adapter = new TaskCardAdapters(listTask, requireContext(), new TaskCardAdapters.onTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                openBottomSheet(task);
            } //открываем bottomsheet при нажатии на карточку
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        loadList(); //загружаем лист с задачами

        //лаунчер для работы с переходом к камере или галерее нашего устройства
        photoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == FragmentContainer.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        if (imageUri !=null && selTask != null) {
                            completedTask(selTask);

                        } else if (selTask != null) {
                            Bundle extras = result.getData().getExtras();

                            if (extras != null && extras.get("data") != null) {
                                completedTask(selTask);
                            }
                        }
                    }
                }
        );


        return view;
    }

    /**
     * Метод для работы с bottomsheet, его открытие, закрытие, описание, нажатие на кнопку и
     * дальнейшие действия пользователя
     * @param task
     */
    private void openBottomSheet(Task task) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet, null);

        bottomSheetDialog.setContentView(view);

        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.shortDescription);

        save = view.findViewById(R.id.button);
        close = view.findViewById(R.id.close);

        title.setText(task.getTitle());
        description.setText(task.getDescription());

        close.setOnClickListener(v -> bottomSheetDialog.dismiss());
        save.setOnClickListener(v -> showDialog(task));

        bottomSheetDialog.show();
    }


    /**
     * Метод для того чтобы выводить диалоговое окно с выбором способа импорта фотографии
     * @param task
     */
    private void showDialog(Task task) {
        selTask = task;

        String[] options = new String[] {"Сделать фото", "Выбрать из галереи"};

        new MaterialAlertDialogBuilder(requireContext(), R.style.RoundedMaterialDialog)
                .setTitle("Подтвердить выполнение")
                .setItems(options, ((dialog, which) -> {
                    if (which == 0) {
                        openCamera();
                    } else {
                        openGallery();
                    }
                }))
                .show();
    }

    /**
     * Метод для открытия галереи
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        photoLauncher.launch(intent);
    }

    /**
     * Метод для открытия камеры
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoLauncher.launch(intent);
    }

    /**
     * Метод для подтверждения завершения задачи
     * @param task
     */
    private void completedTask(Task task) {
        Log.d("COMPLETE_TASK", "Метод вызван: " + task.getTitle() + ", id = " + task.getTaskId());
        repository.completeTask(uid, task, new Repository.completeTaskListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(requireContext(), "Задача успешно выполнена", Toast.LENGTH_SHORT).show();
                loadList();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    /**
     *  Обновленеие листа с карточками каждый раз, когда задача выполняется
     */
    private void loadList() {
        repository.getAvailableTasks(uid, new Repository.taskListGetInfoListener() {
            @Override
            public void onLoaded(List<Task> task) {
                adapter.updateList(task);
            }

            @Override
            public void onError(String error) {
                Log.e("Home", "Error" + error);
            }
        });
    }

}

