package com.sia.poc.util.jackson;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonView;

public class JsonViewPage<T> extends org.springframework.data.domain.PageImpl<T> {

    public JsonViewPage(final List<T> content, final Pageable pageable, final long total) {
        super(content, pageable, total);
    }

    public JsonViewPage(final List<T> content) {
        super(content);
    }

    public JsonViewPage(final Page<T> page, final Pageable pageable) {
        super(page.getContent(), pageable, page.getTotalElements());
    }

    @JsonView(JsonViews.Read.class)
    public int getTotalPages() {
        return super.getTotalPages();
    }

    @JsonView(JsonViews.Read.class)
    public long getTotalElements() {
        return super.getTotalElements();
    }

    @JsonView(JsonViews.Read.class)
    public boolean hasNext() {
        return super.hasNext();
    }

    @JsonView(JsonViews.Read.class)
    public boolean isLast() {
        return super.isLast();
    }

    @JsonView(JsonViews.Read.class)
    public boolean hasContent() {
        return super.hasContent();
    }

    @JsonView(JsonViews.Read.class)
    public List<T> getContent() {
        return super.getContent();
    }
}