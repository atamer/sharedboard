package com.dashboard.sharedboard.domain;

public class Widget {

    private String id;
    private final int x;
    private final int y;
    private Integer z;
    private final int height;
    private final int width;

    public Widget(String id, int x, int y, Integer z, int height, int width) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.height = height;
        this.width = width;
    }

    public Widget(Widget widget) {
        this.id = widget.getId();
        this.x = widget.x;
        this.y = widget.y;
        this.z = widget.z;
        this.height = widget.height;
        this.width = widget.width;

    }

    public static WidgetBuilder builder() {
        return new WidgetBuilder();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return "Widget(id=" + this.getId() + ", x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", height=" + this.getHeight() + ", width=" + this.getWidth() + ")";
    }

    public static class WidgetBuilder {
        private String id;
        private int x;
        private int y;
        private Integer z;
        private int height;
        private int width;

        WidgetBuilder() {
        }

        public WidgetBuilder id(String id) {
            this.id = id;
            return this;
        }

        public WidgetBuilder x(int x) {
            this.x = x;
            return this;
        }

        public WidgetBuilder y(int y) {
            this.y = y;
            return this;
        }

        public WidgetBuilder z(Integer z) {
            this.z = z;
            return this;
        }

        public WidgetBuilder height(int height) {
            this.height = height;
            return this;
        }

        public WidgetBuilder width(int width) {
            this.width = width;
            return this;
        }

        public Widget build() {
            return new Widget(id, x, y, z, height, width);
        }

        public String toString() {
            return "Widget.WidgetBuilder(id=" + this.id + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", height=" + this.height + ", width=" + this.width + ")";
        }
    }
}
