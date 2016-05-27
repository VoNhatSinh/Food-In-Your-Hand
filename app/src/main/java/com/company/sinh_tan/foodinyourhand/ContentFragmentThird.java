package com.company.sinh_tan.foodinyourhand;

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

import com.company.sinh_tan.bus.StoreBUS;
import com.company.sinh_tan.common.constant.Constant;
import com.company.sinh_tan.custom_adapter.CustomItemLoveStoreAdapter;
import com.company.sinh_tan.dto.StoreDTO;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/18/2015.
 */
public class ContentFragmentThird extends Fragment{
    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";

    private LinearLayout linearYourLoveStore;
    private LinearLayout linearSpecialStore;
    private TextView txtLoveStore;

    private ListView lvLoveStore;
    private CustomItemLoveStoreAdapter adapterLove;
    private ArrayList<StoreDTO> storeDTOs;
    private StoreBUS storeBUS;

    public static ContentFragmentThird newInstance(CharSequence title, int indicatorColor,
                                                   int dividerColor) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);

        ContentFragmentThird fragment = new ContentFragmentThird();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_content_fragment_thrid, container, false);
        addControls(view);
        return view;
    }

    private void addControls(View view) {
        storeBUS = new StoreBUS(getActivity());
        linearYourLoveStore = (LinearLayout) view.findViewById(R.id.linearYourLoveStore);
        linearSpecialStore = (LinearLayout) view.findViewById(R.id.linearSpecialStore);
        txtLoveStore = (TextView) view.findViewById(R.id.txtLoveStore);
        lvLoveStore = (ListView) view.findViewById(R.id.lvLoveStore);

        linearYourLoveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtLoveStore.setText("Quán ăn yêu thích của bạn");
                storeDTOs = storeBUS.getListStoreWithLike();
                adapterLove = new CustomItemLoveStoreAdapter(getActivity(), R.layout.listview_item_love_store, storeDTOs);
                lvLoveStore.setAdapter(adapterLove);
            }
        });


        linearSpecialStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtLoveStore.setText("Nổi bật");
                storeDTOs = storeBUS.getListStoreWithHightRating();
                adapterLove = new CustomItemLoveStoreAdapter(getActivity(), R.layout.listview_item_love_store, storeDTOs);
                lvLoveStore.setAdapter(adapterLove);
            }
        });

        lvLoveStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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




    }
}
