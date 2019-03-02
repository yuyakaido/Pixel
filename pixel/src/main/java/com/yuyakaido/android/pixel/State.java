package com.yuyakaido.android.pixel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {

    interface FilterListener {
        void onFilterSelect();
    }

    private List<Filter> filters = Arrays.asList(Filter.values());
    private Filter filter = filters.get(0);
    private List<Editor> editors = Editor.newEditors();
    private Editor editor = editors.get(0);
    private Map<Filter, FilterListener> listeners = new HashMap<>();

    List<Filter> getFilters() {
        return filters;
    }

    Filter getFilter() {
        return filter;
    }

    void setFilter(Filter filter) {
        this.filter = filter;
        for (FilterListener listener : listeners.values()) {
            listener.onFilterSelect();
        }
    }

    List<Editor> getEditors() {
        return editors;
    }

    Editor getEditor() {
        return editor;
    }

    void setEditor(Editor editor) {
        this.editor = editor;
    }

    boolean isDirty(Filter filter) {
        return this.filter == filter;
    }

    boolean isDirty(Editor editor) {
        return editor.isDirty();
    }

    void addFilterListener(Filter filter, FilterListener listener) {
        listeners.put(filter, listener);
    }

    void remoteFilterListener(Filter filter) {
        listeners.remove(filter);
    }

}
