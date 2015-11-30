package com.jenkov.iap.pojos;

/**
 * Created by jjenkov on 16-11-2015.
 */
public class PojoArray1Float {

    public Pojo1Float[] pojos = null;

    public PojoArray1Float(int count) {
        this.pojos = new Pojo1Float[count];
        for(int i=0; i < count; i++){
            this.pojos[i] = new Pojo1Float();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + this.pojos.length + ")";
    }

}
