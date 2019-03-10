package com.yuyakaido.android.pixel;

import android.net.Uri;

import java.util.Arrays;
import java.util.List;

class State {

    private List<Filter> filters = Arrays.asList(Filter.values());
    private Filter filter = filters.get(0);
    private List<Editor> editors = Editor.newEditors();
    private Editor editor = editors.get(0);
    private Uri inputUri = null;
    private Uri outputUri = null;

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

    Uri getInputUri() {
        return inputUri;
    }

    void setInputUri(Uri inputUri) {
        this.inputUri = inputUri;
    }

    Uri getOutputUri() {
        return outputUri;
    }

    void setOutputUri(Uri outputUri) {
        this.outputUri = outputUri;
    }

    boolean isDirty(Filter filter) {
        return this.filter == filter;
    }

    boolean isDirty(Editor editor) {
        return editor.isDirty();
    }

}
