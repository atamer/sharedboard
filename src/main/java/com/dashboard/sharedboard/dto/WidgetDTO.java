package com.dashboard.sharedboard.dto;

public class WidgetDTO {

    private String id;
    private int x;
    private int y;
    private Integer z;
    private int height;
    private int width;
    public WidgetDTO(String id, int x, int y, Integer z, int height, int width) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.height = height;
        this.width = width;
    }
    public WidgetDTO(){

    }

    public static WidgetDTOBuilder builder() {
        return new WidgetDTOBuilder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String toString() {
        return "WidgetDTO(id=" + this.getId() + ", x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", height=" + this.getHeight() + ", width=" + this.getWidth() + ")";
    }

    public static class WidgetDTOBuilder {
        private String id;
        private int x;
        private int y;
        private Integer z;
        private int height;
        private int width;

        WidgetDTOBuilder() {
        }

        public WidgetDTOBuilder id(String id) {
            this.id = id;
            return this;
        }

        public WidgetDTOBuilder x(int x) {
            this.x = x;
            return this;
        }

        public WidgetDTOBuilder y(int y) {
            this.y = y;
            return this;
        }

        public WidgetDTOBuilder z(Integer z) {
            this.z = z;
            return this;
        }

        public WidgetDTOBuilder height(int height) {
            this.height = height;
            return this;
        }

        public WidgetDTOBuilder width(int width) {
            this.width = width;
            return this;
        }

        public WidgetDTO build() {
            return new WidgetDTO(id, x, y, z, height, width);
        }

        public String toString() {
            return "WidgetDTO.WidgetDTOBuilder(id=" + this.id + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", height=" + this.height + ", width=" + this.width + ")";
        }
    }
}
