package com.example.administrator.bazipaipan.utils.cityutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.administrator.bazipaipan.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 城市Picker
 *
 * @author zd
 */
public class CityPicker extends LinearLayout {
    private final String TAG = "CityPicker";
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    private ScrollerNumberPicker countryPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private int tempCounyIndex = -1;

    private Context context;
    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
    private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();
    private static ArrayList<String> province_list_code = new ArrayList<String>();
    private static ArrayList<String> city_list_code = new ArrayList<String>();
    private static ArrayList<String> couny_list_code = new ArrayList<String>();

    private CitycodeUtil citycodeUtil;
    private String city_code_string;
    private String city_string;


    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getaddressinfo();
        // TODO Auto-generated constructor stub
    }

    public CityPicker(Context context) {
        super(context);
        this.context = context;
        getaddressinfo();
        // TODO Auto-generated constructor stub
    }

    // 获取城市信息
    private void getaddressinfo() {
        // TODO Auto-generated method stub
        // 读取城市信息string
        JSONParser parser = new JSONParser();
        String area_str = FileUtil.readAssets(context, "area.json");
        province_list = parser.getJSONParserResult(area_str, "area0");
        // citycodeUtil.setProvince_list_code(parser.province_list_code);
        city_map = parser.getJSONParserResultArray(area_str, "area1");
        // System.out.println("city_mapsize" +
        // parser.city_list_code.toString());
        // citycodeUtil.setCity_list_code(parser.city_list_code);
        couny_map = parser.getJSONParserResultArray(area_str, "area2");
        // citycodeUtil.setCouny_list_code(parser.city_list_code);
        // System.out.println("couny_mapsize" +
        // parser.city_list_code.toString());
    }

    public static class JSONParser {
        public ArrayList<String> province_list_code = new ArrayList<String>();
        public ArrayList<String> city_list_code = new ArrayList<String>();

        public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
            List<Cityinfo> list = new ArrayList<Cityinfo>();
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
                        .next();
                Cityinfo cityinfo = new Cityinfo();

                cityinfo.setCity_name(entry.getValue().getAsString());
                cityinfo.setId(entry.getKey());
                province_list_code.add(entry.getKey());
                list.add(cityinfo);
            }
            System.out.println(province_list_code.size());
            return list;
        }

        public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
                String JSONString, String key) {
            HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
                        .next();
                List<Cityinfo> list = new ArrayList<Cityinfo>();
                JsonArray array = entry.getValue().getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    Cityinfo cityinfo = new Cityinfo();
                    cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0)
                            .getAsString());
                    cityinfo.setId(array.get(i).getAsJsonArray().get(1)
                            .getAsString());
                    city_list_code.add(array.get(i).getAsJsonArray().get(1)
                            .getAsString());
                    list.add(cityinfo);
                }
                hashMap.put(entry.getKey(), list);
            }
            return hashMap;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
        citycodeUtil = CitycodeUtil.getSingleton();
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
        countryPicker = (ScrollerNumberPicker) findViewById(R.id.couny);

        provincePicker.setData(citycodeUtil.getProvince(province_list));
        provincePicker.setDefault(0);
        cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(0)));
        cityPicker.setDefault(1);
        countryPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil.getCity_list_code().get(1)));
        countryPicker.setDefault(1);

        city_code_string = citycodeUtil.getCouny_list_code().get(1);
        Log.i(TAG, "初始化city_code_string-->" + city_code_string);
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                Log.i(TAG, "provincePicker-->endSelect:" + "id-->" + id + "，text----->" + text);
                if (text.equals("") || text == null)
                    return;
                if (tempProvinceIndex != id) {
                    String selectDay = cityPicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = countryPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    // 城市数组
                    cityPicker.setData(citycodeUtil.getCity(city_map,
                            citycodeUtil.getProvince_list_code().get(id)));
                    cityPicker.setDefault(1);
                    countryPicker.setData(citycodeUtil.getCouny(couny_map,
                            citycodeUtil.getCity_list_code().get(1)));
                    countryPicker.setDefault(1);
                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }
                tempProvinceIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                Log.i(TAG, "cityPicker-->endSelect:" + "id-->" + id + ",text----->" + text);
                //20151221
                city_code_string = citycodeUtil.getCouny_list_code().get(1);
                if (text.equals("") || text == null)
                    return;
                if (temCityIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = countryPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    countryPicker.setData(citycodeUtil.getCouny(couny_map,
                            citycodeUtil.getCity_list_code().get(id)));
                    countryPicker.setDefault(1);
                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (id > lastDay) {
                        cityPicker.setDefault(lastDay - 1);
                    }
                }
                temCityIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
        countryPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                Log.i(TAG, "countryPicker-->endSelect:" + "id-->" + id + ",text----->" + text);
                if (text.equals("") || text == null)
                    return;
                if (tempCounyIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = cityPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    // 城市数组
                    city_code_string = citycodeUtil.getCouny_list_code().get(id);
                    Log.i(TAG, "countryPicker:city_code_string-->" + city_code_string);

                    int lastDay = Integer.valueOf(countryPicker.getListSize());
                    if (id > lastDay) {
                        countryPicker.setDefault(lastDay - 1);
                    }
                }
                tempCounyIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity_code_string() {
        Log.i(TAG, "getCity_code_string-->" + city_code_string);
        return city_code_string;
    }

    public String getCity_string() {
        city_string = provincePicker.getSelectedText()
                + cityPicker.getSelectedText() + countryPicker.getSelectedText();
        return city_string;
    }

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }
}
