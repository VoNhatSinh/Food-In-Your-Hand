package com.company.sinh_tan.foodinyourhand;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import com.company.sinh_tan.bus.DistrictBUS;
import com.company.sinh_tan.bus.FoodTypeBUS;
import com.company.sinh_tan.bus.StoreImageBUS;
import com.company.sinh_tan.common.constant.Constant;
import com.company.sinh_tan.custom_adapter.CustomItemFoodTypeAdapter;
import com.company.sinh_tan.custom_adapter.GridViewAdapter;
import com.company.sinh_tan.dto.DistrictDTO;
import com.company.sinh_tan.dto.FoodTypeDTO;
import com.company.sinh_tan.dto.StoreImage;

import java.util.ArrayList;

/**
 * Created by Sinh on 6/10/2015.
 */
public class ContentFragmentFirst extends Fragment {
    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";
    private String titleFragment;
    private Bundle args;
    //spinner district
    private Spinner spinnerDistrict;
    private ArrayAdapter adapterDistrict;
    private ArrayList<DistrictDTO> arrDistricts;
    DistrictBUS districtBUS;
    //gridview food
    private GridView gridViewFood;
    private GridViewAdapter gridViewAdapterFood;
    private ArrayList<StoreImage> arrStoreImages;
    //
    private Context contextFood;

    //*****
    private ListView lvFoodType;
    private ArrayList<FoodTypeDTO>arrFoodTypes;
    private CustomItemFoodTypeAdapter adapterFoodType;
    private FrameLayout frameFoodType;
    private Button btnOK;
    private Button btnFindFoodType;
    private ArrayList<String> arrUserSelection;
    private String idDistrict = "0";
    private  StoreImageBUS storeImageBUS;
    /**
     * @return a new instance of {@link ContentFragmentFirst}, adding the parameters into a bundle and
     * setting them as arguments.
     */

    public static ContentFragmentFirst newInstance(CharSequence title, int indicatorColor,
                                              int dividerColor) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);

        ContentFragmentFirst fragment = new ContentFragmentFirst();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextFood = getActivity().getApplicationContext();
        //get array FoodType
        FoodTypeBUS foodTypeBUS = new FoodTypeBUS(contextFood);
        arrFoodTypes = foodTypeBUS.getListFoodType();
        //get array District
        districtBUS = new DistrictBUS(contextFood);
        arrDistricts = districtBUS.getListDistrict();
        arrUserSelection = new ArrayList<String>();
        //get array StoreLib(name, id, address, image, bitmap)
         storeImageBUS = new StoreImageBUS(contextFood);
        arrStoreImages = storeImageBUS.getListStore();
        idDistrict = "0";
        Log.d("103", "size storeImages " + arrStoreImages.size());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        args = getArguments();
        titleFragment =(String)args.getCharSequence(KEY_TITLE);

        View view = inflater.inflate(R.layout.layout_content_fragment_first, container, false);
        //setSpinnerContent(view);
        addControlDistrict(view);
        addControlsFoodType(view);
        setGridViewContent(view);
        return view;
    }

    private void addControlDistrict(View view)
    {
        spinnerDistrict = (Spinner) view.findViewById(R.id.spinnerFood);


        adapterDistrict = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, arrDistricts);
        adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(adapterDistrict);
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idDistrict = String.valueOf(arrDistricts.get(i).getId());
                if (arrUserSelection != null && arrStoreImages != null) {
                    arrStoreImages = storeImageBUS.getListStoreImageByTypeAndDistrict(arrUserSelection, idDistrict);
                    gridViewAdapterFood = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, arrStoreImages);
                    gridViewFood.setAdapter(gridViewAdapterFood);
                    gridViewAdapterFood.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void addControlsFoodType(View view)
    {
        frameFoodType = (FrameLayout) view.findViewById(R.id.frameFoodType);
        adapterFoodType = new CustomItemFoodTypeAdapter(getActivity(), R.layout.item_food_type_layout, arrFoodTypes);
        lvFoodType = (ListView) view.findViewById(R.id.lvFoodType);
        lvFoodType.setAdapter(adapterFoodType);

        lvFoodType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cbkFoodType = (CheckBox) view.findViewById(R.id.cbkFoodType);
                if (cbkFoodType.isChecked() == true) {
                    cbkFoodType.setChecked(false);
                    arrUserSelection.remove(String.valueOf(arrFoodTypes.get(i).getId()));

                } else {
                    cbkFoodType.setChecked(true);
                    arrUserSelection.add(String.valueOf(arrFoodTypes.get(i).getId()));
                }
            }
        });
        btnFindFoodType = (Button) view.findViewById(R.id.btnFindFoodType);
        btnFindFoodType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (frameFoodType.isShown() == false) {
                    frameFoodType.setVisibility(View.VISIBLE);
                }
            }
        });
        btnOK = (Button) view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (frameFoodType.isShown()) {
                    frameFoodType.setVisibility(View.GONE);
                    arrStoreImages = storeImageBUS.getListStoreImageByTypeAndDistrict(arrUserSelection, idDistrict);
                    gridViewAdapterFood = new GridViewAdapter(view.getContext(), R.layout.grid_item_layout, arrStoreImages);
                    gridViewFood.setAdapter(gridViewAdapterFood);
                    gridViewAdapterFood.notifyDataSetChanged();
                }
            }
        });

    }

    private void setGridViewContent(View view)
    {
        gridViewFood = (GridView) view.findViewById(R.id.gridViewFood);
        gridViewAdapterFood = new GridViewAdapter(view.getContext(), R.layout.grid_item_layout, arrStoreImages);
        gridViewFood.setAdapter(gridViewAdapterFood);
        gridViewFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idStore = String.valueOf(arrStoreImages.get(position).getId());
                /*Intent intentDetailStore = new Intent(contextFood, DetailStoreActivity.class);
                intentDetailStore.putExtra(Constant.ID_STORE, idStore);
                intentDetailStore.putExtra(Constant.REQUEST_CODE, IntentConstant.INTENT_FRAGMENT_FIRST);
                startActivity(intentDetailStore);*/
                /*ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
                viewPager.setVisibility(View.GONE);*/
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentDetailStore detailStore = new FragmentDetailStore();
                Bundle bundleStore = new Bundle();
                bundleStore.putString(Constant.ID_STORE, idStore);
                detailStore.setArguments(bundleStore);
                transaction.replace(R.id.detail_store_fragment, detailStore, Constant.FRAGMENT_DETAIL_STORE);
                transaction.addToBackStack(Constant.FRAGMENT_DETAIL_STORE);
                transaction.commit();

            }
        });
    }


    private void setSpinnerContent(View view)
    {
        spinnerDistrict = (Spinner) view.findViewById(R.id.spinnerFood);
        adapterDistrict = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, arrFoodTypes);
        adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(adapterDistrict);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
