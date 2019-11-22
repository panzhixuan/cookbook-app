package com.example.cookbook.type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.example.cookbook.LoginActivity.MainpageActivity;
import com.example.cookbook.R;

public class TypeFragment extends Fragment{
    private MainpageActivity mainpageActivity;
    //    private List<Button> buttonList;
//    private List<Button> buttonList1;
//    private List<Button> buttonList2;
    private Spinner caishi;
    private Spinner caixi;
    private Spinner changhe;
    private View view;
    private Button confirm;

//    private TypePageController controller;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainpageActivity = (MainpageActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.get("name").toString();
            Log.d("MyFragment", name);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.maintypepage, null);

        initView();
        String[] caishitype = {"清淡", "辣", "酸"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainpageActivity, android.R.layout.simple_list_item_1, caishitype);
        caishi.setAdapter(adapter);
        String[] caixitype = {"川菜", "淮扬菜", "徽菜", "鲁菜", "闽菜", "粤菜", "湘菜", "浙菜"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mainpageActivity, android.R.layout.simple_list_item_1, caixitype);
        caixi.setAdapter(adapter1);
        String[] changhetype = {"家", "饭店", "快餐店"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mainpageActivity, android.R.layout.simple_list_item_1, changhetype);
        changhe.setAdapter(adapter2);

//        controller = new TypePageController(view.findViewById(R.id.maintypepage), this);
//        setListeners(controller);


        return view;
    }

    public static TypeFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        TypeFragment fragment = new TypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListeners(View.OnClickListener onClickListener) {
        view.findViewById(R.id.confirm).setOnClickListener(onClickListener);
    }

    public void initView() {
        caishi = view.findViewById(R.id.caishi);
        caixi = view.findViewById(R.id.caixi);
        changhe = view.findViewById(R.id.changhe);
        confirm=view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(mainpageActivity, TypesearchpageActivity.class);
                intent.putExtra("caixi", caixi.getSelectedItem().toString());
                intent.putExtra("caishi", caishi.getSelectedItem().toString());
                intent.putExtra("changhe", changhe.getSelectedItem().toString());
                startActivity(intent);
            }
        });
//        buttonList = new ArrayList<>();
//        buttonList1 = new ArrayList<>();
//        buttonList2 = new ArrayList<>();
//
//        Button button00 = view.findViewById(R.id.button00);
//        button00.setOnClickListener(controller);
//        buttonList.add(button00);
//
//        Button button10 = view.findViewById(R.id.button10);
//        button10.setOnClickListener(controller);
//        buttonList.add(button10);
//
//        Button button20 = view.findViewById(R.id.button20);
//        button20.setOnClickListener(controller);
//        buttonList.add(button20);
//
//
//        Button button01 = view.findViewById(R.id.button01);
//        button01.setOnClickListener(controller);
//        buttonList1.add(button01);
//
//        Button button11 = view.findViewById(R.id.button11);
//        button11.setOnClickListener(controller);
//        buttonList1.add(button11);
//
//        Button button21 = view.findViewById(R.id.button21);
//        button21.setOnClickListener(controller);
//        buttonList1.add(button21);
//
//
//        Button button02 = view.findViewById(R.id.button02);
//        button02.setOnClickListener(controller);
//        buttonList2.add(button02);
//
//        Button button12 = view.findViewById(R.id.button12);
//        button12.setOnClickListener(controller);
//        buttonList2.add(button12);
//
//        Button button22 = view.findViewById(R.id.button22);
//        button22.setOnClickListener(controller);
//        buttonList2.add(button22);

    }

//    @Override
//    public void submit() {
//        Intent intent = null;
//        intent = new Intent(mainpageActivity, TypesearchpageActivity.class);
//        intent.putExtra("caixi", caixi.getSelectedItem().toString());
//        intent.putExtra("caishi", caishi.getSelectedItem().toString());
//        intent.putExtra("changhe", changhe.getSelectedItem().toString());
//        Log.i("login", caishi.getSelectedItem().toString());
//        Log.i("login", caixi.getSelectedItem().toString());
//        Log.i("login", changhe.getSelectedItem().toString());
//        startActivity(intent);
//    }
}

