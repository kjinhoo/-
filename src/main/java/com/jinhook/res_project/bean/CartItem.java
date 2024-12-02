package com.jinhook.res_project.bean;

public class CartItem {
    private Resfood food;
    private Integer num;
    private Double smallCount;

    public Resfood getFood() {
        return food;
    }

    public void setFood(Resfood food) {
        this.food = food;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getSmallCount() {
        if(food!=null){
            smallCount = this.food.getRealprice() * this.num;
        }
        return smallCount;
    }

    public void setSmallCount(Double smallCount) {
        this.smallCount = smallCount;
    }
}
