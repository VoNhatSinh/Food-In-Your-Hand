package com.company.sinh_tan.foodinyourhand;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.sinh_tan.bus.HistorySearchBUS;
import com.company.sinh_tan.bus.StoreBUS;
import com.company.sinh_tan.common.constant.Constant;
import com.company.sinh_tan.custom_adapter.CustomListViewAdapter;
import com.company.sinh_tan.dto.StoreDTO;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/10/2015.
 */
public class ContentFragmentSecond extends Fragment{
    private Bundle args;
    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";
    private LinearLayout linearNearPlace;
    private LinearLayout linearFindingHistory;

    private HistorySearchBUS historySearchBUS;
    private StoreBUS storeBUS;

    ListView lvFindingHistory;
    CustomListViewAdapter adapter;
    ArrayList<StoreDTO> storeDTOs;

    private Context context;
    public static ContentFragmentSecond newInstance(CharSequence title, int indicatorColor,
                                              int dividerColor)
    {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);
        ContentFragmentSecond fragment = new ContentFragmentSecond();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        storeBUS = new StoreBUS(context);
        historySearchBUS = new HistorySearchBUS(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        args = getArguments();
        View view = inflater.inflate(R.layout.layout_content_fragment_second, container, false);
        lvFindingHistory = (ListView) view.findViewById(R.id.lvFindingHistory);
        linearNearPlace = (LinearLayout) view.findViewById(R.id.linearNearPlace);
        linearNearPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentFindingPlaceNearMe frgFindingNearPlace = new FragmentFindingPlaceNearMe();
                frgFindingNearPlace.setFragmentActivity(getActivity());
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.detail_store_fragment, frgFindingNearPlace);
                transaction.commit();

            }
        });
        linearFindingHistory = (LinearLayout) view.findViewById(R.id.linearFindingHistory);
        linearFindingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> IDs = historySearchBUS.getListHistoryID();
                storeDTOs = storeBUS.getListStoreById(IDs);
                adapter = new CustomListViewAdapter(getActivity(), R.layout.lisview_item_layout, storeDTOs);
                lvFindingHistory.setAdapter(adapter);
            }
        });
        lvFindingHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentDetailStore detailStore = new FragmentDetailStore();
                Bundle bundleStore = new Bundle();
                bundleStore.putString(Constant.ID_STORE, String.valueOf(storeDTOs.get(i).getId()));
                detailStore.setArguments(bundleStore);
                transaction.replace(R.id.detail_store_fragment, detailStore, Constant.FRAGMENT_DETAIL_STORE);
                transaction.addToBackStack(Constant.FRAGMENT_DETAIL_STORE);
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
