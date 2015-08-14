package com.example.truecallertest.io;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.truecallertest.listeners.ResponseListener;
import com.example.truecallertest.models.Error;

import java.util.HashMap;
import java.util.Map;

/**
 * Network Manager for making network calls. It makes calls asynchronously.
 * <p/>
 * Call NetworkManager.getInstance(context) to get instance.
 * <p/>
 * Created by salil on 13/8/15.
 */
public class NetworkManager {
    private static NetworkManager instance = null;
    private final String TAG = NetworkManager.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private Context mContext = null;

    /**
     * Get instance of NetworkManager
     *
     * @param context
     * @return
     */
    public static NetworkManager getInstance(Context context) {
        synchronized (NetworkManager.class) {
            if (instance == null) {
                synchronized (NetworkManager.class) {
                    instance = new NetworkManager(context);
                }
            }
        }

        return instance;
    }

    private NetworkManager(Context context) {
        mContext = context;
    }

    /**
     * Fetch the response from given url and get character at given index.
     * Returns the character at given index.
     *
     * @param url      - URL to fetch the data.
     * @param index    - nth index character to be returned.
     * @param listener - Listener to get response once the request is completed.
     */
    public void getCharacterAtIndex(String url, final int index, final ResponseListener<Character> listener) {
        StringRequest request = new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                if (!TextUtils.isEmpty(response)) {
                    if (response.length() >= index) {
                        sendResponse(listener, response.charAt(index - 1));
                    } else {
                        sendError(listener, new Error(Error.ERROR_INVALID_RESPONSE));
                    }
                } else {
                    sendError(listener, new Error(Error.ERROR_INVALID_RESPONSE));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendError(listener, new Error(error));
            }
        });

        addToRequestQueue(request);
    }

    /**
     * Fetch response from given url and get every character at every index.
     * Returns the array of character at every nth index.
     * <p/>
     *
     * @param url      - URL to fetch the data.
     * @param index    - nth index character to be returned.
     * @param listener - Listener to get response once the request is completed.
     */
    public void getEveryCharacterAtIndex(String url, final int index, final ResponseListener<Character[]> listener) {
        StringRequest request = new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    int length = response.length();
                    if (length >= index) {
                        Character[] characterArray = new Character[Math.abs(length / index) + 1];
                        for (int i = 0, j = index - 1; j < length; j = (j + index), i++) {
                            characterArray[i] = response.charAt(j);
                        }
                        sendResponse(listener, characterArray);
                    } else {
                        sendError(listener, new Error(Error.ERROR_INVALID_RESPONSE));
                    }
                } else {
                    sendError(listener, new Error(Error.ERROR_INVALID_RESPONSE));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendError(listener, new Error(error));
            }
        });

        addToRequestQueue(request);
    }

    /**
     * Fetch the data from given url and return the word occurrences.
     * Returns the map of word and its respective count.
     *
     * @param url      - URL to load the instance.
     * @param listener - Listener to get the reponse once the data is fetched.
     */
    public void getWordCount(String url, final ResponseListener<Map<String, Integer>> listener) {
        StringRequest request = new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Map<String, Integer> map = new HashMap<>();

                // Split string at every space and new line.
                String[] responseArray = response.split("[\\n\\r\\s]+");
                for (String str : responseArray) {
                    if (map.containsKey(str.toLowerCase())) {
                        map.put(str.toLowerCase(), map.get(str.toLowerCase()) + 1);
                    } else {
                        map.put(str.toLowerCase(), 1);
                    }
                }

                sendResponse(listener, map);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendError(listener, new Error(error));
            }
        });

        addToRequestQueue(request);
    }

    private <T> void sendResponse(ResponseListener listener, T t) {
        if (listener != null) {
            listener.onResponse(t);
        }
    }

    private void sendError(ResponseListener listener, com.example.truecallertest.models.Error error) {
        if (listener != null) {
            listener.onError(error);
        }
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    private void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
