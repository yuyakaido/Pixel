package com.yuyakaido.android.pixel;

import java.util.Arrays;
import java.util.List;

class State {

    private List<Filter> filters = Arrays.asList(Filter.values());
    private Filter filter = filters.get(0);
    private List<Editor> editors = Editor.newEditors();
    private Editor editor = editors.get(0);

    List<Filter> getFilters() {
        return filters;
    }

    Filter getFilter() {
        return filter;
    }

    void setFilter(Filter filter) {
        this.filter = filter;
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

}
