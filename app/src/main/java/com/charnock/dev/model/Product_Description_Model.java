package com.charnock.dev.model;

/**
 * Created by BALAKUMAR on 25-Jul-15.
 */
public class Product_Description_Model {
    String product_code;
    String product_name;
    String product_description;
    String Product_specification;

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_specification() {
        return Product_specification;
    }

    public void setProduct_specification(String product_specification) {
        Product_specification = product_specification;
    }
}
